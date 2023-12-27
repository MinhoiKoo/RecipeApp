package com.minhoi.recipeapp.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.adapter.recyclerview.ExpirationDateListAdapter
import com.minhoi.recipeapp.databinding.ActivityExpirationDateBinding
import com.minhoi.recipeapp.model.ExpirationDateDto
import com.minhoi.recipeapp.model.SelectedIngredientDto

class ExpirationDateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpirationDateBinding
    private lateinit var expirationAdapter: ExpirationDateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expiration_date)

        val intent = intent
        // 재료 이름 담김
        val ingredientList =
            intent.getParcelableArrayListExtra<SelectedIngredientDto>("SelectedIngredientList")
                ?.map { ExpirationDateDto(it.imagePath, it.name) }

        expirationAdapter = ExpirationDateListAdapter(this) {

        }
        binding.expirationDateRv.apply {
            adapter = expirationAdapter
            layoutManager = LinearLayoutManager(this@ExpirationDateActivity)
        }
        expirationAdapter.setList(ingredientList!!)


        binding.addIngredientBtn.setOnClickListener {
            val resultIntent = Intent()
            val list = arrayListOf<ExpirationDateDto>()
            list.addAll(expirationAdapter.getList())
            resultIntent.putParcelableArrayListExtra("SelectedIngredientList2", list)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}