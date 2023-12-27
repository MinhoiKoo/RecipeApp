package com.minhoi.recipeapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.adapter.recyclerview.RecipeListAdapter
import com.minhoi.recipeapp.databinding.ActivityRecipeListBinding
import com.minhoi.recipeapp.model.SearchRecipeDto
import com.minhoi.recipeapp.ui.viewmodel.RecipeListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            it as SearchRecipeDto
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
        val type = intent.getStringExtra("type")

        when(type) {
            "ingredient" -> {
                val ingredientList = intent.getStringArrayListExtra("ingredientList")
                viewModel.getRecipe(ingredientList!!)
            }
            else -> {
                val recipeName = intent.getStringExtra("recipeName")
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.getRecipeByName(recipeName!!)
                }
            }

        }

        viewModel.recipeList.observe(this) { recipes ->
            if(recipes==null) {
                binding.loadingSpinKit.visibility = View.VISIBLE
            }
            else {
                binding.loadingSpinKit.visibility = View.GONE
            }
            recipeListAdapter.setSearchLists(recipes)
        }

    }


}