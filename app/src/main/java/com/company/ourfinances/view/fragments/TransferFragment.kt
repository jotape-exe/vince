package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.ShowRecordListActivity
import com.company.ourfinances.view.assets.CustomDatePicker
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
    ): View? {
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

                binding.spinnerCardTransfer.adapter = ArrayAdapter(
                    requireContext().applicationContext,
                    R.layout.style_spinner, cardNames
                )
            }
        }

        viewModel.typePaymentList.observe(viewLifecycleOwner) {
            paymentTypesList = it
            val namePaymentList: List<String> = paymentTypesList.map { item -> item.name }
            binding.spinnerTypePayTransfer.adapter = getAdapter(namePaymentList)
        }

        viewModel.financeRecord.observe(viewLifecycleOwner) { financeRecord ->

            binding.editTitleTransfer.setText(financeRecord.title)
            binding.editValueTransfer.setText(financeRecord.value.toString())
            binding.buttonDatePickerTransfer.text = financeRecord.dateRecord
            binding.editReceiverTransfer.setText(financeRecord.destinationAccount)

            val paymentName = financeRecord.paymentTypeId?.let { id ->
                viewModel.getTypePaymentById(id).name
            }

            val cardName = financeRecord.cardId?.let { id ->
                cardViewModel.getCardNameById(id)
            }

            binding.spinnerTypePayTransfer.setSelection(
                getPositionByName(
                    paymentName,
                    paymentList = paymentTypesList
                )
            )

            financeRecord.cardId?.let {
                binding.spinnerCardTransfer.setSelection(
                    getPositionByName(
                        cardName,
                        cardList = cards
                    )
                )
            }

        }
    }

    override fun doSave() {
        if (TextUtils.isEmpty(binding.editTitleTransfer.text)) {
            binding.editTitleTransfer.error = getString(R.string.title_cannot_be_empty)

        } else if (TextUtils.isEmpty(binding.editReceiverTransfer.text)) {
            binding.editReceiverTransfer.error = getString(R.string.receiver_not_empty)

        } else if (TextUtils.equals(
                binding.buttonDatePickerTransfer.text,
                requireContext().getString(R.string.select_date)
            )
        ) {
            binding.buttonDatePickerTransfer.error = getString(R.string.date_cannot_be_empty)

        } else if (TextUtils.isEmpty(binding.editValueTransfer.text)) {
            binding.editValueTransfer.error = getString(R.string.value_cannot_be_empty)

        } else {

            val paymentListener = object : OnSpinnerListener<PaymentTypeEntity> {
                override fun getIdByName(name: String): Long {
                    return paymentTypesList.find { it.name == name }!!.paymentId
                }
            }

            val financeRecord = FinanceRecordEntity.Builder()
                .setRecordId(recordId)
                .setTitle(binding.editTitleTransfer.text.toString())
                .setValue(binding.editValueTransfer.text.toString().toDouble())
                .setDateRecord(binding.buttonDatePickerTransfer.text.toString())
                .setDestinationAccount(binding.editReceiverTransfer.text.toString())
                .setTypeRecord(RegisterTypeEnum.TRANSFER.value)
                .setPaymentTypeId(paymentListener.getIdByName(binding.spinnerTypePayTransfer.selectedItem.toString()))

            if (binding.spinnerCardTransfer.isVisible) {
                val cardListener = object : OnSpinnerListener<CardEntity> {
                    override fun getIdByName(name: String): Long {
                        return cards.find { it.name == name }!!.cardId
                    }
                }
                financeRecord.setCardId(cardListener.getIdByName(binding.spinnerCardTransfer.selectedItem.toString()))
            }

            viewModel.save(financeRecord.build())
            resetRecordId()

            val bundle = Bundle()
            bundle.putString(getString(R.string.fragmentIdentifier), TitleEnum.TRANSFERENCIA.value)

            clearAll()

            activity?.findViewById<View>(R.id.finance_main)?.let { view ->
                Snackbar.make(view, "Salvo com sucesso!", Snackbar.LENGTH_LONG)
                    .setAction("Ver") {
                        activity?.startActivity(
                            Intent(
                                context,
                                ShowRecordListActivity::class.java
                            ).putExtras(bundle)
                        )
                        activity?.finish()
                    }.show()
            }
        }
    }

    override fun clearAll() {
        binding.editTitleTransfer.text.clear()
        binding.editValueTransfer.text.clear()
        binding.buttonDatePickerTransfer.text = activity?.getString(R.string.select_date)
        binding.editReceiverTransfer.text.clear()
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
        binding.buttonDatePickerTransfer.setOnClickListener { view ->
            CustomDatePicker(binding.buttonDatePickerTransfer, parentFragmentManager)

        }

        binding.spinnerTypePayTransfer.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedName = parent?.getItemAtPosition(position) as? String

                val visibilityBySpinnerSelected = selectedName == "Cartão" && cards.isNotEmpty()

                setCardSpinnerVisibility(visibilityBySpinnerSelected)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
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

    private fun getPositionByName(
        name: String?,
        paymentList: List<PaymentTypeEntity> = listOf(),
        cardList: List<CardEntity> = listOf()
    ): Int {
        return when {
            paymentList.isNotEmpty() -> paymentList.indexOfFirst { it.name == name }
            cardList.isNotEmpty() -> cardList.indexOfFirst { it.name == name }
            else -> -1
        }
    }

    private fun setCardSpinnerVisibility(visibility: Boolean) {
        binding.textCardTransfer.isVisible = visibility
        binding.spinnerCardTransfer.isVisible = visibility
    }


}