package com.minhoi.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.databinding.ActivityUserBookmarkBinding
import com.minhoi.recipeapp.model.RecipeDto
import kotlinx.coroutines.*

class UserBookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBookmarkBinding
    private lateinit var userId: String
    private val bookmarkRcpList = arrayListOf<RecipeDto>()
    private val adapter = UserBookmarkRcpAdapter(this, bookmarkRcpList)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_bookmark)

        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            try {
                val userId = getUserId()

                val bookmarkedRecipeKeys = getBookmarkedRecipeKeys(userId)

                getBookmarkedRecipes(bookmarkedRecipeKeys)
            } catch (e: Exception) {
                // 에러 처리
            }
        }

        val rv = binding.bookmarkRv
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(applicationContext)

        adapter.setItemClickListener(object : UserBookmarkRcpAdapter.OnItemClickListener {
            override fun onClick(v: View, item: RecipeDto) {
                val intent = Intent(applicationContext, RcpInfoActivity::class.java)
                intent.putExtra("name", item.rcp_NM)
                intent.putExtra("ingredient", item.rcp_PARTS_DTLS)
                intent.putExtra("manual01", item.manual01)
                intent.putExtra("manual02", item.manual02)
                intent.putExtra("manual03", item.manual03)
                intent.putExtra("image01", item.manual_IMG01)
                intent.putExtra("image02", item.manual_IMG02)
                intent.putExtra("image03", item.manual_IMG03)
                intent.putExtra("imageSrc", item.att_FILE_NO_MK)
                intent.putExtra("rcpSeq", item.rcp_SEQ)
                startActivity(intent)
            }


        })

    }


    private suspend fun getUserId(): String = withContext(Dispatchers.IO) {
        val completableDeferred = CompletableDeferred<String>()

        UserApiClient.instance.me { user, error ->
            if (user != null) {
                completableDeferred.complete(user.id.toString())
            } else {
                if (error != null) {
                    completableDeferred.completeExceptionally(error)
                }
            }
        }

        completableDeferred.await()
    }

    private suspend fun getBookmarkedRecipeKeys(userId: String): List<String> =
        withContext(Dispatchers.IO) {
            val completableDeferred = CompletableDeferred<List<String>>()

            Ref.userRef.child(userId).child("bookmarkedRecipe")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val keys = dataSnapshot.children.mapNotNull { it.key }
                        completableDeferred.complete(keys)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        completableDeferred.completeExceptionally(databaseError.toException())
                    }
                })

            completableDeferred.await()
        }

    private suspend fun getBookmarkedRecipes(keys: List<String>) = withContext(Dispatchers.IO) {

        keys.forEach { recipeDataKey ->
            val completableDeferred = CompletableDeferred<RecipeDto>()

            Ref.recipeDataRef.child(recipeDataKey)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(recipeDataSnapshot: DataSnapshot) {
                        val recipeData = recipeDataSnapshot.getValue(RecipeDto::class.java)
                        recipeData?.let {
                            completableDeferred.complete(it)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting RecipeData failed, log a message
                    }
                })

            try {
                val recipeData = completableDeferred.await()
                bookmarkRcpList.add(recipeData)
                Log.d("bookmark", recipeData.toString())
            } catch (e: Exception) {
                // 에러 처리
            }
        }

        withContext(Dispatchers.Main) {
            adapter.notifyDataSetChanged()
        }
    }
}