package com.minhoi.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.minhoi.recipeapp.api.MyApi
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.api.RetrofitInstance
import com.minhoi.recipeapp.model.RcpResponse
import com.minhoi.recipeapp.model.RecipeDataModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var navController : NavController
    private val TAG = MainActivity::class.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setupWithNavController(bottomNavigationView, navController)



        // 기존 api 호출 방식에서 Firebase에 저장하여 호출하는 방식으로 변경.


//        dataToFirebase()
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("MyRecipeData")
//        myRef.removeValue()


//    fun dataToFirebase() {
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("MyRecipeData")
//
//        val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)
//
//        val call = retrofitInstance.getRecipe(1, 1000)
//        call.enqueue(object : Callback<RcpResponse> {
//            override fun onResponse(call: Call<RcpResponse>, response: Response<RcpResponse>) {
//
//                val recipeList = response.body()?.COOKRCP01?.row
//
//                recipeList?.forEach { recipe ->
//                    val recipeId = recipe.rcp_SEQ
//                    val manual01 = recipe.manual01+"xx"
//                    val manual02 = recipe.manual02+"xx"
//                    val manual03 = recipe.manual03+"xx"
//                    val manual04 = recipe.manual04+"xx"
//                    val manual05 = recipe.manual05+"xx"
//                    val manual06 = recipe.manual06+"xx"
//                    val manual07 = recipe.manual07+"xx"
//                    val manual08 = recipe.manual08+"xx"
//                    val manual09= recipe.manual09+"xx"
//                    val manual10 = recipe.manual10+"xx"
//                    val manual11 = recipe.manual11+"xx"
//                    val manual12 = recipe.manual12+"xx"
//                    val manual13 = recipe.manual13+"xx"
//                    val manual14 = recipe.manual14+"xx"
//                    val manual15 = recipe.manual15+"xx"
//                    val manual16 = recipe.manual16+"xx"
//                    val manual17 = recipe.manual17+"xx"
//                    val manual18 = recipe.manual18+"xx"
//                    val manual19 = recipe.manual19+"xx"
//                    val manual20 = recipe.manual20.toString()
//
//                    val manualList = arrayListOf<String>()
//                    manualList.add(manual01)
//                    manualList.add(manual02)
//                    manualList.add(manual03)
//                    manualList.add(manual04)
//                    manualList.add(manual05)
//                    manualList.add(manual06)
//                    manualList.add(manual07)
//                    manualList.add(manual08)
//                    manualList.add(manual09)
//                    manualList.add(manual10)
//                    manualList.add(manual11)
//                    manualList.add(manual12)
//                    manualList.add(manual13)
//                    manualList.add(manual14)
//                    manualList.add(manual15)
//                    manualList.add(manual16)
//                    manualList.add(manual17)
//                    manualList.add(manual18)
//                    manualList.add(manual19)
//                    manualList.add(manual20)
//
//                    val manual = java.lang.StringBuilder()
//                    manual.append(manual01 + manual02 + manual03 +
//                    manual04+
//                    manual05+
//                    manual06+
//                    manual07+
//                    manual08+
//                    manual09+
//                    manual10+
//                    manual11+
//                    manual12+
//                   manual13+
//                     manual14+
//                    manual15 +
//                     manual16+
//                    manual17+
//                     manual18+
//                    manual19+
//                    manual20)
//
//                    val image = java.lang.StringBuilder()
//                    val image01 = recipe.manual_IMG01.toString()
//                    val image02 = recipe.manual_IMG02.toString()
//                    val image03 = recipe.manual_IMG03.toString()
//                    val image04 = recipe.manual_IMG04.toString()
//                    val image05 = recipe.manual_IMG05.toString()
//                    val image06 = recipe.manual_IMG06.toString()
//                    val image07 = recipe.manual_IMG07.toString()
//                    val image08 = recipe.manual_IMG08.toString()
//                    val image09 = recipe.manual_IMG09.toString()
//                    val image10 = recipe.manual_IMG10.toString()
//                    val image11 = recipe.manual_IMG11.toString()
//                    val image12 = recipe.manual_IMG12.toString()
//                    val image13 = recipe.manual_IMG13.toString()
//                    val image14 = recipe.manual_IMG14.toString()
//                    val image15 = recipe.manual_IMG15.toString()
//                    val image16 = recipe.manual_IMG16.toString()
//                    val image17 = recipe.manual_IMG17.toString()
//                    val image18 = recipe.manual_IMG18.toString()
//                    val image19 = recipe.manual_IMG19.toString()
//                    val image20 = recipe.manual_IMG20.toString()
//                    val imageList = arrayListOf<String>()
//                    imageList.add(image01)
//                    imageList.add(image02)
//                    imageList.add(image03)
//                    imageList.add(image04)
//                    imageList.add(image05)
//                    imageList.add(image06)
//                    imageList.add(image07)
//                    imageList.add(image08)
//                    imageList.add(image09)
//                    imageList.add(image10)
//                    imageList.add(image11)
//                    imageList.add(image12)
//                    imageList.add(image13)
//                    imageList.add(image14)
//                    imageList.add(image15)
//                    imageList.add(image16)
//                    imageList.add(image17)
//                    imageList.add(image18)
//                    imageList.add(image19)
//                    imageList.add(image20)
//
//                    image.append(image01 + image02 + image03 + image04 + image05 + image06 + image07 + image08 + image09 + image10
//                    + image11 + image12 + image13 + image14 + image15 + image16 + image17 + image18 + image19 + image20)
//
//                    for (i in imageList.indices) {
//                        imageList
//                        manualList
//                    }
//
//
//                    //rcp_NM : String = "",
//                    //
//                    //    val rcp_SEQ : String = "",
//                    //    // 이미지 경로
//                    //    val att_FILE_NO_MK : String = "",
//                    //    val att_FILE_NO_MAIN: String = "",
//                    //    val rcp_PARTS_DTLS : String = "",
//                    //    val manual : String = "",
//                    //
//                    //    val image : String = "",
//                    //    val rcp_NA_TIP : String = "",
//                    //    val info_ENG : String = "",
//                    //    val rcp_PAT2 : String = "",
//                    //    val info_WGT : String = "",
//                    //    val info_CAR : String = "",
//                    //    val info_PRO : String = "",
//                    //    val info_FAT : String = "",
//                    //    val info_NA : String = ""
//                    // Firebase Realtime Database에 1개씩 저장
//                    myRef.child(recipeId).setValue(
//                        RecipeDataModel(recipe.rcp_NM,recipe.rcp_SEQ, recipe.att_FILE_NO_MK.toString(), recipe.att_FILE_NO_MAIN.toString(), recipe.rcp_PARTS_DTLS.toString(),
//                            manualList.joinToString(),imageList.joinToString()
//                        , recipe.rcp_NA_TIP.toString(), recipe.info_ENG.toString(), recipe.rcp_PAT2.toString(), recipe.info_WGT.toString(), recipe.info_CAR.toString()
//                            , recipe.info_PRO.toString(), recipe.info_FAT.toString(), recipe.info_NA.toString())) { error, _ ->
//                        if (error != null) {
//                            Log.d("data", error.toString())
//                        } else {
//                            Log.d("data", "success")
//                        }
//                    }
//
//
//                }
//            }

//            override fun onFailure(call: Call<RcpResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }
}