package com.minhoi.recipeapp.model

import com.minhoi.recipeapp.adapter.recyclerview.IngredientListItem

class IngredientDto (
    override val name: String = "",
    override val imagePath: String = "")
    : IngredientListItem
