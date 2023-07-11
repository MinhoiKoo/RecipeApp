package com.minhoi.recipeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.databinding.RecipeRandomItemRowBinding
import com.minhoi.recipeapp.model.RecipeDto

class RcpListAdapter(val context: Context, private val dataSet: ArrayList<RecipeDto> ) : RecyclerView.Adapter<RcpListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v : View, position : Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    private lateinit var itemClickListener : OnItemClickListener

    class ViewHolder(binding : RecipeRandomItemRowBinding ) : RecyclerView.ViewHolder(binding.root) {
        val rcpImage : ImageView
        val rcpName : TextView

        init {
            rcpImage = binding.rcpImage
            rcpName = binding.rcpName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<RecipeRandomItemRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recipe_random_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.rcpName.text = dataSet[position].rcp_NM
        Glide.with(context)
            .load(dataSet[position].att_FILE_NO_MK)
            .into(holder.rcpImage)
    }
}