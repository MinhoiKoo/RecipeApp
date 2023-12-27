package com.minhoi.recipeapp.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.RecipeListItemRowBinding
import com.minhoi.recipeapp.databinding.RecipePopularItemBinding
import com.minhoi.recipeapp.databinding.RecipeRandomItemRowBinding
import com.minhoi.recipeapp.model.InfoIngredientDto
import com.minhoi.recipeapp.model.ItemViewType
import com.minhoi.recipeapp.model.RecipeCookingWayData
import com.minhoi.recipeapp.model.RecipeDataModel
import com.minhoi.recipeapp.model.SearchRecipeDto

class RecipeListAdapter(val context: Context, private val itemClick : (ItemViewType) -> Unit)
    : Adapter<RecyclerView.ViewHolder>() {

    private val recipeList = mutableListOf<ItemViewType>()

    inner class RandomViewHolder(binding : RecipeRandomItemRowBinding ) : RecyclerView.ViewHolder(binding.root) {
        private val rcpImage : ImageView
        private val rcpName : TextView

        init {
            rcpImage = binding.rcpImage
            rcpName = binding.rcpName
        }

        fun bind(items : RecipeDataModel) {
            itemView.setOnClickListener {
                itemClick(recipeList[bindingAdapterPosition])
            }
            rcpName.text = items.rcp_NM

            Glide.with(context)
                .load(items.att_FILE_NO_MK)
                .into(rcpImage)
        }

    }

    inner class SearchViewHolder(binding : RecipeListItemRowBinding ) : RecyclerView.ViewHolder(binding.root) {
        private val rcpImage : ImageView
        private val rcpName : TextView
        private val rcpWhenSaved : TextView

        init {
            rcpImage = binding.bookmarkRcpImage
            rcpName = binding.bookmarkRcpName
            rcpWhenSaved = binding.bookmarkRcpDate
        }

        fun bind(items : SearchRecipeDto) {
            itemView.setOnClickListener {
                itemClick(recipeList[bindingAdapterPosition])
            }
            rcpName.text = items.rcp_NM
            rcpWhenSaved.text = items.rcp_PAT2
            Glide.with(context)
                .load(items.att_FILE_NO_MK)
                .into(rcpImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = when(viewType) {
            VIEW_TYPE_RANDOM -> R.layout.recipe_random_item_row
            VIEW_TYPE_SEARCH -> R.layout.recipe_list_item_row
            else -> throw IllegalArgumentException("Invalid type")

        }


        val view = when (viewType) {

            VIEW_TYPE_RANDOM -> {
                val randomBinding = DataBindingUtil.inflate<RecipeRandomItemRowBinding>(
                        LayoutInflater.from(parent.context), layout, parent, false)
                RandomViewHolder(randomBinding)
            }
            VIEW_TYPE_SEARCH -> {
                val searchBinding = DataBindingUtil.inflate<RecipeListItemRowBinding>(
                        LayoutInflater.from(parent.context), layout, parent, false)
                SearchViewHolder(searchBinding)
            }
            else -> throw IllegalArgumentException("Invalid type")
        }

        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = recipeList[position]

        when (recipeList[position].viewType) {
            VIEW_TYPE_RANDOM ->  {
                (holder as RecipeListAdapter.RandomViewHolder).bind(item as RecipeDataModel)
            }
            VIEW_TYPE_SEARCH -> {
                (holder as RecipeListAdapter.SearchViewHolder).bind(item as SearchRecipeDto)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    fun setRandomLists(items : List<RecipeDataModel>) {
        recipeList.clear()
        recipeList.addAll(items)
        notifyDataSetChanged()
    }
    fun setSearchLists(items : List<SearchRecipeDto>) {
        recipeList.clear()
        recipeList.addAll(items)
        notifyDataSetChanged()
    }




    override fun getItemViewType(position: Int): Int {
        return recipeList[position].viewType
    }

    companion object {
        private const val VIEW_TYPE_RANDOM = 1
        private const val VIEW_TYPE_SEARCH= 2
    }

}