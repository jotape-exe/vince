package com.company.ourfinances.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentInsightsBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.enums.EnumUtils
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pie = AnyChart.pie()
        pie.labels().position("outside")
        pie.title(getString(R.string.transactions).uppercase())
        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER);

        viewModel.getAllFinanceRecords()

        observe()

    }

    private fun calculateTotalByType(list: List<FinanceRecordEntity>, typeEnum: RegisterTypeEnum): Float {
        return list.filter { record -> record.typeRecord == typeEnum.value }
            .sumOf { record -> record.value }
            .toFloat()
    }

    private fun observe(){
        viewModel.financeRecordList.observe(viewLifecycleOwner) { list ->

            binding.root.findViewById<TextView>(R.id.text_not_data).isVisible = list.isEmpty()

            val revenueValue = calculateTotalByType(list, RegisterTypeEnum.RECEITA)
            val transferValue = calculateTotalByType(list, RegisterTypeEnum.TRANSFERENCIA)
            val expenseValue = calculateTotalByType(list, RegisterTypeEnum.DESPESA)

            val dataEntries = listOf(
                ValueDataEntry(EnumUtils.getRegisterType(RegisterTypeEnum.RECEITA, requireContext()), revenueValue),
                ValueDataEntry(EnumUtils.getRegisterType(RegisterTypeEnum.DESPESA, requireContext()), transferValue),
                ValueDataEntry(EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA, requireContext()), expenseValue)
            )

            pie.data(dataEntries)
            binding.anyChart.setChart(pie)
            binding.anyChart.setProgressBar(binding.progressBar)
        }
    }


}