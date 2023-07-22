package com.minhoi.recipeapp.adapter

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
import com.minhoi.recipeapp.databinding.RecipeRandomItemRowBinding
import com.minhoi.recipeapp.model.RecipeDto

class RecipeListAdapter(val context: Context, private val itemClick : (RecipeDto) -> Unit)
    : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v : View, position : Int)
    }

    private lateinit var itemClickListener : OnItemClickListener
    private val recipeList = mutableListOf<RecipeDto>()

    inner class ViewHolder(binding : RecipeRandomItemRowBinding ) : RecyclerView.ViewHolder(binding.root) {
        val rcpImage : ImageView
        val rcpName : TextView

        init {
            rcpImage = binding.rcpImage
            rcpName = binding.rcpName
        }

        fun bind(items : RecipeDto) {
            itemView.setOnClickListener {
                itemClick(recipeList[adapterPosition])
            }
            rcpName.text = items.rcp_NM

            Glide.with(context)
                .load(items.att_FILE_NO_MK)
                .into(rcpImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<RecipeRandomItemRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recipe_random_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position])
    }

    fun setLists(items : List<RecipeDto>) {
        recipeList.clear()
        recipeList.addAll(items)
        notifyDataSetChanged()
    }





}