package com.minhoi.recipeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.RecipeRandomItemRowBinding
import com.minhoi.recipeapp.databinding.SearchAutocompleteItemRowBinding

class SearchAutoCompleteAdapter : RecyclerView.Adapter<SearchAutoCompleteAdapter.ViewHolder>() {

    private val recipeNameList = mutableListOf<String>()

    inner class ViewHolder(binding: SearchAutocompleteItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        val recipeTitle = binding.recipeTitle
        fun bind(title : String) {
            recipeTitle.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<SearchAutocompleteItemRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_autocomplete_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeNameList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeNameList[position])
    }


    fun setLists(data : ArrayList<String>) {
        recipeNameList.clear()
        recipeNameList.addAll(data)
        notifyDataSetChanged()
    }

}