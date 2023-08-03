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
import android.widget.Toast
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
import com.company.ourfinances.viewmodel.RevenueFragmentViewModel
import com.company.ourfinances.view.listener.FabClickListener
import com.google.android.material.snackbar.Snackbar

class RevenueFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var viewModel: RevenueFragmentViewModel

    private lateinit var paymentTypes: List<PaymentTypeEntity>
    private lateinit var categoryExpense: List<CategoryExpenseEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRevenueBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RevenueFragmentViewModel::class.java]

        viewModel.getAllCategories()
        viewModel.getAllTypePayments()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        listeners()

    }

    override fun getData(): FinanceRecordEntity? {
        if (TextUtils.isEmpty(binding.editTitle.text)) {
            binding.editTitle.error = getString(R.string.title_cannot_be_empty)
            return null
        } else if (TextUtils.isEmpty(binding.editValue.text)) {
            binding.editValue.error = getString(R.string.value_cannot_be_empty)
            return null
        } else if (TextUtils.equals(binding.buttonDatePicker.text,getString(R.string.select_date))
        ) {
            binding.buttonDatePicker.error = getString(R.string.date_cannot_be_empty)
            return null
        }

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
            getIdPaymentTypeFromName(binding.spinnerTypePay.selectedItem.toString(), paymentTypes)
        )

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

        return financeRecordEntity
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

    //Refatorar 
    private fun getIdCategoryExpenseFromName(
        name: String,
        itemList: List<CategoryExpenseEntity>
    ): Long? {
        val item = itemList.find { it.name == name }
        return item?.id
    }

    private fun getIdPaymentTypeFromName(name: String, itemList: List<PaymentTypeEntity>): Long? {
        val item = itemList.find { it.name == name }
        return item?.paymentId
    }

    private fun clearAll() {
        binding.editTitle.text.clear()
        binding.editValue.text.clear()
        binding.buttonDatePicker.text = activity?.getString(R.string.select_date)
    }


}