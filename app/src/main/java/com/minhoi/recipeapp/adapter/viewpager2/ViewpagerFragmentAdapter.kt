package com.minhoi.recipeapp.adapter.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.minhoi.recipeapp.ui.ingredients.IngredientFruitFragment
import com.minhoi.recipeapp.ui.ingredients.IngredientMeatFragment
import com.minhoi.recipeapp.ui.ingredients.IngredientSeafoodFragment
import com.minhoi.recipeapp.ui.ingredients.IngredientVegetableFragment

class ViewpagerFragmentAdapter(fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf<Fragment>(IngredientVegetableFragment(), IngredientMeatFragment(), IngredientSeafoodFragment(), IngredientFruitFragment())

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}