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
import com.company.ourfinances.model.enums.EnumUtils
import com.company.ourfinances.view.adapters.ColorSpinnerAdapter
import com.company.ourfinances.view.utils.ColorList
import com.company.ourfinances.view.utils.ColorObject
import com.company.ourfinances.view.utils.CryptoUtils
import com.company.ourfinances.viewmodel.CardViewModel


class CardCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardCreateBinding
    lateinit var selectedColor: ColorObject

    private lateinit var viewModel: CardViewModel
    private lateinit var colorHex: String
    private lateinit var textColorHex: String
    private val cripto = CryptoUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CardViewModel::class.java]

        val typeCards = listOf(
            EnumUtils.getCardTypeName(CardTypeEnum.CREDIT, applicationContext),
            EnumUtils.getCardTypeName(CardTypeEnum.DEBIT, applicationContext)
        )

        binding.spinnerCardType.setAdapter(getAdapter(typeCards))

        spinnerColorInitializer()

        listener()

    }

    private fun getAdapter(itemsList: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(this, R.layout.style_spinner, itemsList)
    }

    private fun spinnerColorInitializer() {

        selectedColor = ColorList(this).defaultColor
        colorHex = selectedColor.hexHash
        textColorHex = selectedColor.contrastHexHash

        binding.colorSpinner.apply {
            adapter = ColorSpinnerAdapter(this@CardCreateActivity, ColorList(this@CardCreateActivity).getColors())
            setSelection(ColorList(this@CardCreateActivity).colorPosition(selectedColor), false)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedColor = ColorList(this@CardCreateActivity).getColors()[position]
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
                binding.editCardNumber.error = null
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
            } else if(TextUtils.equals(binding.spinnerCardType.text, getString(R.string.select))){
                binding.spinnerCardTypeLayout.error = getString(R.string.select_a_type)
            } else{

                val card = CardEntity(
                    cardColor = colorHex,
                    cardNumber = cripto.encrypt(binding.inputCardNumber.text.toString()),
                    name = binding.inputCardName.text.toString(),
                    cardType = binding.spinnerCardType.text.toString(),
                    cardTextColor = textColorHex
                )

                viewModel.insertCard(card)

                Toast.makeText(this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

}