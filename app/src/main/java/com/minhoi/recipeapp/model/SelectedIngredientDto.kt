package com.minhoi.recipeapp.model

import com.minhoi.recipeapp.adapter.recyclerview.IngredientListItem

data class SelectedIngredientDto (
    override val name: String = " ",
    override val imagePath: String= " ")
: IngredientListItem
