package com.minhoi.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.adapter.RecipeListAdapter
import com.minhoi.recipeapp.databinding.ActivityRecipeListBinding
import com.minhoi.recipeapp.model.RecipeDto

class RecipeListActivity : AppCompatActivity() {

    private val TAG = RecipeListActivity::class.java.simpleName
    private lateinit var binding : ActivityRecipeListBinding
    private lateinit var viewModel : RecipeListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list)
        viewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)

        val intent = intent
        val ingredientList = intent.getStringArrayListExtra("ingredientList")

        viewModel.getRecipe(ingredientList!!)


//        viewModel.recipeList.observe(this) { recipes ->
//            val rv = binding.recipeListRv
//            val adapter = RecipeListAdapter(this, recipes as ArrayList<RecipeDto>)
//            rv.adapter = adapter
//            rv.layoutManager = LinearLayoutManager(this)
//
//            adapter.setItemClickListener(object : RecipeListAdapter.OnItemClickListener {
//                override fun onClick(v: View, position: Int) {
//                    val recipe = recipes[position]
//                    val intent = Intent(this@RecipeListActivity, RcpInfoActivity::class.java).apply {
//                        putExtra("name", recipe.rcp_NM)
//                        putExtra("ingredient", recipe.rcp_PARTS_DTLS)
//                        putExtra("manual01", recipe.manual01)
//                        putExtra("manual02", recipe.manual02)
//                        putExtra("manual03", recipe.manual03)
//                        putExtra("image01", recipe.manual_IMG01)
//                        putExtra("image02", recipe.manual_IMG02)
//                        putExtra("image03", recipe.manual_IMG03)
//                        putExtra("imageSrc", recipe.att_FILE_NO_MK)
//                        putExtra("rcpSeq", recipe.rcp_SEQ)
//                    }
//                    startActivity(intent)
//                }
//            })
//        }
//
    }


}