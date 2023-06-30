package com.minhoi.recipeapp.model

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("RCP_NM")
    val RCP_NM : String?,
    // 이미지 경로
    @SerializedName("ATT_FILE_NO_MK")
    val ATT_FILE_NO_MK : String?,
    @SerializedName("RCP_PARTS_DTLS")
    val RCP_PARTS_DTLS : String?,
    @SerializedName("MANUAL01")
    val MANUAL01 : String?,
    @SerializedName("MANUAL02")
    val MANUAL02 : String?,
    @SerializedName("MANUAL03")
    val MANUAL03 : String?,
    @SerializedName("MANUAL_IMG01")
    val MANUAL_IMG01 : String?,
    @SerializedName("MANUAL_IMG02")
    val MANUAL_IMG02 : String?,
    @SerializedName("MANUAL_IMG03")
    val MANUAL_IMG03 : String?
)