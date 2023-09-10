package com.minhoi.recipeapp.adapter.viewpager2.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.RecipePopularItemBinding
import com.minhoi.recipeapp.model.RecipeDataModel

class PopularRecipeListAdapter(private val context : Context) : RecyclerView.Adapter<PopularRecipeListAdapter.ViewHolder>() {

    private val recipeList = arrayListOf<RecipeDataModel>()
    inner class ViewHolder(binding : RecipePopularItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.recipeImage
        val name = binding.recipeName

        fun bind(items : RecipeDataModel) {
            Glide.with(context)
                .load(items.att_FILE_NO_MAIN)
                .into(image)
            name.text = items.rcp_NM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<RecipePopularItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recipe_popular_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position])
    }

    fun setList(items : ArrayList<RecipeDataModel>) {
        recipeList.clear()
        // data가 10개이상 존재/하지 않을때 구분
        val endIndex = maxOf(0, items.size - 1)
        val startIndex = maxOf(0, endIndex - 9)

        for (i in endIndex downTo startIndex) {
            recipeList.add(items[i])
        }
        recipeList.shuffle()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.recipe_popular_item
    }
}