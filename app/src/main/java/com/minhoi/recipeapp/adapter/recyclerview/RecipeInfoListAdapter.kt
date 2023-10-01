package com.minhoi.recipeapp.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.InfoIngredientListItemRowBinding
import com.minhoi.recipeapp.databinding.RecipeCookwayItemRowBinding
import com.minhoi.recipeapp.model.InfoIngredientDto
import com.minhoi.recipeapp.model.IngredientDto
import com.minhoi.recipeapp.model.ItemViewType
import com.minhoi.recipeapp.model.RecipeCookingWayData
import com.minhoi.recipeapp.model.SelectedIngredientDto

class RecipeInfoListAdapter(val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val infoList = mutableListOf<ItemViewType>()

    inner class IngredientViewHolder(binding : InfoIngredientListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        private val name = binding.infoIngredientName

        fun bind(items : InfoIngredientDto) {
            name.text = items.name
        }
    }

    inner class CookWayViewHolder(binding : RecipeCookwayItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        private val wayText = binding.cookingWayText
        private val wayImage = binding.cookingWayImage

        fun bind(items : RecipeCookingWayData) {
            wayText.text = items.cookingWayText
            Glide.with(context)
                .load(items.cookingWayImageUrl)
                .into(wayImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layout = when(viewType) {
            VIEW_TYPE_INFO_INGREDIENT -> R.layout.info_ingredient_list_item_row
            VIEW_TYPE_INFO_COOKWAY-> R.layout.recipe_cookway_item_row
            else -> throw IllegalArgumentException("Invalid type")
        }

        val view = when (viewType) {
            VIEW_TYPE_INFO_INGREDIENT -> {
            val ingredientBinding = DataBindingUtil.inflate<InfoIngredientListItemRowBinding>(
                LayoutInflater.from(parent.context),
                layout, parent, false)
            IngredientViewHolder(ingredientBinding)
            }
            VIEW_TYPE_INFO_COOKWAY -> {
            val selectedBinding = DataBindingUtil.inflate<RecipeCookwayItemRowBinding>(
                LayoutInflater.from(parent.context),
                layout, parent, false)
            CookWayViewHolder(selectedBinding)
            }

            else -> throw IllegalArgumentException("Invalid type")
    }
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = infoList[position]

        when (infoList[position].viewType) {
            VIEW_TYPE_INFO_INGREDIENT ->  {
                (holder as RecipeInfoListAdapter.IngredientViewHolder).bind(item as InfoIngredientDto)
            }
            VIEW_TYPE_INFO_COOKWAY -> {
                (holder as RecipeInfoListAdapter.CookWayViewHolder).bind(item as RecipeCookingWayData)
            }
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    fun setWays(items : ArrayList<RecipeCookingWayData>) {
        infoList.clear()
        for(i in items) {
            if(i.cookingWayText.length !=1) {
                infoList.add(i)
            }
        }
        notifyDataSetChanged()
    }

    fun setIngredients(items : List<InfoIngredientDto>) {
        infoList.clear()
        infoList.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return infoList[position].viewType
    }

    companion object {
        private val VIEW_TYPE_INFO_INGREDIENT = 1
        private val VIEW_TYPE_INFO_COOKWAY= 2
    }
}