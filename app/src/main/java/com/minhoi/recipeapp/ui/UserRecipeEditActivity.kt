package com.minhoi.recipeapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.model.UserRecipeData
import com.minhoi.recipeapp.databinding.ActivityUserRecipeEditBinding
import com.minhoi.recipeapp.model.UserRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRecipeEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserRecipeEditBinding
    private val userRecipeRepository = UserRecipeRepository()
    private lateinit var recipeData : UserRecipeData
    private lateinit var imagePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_recipe_edit)

        // 수정 화면에 들어오면 값을 가져와서 나타내줘야함
        // 사용자 uid로 db에 접근 후 가져오기

        // 나만의 레시피 게시물의 key값
        val uid = intent.getStringExtra("uid").toString()
        val key = intent.getStringExtra("key").toString()

        lifecycleScope.launch(Dispatchers.IO) {
            recipeData = getUserRecipe(uid,key)
            withContext(Dispatchers.Main) {
                imagePath = recipeData.imagePath
                binding.apply {
                    editUserRecipeTitle.setText(recipeData.title)
                    editUserRecipeIngredient.setText(recipeData.ingredient)
                    editUserRecipeWay.setText(recipeData.way)
                    Glide.with(this@UserRecipeEditActivity)
                        .load(recipeData.imagePath)
                        .into(editUserRecipeImage)
                }
            }
        }
        binding.editBtnUserRecipe.setOnClickListener {
            val x = UserRecipeData(binding.editUserRecipeTitle.text.toString(), binding.editUserRecipeIngredient.text.toString(), binding.editUserRecipeWay.text.toString(), "2023/11/23",imagePath)
            editUserRecipe(uid, key, recipeData)
            Toast.makeText(this, "레시피가 수정됐어요!", Toast.LENGTH_LONG).show()
        }
    }
    private suspend fun getUserRecipe(uid: String, key: String): UserRecipeData {
        return userRecipeRepository.getUserRecipeByKey(uid, key)
    }
    private fun editUserRecipe(uid: String, key: String, data : UserRecipeData) {
        userRecipeRepository.editUserRecipe(uid, key, data)
    }
}