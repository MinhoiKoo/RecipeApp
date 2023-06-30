package com.minhoi.recipeapp.model

import com.google.gson.annotations.SerializedName

data class RcpResponse(
    @SerializedName("COOKRCP01")
    var COOKRCP01 : RecipeList?
)