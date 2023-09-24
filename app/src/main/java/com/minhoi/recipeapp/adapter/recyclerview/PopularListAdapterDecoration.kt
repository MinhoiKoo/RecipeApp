package com.minhoi.recipeapp.adapter.recyclerview

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PopularListAdapterDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val count = state.itemCount
        val spacing = (15f * Resources.getSystem().displayMetrics.density).toInt()

        when(position) {
            0 -> {
                outRect.left = spacing
                outRect.right = spacing / 2
            }
            count -1 -> {
                outRect.right = spacing
                outRect.left = spacing / 2
            }
            else -> {
                outRect.left = spacing / 2
                outRect.right = spacing / 2

            }

        }
    }
}