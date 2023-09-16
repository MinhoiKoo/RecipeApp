package com.minhoi.recipeapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.FilterDialog
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.RcpInfoActivity
import com.minhoi.recipeapp.ui.viewmodel.SearchViewModel
import com.minhoi.recipeapp.adapter.recyclerview.RecipeListAdapter
import com.minhoi.recipeapp.adapter.recyclerview.SearchAutoCompleteAdapter
import com.minhoi.recipeapp.databinding.FragmentSearchBinding
import com.minhoi.recipeapp.util.textChangesToFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    // 메뉴명, 재료명 으로 검색 가능하도록 구현 예정
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchListAdapter : RecipeListAdapter
    private lateinit var autoCompleteAdapter : SearchAutoCompleteAdapter
    private var searchJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)


        autoCompleteAdapter = SearchAutoCompleteAdapter { key ->
            //onClickListener
            val intent = Intent(requireContext(), RcpInfoActivity::class.java)
            intent.putExtra("rcpSeq", key)
            startActivity(intent)
        }

        binding.autoCompleteRv.apply {
            adapter = autoCompleteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.inputRecipe.textChangesToFlow()
            .debounce(1000L)
            .onEach { query ->
                // 새로운 입력이 있을 때마다 이전 검색 Job을 취소하고 새로운 Job 시작
                searchJob?.cancel()
                searchJob = lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.getRecipeName(query.toString())
                }
            }
            .launchIn(lifecycleScope)


        binding.inputRecipe.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 수정 필요. EditText <-> liveData 오류로 인한 임시 코드
                viewModel._mutableSearchInput.value = binding.inputRecipe.text.toString()
                lifecycleScope.launch{
                    viewModel.searchRcp()
                    handled = true
                }
                Log.d("edit", viewModel._mutableSearchInput.value.toString())
            }
            handled
        }

        searchListAdapter = RecipeListAdapter(requireContext()) {
            val intent = Intent(requireActivity(), RcpInfoActivity::class.java)
            intent.apply {
                putExtra("rcpSeq", it.rcp_SEQ)
            }
            startActivity(intent)
        }


        binding.searchRv.apply {
            visibility = View.GONE
            adapter = searchListAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }


//        viewModel.searchList.observe(viewLifecycleOwner) {
//            val adapter = RecipeListAdapter(context!!, it as ArrayList<RecipeDto>)
//            rv.adapter = adapter
//            rv.layoutManager = GridLayoutManager(activity, 1)
//            adapter.setItemClickListener(object : RecipeListAdapter.OnItemClickListener {
//                override fun onClick(v: View, position: Int) {
//                    Log.d("kcal", it[position].info_ENG?.toDouble().toString())
//                }
//
//            })
//        }

        binding.filterBtn.setOnClickListener {

            val dialog = FilterDialog(requireContext())
            dialog.showDialog()

            dialog.setOnClickedListener(object : FilterDialog.FilterDialogListener {
                override fun onApplyClicked(minRange: String, maxRange: String, foodType: String) {
                    viewModel.filter(minRange, maxRange, foodType)
                }
            })
        }

        setObserve()

        return binding.root
    }

    private fun setObserve() {
        viewModel.searchList.observe(viewLifecycleOwner) {
            searchListAdapter.setLists(it)
        }

        viewModel.autoCompleteList.observe(viewLifecycleOwner) {
            autoCompleteAdapter.setLists(it)
        }
    }
}

