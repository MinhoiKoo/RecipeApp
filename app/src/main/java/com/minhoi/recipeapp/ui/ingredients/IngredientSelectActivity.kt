package com.minhoi.recipeapp.ui.ingredients

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.minhoi.recipeapp.ui.ExpirationDateActivity
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.adapter.recyclerview.SelectIngredientAdapter
import com.minhoi.recipeapp.adapter.viewpager2.ViewpagerFragmentAdapter
import com.minhoi.recipeapp.databinding.ActivityIngredientSelectBinding
import com.minhoi.recipeapp.model.SelectedIngredientDto
import java.util.ArrayList

class IngredientSelectActivity : AppCompatActivity() {
    private lateinit var binding : ActivityIngredientSelectBinding
    private lateinit var viewPager : ViewPager2
    private lateinit var viewModel : IngredientViewModel
    private lateinit var selectedAdapter : SelectIngredientAdapter
    private val tabTextList = listOf("채소", "육류", "수산물", "과일")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ingredient_select)
        viewModel = ViewModelProvider(this).get(IngredientViewModel::class.java)

        viewPager = binding.viewPagerIngredient
        binding.viewPagerIngredient.apply {
            adapter = ViewpagerFragmentAdapter(this@IngredientSelectActivity)
        }
        // Viewpager <-> TabLayout 연결
        TabLayoutMediator(binding.ingredientTabLayout, viewPager) {tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        selectedAdapter = SelectIngredientAdapter(this) {
            //onClickListener
            val select = SelectedIngredientDto(it.name, it.imagePath)
            viewModel.deleteIngredient(select)
        }

        binding.selectedRv.apply {
            adapter = selectedAdapter
            layoutManager = LinearLayoutManager(this@IngredientSelectActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.toExpirationBtn.setOnClickListener {
            val item = viewModel.ingredientList.value
            // 선택된 재료 담긴 List
            val selectedItemList = ArrayList<SelectedIngredientDto>()
            when(item) {
                null -> Toast.makeText(this, "재료를 담아주세요", Toast.LENGTH_LONG).show()
                else -> {
                    for(ingredient in item) {
                        selectedItemList.add(ingredient)
                    }
                    // Intent에 재료 리스트 추가
                    val intent = Intent(this, ExpirationDateActivity::class.java)
                    intent.putParcelableArrayListExtra("SelectedIngredientList", selectedItemList)
                    getExpirationDate.launch(intent)
                }
            }
        }

        viewModel.ingredientList.observe(this) {
            selectedAdapter.setSelectedList(it)
        }

    }


    // 유통기한 정보 담겨있음
    private val getExpirationDate = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == Activity.RESULT_OK) {
            val data: Intent? = it.data
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

}