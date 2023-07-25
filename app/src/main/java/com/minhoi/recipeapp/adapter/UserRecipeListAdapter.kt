package com.minhoi.recipeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.UserRecipeData
import com.minhoi.recipeapp.databinding.RecipeListItemRowBinding

class UserRecipeListAdapter(private val context : Context, private val dataSet : ArrayList<UserRecipeData>) : RecyclerView.Adapter<UserRecipeListAdapter.ViewHolder>() {

    inner class ViewHolder(binding : RecipeListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val rcpName : TextView
        val rcpImage : ImageView
        val whenEdited : TextView

        init {
            rcpName = binding.bookmarkRcpName
            rcpImage = binding.bookmarkRcpImage
            whenEdited = binding.bookmarkRcpDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<RecipeListItemRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recipe_list_item_row, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rcpName.text = dataSet[position].title
        holder.whenEdited.text = dataSet[position].date

    }
}