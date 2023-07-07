package com.minhoi.recipeapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.minhoi.recipeapp.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private lateinit var binding : FragmentMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false )

        binding.homeBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_homeFragment)
        }

        binding.searchBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_searchFragment)
        }

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }



        return binding.root
    }

}