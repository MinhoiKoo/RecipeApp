package com.minhoi.recipeapp.model

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("RCP_NM")
    val rcp_NM : String = "",
    @SerializedName("RCP_SEQ")
    val rcp_SEQ : String = "",
    // 이미지 경로
    @SerializedName("ATT_FILE_NO_MK")
    val att_FILE_NO_MK : String? = "",
    @SerializedName("ATT_FILE_NO_MAIN")
    val att_FILE_NO_MAIN: String? = "",
    @SerializedName("RCP_PARTS_DTLS")
    val rcp_PARTS_DTLS : String? = "",
    @SerializedName("MANUAL01")
    val manual01 : String? = "",
    @SerializedName("MANUAL02")
    val manual02 : String? = "",
    @SerializedName("MANUAL03")
    val manual03 : String? = "",
    @SerializedName("MANUAL04")
    val manual04 : String? = "",
    @SerializedName("MANUAL05")
    val manual05 : String? = "",
    @SerializedName("MANUAL06")
    val manual06 : String? = "",
    @SerializedName("MANUAL07")
    val manual07 : String? = "",
    @SerializedName("MANUAL08")
    val manual08 : String? = "",
    @SerializedName("MANUAL09")
    val manual09 : String? = "",
    @SerializedName("MANUAL10")
    val manual10 : String? = "",
    @SerializedName("MANUAL11")
    val manual11 : String? = "",
    @SerializedName("MANUAL12")
    val manual12 : String? = "",
    @SerializedName("MANUAL13")
    val manual13 : String? = "",
    @SerializedName("MANUAL14")
    val manual14 : String? = "",
    @SerializedName("MANUAL15")
    val manual15 : String? = "",
    @SerializedName("MANUAL16")
    val manual16 : String? = "",
    @SerializedName("MANUAL17")
    val manual17 : String? = "",
    @SerializedName("MANUAL18")
    val manual18 : String? = "",
    @SerializedName("MANUAL19")
    val manual19 : String? = "",
    @SerializedName("MANUAL20")
    val manual20 : String? = "",
    @SerializedName("MANUAL_IMG01")
    val manual_IMG01 : String? = "",
    @SerializedName("MANUAL_IMG02")
    val manual_IMG02 : String? = "",
    @SerializedName("MANUAL_IMG03")
    val manual_IMG03 : String? = "",
    @SerializedName("MANUAL_IMG04")
    val manual_IMG04 : String? = "",
    @SerializedName("MANUAL_IMG05")
    val manual_IMG05 : String? = "",
    @SerializedName("MANUAL_IMG06")
    val manual_IMG06 : String? = "",
    @SerializedName("MANUAL_IMG07")
    val manual_IMG07 : String? = "",
    @SerializedName("MANUAL_IMG08")
    val manual_IMG08 : String? = "",
    @SerializedName("MANUAL_IMG09")
    val manual_IMG09 : String? = "",
    @SerializedName("MANUAL_IMG10")
    val manual_IMG10 : String? = "",
    @SerializedName("MANUAL_IMG11")
    val manual_IMG11 : String? = "",
    @SerializedName("MANUAL_IMG12")
    val manual_IMG12 : String? = "",
    @SerializedName("MANUAL_IMG13")
    val manual_IMG13 : String? = "",
    @SerializedName("MANUAL_IMG14")
    val manual_IMG14 : String? = "",
    @SerializedName("MANUAL_IMG15")
    val manual_IMG15 : String? = "",
    @SerializedName("MANUAL_IMG16")
    val manual_IMG16 : String? = "",
    @SerializedName("MANUAL_IMG17")
    val manual_IMG17 : String? = "",
    @SerializedName("MANUAL_IMG18")
    val manual_IMG18 : String? = "",
    @SerializedName("MANUAL_IMG19")
    val manual_IMG19 : String? = "",
    @SerializedName("MANUAL_IMG20")
    val manual_IMG20 : String? = "",
    @SerializedName("RCP_NA_TIP")
    val rcp_NA_TIP : String? = "",
    @SerializedName("INFO_ENG")
    val info_ENG : String? = "",
    @SerializedName("RCP_PAT2")
    val rcp_PAT2 : String? = "",
    @SerializedName("INFO_WGT")
    val info_WGT : String? = "",
    @SerializedName("INFO_CAR")
    val info_CAR : String? = "",
    @SerializedName("INFO_PRO")
    val info_PRO : String? = "",
    @SerializedName("INFO_FAT")
    val info_FAT : String? = "",
    @SerializedName("INFO_NA")
    val info_NA : String? = ""

)