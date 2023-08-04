package com.company.ourfinances.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentTransferBinding
import com.company.ourfinances.databinding.FragmentTransferListBinding
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.view.adapters.FinanceRecordAdapter
import com.company.ourfinances.view.adapters.TransferRecordAdapter
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class TransferListFragment : Fragment() {

    private lateinit var binding: FragmentTransferListBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var adapter: TransferRecordAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransferListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        viewModel.getAllByExpenseCategory(RegisterTypeEnum.TRANSFER.value)

        adapter = TransferRecordAdapter()

        binding.recyclerTransfer.layoutManager = LinearLayoutManager(context)
        binding.recyclerTransfer.adapter = adapter

        val listener = object : OnFinanceRecordListener {
            override fun onDelete(id: Long) {

                viewModel.delete(id)
                viewModel.getAllByExpenseCategory(RegisterTypeEnum.TRANSFER.value)
            }
        }

        adapter.attachToListener(listener)

        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllByExpenseCategory(RegisterTypeEnum.TRANSFER.value)
    }

    private fun observe() {
        viewModel.financeRecord.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }

}