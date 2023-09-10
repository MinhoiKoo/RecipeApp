package com.minhoi.recipeapp.ui.ingredients

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.adapter.viewpager2.ViewpagerFragmentAdapter
import com.minhoi.recipeapp.databinding.ActivityIngredientSelectBinding

class IngredientSelectActivity : AppCompatActivity() {
    private lateinit var binding : ActivityIngredientSelectBinding
    private lateinit var viewPager : ViewPager2
    private val tabTextList = listOf("채소", "육류", "수산물", "과일")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ingredient_select)
        viewPager = binding.viewPagerIngredient
        binding.viewPagerIngredient.apply {
            adapter = ViewpagerFragmentAdapter(this@IngredientSelectActivity)
        }

        // Viewpager <-> TabLayout 연결
        TabLayoutMediator(binding.ingredientTabLayout, viewPager) {tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }

}