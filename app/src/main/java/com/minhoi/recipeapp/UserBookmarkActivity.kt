package com.minhoi.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.databinding.ActivityUserBookmarkBinding
import com.minhoi.recipeapp.model.RecipeDto

class UserBookmarkActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBookmarkBinding
    private lateinit var userId : String
    private val bookmarkRcpList = arrayListOf<RecipeDto>()
    private val adapter = UserBookmarkRcpAdapter(this, bookmarkRcpList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_bookmark)

        // 사용자 ID 불러오기
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                userId = user.id.toString()
            }
        }

        Ref.userRef.child(userId).child("bookmarkedRecipe").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (bookmarkSnapShot in dataSnapshot.children) {
                    val recipeDataKey = bookmarkSnapShot.key
                    if (recipeDataKey != null) {
                        Ref.recipeDataRef.child(recipeDataKey).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(recipeDataSnapshot: DataSnapshot) {
                                val recipeData = recipeDataSnapshot.getValue(RecipeDto::class.java)
                                if (recipeData != null) {
                                    bookmarkRcpList.add(recipeData)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Getting RecipeData failed, log a message
                            }
                        })
                    }
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Bookmark failed, log a message
            }
        })


        val rv = binding.bookmarkRv
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

    }
}