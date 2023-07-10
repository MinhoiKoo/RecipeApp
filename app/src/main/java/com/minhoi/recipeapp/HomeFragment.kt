package com.minhoi.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        Log.d("onCreateView", "onCreateView")
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.searchBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.mypageBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mypageFragment)
        }



        val range = 1..1100
        val indexNum = range.random()

        viewModel.getRandomRcp()



        val rv = binding.recipeRv


        viewModel.liveRcpList.observe(viewLifecycleOwner) {
            val adapter = RcpListAdapter(context!!, it as ArrayList<RecipeDto>)
            rv.adapter = adapter
            rv.layoutManager = GridLayoutManager(activity, 2)

            adapter.setItemClickListener(object : RcpListAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    val intent = Intent(activity, RcpInfoActivity::class.java)
                    intent.putExtra("name", it[position].RCP_NM)
                    intent.putExtra("ingredient", it[position].RCP_PARTS_DTLS)
                    intent.putExtra("manual01", it[position].MANUAL01)
                    intent.putExtra("manual02", it[position].MANUAL02)
                    intent.putExtra("manual03", it[position].MANUAL03)
                    intent.putExtra("image01", it[position].MANUAL_IMG01)
                    intent.putExtra("image02", it[position].MANUAL_IMG02)
                    intent.putExtra("image03", it[position].MANUAL_IMG03)
                    startActivity(intent)
                }
            })
        }

        return binding.root
    }

}