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
        return if (recipeNameList.size <= 10) {
            recipeNameList.size
        } else { 10 }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeNameList[position])
    }


    fun setLists(data : ArrayList<String>) {
        // 검색된 데이터 랜덤으로 섞어서 10개만 표출
        data.shuffle()
        recipeNameList.clear()
        recipeNameList.addAll(data.take(10))
        notifyDataSetChanged()
    }

}