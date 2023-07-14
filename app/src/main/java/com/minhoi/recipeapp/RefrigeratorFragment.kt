package com.minhoi.recipeapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.model.RecipeDto


class RefrigeratorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var filterList = arrayListOf<RecipeDto>()

        Ref.recipeDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val value = data.getValue(RecipeDto::class.java)

                    if (value != null) {
                        if(value.rcp_PARTS_DTLS?.contains("새우") == true && value.rcp_PARTS_DTLS != "건새우") {
                            filterList.add(value)
                            Log.d("filter", value.toString())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return inflater.inflate(R.layout.fragment_refrigerator, container, false)
    }

}