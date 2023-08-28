package com.company.ourfinances.view.utils

import androidx.fragment.app.FragmentManager
import com.company.ourfinances.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Locale

class CustomDatePicker(button: MaterialButton, fragmentManager: FragmentManager) {

    init {
            val materialDatePicker: MaterialDatePicker<Long> =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.select_date)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            materialDatePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selection

                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)

                val date: String = String.format(
                    Locale.getDefault(),
                    "%02d/%02d/%04d",
                    dayOfMonth, month, year
                )
                button.text = date
            }


            fragmentManager?.let { manager ->
                materialDatePicker.show( manager, "tag")
            }
    }


}