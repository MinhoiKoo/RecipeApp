package com.minhoi.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import com.minhoi.recipeapp.databinding.ActivityRcpInfoBinding

class RcpInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRcpInfoBinding
    private lateinit var userId : String
    private lateinit var viewModel : RcpInfoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rcp_info)
        viewModel = ViewModelProvider(this).get(RcpInfoViewModel::class.java)

        val intent = getIntent()

        val rcpSeq = intent.getStringExtra("rcpSeq")
        val name = intent.getStringExtra("name")
        val ingredient = intent.getStringExtra("ingredient")
        val manual01 = intent.getStringExtra("manual01")
        val manual02 = intent.getStringExtra("manual02")
        val manual03 = intent.getStringExtra("manual03")

        val image01 = intent.getStringExtra("image01")
        val image02 = intent.getStringExtra("image02")
        val image03 = intent.getStringExtra("image03")

        val imageSrc = intent.getStringExtra("imageSrc")

        val menuName = findViewById<TextView>(R.id.menuName)
        menuName.text = name
        val ingredientText = findViewById<TextView>(R.id.menuIngredient)

        if (ingredient != null) {
            ingredientText.text = split(ingredient)
        }

        val back = findViewById<ImageView>(R.id.menuBackBtn)
        back.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(imageSrc)
            .into(binding.menuImage)

        UserApiClient.instance.me { user, error ->
            userId = user?.id.toString()
            viewModel.isBookmark(userId, rcpSeq!!)
        }


        viewModel.isBookmarked.observe(this) {
            if(it){
                binding.rcpBookmarkBtn.setImageResource(R.drawable.bookmarked)
            } else {
                binding.rcpBookmarkBtn.setImageResource(R.drawable.bookmark)
            }
        }

        binding.rcpBookmarkBtn.setOnClickListener {
            UserApiClient.instance.me { user, error ->
                if(viewModel.isBookmarked.value == false) {
                    viewModel.setBookmark(user?.id.toString(), rcpSeq!!)
                } else {
                    viewModel.deleteBookmark(user?.id.toString(), rcpSeq!!)
                }
            }

        }
    }

    private fun split(str : String) : String {
        val strSplit = str.split(",")
        val sb = StringBuilder()
        for( i in strSplit.indices) {
            sb.append(strSplit[i].trim() + "\n")
        }
        return sb.toString()
    }
}