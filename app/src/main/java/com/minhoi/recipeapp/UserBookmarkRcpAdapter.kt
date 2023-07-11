package com.minhoi.recipeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.databinding.RecipeBookmarkItemRowBinding
import com.minhoi.recipeapp.databinding.RecipeRandomItemRowBinding
import com.minhoi.recipeapp.model.RecipeDto

class UserBookmarkRcpAdapter(private val context : Context, private val dataSet : ArrayList<RecipeDto>) : RecyclerView.Adapter<UserBookmarkRcpAdapter.ViewHolder>() {

    class ViewHolder(binding : RecipeBookmarkItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val rcpName : TextView
        val rcpImage : ImageView
        val whenBookmarked : TextView

        init {
            rcpName = binding.bookmarkRcpName
            rcpImage = binding.bookmarkRcpImage
            whenBookmarked = binding.bookmarkRcpDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<RecipeBookmarkItemRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recipe_bookmark_item_row, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ref = Ref()
        holder.rcpName.text = dataSet[position].rcp_NM
        holder.whenBookmarked.text = ref.getDate()
        Glide.with(context)
            .load(dataSet[position].att_FILE_NO_MK)
            .into(holder.rcpImage)
    }
}