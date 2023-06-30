package com.minhoi.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.databinding.FragmentHomeBinding
import com.minhoi.recipeapp.model.RecipeDto

class HomeFragment : Fragment() {

    private lateinit var viewModel : HomeViewModel
    private lateinit var binding : FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        viewModel.getRandomRcp(1,6)

        val rv = binding.recipeRv
        viewModel.liveRcpList.observe(viewLifecycleOwner) {
            val adapter = RcpListAdapter(context!!, it as ArrayList<RecipeDto>)
            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(context)
        }

        return binding.root
    }

}