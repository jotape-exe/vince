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
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.enums.EnumUtils
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.view.FinanceActivity
import com.company.ourfinances.view.adapters.TransferRecordAdapter
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.viewmodel.CardViewModel
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class TransferListFragment : Fragment() {

    private lateinit var binding: FragmentTransferListBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private lateinit var adapter: TransferRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]

        binding.recyclerTransfer.layoutManager = LinearLayoutManager(context)


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllByExpenseCategory(EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA,requireContext()))

        val listener = object : OnFinanceRecordListener {
            override fun onDelete(id: Long) {
                adapter.removeItemById(id)
                viewModel.delete(id)
                adapter.notifyDataSetChanged()
                viewModel.getAllByExpenseCategory(EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA, requireContext()))
            }

            override fun onClick(id: Long) {
                val bundle = Bundle()

                bundle.putLong(DatabaseConstants.FinanceRecord.recordId, id)
                bundle.putString(activity?.getString(R.string.fragmentIdentifier), EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA, requireContext()))

                startActivity(Intent(context, FinanceActivity::class.java).putExtras(bundle))
            }

            override fun getCardById(id: Long?): CardEntity? {
                return id?.let { cardViewModel.getCardById(it) }
            }
        }

        viewModel.financeRecordList.value?.let{
            adapter = TransferRecordAdapter(it)
        }

        binding.recyclerTransfer.adapter = adapter

        adapter.attachToListener(listener)

        observe()

    }

    private fun observe() {
        viewModel.financeRecordList.observe(viewLifecycleOwner) {
            binding.root.findViewById<TextView>(R.id.text_not_data).isVisible = it.isEmpty()
        }
    }

}