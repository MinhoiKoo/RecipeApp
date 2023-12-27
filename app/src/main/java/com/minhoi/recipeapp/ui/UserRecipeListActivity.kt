package com.minhoi.recipeapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.model.UserRecipeData
import com.minhoi.recipeapp.adapter.recyclerview.UserRecipeListAdapter
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.databinding.ActivityUserRecipeListBinding
import com.minhoi.recipeapp.model.KakaoUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRecipeListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserRecipeListBinding
    private var userRepository = KakaoUserRepository()
    private val recipeList = arrayListOf<Pair<String, UserRecipeData>>()
    private lateinit var myAdapter : UserRecipeListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_recipe_list)

        lifecycleScope.launch(Dispatchers.IO) {
            when(val id = userRepository.getUser()) {
                "" -> Toast.makeText(this@UserRecipeListActivity, "로그인 후 이용 가능합니다.", Toast.LENGTH_LONG).show()
                else -> {
                    getRecipe(id)
                    withContext(Dispatchers.Main) {
                        myAdapter = UserRecipeListAdapter(this@UserRecipeListActivity, recipeList) {
                            //onClickListener
                            val intent = Intent(this@UserRecipeListActivity, UserRecipeInfoActivity::class.java)
                            intent.putExtra("uid", id)
                            intent.putExtra("key", it.first)
                            intent.putExtra("title", it.second.title)
                            intent.putExtra("imagePath", it.second.imagePath)
                            intent.putExtra("ingredients", it.second.ingredient)
                            intent.putExtra("way", it.second.way)
                            startActivity(intent)
                            finish()
                        }
                        binding.userRecipeRv.apply {
                            adapter = myAdapter
                            layoutManager = LinearLayoutManager(this@UserRecipeListActivity)
                        }
                    }
                }
            }
        }
    }


    private fun getRecipe(userId : String) {
        Ref.userRecipeDataRef.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapShot in dataSnapshot.children) {
                        val key = snapShot.key
                        val data = snapShot.getValue(UserRecipeData::class.java)
                        if (key!= null) {
                            recipeList.add(Pair(key,data!!))
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