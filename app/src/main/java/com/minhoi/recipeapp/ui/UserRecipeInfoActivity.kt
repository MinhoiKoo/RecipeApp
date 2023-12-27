package com.minhoi.recipeapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.adapter.recyclerview.RecipeInfoListAdapter
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.databinding.ActivityUserRecipeInfoBinding
import com.minhoi.recipeapp.model.InfoIngredientDto

class UserRecipeInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserRecipeInfoBinding
    private lateinit var ingredientAdapter : RecipeInfoListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_recipe_info)
        ingredientAdapter = RecipeInfoListAdapter(this)

        val uid = intent.getStringExtra("uid")
        val key = intent.getStringExtra("key")
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
                val intent = Intent(this@UserRecipeInfoActivity, UserRecipeEditActivity::class.java)
                intent.putExtra("uid", uid)
                intent.putExtra("key", key)
                startActivity(intent)
            }
            deleteUserRecipeBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this@UserRecipeInfoActivity)
                builder.setTitle("삭제 확인")
                    .setMessage("정말로 삭제하시겠습니까?")

                    // "삭제" 버튼 클릭 시
                    .setPositiveButton("삭제") { _, _ ->
                        Ref.userRecipeDataRef.child(uid!!).child(key!!).removeValue()
                        finish()
                        // 삭제 후 추가적인 로직이 있다면 여기에 추가
                    }

                    // "취소" 버튼 클릭 시
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }

                val alertDialog = builder.create()
                alertDialog.show()
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