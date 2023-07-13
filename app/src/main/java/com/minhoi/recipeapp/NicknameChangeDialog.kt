package com.minhoi.recipeapp

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton


class NicknameChangeDialog(context : Context) : Dialog(context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: NicknameChangeDialogListener

    interface NicknameChangeDialogListener {
        fun onApplyClicked(nickName : String)
    }

    fun setOnClickedListener(listener: NicknameChangeDialogListener) {
        onClickListener = listener
    }

    fun showDialog() {
        dialog.setContentView(R.layout.nicknamechange_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.show()

        val inputNickname = dialog.findViewById<EditText>(R.id.inputChangeNickname)
        val applyBtn = dialog.findViewById<AppCompatButton>(R.id.nicknameChangeApply)
        val cancelBtn = dialog.findViewById<AppCompatButton>(R.id.nicknameChangeCancel)

        applyBtn.setOnClickListener {
            onClickListener.onApplyClicked(inputNickname.text.toString())
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }


    }
}