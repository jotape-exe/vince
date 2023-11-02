package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentTransferBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.ShowRecordListActivity
import com.company.ourfinances.view.utils.CustomDatePicker
import com.company.ourfinances.view.listener.FabClickListener
import com.company.ourfinances.view.listener.OnSpinnerListener
import com.company.ourfinances.viewmodel.CardViewModel
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.google.android.material.snackbar.Snackbar

class TransferFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentTransferBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private lateinit var paymentTypesList: List<PaymentTypeEntity>
    private var cards: List<CardEntity> = listOf()

    private var recordId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]

        viewModel.getAllTypePayments()
        cardViewModel.getAllCards()

        loadRecord()

        observe()

        listeners()

        return binding.root
    }

    private fun observe() {
        cardViewModel.cardRecordList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                cards = it
                val cardNames: List<String> = cards.map { cardEntity -> cardEntity.name }

                binding.spinnerCardTransfer.setAdapter(
                    ArrayAdapter(
                        requireContext().applicationContext,
                        R.layout.style_spinner, cardNames
                    )
                )
            }
        }

        viewModel.typePaymentList.observe(viewLifecycleOwner) {
            paymentTypesList = it
            val namePaymentList: List<String> = paymentTypesList.map { item -> item.name }
            binding.spinnerTypePayTransfer.setAdapter(getAdapter(namePaymentList))
        }

        viewModel.financeRecord.observe(viewLifecycleOwner) { financeRecord ->

            binding.inputTitleTransfer.setText(financeRecord.title)
            binding.inputValueTransfer.setText(financeRecord.value.toString())
            binding.buttonDatePickerTransfer.setText(financeRecord.dateRecord)
            binding.inputReceiverTransfer.setText(financeRecord.destinationAccount)

            val paymentName = financeRecord.paymentTypeId?.let { id ->
                viewModel.getTypePaymentById(id).name
            }

            val cardName = financeRecord.cardId?.let { id ->
                cardViewModel.getCardById(id)?.name
            }

            binding.spinnerTypePayTransfer.setText(paymentName, false)

            financeRecord.cardId?.let {
                binding.spinnerCardTransfer.setText(cardName, false)
            }

        }
    }

    override fun doSave() {
        if (TextUtils.isEmpty(binding.inputTitleTransfer.text)) {
            binding.editTitleTransfer.error = getString(R.string.title_cannot_be_empty)
        } else if (TextUtils.isEmpty(binding.inputReceiverTransfer.text)) {
            binding.editReceiverTransfer.error = getString(R.string.receiver_not_empty)
        } else if (TextUtils.equals(binding.buttonDatePickerTransfer.text, requireContext().getString(R.string.select_date))) {
            binding.buttonDatePickerLayoutTransfer.error = getString(R.string.date_cannot_be_empty)
        } else if (TextUtils.equals(binding.spinnerTypePayTransfer.text, getString(R.string.select))) {
            binding.spinnerTypePayLayoutTransfer.error = "Selecione um tipo!"
        } else if (TextUtils.equals(binding.spinnerCardTransfer.text, getString(R.string.select)) and binding.spinnerTypePayTransfer.text.equals("Cartão")){
            binding.spinnerCardLayoutTransfer.error = "Escolha um cartão"
        } else if (TextUtils.isEmpty(binding.inputValueTransfer.text)) {
            binding.editValueTransfer.error = getString(R.string.value_cannot_be_empty)
        } else {

            val paymentListener = object : OnSpinnerListener<PaymentTypeEntity> {
                override fun getIdByName(name: String): Long {
                    return paymentTypesList.find { it.name == name }!!.paymentId
                }
            }

            val financeRecord = FinanceRecordEntity.Builder()
                .setRecordId(recordId)
                .setTitle(binding.inputTitleTransfer.text.toString())
                .setValue(binding.inputValueTransfer.text.toString().toDouble())
                .setDateRecord(binding.buttonDatePickerTransfer.text.toString())
                .setDestinationAccount(binding.inputReceiverTransfer.text.toString())
                .setTypeRecord(RegisterTypeEnum.TRANSFER.value)
                .setPaymentTypeId(paymentListener.getIdByName(binding.spinnerTypePayTransfer.text.toString()))

            if (binding.spinnerCardTransfer.isVisible) {
                val cardListener = object : OnSpinnerListener<CardEntity> {
                    override fun getIdByName(name: String): Long {
                        return cards.find { it.name == name }!!.cardId
                    }
                }
                financeRecord.setCardId(cardListener.getIdByName(binding.spinnerCardTransfer.text.toString()))
            }

            viewModel.save(financeRecord.build())

            val bundle = Bundle()
            bundle.putString(getString(R.string.fragmentIdentifier), TitleEnum.TRANSFERENCIA.value)

            clearAll()

            val extras = activity?.intent?.extras?.let {
                it.getString(activity?.getString(R.string.fragmentIdentifier)) ?: ""
            }

            activity?.findViewById<View>(R.id.finance_main)?.let { view ->
                Snackbar.make(view, "Salvo com sucesso!", Snackbar.LENGTH_LONG)
                    .setAction("Ver") {
                        if (extras.isNullOrBlank()) {
                            activity?.startActivity(
                                Intent(
                                    context,
                                    ShowRecordListActivity::class.java
                                ).putExtras(bundle)
                            )
                        }
                        activity?.finish()
                    }.show()
            }

            resetRecordId()
        }
    }

    override fun clearAll() {
        binding.inputTitleTransfer.text?.clear()
        binding.inputValueTransfer.text?.clear()
        binding.buttonDatePickerTransfer.setText(activity?.getString(R.string.select_date))
        binding.inputReceiverTransfer.text?.clear()
    }

    private fun getAdapter(itemsList: List<String>): ArrayAdapter<String>? {
        val adapter = activity?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.style_spinner, itemsList
            )
        }
        return adapter
    }

    private fun resetRecordId() {
        recordId = 0
    }

    private fun listeners() {
        binding.inputTitleTransfer.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editTitleTransfer.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.inputReceiverTransfer.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editReceiverTransfer.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonDatePickerTransfer.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonDatePickerLayoutTransfer.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.inputValueTransfer.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editValueTransfer.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.spinnerCardTransfer.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.spinnerCardLayoutTransfer.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonDatePickerTransfer.setOnClickListener {
            CustomDatePicker(binding.buttonDatePickerTransfer, parentFragmentManager)
        }

        binding.spinnerTypePayTransfer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val selectedName = binding.spinnerTypePayTransfer.text.toString()

                val visibilityBySpinnerSelected = selectedName == "Cartão" && cards.isNotEmpty()

                setCardSpinnerVisibility(visibilityBySpinnerSelected)

                binding.spinnerTypePayLayoutTransfer.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonDatePickerTransfer.isFocusable = false
        binding.buttonDatePickerTransfer.isClickable = false


    }

    private fun loadRecord() {
        val bundle = activity?.intent?.extras
        bundle?.let { bundleObj ->
            if (bundleObj.getString(activity?.getString(R.string.fragmentIdentifier)) == TitleEnum.TRANSFERENCIA.value) {
                recordId = bundleObj.getLong(DatabaseConstants.FinanceRecord.recordId)
                viewModel.getRecordById(recordId)
            }
        }
    }

    private fun setCardSpinnerVisibility(visibility: Boolean) {
        binding.textCardTransfer.isVisible = visibility
        binding.spinnerCardTransfer.isVisible = visibility
        binding.spinnerCardLayoutTransfer.isVisible = visibility
    }


}