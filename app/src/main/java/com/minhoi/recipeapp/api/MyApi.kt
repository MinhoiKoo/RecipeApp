package com.minhoi.recipeapp.api

import com.minhoi.recipeapp.model.RcpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApi {

    @GET("COOKRCP01/json/{startidx}/{endidx}")
    fun getRecipe(
        @Path("startidx") startidx: Int,
        @Path("endidx") endidx: Int
    ): Response<RcpResponse>

    @GET("COOKRCP01/json/{startidx}/{endidx}/RCP_NM={rcpName}")
    suspend fun searchRecipe(
        @Path("startidx") startidx: Int,
        @Path("endidx") endidx: Int,
        @Path("rcpName") rcpName : String,
    ) : Response<RcpResponse>

}