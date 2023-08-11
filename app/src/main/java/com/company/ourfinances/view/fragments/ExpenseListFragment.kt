package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentExpenseListBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.FinanceActivity
import com.company.ourfinances.view.adapters.FinanceRecordAdapter
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class ExpenseListFragment : Fragment() {

    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var adapter: FinanceRecordAdapter
    private lateinit var binding: FragmentExpenseListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

        viewModel.getAllByExpenseCategory(RegisterTypeEnum.EXPENSE.value)

        adapter = FinanceRecordAdapter(viewModel)

        binding.recyclerExpense.layoutManager = LinearLayoutManager(context)
        binding.recyclerExpense.adapter = adapter

        val listener = object : OnFinanceRecordListener {
            override fun onDelete(id: Long) {
                viewModel.delete(id)
                viewModel.getAllByExpenseCategory(RegisterTypeEnum.EXPENSE.value)
            }

            override fun onClick(id: Long) {
                val bundle = Bundle()

                //DoRefactor(FinancePreferences)
                bundle.putLong(DatabaseConstants.FinanceRecord.recordId, id)
                bundle.putString(activity?.getString(R.string.fragmentIdentifier), TitleEnum.DESPESA.value)

                startActivity(Intent(context, FinanceActivity::class.java).putExtras(bundle))
                activity?.finish()
            }
        }

        adapter.attachToListener(listener)

        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllByExpenseCategory(RegisterTypeEnum.EXPENSE.value)
    }

    private fun observe() {
        viewModel.financeRecordList.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }

}