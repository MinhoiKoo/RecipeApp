
package com.minhoi.recipeapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.util.ViewModelFactory
import com.minhoi.recipeapp.ui.viewmodel.HomeViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var viewModel:HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProvider(this, ViewModelFactory(this.application))
            .get(HomeViewModel::class.java)
        Handler().postDelayed(Runnable {
            // 앱의 main activity로 넘어가기
            val i = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(i)
            // 현재 액티비티 닫기
            finish()
        }, 2500L)
    }
}