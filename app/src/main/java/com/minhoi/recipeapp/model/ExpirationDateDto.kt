package com.minhoi.recipeapp.model

import android.os.Parcelable
import com.minhoi.recipeapp.adapter.recyclerview.IngredientListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExpirationDateDto(
    override val imagePath: String,
    override val name: String,
    var date: String = "00.00.00 까지"
) : IngredientListItem, Parcelable
