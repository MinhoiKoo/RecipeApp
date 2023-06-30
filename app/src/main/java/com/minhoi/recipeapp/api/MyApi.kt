package com.minhoi.recipeapp.api

import com.minhoi.recipeapp.model.RcpResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyApi {

    @GET("COOKRCP01/json/{startidx}/{endidx}")
    fun getRecipe(
        @Path("startidx") startidx: Int,
        @Path("endidx") endidx: Int
    ): Call<RcpResponse>
}