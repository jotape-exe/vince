package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentRevenueBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.CategoryRecordEntity
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

class RevenueFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private lateinit var paymentTypesList: List<PaymentTypeEntity>
    private lateinit var categoryRecordList: List<CategoryRecordEntity>
    private var cards: List<CardEntity> = listOf()

    private var recordId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRevenueBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]

        viewModel.getAllCategories()
        viewModel.getAllTypePayments()
        cardViewModel.getAllCards()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadRecord()

        observe()

        listeners()


    }

    override fun doSave() {
        if (TextUtils.isEmpty(binding.inputTitle.text)) {
            binding.editTitle.error = getString(R.string.title_cannot_be_empty)

        } else if (TextUtils.equals(
                binding.buttonDatePicker.text,
                requireContext().getString(R.string.select_date)
            )
        ) {
            binding.buttonDatePicker.error = getString(R.string.date_cannot_be_empty)

        } else if (TextUtils.isEmpty(binding.inputValue.text)) {
            binding.editValue.error = getString(R.string.value_cannot_be_empty)

        } else {

            val categoryListener = object : OnSpinnerListener<CategoryRecordEntity> {
                override fun getIdByName(name: String): Long {
                    return categoryRecordList.find { it.name == name }!!.id
                }

            }

            val paymentListener = object : OnSpinnerListener<PaymentTypeEntity> {
                override fun getIdByName(name: String): Long {
                    return paymentTypesList.find { it.name == name }!!.paymentId
                }

            }

            val financeRecord = FinanceRecordEntity.Builder()
                .setRecordId(recordId)
                .setTitle(binding.inputTitle.text.toString())
                .setValue(binding.inputValue.text.toString().toDouble())
                .setDateRecord(binding.buttonDatePicker.text.toString())
                .setTypeRecord(RegisterTypeEnum.REVENUE.value)
                .setCategoryRecordId(categoryListener.getIdByName(binding.spinnerCategory.text.toString()))
                .setPaymentTypeId(paymentListener.getIdByName(binding.spinnerTypePay.text.toString()))

            if (binding.spinnerCard.isVisible) {
                val cardListener = object : OnSpinnerListener<CardEntity> {
                    override fun getIdByName(name: String): Long {
                        return cards.find { it.name == name }!!.cardId
                    }
                }
                financeRecord.setCardId(cardListener.getIdByName(binding.spinnerCard.text.toString()))
            }

            viewModel.save(financeRecord.build())


            clearAll()

            val bundle = Bundle()
            bundle.putString(getString(R.string.fragmentIdentifier), TitleEnum.RECEITA.value)

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

    private fun resetRecordId() {
        recordId = 0
    }

    override fun clearAll() {
        binding.inputTitle.text?.clear()
        binding.inputValue.text?.clear()
        binding.buttonDatePicker.setText(activity?.getString(R.string.select_date))
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

        binding.spinnerTypePay.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val selectedName = binding.spinnerTypePay.text.toString()

                val visibilityBySpinnerSelected = selectedName == "Cartão" && cards.isNotEmpty()

                setCardSpinnerVisibility(visibilityBySpinnerSelected)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonDatePicker.isFocusable = false
        binding.buttonDatePicker.isClickable = false

        binding.buttonDatePicker.setOnClickListener {
            CustomDatePicker(binding.buttonDatePicker, parentFragmentManager)
        }
    }

    private fun observe() {
        cardViewModel.cardRecordList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                cards = it
                val cardNames: List<String> = cards.map { cardEntity -> cardEntity.name }

                binding.spinnerCard.setAdapter(
                    ArrayAdapter(
                        requireContext().applicationContext,
                        R.layout.style_spinner, cardNames
                    )
                )
            }
        }

        viewModel.categoryRecordList.observe(viewLifecycleOwner) {
            categoryRecordList = it
            val nameCategoriesList: List<String> = categoryRecordList.map { item -> item.name }
            binding.spinnerCategory.setAdapter(getAdapter(nameCategoriesList))
        }

        viewModel.typePaymentList.observe(viewLifecycleOwner) {
            paymentTypesList = it
            val namePaymentList: List<String> = paymentTypesList.map { item -> item.name }
            binding.spinnerTypePay.setAdapter(getAdapter(namePaymentList))
        }

        viewModel.financeRecord.observe(viewLifecycleOwner) { financeRecord ->

            binding.inputTitle.setText(financeRecord.title)
            binding.inputValue.setText(financeRecord.value.toString())
            binding.buttonDatePicker.setText(financeRecord.dateRecord)

            val categoryName = financeRecord.categoryRecordId?.let { id ->
                viewModel.getCategoryById(id).name
            }

            val paymentName = financeRecord.paymentTypeId?.let { id ->
                viewModel.getTypePaymentById(id).name
            }

            val cardName = financeRecord.cardId?.let { id ->
                cardViewModel.getCardById(id)?.name
            }

            binding.spinnerCategory.setText(categoryName, false)

            binding.spinnerTypePay.setText(paymentName, false)

            financeRecord.cardId?.let {
                binding.spinnerCard.setText(cardName, false)
            }

        }
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

    private fun loadRecord() {
        val bundle = activity?.intent?.extras
        bundle?.let { bundleObj ->
            if (bundleObj.getString(activity?.getString(R.string.fragmentIdentifier)) == TitleEnum.RECEITA.value) {
                recordId = bundleObj.getLong(DatabaseConstants.FinanceRecord.recordId)
                viewModel.getRecordById(recordId)
            }
        }
    }


    private fun setCardSpinnerVisibility(visibility: Boolean) {
        binding.textCard.isVisible = visibility
        binding.spinnerCard.isVisible = visibility
        binding.spinnerCardLayout.isVisible = visibility
    }


}