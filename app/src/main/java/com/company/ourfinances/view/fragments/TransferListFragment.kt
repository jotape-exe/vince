package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentTransferListBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.FinanceActivity
import com.company.ourfinances.view.adapters.TransferRecordAdapter
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class TransferListFragment : Fragment() {

    private lateinit var binding: FragmentTransferListBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var adapter: TransferRecordAdapter
    private lateinit var textNotEmpty: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransferListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        adapter = TransferRecordAdapter()

        binding.recyclerTransfer.layoutManager = LinearLayoutManager(context)
        binding.recyclerTransfer.adapter = adapter

        val listener = object : OnFinanceRecordListener {
            override fun onDelete(id: Long) {
                viewModel.delete(id)
                viewModel.getAllByExpenseCategory(RegisterTypeEnum.TRANSFER.value)
            }

            override fun onClick(id: Long) {
                val bundle = Bundle()

                bundle.putLong(DatabaseConstants.FinanceRecord.recordId, id)
                bundle.putString(activity?.getString(R.string.fragmentIdentifier), TitleEnum.TRANSFERENCIA.value)

                startActivity(Intent(context, FinanceActivity::class.java).putExtras(bundle))
                activity?.finish()
            }

            override fun getPaymentNameById(id: Long?): String {
                return viewModel.getTypePaymentById(id!!).name
            }

            override fun getCategoryNameById(id: Long?): String {
                return ""
            }
        }

        adapter.attachToListener(listener)

        textNotEmpty = binding.root.findViewById<TextView>(R.id.text_not_data)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllByExpenseCategory(RegisterTypeEnum.TRANSFER.value)

        observe()

    }

    private fun observe() {
        viewModel.financeRecordList.observe(viewLifecycleOwner) {
            textNotEmpty.isVisible = it.isEmpty()
            adapter.updateList(it)
        }
    }

}