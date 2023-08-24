package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentExpenseListBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.FinanceActivity
import com.company.ourfinances.view.adapters.FinanceRecordAdapter
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.viewmodel.CardViewModel
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class ExpenseListFragment : Fragment() {

    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private lateinit var adapter: FinanceRecordAdapter
    private lateinit var binding: FragmentExpenseListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]

        viewModel.getAllByExpenseCategory(RegisterTypeEnum.EXPENSE.value)

        adapter = FinanceRecordAdapter()

        binding.recyclerExpense.layoutManager = LinearLayoutManager(context)
        binding.recyclerExpense.adapter = adapter

        val listener = object : OnFinanceRecordListener {
            override fun onDelete(id: Long) {
                viewModel.delete(id)
                viewModel.getAllByExpenseCategory(RegisterTypeEnum.EXPENSE.value)
            }

            override fun onClick(id: Long) {
                val bundle = Bundle()

                bundle.putLong(DatabaseConstants.FinanceRecord.recordId, id)
                bundle.putString(activity?.getString(R.string.fragmentIdentifier), TitleEnum.DESPESA.value)

                startActivity(Intent(context, FinanceActivity::class.java).putExtras(bundle))
            }

            override fun getPaymentNameById(id: Long?): String {
                return viewModel.getTypePaymentById(id!!).name
            }

            override fun getCategoryNameById(id: Long?): String {
                return viewModel.getCategoryById(id!!).name
            }

            override fun getCardById(id: Long?): CardEntity? {
                return id?.let { cardViewModel.getCardById(it) }
            }

        }

        adapter.attachToListener(listener)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllByExpenseCategory(RegisterTypeEnum.EXPENSE.value)

        observe()
    }

    private fun observe() {
        viewModel.financeRecordList.observe(viewLifecycleOwner) {
            binding.root.findViewById<TextView>(R.id.text_not_data).isVisible = it.isEmpty()
            adapter.updateList(it)
        }
    }

}