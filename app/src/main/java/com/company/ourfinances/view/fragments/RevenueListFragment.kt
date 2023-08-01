package com.company.ourfinances.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.ourfinances.databinding.FragmentRevenueListBinding
import com.company.ourfinances.view.adapters.FinanceRecordAdapter
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.viewmodel.RevenueFragmentViewModel

class RevenueListFragment : Fragment() {

    private lateinit var binding: FragmentRevenueListBinding
    private lateinit var viewModel: RevenueFragmentViewModel
    private lateinit var adapter: FinanceRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRevenueListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RevenueFragmentViewModel::class.java]

        viewModel.getAllRecords()

        adapter = FinanceRecordAdapter(viewModel)

        binding.recyclerRevenue.layoutManager = LinearLayoutManager(context)
        binding.recyclerRevenue.adapter = adapter

        val listener = object : OnFinanceRecordListener{
            override fun onDelete(id: Long) {
                Toast.makeText(context, "Meu ID -> $id", Toast.LENGTH_SHORT).show()
            }

        }

        adapter.attachToListener(listener)

        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.financeRecord.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }
}
