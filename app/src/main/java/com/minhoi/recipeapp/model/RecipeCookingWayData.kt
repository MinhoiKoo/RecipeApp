package com.minhoi.recipeapp.model

data class RecipeCookingWayData(
    val cookingWayText: String = "",
    val cookingWayImageUrl: String = "",
    override val viewType: Int = 2
) : ItemViewType
