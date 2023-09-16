package com.minhoi.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.adapter.recyclerview.RecipeListAdapter
import com.minhoi.recipeapp.databinding.ActivityRecipeListBinding
import com.minhoi.recipeapp.ui.viewmodel.RecipeListViewModel

class RecipeListActivity : AppCompatActivity() {

    private val TAG = RecipeListActivity::class.java.simpleName
    private lateinit var binding : ActivityRecipeListBinding
    private lateinit var viewModel : RecipeListViewModel
    private lateinit var  recipeListAdapter : RecipeListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list)
        viewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)

        recipeListAdapter = RecipeListAdapter(this) {
            // OnClickListener
            val intent = Intent(this, RcpInfoActivity::class.java)
            intent.putExtra("rcpSeq", it.rcp_SEQ)
            startActivity(intent)
        }

        binding.menuBackBtn.setOnClickListener {
            finish()
        }

        binding.recipeListRv.apply {
            adapter = recipeListAdapter
            layoutManager = LinearLayoutManager(this@RecipeListActivity)
        }

        val intent = intent
        val ingredientList = intent.getStringArrayListExtra("ingredientList")

        viewModel.getRecipe(ingredientList!!)

        viewModel.recipeList.observe(this) { recipes ->
            recipeListAdapter.setLists(recipes)
        }

    }


}