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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.databinding.FragmentRefrigeratorBinding
import com.minhoi.recipeapp.model.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RefrigeratorFragment : Fragment() {
    private val TAG = RefrigeratorFragment::class.java.simpleName

    private lateinit var binding : FragmentRefrigeratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_refrigerator, container, false )


        binding.homeBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_refrigeratorFragment_to_homeFragment)
        }

        binding.searchBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_refrigeratorFragment_to_searchFragment)
        }

        binding.mypageBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_refrigeratorFragment_to_mypageFragment)
        }

        val input = binding.inputIngredient
        val array = mutableListOf<String>()

        val rv = binding.ingredientRv
        val adapter = IngredientListAdapter()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // 처음 입력이 공백으로 들어오면 EditText clear / 사용자 입력 공백 기준으로 List에 추가.
                Log.d(TAG, s.toString())
                if(s != null && s.toString().contains(" ")){
                    val ingredient = s.toString().trim()

                    if(ingredient == "") {
                        input.text.clear()
                    } else {
                        array.add(ingredient)
                        adapter.setIngredients(array)
                        input.text.clear()
                    }
                    Log.d("array", array.toString())
                }
            }
        })


        // 재료 추가 후 검색 버튼 누르면 재료 리스트를 Intent에 담아서 전달.
        binding.searchRefriBtn.setOnClickListener {

            if(array.size != 0) {
                val intent = Intent(activity, RecipeListActivity::class.java)
                intent.putExtra("ingredientList", array as ArrayList<String>)
                startActivity(intent)
            }

//            getRecipe(array) {
//                if(it.size != 0) {
//                    val intent = Intent(getActivity(), RecipeListActivity::class.java)
//                    intent.putExtra("recipeList", it)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(requireContext(), "재료르 입력해주세요.", Toast.LENGTH_LONG).show()
//                }
//            }

        }

        return binding.root
    }


    // 레시피 전체 정보를 받아와서 레시피의 재료가 사용자가 선택한 재료를 필터링하여 리스트에 추가.



}