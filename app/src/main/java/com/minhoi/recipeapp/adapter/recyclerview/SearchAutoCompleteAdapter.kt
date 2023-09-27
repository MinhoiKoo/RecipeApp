package com.minhoi.recipeapp.adapter.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.SearchAutocompleteItemRowBinding

class SearchAutoCompleteAdapter(private val onCLickListener : (String) -> Unit) : RecyclerView.Adapter<SearchAutoCompleteAdapter.ViewHolder>() {

    private val recipeNameList = mutableListOf<Pair<String, String>>()

    inner class ViewHolder(binding: SearchAutocompleteItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        private val recipeTitle = binding.recipeTitle

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCLickListener(recipeNameList[position].first)
                }
            }
        }

        fun bind(data : Pair<String, String>) {
            recipeTitle.text = data.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<SearchAutocompleteItemRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_autocomplete_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (recipeNameList.size <= 20) {
            recipeNameList.size
        } else { 20 }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeNameList[position])
        Log.d("seesesese", "onBindViewHolder: ${recipeNameList[position]}")
    }


    fun setLists(data : ArrayList<Pair<String, String>>) {
        // 검색된 데이터 랜덤으로 섞어서 10개만 표출
        data.shuffle()
        recipeNameList.clear()
        recipeNameList.addAll(data.take(20))
        notifyDataSetChanged()
    }

}