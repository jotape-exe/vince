package com.company.ourfinances.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentInsightsBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var entries: ArrayList<PieEntry>
    private lateinit var pieDataSet: PieDataSet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        binding = FragmentInsightsBinding.inflate(inflater, container, false)

        entries = ArrayList()

        pieDataSet = PieDataSet(entries, "")

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFinanceRecords()

        viewModel.financeRecordList.observe(viewLifecycleOwner) { list ->
            entries.clear()

           /*entries.add(PieEntry(0f, "Receita"))
            entries.add(PieEntry(0f, "Transferencia"))
            entries.add(PieEntry(0f, "Despesa"))*/

            addPieEntryForType(list, RegisterTypeEnum.REVENUE, TitleEnum.RECEITA)
            addPieEntryForType(list, RegisterTypeEnum.TRANSFER, TitleEnum.TRANSFERENCIA)
            addPieEntryForType(list, RegisterTypeEnum.EXPENSE, TitleEnum.DESPESA)

            val label: String = if (list.isEmpty()) "Sem Registros" else "Valor em R$"
            binding.piechart.centerText = label
            binding.piechart.setCenterTextSize(18f)

        }

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


    }

    private fun addPieEntryForType(
        list: List<FinanceRecordEntity>,
        typeEnum: RegisterTypeEnum,
        title: TitleEnum
    ) {
        val value = list.filter { record ->
            record.typeRecord == typeEnum.value
        }.sumOf { record ->
            record.value
        }.toFloat()

        if (value > 0) {
            while (entries.size < 3) {
                entries.add(PieEntry(0f, "Registro a salvar"))
            }

            when (typeEnum) {
                RegisterTypeEnum.REVENUE -> {
                    entries[0] = PieEntry(value, title.value)
                }

                RegisterTypeEnum.TRANSFER -> {
                    entries[1] = PieEntry(value, title.value)
                }

                RegisterTypeEnum.EXPENSE -> {
                    entries[2] = PieEntry(value, title.value)
                }
            }
        }
    }


}