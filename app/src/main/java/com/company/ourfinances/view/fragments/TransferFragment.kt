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
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentTransferBinding
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

class TransferFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentTransferBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var paymentTypes: List<PaymentTypeEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransferBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        viewModel.getAllTypePayments()

        observe()

        listeners()

        return binding.root
    }

    private fun observe() {
        viewModel.typePay.observe(viewLifecycleOwner) { tiposDePagamento ->
            paymentTypes = tiposDePagamento
            val namePaymentList: List<String> = paymentTypes.map { item -> item.name }
            binding.spinnerTypePayTransfer.adapter = getAdapter(namePaymentList)
        }
    }

    override fun doSave() {
        if (TextUtils.isEmpty(binding.editTitleTransfer.text)) {
            binding.editTitleTransfer.error = getString(R.string.title_cannot_be_empty)
        } else if (TextUtils.isEmpty(binding.editValueTransfer.text)) {
            binding.editValueTransfer.error = getString(R.string.value_cannot_be_empty)
        } else if (TextUtils.equals(binding.buttonDatePickerTransfer.text, getString(R.string.select_date))) {
            binding.buttonDatePickerTransfer.error = getString(R.string.date_cannot_be_empty)
        } else if (TextUtils.isEmpty(binding.editReceiverTransfer.text)){
            binding.editReceiverTransfer.error = getString(R.string.receiver_not_empty)
        } else {
            val financeRecordEntity = FinanceRecordEntity(
                0,
                binding.editTitleTransfer.text.toString(),
                binding.editValueTransfer.text.toString().toDouble(),
                binding.buttonDatePickerTransfer.text.toString(),
                null,
                binding.editReceiverTransfer.text.toString(),
                RegisterTypeEnum.TRANSFER.value,
                null,
                getIdPaymentTypeFromName(
                    binding.spinnerTypePayTransfer.selectedItem.toString(),
                    paymentTypes
                )
            )

            viewModel.save(financeRecordEntity)

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

    override fun getIdCategoryExpenseFromName(
        spinnerItemName: String,
        list: List<CategoryExpenseEntity>
    ): Long? {
        return 0
    }

    override fun getIdPaymentTypeFromName(
        spinnerItemName: String,
        list: List<PaymentTypeEntity>
    ): Long? {
        val item = list.find { it.name == spinnerItemName }
        return item?.paymentId
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

    private fun listeners() {
        binding.spinnerTypePayTransfer.onItemSelectedListener = object :
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


        binding.buttonDatePickerTransfer.setOnClickListener {
            CustomDatePicker(binding.buttonDatePickerTransfer, this)
        }
    }
}