package com.minhoi.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.adapter.recyclerview.RecipeInfoListAdapter
import com.minhoi.recipeapp.databinding.ActivityUserRecipeInfoBinding
import com.minhoi.recipeapp.model.InfoIngredientDto

class UserRecipeInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserRecipeInfoBinding
    private lateinit var ingredientAdapter : RecipeInfoListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_recipe_info)
        ingredientAdapter = RecipeInfoListAdapter(this)

        val title = intent.getStringExtra("title")
        val imagePath = intent.getStringExtra("imagePath")
        val cookingWay = intent.getStringExtra("way")
        val ingredients = intent.getStringExtra("ingredients")!!.split(",").map{ InfoIngredientDto(it.trim())}

        binding.apply {
            menuName.text = title
            cookingWayTextView.text = cookingWay
            menuBackBtn.setOnClickListener {
                finish()
            }
            editRecipeBtn.setOnClickListener {
                val intent = Intent(this@UserRecipeInfoActivity, UserRecipeAddActivity::class.java)
                startActivity(intent)
            }
        }
        ingredientAdapter.setIngredients(ingredients)

        Glide.with(this)
            .load(imagePath)
            .into(binding.menuImage)

        binding.userRecipeInfoIngredientRv.apply {
            adapter = ingredientAdapter
            layoutManager = LinearLayoutManager(this@UserRecipeInfoActivity)
            val decoration = DividerItemDecoration(this@UserRecipeInfoActivity, 1)
            addItemDecoration(decoration)
        }

    }
}