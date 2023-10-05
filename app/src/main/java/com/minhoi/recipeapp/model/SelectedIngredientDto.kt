package com.minhoi.recipeapp.model

import android.os.Parcelable
import com.minhoi.recipeapp.adapter.recyclerview.IngredientListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedIngredientDto (
    override val name: String = "",
    override val imagePath: String= "")
: IngredientListItem, Parcelable
