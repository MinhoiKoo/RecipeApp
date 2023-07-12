package com.minhoi.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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