package com.minhoi.recipeapp.adapter.viewpager2.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.RecipeListItemRowBinding
import com.minhoi.recipeapp.model.RecipeDto

class UserBookmarkListAdapter(private val context : Context, private val dataSet : ArrayList<RecipeDto>,
                              private val bookmarkedTime : ArrayList<String>) : RecyclerView.Adapter<UserBookmarkListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v : View, item : RecipeDto)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    private lateinit var itemClickListener : OnItemClickListener

    class ViewHolder(binding : RecipeListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
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

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, dataSet[position])
        }
        holder.rcpName.text = dataSet[position].rcp_NM
        holder.whenEdited.text = bookmarkedTime[position]
        Glide.with(context)
            .load(dataSet[position].att_FILE_NO_MK)
            .into(holder.rcpImage)
    }
}