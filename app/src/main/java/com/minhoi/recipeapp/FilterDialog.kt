package com.minhoi.recipeapp

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatButton
import com.minhoi.recipeapp.databinding.FilterDialogBinding

class FilterDialog(context : Context) : Dialog(context) {
    private val dialog = Dialog(context)
    private lateinit var binding : FilterDialogBinding
    private lateinit var onClickListener: FilterDialogListener

    interface FilterDialogListener {
        fun onApplyClicked(minRange : String, maxRange : String, foodType : String)
    }

    fun setOnClickedListener(listener: FilterDialogListener) {
        onClickListener = listener
    }

    fun showDialog() {
        dialog.setContentView(R.layout.filter_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.show()

        val minRange = dialog.findViewById<EditText>(R.id.minRange)
        val maxRange = dialog.findViewById<EditText>(R.id.maxRange)
        val typeRadioGroup = dialog.findViewById<RadioGroup>(R.id.typeRadioGroup)

        val btnApply = dialog.findViewById<AppCompatButton>(R.id.apply)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.cancel)

        btnApply.setOnClickListener {

            val checkedRadioButtonId = typeRadioGroup.checkedRadioButtonId
            val checkedRadioButton = dialog.findViewById<RadioButton>(checkedRadioButtonId)
            val selectedType = checkedRadioButton?.text?.toString() ?: ""


            onClickListener.onApplyClicked(
                minRange.text.toString(),
                maxRange.text.toString(),
                selectedType
            )
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

}

