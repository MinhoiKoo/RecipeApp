package com.minhoi.recipeapp

data class User (
    val id : String,
    val nickName : String,
    val bookmarkedRecipe : Int? = null
)
