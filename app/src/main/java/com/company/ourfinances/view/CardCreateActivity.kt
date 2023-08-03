package com.company.ourfinances.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.company.ourfinances.databinding.ActivityCardCreateBinding
import com.company.ourfinances.view.adapters.ColorSpinnerAdapter
import com.company.ourfinances.view.assets.ColorList
import com.company.ourfinances.view.assets.ColorObject


class CardCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardCreateBinding
    lateinit var selectedColor: ColorObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()

        spinnerInitializer()


    }

    private fun spinnerInitializer() {
        selectedColor = ColorList().defaultColor
        binding.colorSpinner.apply {
            adapter = ColorSpinnerAdapter(applicationContext, ColorList().getColors())
            setSelection(ColorList().colorPosition(selectedColor), false)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedColor = ColorList().getColors()[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
    }


    private fun listener() {
        binding.imageClose.setOnClickListener {
            finish()
        }
    }

}