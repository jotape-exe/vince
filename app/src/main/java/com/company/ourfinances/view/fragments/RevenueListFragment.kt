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
import com.company.ourfinances.databinding.FragmentRevenueListBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.CategoryRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.FinanceActivity
import com.company.ourfinances.view.adapters.FinanceRecordAdapter
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.viewmodel.CardViewModel
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class RevenueListFragment : Fragment() {

    private lateinit var binding: FragmentRevenueListBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel
    private lateinit var adapter: FinanceRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRevenueListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]


        adapter = FinanceRecordAdapter()

        binding.recyclerRevenue.layoutManager = LinearLayoutManager(context)
        binding.recyclerRevenue.adapter = adapter

        val listener = object : OnFinanceRecordListener{
            override fun onDelete(id: Long) {
                viewModel.delete(id)
                viewModel.getAllByExpenseCategory(RegisterTypeEnum.REVENUE.value)
            }

            override fun onClick(id: Long) {
                val bundle = Bundle()

                bundle.putLong(DatabaseConstants.FinanceRecord.recordId, id)
                bundle.putString(activity?.getString(R.string.fragmentIdentifier), TitleEnum.RECEITA.value)

                startActivity(Intent(context, FinanceActivity::class.java).putExtras(bundle))
            }

            override fun <T> getEntityNameById(id: Long?, entityType: Class<T>): String {
                return when (entityType) {
                    PaymentTypeEntity::class.java -> {
                        viewModel.getTypePaymentById(id!!).name
                    }

                    CategoryRecordEntity::class.java -> {
                        viewModel.getCategoryById(id!!).name
                    }

                    else -> throw IllegalArgumentException("Tipo de entidade não suportado")
                }
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
        viewModel.getAllByExpenseCategory(RegisterTypeEnum.REVENUE.value)

        observe()
    }

    private fun observe() {
        viewModel.financeRecordList.observe(viewLifecycleOwner) {
            binding.root.findViewById<TextView>(R.id.text_not_data).isVisible = it.isEmpty()
            adapter.updateList(it)
        }

    }

}
