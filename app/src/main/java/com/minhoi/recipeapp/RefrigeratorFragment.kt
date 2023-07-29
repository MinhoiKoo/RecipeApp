package com.minhoi.recipeapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.adapter.IngredientListAdapter
import com.minhoi.recipeapp.databinding.FragmentRefrigeratorBinding


class RefrigeratorFragment : Fragment() {
    private val TAG = RefrigeratorFragment::class.java.simpleName

    private lateinit var binding : FragmentRefrigeratorBinding
    private lateinit var ingredientAdapter : IngredientListAdapter
    private lateinit var input : EditText
    private val array = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_refrigerator, container, false )

        binding.apply {
            homeBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_refrigeratorFragment_to_homeFragment)
            }

            searchBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_refrigeratorFragment_to_searchFragment)
            }

            mypageBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_refrigeratorFragment_to_mypageFragment)
            }
        }


        ingredientAdapter = IngredientListAdapter {
            // onDeleteClickListener
            array.removeAt(it)
            ingredientAdapter.setIngredients(array)
        }

        binding.ingredientRv.apply {
            adapter = ingredientAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        input = binding.inputIngredient
        input.addTextChangedListener(inputFormal)

        // 재료 추가 후 검색 버튼 누르면 재료 리스트를 Intent에 담아서 전달.
        binding.searchRefriBtn.setOnClickListener {
            if (array.isNotEmpty()) {
                val intent = Intent(activity, RecipeListActivity::class.java)
                intent.putExtra("ingredientList", array as ArrayList<String>)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "재료를 입력해주세요.", Toast.LENGTH_LONG).show()
            }
        }


//            getRecipe(array) {
//                if(it.size != 0) {
//                    val intent = Intent(getActivity(), RecipeListActivity::class.java)
//                    intent.putExtra("recipeList", it)
//                    startActivity(intent)
//                }
//            }



        return binding.root
    }

    private val inputFormal = object : TextWatcher {
        // 공백이 생기면 재료 목록을 자동으로 추가해주는 TextWatcher
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if(s != null && s.toString().contains(" ")){
                val ingredient = s.toString().trim()

                // 첫 입력에 공백이 들어가면 사용자가 입력하지 못하게 Input Clear
                if(ingredient == "") {
                    input.text.clear()
                } else {
                    array.add(ingredient)
                    ingredientAdapter.setIngredients(array)
                    input.text.clear()
                }
                Log.d("array", array.toString())
            }
        }
    }

}