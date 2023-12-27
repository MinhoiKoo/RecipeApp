package com.minhoi.recipeapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.ui.viewmodel.HomeViewModel
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.ui.RcpInfoActivity
import com.minhoi.recipeapp.util.ViewModelFactory
import com.minhoi.recipeapp.adapter.recyclerview.PopularListAdapterDecoration
import com.minhoi.recipeapp.adapter.recyclerview.PopularRecipeListAdapter
import com.minhoi.recipeapp.adapter.recyclerview.RecipeListAdapter
import com.minhoi.recipeapp.adapter.recyclerview.RandomListAdapterDecoration
import com.minhoi.recipeapp.databinding.FragmentHomeBinding
import com.minhoi.recipeapp.model.RecipeDataModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val TAG = HomeFragment::class.simpleName
//    private val viewModel : HomeViewModel by activityViewModels()
    private lateinit var viewModel: HomeViewModel
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))
            .get(HomeViewModel::class.java)

        // 나만의 레시피 추가 화면으로 이동, userId 는 비동기 방식으로 가져오기 때문에 코루틴 내부에서 순차적으로 실행.

        randomListAdapter = RecipeListAdapter(requireContext()) {
            // onClickListener
            it as RecipeDataModel
            clickRecipe(it.rcp_SEQ)
            Log.d(TAG, "onCreateView: ${it.rcp_SEQ}")
        }

        popularListAdapter = PopularRecipeListAdapter(requireContext()) {
            //onClickListener
            clickRecipe(it)
            Log.d(TAG, "onCreateView: ${it}")

        }

        binding.recipeRv.apply {
            setHasFixedSize(true)
            adapter = randomListAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
            addItemDecoration(RandomListAdapterDecoration())
            }

        binding.popularRecipeRv.apply {
            setHasFixedSize(true)
            adapter = popularListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(PopularListAdapterDecoration())
        }

        setObserve()
        setSwipe()

        return binding.root
    }

    private fun setObserve() {
        viewModel.liveRcpList.observe(viewLifecycleOwner) {
            randomListAdapter.setRandomLists(it)
        }

        viewModel.livePopularList.observe(viewLifecycleOwner) {
            popularListAdapter.setList(it)
        }
    }

    private fun clickRecipe(rcpSeq : String) {
        val intent = Intent(requireActivity(), RcpInfoActivity::class.java)
        intent.putExtra("rcpSeq", rcpSeq)
        startActivity(intent)
    }

    private fun setSwipe() {
        binding.swipeLayout.apply {
            setColorSchemeResources(R.color.myGreen)
            setOnRefreshListener {
                isRefreshing = true
                lifecycleScope.launch {
                    viewModel.setRandomRecipe()
                    // suspend 함수를 호출 -> 데이터를 다 가져온 후(코루틴 일시정지됨) endSwipe() 호출됨
                    // 부드럽게 보이게 하기위해 데이터 가져온 후 RecyclerView가 업데이트되는 시간 고려하여 delay 설정
                    delay(500L)
                    isRefreshing = false
                }
            }
        }
    }

}