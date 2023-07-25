package com.company.ourfinances.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentRevenueBinding
import com.company.ourfinances.view.MainActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RevenueFragment : Fragment() {

    private lateinit var binding: FragmentRevenueBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRevenueBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Dados irão vir do SQLite
        val categoriesList = mutableListOf("Item 1", "Item 2", "item 3")
        val adapter =
            activity?.let {
                ArrayAdapter(
                    it.applicationContext,
                    R.layout.style_spinner, categoriesList
                )
            }

        binding.spinnerCategory.adapter = adapter

        listeners()


    }

    private fun listeners() {
        binding.spinnerCategory.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.buttonDatePicker.setOnClickListener {
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
                binding.buttonDatePicker.text = date
            }

            activity?.supportFragmentManager?.let { manager ->
                materialDatePicker.show(
                    manager,
                    "tag"
                )
            }
        }
    }

}