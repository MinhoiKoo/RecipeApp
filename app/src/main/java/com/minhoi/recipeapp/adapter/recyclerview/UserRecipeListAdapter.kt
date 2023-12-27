package com.minhoi.recipeapp.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.model.UserRecipeData
import com.minhoi.recipeapp.databinding.RecipeListItemRowBinding

class UserRecipeListAdapter(private val context : Context, private val dataSet : ArrayList<Pair<String, UserRecipeData>>, private val onClickListener : (Pair<String, UserRecipeData>) -> Unit) : RecyclerView.Adapter<UserRecipeListAdapter.ViewHolder>() {

    inner class ViewHolder(binding : RecipeListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val rcpName : TextView
        val rcpImage : ImageView
        val whenEdited : TextView

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                onClickListener(dataSet[position])
            }
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
        holder.rcpName.text = dataSet[position].second.title
        holder.whenEdited.text = dataSet[position].second.date
        Glide.with(context)
            .load(dataSet[position].second.imagePath)
            .into(holder.rcpImage)
    }
}