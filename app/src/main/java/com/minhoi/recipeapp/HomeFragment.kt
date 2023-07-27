package com.minhoi.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.sdk.user.UserApiClient
import com.minhoi.recipeapp.adapter.RecipeListAdapter
import com.minhoi.recipeapp.databinding.FragmentHomeBinding
import com.minhoi.recipeapp.model.KakaoUserAPIModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var viewModel : HomeViewModel
    private lateinit var binding : FragmentHomeBinding
    private var userId : String? = null
    private lateinit var myAdapter : RecipeListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreateView", "onCreateView")
        // Inflate the layout for this fragment
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                userId = user.id.toString()
            }
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.apply {
            searchBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }

            mypageBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_mypageFragment)
            }

            refrigeratorBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_refrigeratorFragment)
            }
        }

        // 나만의 레시피 추가 화면으로 이동, userId 는 비동기 방식으로 가져오기 때문에 코루틴 내부에서 실행.

        lifecycleScope.launch {
            val userId = viewModel.getUser()
            binding.btnAddUserRecipe.setOnClickListener {
                if(userId == "") {
                    Toast.makeText(requireContext(), "로그인 후 이용 가능합니다.", Toast.LENGTH_LONG).show()
                } else {
                    startActivity(Intent(requireActivity(), UserRecipeAddActivity::class.java))
                }
            }
        }

        val range = 1..1100
        val indexNum = range.random()

        viewModel.getRandomRcp()

        myAdapter = RecipeListAdapter(requireContext()) {
            val intent = Intent(requireActivity(), RcpInfoActivity::class.java)
            intent.apply {
                putExtra("rcpSeq", it.rcp_SEQ)
            }
            startActivity(intent)
        }

        viewModel.liveRcpList.observe(this) {
            myAdapter.setLists(it)
        }

        binding.recipeRv.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)

        }

        return binding.root
    }

}