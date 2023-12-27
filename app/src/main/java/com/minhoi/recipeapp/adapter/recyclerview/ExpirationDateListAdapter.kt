package com.minhoi.recipeapp.adapter.recyclerview

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.databinding.ExpirationDateListRowBinding
import com.minhoi.recipeapp.model.ExpirationDateDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpirationDateListAdapter(private val context: Context, private val onDeleteClick: (Int) -> (Unit)) : RecyclerView.Adapter<ExpirationDateListAdapter.ViewHolder>() {

    private val ingredients = mutableListOf<ExpirationDateDto>()
    inner class ViewHolder(binding: ExpirationDateListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.expirationIngredientImage
        val name = binding.expirationIngredientName
        val expirationDate = binding.expirationIngredientDate
        val closeBtn = binding.closeButton

        init {
            // RecyclerView 아이템에서 TextView를 클릭하면 DatePickerDialog를 띄움
            expirationDate.setOnClickListener {
                showDatePickerDialog()
            }
            closeBtn.setOnClickListener {
                onDeleteClick(adapterPosition)
            }
            expirationDate.text = getCurrentDate()
        }

        fun bind(item: ExpirationDateDto) {
            name.text = item.name
            expirationDate.text = item.date
            Glide.with(context)
                .load(item.imagePath)
                .into(image)
        }

        private fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        private fun showDatePickerDialog() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // 선택한 날짜로 TextView 업데이트
                val selectedDate = updateDateInView(year, monthOfYear, dayOfMonth)

                // 해당 아이템의 날짜 업데이트
                ingredients[adapterPosition].date = "$selectedDate 까지"

                // RecyclerView 업데이트
                notifyItemChanged(adapterPosition)

            }, year, month, day)

            // 현재 날짜 이전의 날짜를 선택하지 못하도록 설정
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

            datePickerDialog.show()
        }

        @SuppressLint("SetTextI18n")
        private fun updateDateInView(year: Int, month: Int, day: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            // SimpleDateFormat을 사용하여 선택한 날짜를 원하는 형식으로 표시
            val dateFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<ExpirationDateListRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.expiration_date_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    fun setList(items: List<ExpirationDateDto>) {
        ingredients.clear()
        ingredients.addAll(items)
        notifyDataSetChanged()
    }

    fun getList() = ingredients
}