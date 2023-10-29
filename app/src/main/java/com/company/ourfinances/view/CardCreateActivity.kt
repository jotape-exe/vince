package com.company.ourfinances.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityCardCreateBinding
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.enums.CardTypeEnum
import com.company.ourfinances.view.adapters.ColorSpinnerAdapter
import com.company.ourfinances.view.utils.ColorList
import com.company.ourfinances.view.utils.ColorObject
import com.company.ourfinances.viewmodel.CardViewModel


class CardCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardCreateBinding
    lateinit var selectedColor: ColorObject

    private lateinit var viewModel: CardViewModel
    private lateinit var colorHex: String
    private lateinit var textColorHex: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CardViewModel::class.java]

        val typeCards = listOf(CardTypeEnum.CREDIT.value, CardTypeEnum.DEBIT.value)

        binding.spinnerCardType.setAdapter(getAdapter(typeCards))

        spinnerColorInitializer()

        listener()

    }

    private fun getAdapter(itemsList: List<String>): ArrayAdapter<String>? {
        return ArrayAdapter(applicationContext, R.layout.style_spinner, itemsList)
    }

    private fun spinnerColorInitializer() {

        selectedColor = ColorList().defaultColor
        colorHex = selectedColor.hexHash
        textColorHex = selectedColor.contrastHexHash

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

        binding.inputCardName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editCardName.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.inputCardNumber.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editCardName.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.spinnerCardType.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.spinnerCardTypeLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonSaveCard.setOnClickListener {
            if (TextUtils.isEmpty(binding.inputCardName.text)){
                binding.editCardName.error = getString(R.string.name_not_empty)
            } else if (TextUtils.isEmpty(binding.inputCardNumber.text)){
                binding.editCardNumber.error = getString(R.string.number_not_empty)
            } else if(TextUtils.equals(binding.spinnerCardType.text, "Selecionar")){
                binding.spinnerCardTypeLayout.error = "Selecione um Tipo!"
            } else{

                val card = CardEntity(
                    cardColor = colorHex,
                    cardNumber = binding.inputCardNumber.text.toString(),
                    name = binding.inputCardName.text.toString(),
                    cardType = binding.spinnerCardType.text.toString(),
                    cardTextColor = textColorHex
                )

                viewModel.insertCard(card)

                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

}