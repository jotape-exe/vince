package com.company.ourfinances.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityCardCreateBinding
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.view.adapters.ColorSpinnerAdapter
import com.company.ourfinances.view.assets.ColorList
import com.company.ourfinances.view.assets.ColorObject
import com.company.ourfinances.viewmodel.FinanceActivityViewModel


class CardCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardCreateBinding
    lateinit var selectedColor: ColorObject

    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var colorHex: String
    private lateinit var textColorHex: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        val typeCards = listOf("Crédito", "Débito")

        binding.spinnerCardType.adapter = getAdapter(typeCards)

        spinnerColorInitializer()

        listener()

        observe()


    }

    private fun observe() {

    }

    private fun getAdapter(itemsList: List<String>): ArrayAdapter<String>? {
        //Adapter para tipo de cartão
        return ArrayAdapter(applicationContext, R.layout.style_spinner, itemsList)
    }

    private fun spinnerColorInitializer() {
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
                    colorHex = selectedColor.hexHash
                    textColorHex = selectedColor.contrastHexHash
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

        binding.buttonSaveCard.setOnClickListener {
            if (TextUtils.isEmpty(binding.editCardName.text)){
                binding.editCardName.error = getString(R.string.name_not_empty)
            } else if (TextUtils.isEmpty(binding.editCardNumber.text)){
                binding.editCardNumber.error = getString(R.string.number_not_empty)
            } else{

                val card = CardEntity(
                    cardColor = colorHex,
                    cardNumber = binding.editCardNumber.text.toString(),
                    name = binding.editCardName.text.toString(),
                    cardType = binding.spinnerCardType.selectedItem.toString(),
                    cardTextColor = textColorHex
                )

                viewModel.insertCard(card)

                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }





}