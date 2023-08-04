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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentExpenseBinding
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.ShowRecordListActivity
import com.company.ourfinances.view.assets.CustomDatePicker
import com.company.ourfinances.view.listener.FabClickListener
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.google.android.material.snackbar.Snackbar

class ExpenseFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentExpenseBinding
    private lateinit var paymentTypes: List<PaymentTypeEntity>
    private lateinit var categoryExpense: List<CategoryExpenseEntity>
    private lateinit var viewModel: FinanceActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        viewModel.getAllCategories()
        viewModel.getAllTypePayments()

        observe()

        listeners()

        return binding.root
    }

    override fun doSave() {
        if (TextUtils.isEmpty(binding.editTitleExpense.text)) {
            binding.editTitleExpense.error = getString(R.string.title_cannot_be_empty)
        } else if (TextUtils.isEmpty(binding.editValueExpense.text)) {
            binding.editValueExpense.error = getString(R.string.value_cannot_be_empty)
        } else if (TextUtils.equals(binding.buttonDatePickerExpense.text, getString(R.string.select_date))
        ) {
            binding.buttonDatePickerExpense.error = getString(R.string.date_cannot_be_empty)
        } else {
            val financeRecordEntity = FinanceRecordEntity(
                0,
                binding.editTitleExpense.text.toString(),
                binding.editValueExpense.text.toString().toDouble(),
                binding.buttonDatePickerExpense.text.toString(),
                null,
                null,
                RegisterTypeEnum.EXPENSE.value,
                getIdCategoryExpenseFromName(
                    binding.spinnerCategoryExpense.selectedItem.toString(),
                    categoryExpense
                ),
                getIdPaymentTypeFromName(
                    binding.spinnerTypePayExpense.selectedItem.toString(),
                    paymentTypes
                )
            )

            viewModel.insert(financeRecordEntity)

            val bundle = Bundle()

            bundle.putString(getString(R.string.fragmentidentifier), TitleEnum.DESPESA.value)

            clearAll()

            Snackbar.make(binding.root, "Salvo com sucesso!", Snackbar.LENGTH_LONG)
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

    override fun clearAll() {
        binding.editTitleExpense.text.clear()
        binding.editValueExpense.text.clear()
        binding.buttonDatePickerExpense.text = activity?.getString(R.string.select_date)

    }

    override fun getIdCategoryExpenseFromName(
        spinnerItemName: String,
        list: List<CategoryExpenseEntity>
    ): Long? {
        val item = list.find { it.name == spinnerItemName }
        return item?.id
    }

    override fun getIdPaymentTypeFromName(
        spinnerItemName: String,
        list: List<PaymentTypeEntity>
    ): Long? {
        val item = list.find { it.name == spinnerItemName }
        return item?.paymentId
    }

    private fun observe() {
        viewModel.categoryExpenseList.observe(viewLifecycleOwner) { categorias ->
            categoryExpense = categorias
            val nameCategoriesList: List<String> = categoryExpense.map { item -> item.name }
            binding.spinnerCategoryExpense.adapter = getAdapter(nameCategoriesList)
        }

        viewModel.typePay.observe(viewLifecycleOwner) { tiposDePagamento ->
            paymentTypes = tiposDePagamento
            val namePaymentList: List<String> = paymentTypes.map { item -> item.name }
            binding.spinnerTypePayExpense.adapter = getAdapter(namePaymentList)
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
            CustomDatePicker(binding.buttonDatePickerExpense, this)
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
}