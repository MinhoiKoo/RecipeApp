package com.minhoi.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class RcpInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rcp_info)
        val intent = getIntent()

        val name = intent.getStringExtra("name")
        val ingredient = intent.getStringExtra("ingredient")
        val manual01 = intent.getStringExtra("manual01")
        val manual02 = intent.getStringExtra("manual02")
        val manual03 = intent.getStringExtra("manual03")

        val image01 = intent.getStringExtra("image01")
        val image02 = intent.getStringExtra("image02")
        val image03 = intent.getStringExtra("image03")

        val split = ingredient?.split(",")
        val sb = StringBuilder()
        for( i in 0 until split!!.size) {
            sb.append(split[i] + "\n")
        }
        Log.d("str", sb.toString())
        val text = findViewById<TextView>(R.id.textView)
        text.text = sb.toString()
    }
}