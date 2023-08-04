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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentRevenueBinding
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

class RevenueFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var viewModel: FinanceActivityViewModel

    private lateinit var paymentTypes: List<PaymentTypeEntity>
    private lateinit var categoryExpense: List<CategoryExpenseEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRevenueBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        viewModel.getAllCategories()
        viewModel.getAllTypePayments()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        listeners()

    }

    override fun doSave() {
        if (TextUtils.isEmpty(binding.editTitle.text)) {
            binding.editTitle.error = getString(R.string.title_cannot_be_empty)
        } else if (TextUtils.isEmpty(binding.editValue.text)) {
            binding.editValue.error = getString(R.string.value_cannot_be_empty)
        } else if (TextUtils.equals(binding.buttonDatePicker.text, getString(R.string.select_date))) {
            binding.buttonDatePicker.error = getString(R.string.date_cannot_be_empty)
        } else {
            val financeRecordEntity = FinanceRecordEntity(
                0,
                binding.editTitle.text.toString(),
                binding.editValue.text.toString().toDouble(),
                binding.buttonDatePicker.text.toString(),
                null,
                null,
                RegisterTypeEnum.REVENUE.value,
                getIdCategoryExpenseFromName(
                    binding.spinnerCategory.selectedItem.toString(),
                    categoryExpense
                ),
                getIdPaymentTypeFromName(
                    binding.spinnerTypePay.selectedItem.toString(),
                    paymentTypes
                )
            )

            viewModel.insert(financeRecordEntity)

            val bundle = Bundle()

            bundle.putString(getString(R.string.fragmentidentifier), TitleEnum.RECEITA.value)

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

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.buttonDatePicker.setOnClickListener {
            CustomDatePicker(binding.buttonDatePicker, this)
        }
    }

    private fun observe() {
        viewModel.categoryExpenseList.observe(viewLifecycleOwner) {
            categoryExpense = it
            val nameCategoriesList: List<String> = categoryExpense.map { item -> item.name }
            binding.spinnerCategory.adapter = getAdapter(nameCategoriesList)
        }

        viewModel.typePay.observe(viewLifecycleOwner) {
            paymentTypes = it
            val namePaymentList: List<String> = paymentTypes.map { item -> item.name }
            binding.spinnerTypePay.adapter = getAdapter(namePaymentList)
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

    //Logia na viewModel
    override fun getIdCategoryExpenseFromName(
        spinnerItemName: String,
        list: List<CategoryExpenseEntity>
    ): Long? {
        val item = list.find { it.name == spinnerItemName }
        return item?.id
    }

    override fun getIdPaymentTypeFromName(spinnerItemName: String, list: List<PaymentTypeEntity>): Long? {
        val item = list.find { it.name == spinnerItemName }
        return item?.paymentId
    }

    override fun clearAll() {
        binding.editTitle.text.clear()
        binding.editValue.text.clear()
        binding.buttonDatePicker.text = activity?.getString(R.string.select_date)
    }


}