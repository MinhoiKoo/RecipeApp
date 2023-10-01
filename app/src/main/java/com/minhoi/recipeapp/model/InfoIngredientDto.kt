package com.minhoi.recipeapp.model

data class InfoIngredientDto (
    val name : String,
    override val viewType: Int = 1
) : ItemViewType
