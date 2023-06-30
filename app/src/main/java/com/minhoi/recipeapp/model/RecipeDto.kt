package com.minhoi.recipeapp.model

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("RCP_NM")
    val RCP_NM : String?,
    // 이미지 경로
    @SerializedName("ATT_FILE_NO_MK")
    val ATT_FILE_NO_MK : String?
)