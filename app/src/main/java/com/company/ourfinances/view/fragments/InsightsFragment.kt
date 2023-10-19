package com.company.ourfinances.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.company.ourfinances.databinding.FragmentInsightsBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.github.mikephil.charting.data.BarEntry

class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var pie: Pie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        binding = FragmentInsightsBinding.inflate(inflater, container, false)
        viewModel.getAllFinanceRecords()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pie = AnyChart.pie()

        viewModel.getAllFinanceRecords()

        viewModel.financeRecordList.observe(viewLifecycleOwner) { list ->
            val revenueValue = calculateTotalByType(list, RegisterTypeEnum.REVENUE)
            val transferValue = calculateTotalByType(list, RegisterTypeEnum.TRANSFER)
            val expenseValue = calculateTotalByType(list, RegisterTypeEnum.EXPENSE)

            val dataEntries = listOf(
                ValueDataEntry(TitleEnum.RECEITA.value, revenueValue),
                ValueDataEntry(TitleEnum.TRANSFERENCIA.value, transferValue),
                ValueDataEntry(TitleEnum.DESPESA.value, expenseValue)
            )

            pie.data(dataEntries)
            binding.anyChart.setChart(pie)
            binding.anyChart.setProgressBar(binding.progressBar)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun calculateTotalByType(list: List<FinanceRecordEntity>, typeEnum: RegisterTypeEnum): Float {
        return list.filter { record -> record.typeRecord == typeEnum.value }
            .sumOf { record -> record.value }
            .toFloat()
    }


}