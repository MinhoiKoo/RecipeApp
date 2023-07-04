package com.minhoi.recipeapp

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.minhoi.recipeapp.databinding.FilterDialogBinding

class FilterDialog(private val context : Context) {
    private val dialog = Dialog(context)
    private lateinit var binding : FilterDialogBinding
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.filter_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.show()

    }

    interface OnDialogClickListener
    {
        fun onApplyClicked(minRange : Double, maxRange : Double, foodType : String)
        fun onCancelClicked()
    }

}