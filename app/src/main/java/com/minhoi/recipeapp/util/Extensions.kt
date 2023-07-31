package com.minhoi.recipeapp.util
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun EditText.textChangesToFlow() : Flow<CharSequence?> {
    return callbackFlow<CharSequence> {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 값 내보내기
                trySend(s!!)
            }
        }
        addTextChangedListener(listener)

        // 콜백이 사라질 때 실행됨
        // 리스너 제거
        awaitClose {
            removeTextChangedListener(listener)
        }
    }.onStart {
        emit(text)
    }
}