package com.minhoi.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.minhoi.recipeapp.api.MyApi
import com.minhoi.recipeapp.api.RetrofitInstance
import com.minhoi.recipeapp.model.RcpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 기존 api 호출 방식에서 Firebase에 저장하여 호출하는 방식으로 변경.
//        dataToFirebase()
    }



    fun dataToFirebase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("RecipeData")

        val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)

        val call = retrofitInstance.getRecipe(1001, 1111)
        call.enqueue(object : Callback<RcpResponse> {
            override fun onResponse(call: Call<RcpResponse>, response: Response<RcpResponse>) {

                val recipeList = response.body()?.COOKRCP01?.row

                recipeList?.forEach { recipe ->
                    val recipeId = recipe.RCP_SEQ

                    // Firebase Realtime Database에 1개씩 저장
                    myRef.child(recipeId).setValue(recipe) { error, _ ->
                        if (error != null) {
                            println("recipeId 저장 실패: $error")
                        } else {
                            println("recipeId 저장 성공: $recipeId")
                        }
                    }


                }
            }

            override fun onFailure(call: Call<RcpResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}