package com.minhoi.recipeapp.ui.fragment

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
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.ui.viewmodel.HomeViewModel
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.RcpInfoActivity
import com.minhoi.recipeapp.UserRecipeAddActivity
import com.minhoi.recipeapp.adapter.viewpager2.recyclerview.PopularRecipeListAdapter
import com.minhoi.recipeapp.adapter.viewpager2.recyclerview.RecipeListAdapter
import com.minhoi.recipeapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private val TAG = HomeFragment::class.simpleName
    private lateinit var viewModel : HomeViewModel
    private lateinit var binding : FragmentHomeBinding
    private lateinit var randomListAdapter : RecipeListAdapter
    private lateinit var popularListAdapter : PopularRecipeListAdapter
    private lateinit var concatAdapter : ConcatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreateView", "onCreateView")

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        // 나만의 레시피 추가 화면으로 이동, userId 는 비동기 방식으로 가져오기 때문에 코루틴 내부에서 순차적으로 실행.
        lifecycleScope.launch {
            val userId = viewModel.getUser() // 코루틴 일시중지
            // resume 후 아래 코드 실행
            binding.btnAddUserRecipe.setOnClickListener {
                if(userId == "") {
                    Toast.makeText(requireContext(), "로그인 후 이용 가능합니다.", Toast.LENGTH_LONG).show()
                } else {
                    startActivity(Intent(requireActivity(), UserRecipeAddActivity::class.java))
                }
            }
        }

        randomListAdapter = RecipeListAdapter(requireContext()) {
            // onClickListener
            val intent = Intent(requireActivity(), RcpInfoActivity::class.java)
            intent.putExtra("rcpSeq", it.rcp_SEQ)
            startActivity(intent)
        }

        popularListAdapter = PopularRecipeListAdapter(requireContext())



        binding.recipeRv.apply {
            isNestedScrollingEnabled = false
            adapter = randomListAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
            }

        binding.popularRecipeRv.apply {
            setHasFixedSize(true)
            adapter = popularListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }


        viewModel.liveRcpList.observe(viewLifecycleOwner) {
            randomListAdapter.setLists(it)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val queue = viewModel.getPopularRcp()
            withContext(Dispatchers.Main) {
                popularListAdapter.setList(queue)
            }
        }

        return binding.root
    }

    private val customSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (concatAdapter.getItemViewType(position)) {
                R.layout.recipe_random_item_row -> 1
                else -> 2
            }
        }
    }

}