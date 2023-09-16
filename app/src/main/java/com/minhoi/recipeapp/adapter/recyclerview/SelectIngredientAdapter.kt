package com.minhoi.recipeapp.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.IngredientSelectItemBinding
import com.minhoi.recipeapp.databinding.IngredientSelectedItemBinding
import com.minhoi.recipeapp.model.IngredientDto
import com.minhoi.recipeapp.model.SelectedIngredientDto

class SelectIngredientAdapter(private val context : Context, private val onClickListener : (IngredientListItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ingredientList = arrayListOf<IngredientListItem>()

    inner class ItemViewHolder(binding : IngredientSelectItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.ingredientImage
        private val name = binding.ingredientName

        fun bind(item : IngredientListItem) {
            itemView.setOnClickListener {
                val selectedIngredientDto = SelectedIngredientDto(item.name, item.imagePath)
                onClickListener(selectedIngredientDto)
            }
            name.text = item.name
            Glide.with(context)
                .load(item.imagePath)
                .into(image)
        }
    }

    inner class SelectedItemViewHolder(private val binding: IngredientSelectedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.ingredientImage
        private val name = binding.ingredientName


        fun bind(item: SelectedIngredientDto) {
            itemView.setOnClickListener {
                val selectedIngredientDto = SelectedIngredientDto(item.name, item.imagePath)
                onClickListener(selectedIngredientDto)
            }
            name.text = item.name
            Glide.with(context)
                .load(item.imagePath)
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = when(viewType) {
            VIEW_TYPE_INGREDIENT -> R.layout.ingredient_select_item
            VIEW_TYPE_SELECTED_INGREDIENT -> R.layout.ingredient_selected_item
            else -> throw IllegalArgumentException("Invalid type")
        }

        val view = when (viewType) {
            VIEW_TYPE_INGREDIENT -> {
                val ingredientBinding = DataBindingUtil.inflate<IngredientSelectItemBinding>(
                    LayoutInflater.from(parent.context),
                    layout, parent, false)
                ItemViewHolder(ingredientBinding)
            }
            VIEW_TYPE_SELECTED_INGREDIENT -> {
                val selectedBinding = DataBindingUtil.inflate<IngredientSelectedItemBinding>(
                    LayoutInflater.from(parent.context),
                    layout, parent, false)
                SelectedItemViewHolder(selectedBinding)
            }
            else -> throw IllegalArgumentException("Invalid type")
        }
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = ingredientList[position]) {
            is IngredientDto -> (holder as ItemViewHolder).bind(item)
            is SelectedIngredientDto -> (holder as SelectedItemViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(ingredientList[position]) {
            is IngredientDto -> VIEW_TYPE_INGREDIENT
            is SelectedIngredientDto -> VIEW_TYPE_SELECTED_INGREDIENT
            else -> super.getItemViewType(position)
        }
    }

    fun setList(items : List<IngredientDto>) {
        ingredientList.clear()
        ingredientList.addAll(items)
        notifyDataSetChanged()
    }

    fun setSelectedList(items : List<SelectedIngredientDto>) {
        ingredientList.clear()
        ingredientList.addAll(items)
        notifyDataSetChanged()
    }

    fun deleteSelectedList(items : SelectedIngredientDto) {
        val position = ingredientList.indexOf(items)
        if (position != -1) {
            ingredientList.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    companion object {
        private val VIEW_TYPE_INGREDIENT = 1
        private val VIEW_TYPE_SELECTED_INGREDIENT = 2
    }

}