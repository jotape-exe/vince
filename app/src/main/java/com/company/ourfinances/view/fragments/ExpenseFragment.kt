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
import com.company.ourfinances.databinding.FragmentExpenseBinding
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

class ExpenseFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentExpenseBinding

    private lateinit var paymentTypesList: List<PaymentTypeEntity>
    private lateinit var categoryExpenseList: List<CategoryExpenseEntity>
    private lateinit var cards: List<CardEntity>

    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private var selectedName: String? = null
    private var recordId: Long = 0
    private var cardId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]

        viewModel.getAllCategories()
        viewModel.getAllTypePayments()
        cardViewModel.getAllCards()

        loadRecord()

        listeners()

        observe()

        return binding.root
    }

    override fun doSave() {
        if (TextUtils.isEmpty(binding.editTitleExpense.text)) {
            binding.editTitleExpense.error = getString(R.string.title_cannot_be_empty)

        } else if  (TextUtils.equals(binding.buttonDatePickerExpense.text, requireContext().getString(R.string.select_date))) {
            binding.buttonDatePickerExpense.error = getString(R.string.date_cannot_be_empty)

        } else if (TextUtils.isEmpty(binding.editValueExpense.text)) {
            binding.editValueExpense.error = getString(R.string.value_cannot_be_empty)

        } else {

            val categoryExpenseId = getIdCategoryExpenseFromName(
                binding.spinnerCategoryExpense.selectedItem.toString(),
                categoryExpenseList
            )

            val paymentTypeId = getIdPaymentTypeFromName(
                binding.spinnerTypePayExpense.selectedItem.toString(),
                paymentTypesList
            )
            val financeRecord = FinanceRecordEntity.Builder()
                .setRecordId(recordId)
                .setTitle( binding.editTitleExpense.text.toString())
                .setValue(binding.editValueExpense.text.toString().toDouble())
                .setDateRecord(binding.buttonDatePickerExpense.text.toString())
                .setTypeRecord(RegisterTypeEnum.EXPENSE.value)
                .setCategoryExpenseId(categoryExpenseId)
                .setPaymentTypeId(paymentTypeId)

            if (binding.spinnerCardExpense.isVisible) {
                cardId = findCardIdByName(binding.spinnerCardExpense.selectedItem.toString())
                financeRecord.setCardId(cardId)
            }


            viewModel.save(financeRecord.build())
            resetRecordId()

            val bundle = Bundle()
            bundle.putString(getString(R.string.fragmentIdentifier), TitleEnum.DESPESA.value)

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
        binding.editTitleExpense.text.clear()
        binding.editValueExpense.text.clear()
        binding.buttonDatePickerExpense.text = activity?.getString(R.string.select_date)

    }

    override fun getIdCategoryExpenseFromName(spinnerItemName: String, list: List<CategoryExpenseEntity>): Long? {
        val item = list.find { it.name == spinnerItemName }
        return item?.id
    }

    override fun getIdPaymentTypeFromName(spinnerItemName: String, list: List<PaymentTypeEntity>): Long? {
        val item = list.find { it.name == spinnerItemName }
        return item?.paymentId
    }

    private fun resetRecordId() {
        recordId = 0
    }

    private fun observe() {
        cardViewModel.cardRecordList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                cards = it
                val cardNames: List<String> = cards.map { cardEntity -> cardEntity.name }

                binding.spinnerCardExpense.adapter = ArrayAdapter(
                    requireContext().applicationContext,
                    R.layout.style_spinner, cardNames
                )
            }
        }

        viewModel.categoryExpenseList.observe(viewLifecycleOwner) { categorias ->
            categoryExpenseList = categorias
            val nameCategoriesList: List<String> = categoryExpenseList.map { item -> item.name }
            binding.spinnerCategoryExpense.adapter = getAdapter(nameCategoriesList)
        }

        viewModel.typePaymentList.observe(viewLifecycleOwner) { tiposDePagamento ->
            paymentTypesList = tiposDePagamento
            val namePaymentList: List<String> = paymentTypesList.map { item -> item.name }
            binding.spinnerTypePayExpense.adapter = getAdapter(namePaymentList)
        }

        viewModel.financeRecord.observe(viewLifecycleOwner) { financeRecord ->

            binding.editTitleExpense.setText(financeRecord.title)
            binding.editValueExpense.setText(financeRecord.value.toString())
            binding.buttonDatePickerExpense.text = financeRecord.dateRecord

            val categoryName = financeRecord.categoryExpenseId?.let { id ->
                viewModel.getCategoryById(id).name
            }

            val paymentName = financeRecord.paymentTypeId?.let { id ->
                viewModel.getTypePaymentById(id).name
            }

            val cardName = financeRecord.cardId?.let {id->
                cardViewModel.getCardNameById(id)
            }

            binding.spinnerCategoryExpense.setSelection(getPositionByName(categoryName, expenseList = categoryExpenseList))

            binding.spinnerTypePayExpense.setSelection(getPositionByName(paymentName, paymentList = paymentTypesList))


            financeRecord.cardId.let{
                binding.spinnerCardExpense.setSelection(
                    getPositionByName(
                        cardName,
                        cardList = cards
                    )
                )
            }

        }
    }

    private fun listeners() {
        binding.spinnerCategoryExpense.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
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

        binding.spinnerTypePayExpense.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
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

        binding.buttonDatePickerExpense.setOnClickListener {
            CustomDatePicker(binding.buttonDatePickerExpense, parentFragmentManager)
        }

        binding.spinnerTypePayExpense.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedName = parent?.getItemAtPosition(position) as? String

                if (selectedName == "Cartão" && cards.isNotEmpty()) {
                    binding.textCardExpense.isVisible = true
                    binding.spinnerCardExpense.isVisible = true
                } else {
                    binding.textCardExpense.isVisible = false
                    binding.spinnerCardExpense.isVisible = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

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
            if (bundleObj.getString(activity?.getString(R.string.fragmentIdentifier)) == TitleEnum.DESPESA.value){
                recordId = bundleObj.getLong(DatabaseConstants.FinanceRecord.recordId)
                viewModel.getRecordById(recordId)
            }
        }
    }

    private fun findCardIdByName(name: String): Long {
        val card: CardEntity? = cards.find { it.name == name }
        return card!!.cardId
    }

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