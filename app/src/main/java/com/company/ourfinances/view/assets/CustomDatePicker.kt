package com.company.ourfinances.view.assets

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Locale

class CustomDatePicker(button: MaterialButton, fragment: Fragment) {

    init {
            val materialDatePicker: MaterialDatePicker<Long> =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecionar Data")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            materialDatePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selection

                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) + 1
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)

                val date: String = String.format(
                    Locale.getDefault(),
                    "%02d/%02d/%04d",
                    dayOfMonth, month, year
                )
                button.text = date
            }

            fragment.activity?.supportFragmentManager?.let { manager ->
                materialDatePicker.show( manager, "tag")
            }
    }


}