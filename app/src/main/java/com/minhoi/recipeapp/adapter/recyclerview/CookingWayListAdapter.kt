package com.minhoi.recipeapp.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.RecipeCookwayItemRowBinding
import com.minhoi.recipeapp.model.RecipeCookingWayData

class CookingWayListAdapter(val context : Context) : RecyclerView.Adapter<CookingWayListAdapter.ViewHolder>() {

    private val cookingWayList = mutableListOf<RecipeCookingWayData>()

    inner class ViewHolder(binding : RecipeCookwayItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        private val wayText = binding.cookingWayText
        private val wayImage = binding.cookingWayImage

        fun bind(items : RecipeCookingWayData) {
            wayText.text = items.cookingWayText
            Glide.with(context)
                .load(items.cookingWayImageUrl)
                .into(wayImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<RecipeCookwayItemRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recipe_cookway_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cookingWayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cookingWayList[position])
    }

    fun setWays(items : ArrayList<RecipeCookingWayData>) {
        cookingWayList.clear()
        for(i in items) {
            if(i.cookingWayText.length !=1) {
                cookingWayList.add(i)
            }
        }
        notifyDataSetChanged()
    }
}