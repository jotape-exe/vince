package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.ShowRecordListActivity
import com.company.ourfinances.view.assets.CustomDatePicker
import com.company.ourfinances.view.listener.FabClickListener
import com.company.ourfinances.viewmodel.CardViewModel
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.google.android.material.snackbar.Snackbar

class RevenueFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private lateinit var paymentTypesList: List<PaymentTypeEntity>
    private lateinit var categoryExpenseList: List<CategoryExpenseEntity>
    private lateinit var cards: List<CardEntity>

    private var selectedName: String? = null
    private var recordId: Long = 0
    private var cardId: Long? = null

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

        listeners()

        observe()

    }

    override fun doSave() {
        if (TextUtils.isEmpty(binding.editTitle.text)) {
            binding.editTitle.error = getString(R.string.title_cannot_be_empty)

        } else if (TextUtils.equals(
                binding.buttonDatePicker.text,
                requireContext().getString(R.string.select_date)
            )
        ) {
            binding.buttonDatePicker.error = getString(R.string.date_cannot_be_empty)

        } else if (TextUtils.isEmpty(binding.editValue.text)) {
            binding.editValue.error = getString(R.string.value_cannot_be_empty)

        } else {

            val categoryExpenseId = getIdCategoryExpenseFromName(
                binding.spinnerCategory.selectedItem.toString(),
                categoryExpenseList
            )

            val paymentTypeId = getIdPaymentTypeFromName(
                binding.spinnerTypePay.selectedItem.toString(),
                paymentTypesList
            )

            val financeRecord = FinanceRecordEntity.Builder()
                .setRecordId(recordId)
                .setTitle(binding.editTitle.text.toString())
                .setValue(binding.editValue.text.toString().toDouble())
                .setDateRecord(binding.buttonDatePicker.text.toString())
                .setTypeRecord(RegisterTypeEnum.REVENUE.value)
                .setCategoryExpenseId(categoryExpenseId)
                .setPaymentTypeId(paymentTypeId)

            if (binding.spinnerCard.isVisible) {
                cardId = findCardIdByName(binding.spinnerCard.selectedItem.toString())
                financeRecord.setCardId(cardId)
            }

            viewModel.save(financeRecord.build())
            resetRecordId()

            clearAll()

            val bundle = Bundle()
            bundle.putString(getString(R.string.fragmentIdentifier), TitleEnum.RECEITA.value)

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

    private fun findCardIdByName(name: String): Long {
        val card: CardEntity? = cards.find { it.name == name }
        return card!!.cardId
    }

    private fun resetRecordId() {
        recordId = 0
    }

    override fun getIdCategoryExpenseFromName(spinnerItemName: String, list: List<CategoryExpenseEntity>): Long? {
        val item = list.find { categoryExpenseEntity -> categoryExpenseEntity.name == spinnerItemName }
        return item?.id
    }

    override fun getIdPaymentTypeFromName(spinnerItemName: String, list: List<PaymentTypeEntity>): Long? {
        val item = list.find { paymentTypeEntity -> paymentTypeEntity.name == spinnerItemName }
        return item?.paymentId
    }

    override fun clearAll() {
        binding.editTitle.text.clear()
        binding.editValue.text.clear()
        binding.buttonDatePicker.text = activity?.getString(R.string.select_date)
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


        binding.spinnerTypePay.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedName = parent?.getItemAtPosition(position) as? String

                if (selectedName == "Cartão" && cards.isNotEmpty()) {
                    binding.textCard.isVisible = true
                    binding.spinnerCard.isVisible = true
                } else {
                    binding.textCard.isVisible = false
                    binding.spinnerCard.isVisible = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.buttonDatePicker.setOnClickListener {
            CustomDatePicker(binding.buttonDatePicker, parentFragmentManager)
        }
    }

    private fun observe() {
        cardViewModel.cardRecordList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                cards = it
                val cardNames: List<String> = cards.map { cardEntity -> cardEntity.name }

                binding.spinnerCard.adapter = ArrayAdapter(
                    requireContext().applicationContext,
                    R.layout.style_spinner, cardNames
                )
            }
        }

        viewModel.categoryExpenseList.observe(viewLifecycleOwner) {
            categoryExpenseList = it
            val nameCategoriesList: List<String> = categoryExpenseList.map { item -> item.name }
            binding.spinnerCategory.adapter = getAdapter(nameCategoriesList)
        }

        viewModel.typePaymentList.observe(viewLifecycleOwner) {
            paymentTypesList = it
            val namePaymentList: List<String> = paymentTypesList.map { item -> item.name }
            binding.spinnerTypePay.adapter = getAdapter(namePaymentList)
        }

        viewModel.financeRecord.observe(viewLifecycleOwner) { financeRecord ->

            binding.editTitle.setText(financeRecord.title)
            binding.editValue.setText(financeRecord.value.toString())
            binding.buttonDatePicker.text = financeRecord.dateRecord

            val categoryName = financeRecord.categoryExpenseId?.let { id ->
                viewModel.getCategoryById(id).name
            }

            val paymentName = financeRecord.paymentTypeId?.let { id ->
                viewModel.getTypePaymentById(id).name
            }

            val cardName = financeRecord.cardId?.let {id->
                cardViewModel.getCardNameById(id)
            }

            binding.spinnerCategory.setSelection(
                getPositionByName(
                    categoryName,
                    expenseList = categoryExpenseList
                )
            )

            binding.spinnerTypePay.setSelection(
                getPositionByName(
                    paymentName,
                    paymentList = paymentTypesList
                )
            )

            if (financeRecord.cardId != null){
                binding.spinnerCard.setSelection(
                    getPositionByName(
                        cardName,
                        cardList = cards
                    )
                )
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

    //REFACTOR
    private fun getPositionByName(
        name: String?,
        expenseList: List<CategoryExpenseEntity> = listOf(),
        paymentList: List<PaymentTypeEntity> = listOf(),
        cardList: List<CardEntity> = listOf()
    ): Int {
        return when {
            expenseList.isNotEmpty() -> expenseList.indexOfFirst { it.name == name }
            paymentList.isNotEmpty() -> paymentList.indexOfFirst { it.name == name }
            cardList.isNotEmpty() -> cardList.indexOfFirst { it.name == name }
            else -> -1
        }
    }


}