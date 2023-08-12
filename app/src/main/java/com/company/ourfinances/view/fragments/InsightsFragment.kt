package com.company.ourfinances.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentInsightsBinding
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding
    private lateinit var viewModel: FinanceActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        binding = FragmentInsightsBinding.inflate(inflater, container, false)

        val entries = ArrayList<PieEntry>()
        viewModel.getAllFinanceRecords()

        viewModel.financeRecordList.observe(viewLifecycleOwner) { list ->
            entries.add(PieEntry(list.filter { it.typeRecord == RegisterTypeEnum.REVENUE.value }.sumOf { it.value }.toFloat(), "Receitas"))
            entries.add(PieEntry(list.filter { it.typeRecord == RegisterTypeEnum.TRANSFER.value }.sumOf { it.value }.toFloat(), "Transferencias"))
            entries.add(PieEntry(list.filter { it.typeRecord == RegisterTypeEnum.EXPENSE.value }.sumOf { it.value }.toFloat(), "Despesas"))
        }

        val pieDataSet = PieDataSet(entries, "")

        val colors: ArrayList<Int> = ArrayList()
        colors.add(requireContext().getColor(R.color.green))
        colors.add(requireContext().getColor(R.color.blue))
        colors.add(requireContext().getColor(R.color.red))

        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 16f

        val pieData = PieData(pieDataSet)

        binding.piechart.data = pieData
        binding.piechart.description.isEnabled = false
        binding.piechart.animateY(800)
        binding.piechart.postInvalidate()

        binding.piechart.setEntryLabelColor(requireContext().getColor(R.color.black))
        binding.piechart.setEntryLabelTextSize(14f)

        return binding.root
    }

}