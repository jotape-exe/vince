package com.company.ourfinances.view.utils

import androidx.fragment.app.FragmentManager
import com.company.ourfinances.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId.systemDefault
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CustomDatePicker(button: MaterialButton, fragmentManager: FragmentManager) {
    init {
        val materialDatePicker: MaterialDatePicker<Long> =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        materialDatePicker.addOnPositiveButtonClickListener { selection ->
            val offset = systemDefault().rules.getOffset(Instant.ofEpochMilli(selection))

            val zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(selection), systemDefault())
            val localZonedDateTime = zonedDateTime.minusSeconds(offset.totalSeconds.toLong())

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone(systemDefault())

            val date: String = dateFormat.format(Date.from(localZonedDateTime.toInstant()))
            button.text = date
        }

        fragmentManager?.let { manager ->
            materialDatePicker.show(manager, "tag")
        }
    }


}