package com.minhoi.recipeapp.model

data class RecipeDataModel(

    val rcp_NM : String = "",

    val rcp_SEQ : String = "",
    // 이미지 경로
    val att_FILE_NO_MK : String = "",
    val att_FILE_NO_MAIN: String = "",
    val rcp_PARTS_DTLS : String = "",
    val manual : String = "",

    val image : String = "",
    val rcp_NA_TIP : String = "",
    val info_ENG : String = "",
    val rcp_PAT2 : String = "",
    val info_WGT : String = "",
    val info_CAR : String = "",
    val info_PRO : String = "",
    val info_FAT : String = "",
    val info_NA : String = "",

    override val viewType: Int = 1
) : ItemViewType
