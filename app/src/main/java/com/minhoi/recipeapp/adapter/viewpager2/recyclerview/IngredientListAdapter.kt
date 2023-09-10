package com.minhoi.recipeapp.adapter.viewpager2.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.minhoi.recipeapp.R

class IngredientListAdapter(private val onDeleteClickListener : (Int) -> Unit) :
    RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>() {

    private val ingredientList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder : IngredientViewHolder, position : Int) {
        val ingredient = ingredientList[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewIngredient: TextView = itemView.findViewById(R.id.textViewIngredient)
        private val imageViewDelete: ImageView = itemView.findViewById(R.id.imageViewDelete)

        fun bind(ingredient: String) {
            textViewIngredient.text = ingredient
            imageViewDelete.setOnClickListener {
                onDeleteClickListener(adapterPosition)
            }
        }
    }

    fun setIngredients(ingredients: List<String>) {
        ingredientList.clear()
        ingredientList.addAll(ingredients)
        notifyDataSetChanged()
    }

}