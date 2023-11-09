package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentTransferBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.enums.EnumUtils
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.view.CardCreateActivity
import com.company.ourfinances.view.ShowRecordListActivity
import com.company.ourfinances.view.utils.CustomDatePicker
import com.company.ourfinances.view.listener.FabClickListener
import com.company.ourfinances.view.listener.OnSpinnerListener
import com.company.ourfinances.view.utils.TypePaymentList
import com.company.ourfinances.viewmodel.CardViewModel
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.google.android.material.snackbar.Snackbar

class TransferFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentTransferBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private var cards: List<CardEntity> = listOf()

    private var recordId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadRecord()

        setupLists()

        observe()

        listeners()

    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllTypePayments()
        cardViewModel.getAllCards()

        cardViewModel.cardRecordList.value?.let {
            if (it.isNotEmpty()){
                binding.spinnerTypePayTransfer.setText(context?.getString(R.string.select), false)
            }
        }
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

        viewModel.financeRecord.observe(viewLifecycleOwner) { financeRecord ->

            binding.inputTitleTransfer.setText(financeRecord.title)
            binding.inputValueTransfer.setText(financeRecord.value.toString())
            binding.buttonDatePickerTransfer.setText(financeRecord.dateRecord)
            binding.inputReceiverTransfer.setText(financeRecord.destinationAccount)

            val cardName = financeRecord.cardId?.let { id ->
                cardViewModel.getCardById(id)?.name
            }

            binding.spinnerTypePayTransfer.setText(financeRecord.paymentType, false)

            financeRecord.cardId?.let {
                binding.spinnerCardTransfer.setText(cardName, false)
            }

        }
    }

    override fun doSave() {
        if (validateFields()) {

            val financeRecord = FinanceRecordEntity.Builder()
                .setRecordId(recordId)
                .setTitle(binding.inputTitleTransfer.text.toString())
                .setValue(binding.inputValueTransfer.text.toString().toDouble())
                .setDateRecord(binding.buttonDatePickerTransfer.text.toString())
                .setDestinationAccount(binding.inputReceiverTransfer.text.toString())
                .setTypeRecord(EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA, requireContext()))
                .setPaymentType(binding.spinnerTypePayTransfer.text.toString())

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
            bundle.putString(getString(R.string.fragmentIdentifier), EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA, requireContext()))

            clearAll()

            val extras = activity?.intent?.extras?.let {
                it.getString(activity?.getString(R.string.fragmentIdentifier)) ?: ""
            }

            activity?.findViewById<View>(R.id.finance_main)?.let { view ->
                Snackbar.make(view, getString(R.string.saved_successfully), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.view)) {
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

    private fun validateFields(): Boolean {

        var isValid = false

        if (TextUtils.isEmpty(binding.inputTitleTransfer.text)) {
            binding.editTitleTransfer.error = getString(R.string.title_cannot_be_empty)
        } else if (TextUtils.isEmpty(binding.inputReceiverTransfer.text)) {
            binding.editReceiverTransfer.error = getString(R.string.receiver_not_empty)
        } else if (TextUtils.equals(binding.buttonDatePickerTransfer.text, requireContext().getString(R.string.select_date))) {
            binding.buttonDatePickerLayoutTransfer.error = getString(R.string.date_cannot_be_empty)
        } else if (TextUtils.equals(binding.spinnerTypePayTransfer.text, getString(R.string.select))) {
            binding.spinnerTypePayLayoutTransfer.error = getString(R.string.select_a_type)
        }else if(cardViewModel.cardRecordList.value!!.isEmpty()) {
            binding.spinnerTypePayLayoutTransfer.error = getString(R.string.no_card)

            Snackbar.make(
                binding.constraintTransfer,
                getString(R.string.dont_have_cards),
                Snackbar.LENGTH_SHORT
            ).setAction(getString(R.string.add_card)) {
                startActivity(Intent(context, CardCreateActivity::class.java))
            }.show()

        } else if (TextUtils.equals(binding.spinnerCardTransfer.text, getString(R.string.select)) and binding.spinnerTypePayTransfer.text.equals("Cartão")){
            binding.spinnerCardLayoutTransfer.error = getString(R.string.choose_a_card)
        } else if (TextUtils.isEmpty(binding.inputValueTransfer.text)) {
            binding.editValueTransfer.error = getString(R.string.value_cannot_be_empty)
        } else {
            isValid = true
        }
        return isValid
    }

    override fun clearAll() {
        binding.inputTitleTransfer.text?.clear()
        binding.inputValueTransfer.text?.clear()
        binding.buttonDatePickerTransfer.setText(activity?.getString(R.string.select_date))
        binding.inputReceiverTransfer.text?.clear()
    }

    private fun setupLists() {
        val namePaymentList: List<String> = TypePaymentList(requireContext()).getList()
        binding.spinnerTypePayTransfer.setAdapter(getAdapter(namePaymentList))
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

                val visibilityBySpinnerSelected = selectedName == getString(R.string.card) && cards.isNotEmpty()

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
            if (bundleObj.getString(activity?.getString(R.string.fragmentIdentifier)) == EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA, requireContext())) {
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