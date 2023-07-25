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
import com.minhoi.recipeapp.adapter.UserRecipeListAdapter
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.databinding.ActivityUserRecipeListBinding

class UserRecipeListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserRecipeListBinding
    private val recipeList = arrayListOf<UserRecipeData>()
    private var myAdapter = UserRecipeListAdapter(this, recipeList)
    private var userId : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_recipe_list)

        UserApiClient.instance.me { user, error ->
            if (user != null) {
                userId = user.id.toString()
                getRecipe(userId!!)
            }
        }

        binding.userRecipeRv.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(this@UserRecipeListActivity)
        }
    }


    private fun getRecipe(userId : String) {
        Ref.userRecipeDataRef.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapShot in dataSnapshot.children) {
                        val data = snapShot.getValue(UserRecipeData::class.java)
                        if (data != null) {
                            recipeList.add(data)
                            Log.d("hi", data.toString())
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }
}