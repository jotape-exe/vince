package com.company.ourfinances.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentRevenueBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.view.assets.CustomDatePicker
import com.company.ourfinances.viewmodel.RevenueFragmentViewModel
import com.company.ourfinances.viewmodel.service.FabClickListener

class RevenueFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var viewModel: RevenueFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRevenueBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RevenueFragmentViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapters()

        listeners()

    }


    //Revomer logica para viewModel
    override fun onFabClicked() {

        val financeRecordEntity = FinanceRecordEntity(
            0,
            binding.editTitle.text.toString(),
            binding.editValue.text.toString().toDouble(),
            null,
            null,
            RegisterTypeEnum.REVENUE.value,
            1, //Selecionar no Spinner
            1
        )

        Toast.makeText(requireContext(), "$financeRecordEntity", Toast.LENGTH_LONG).show()
    }

    private fun adapters() {
        //Usar SQLITE
        val categoriesList = mutableListOf("Alimentação", "Lazer", "Moradia")
        val typePayList = mutableListOf("Cartão 1", "Dinheiro", "Pix")

        binding.spinnerTypePay.adapter = getAdapter(typePayList)
        binding.spinnerCategory.adapter = getAdapter(categoriesList)
    }

    private fun listeners() {
        binding.spinnerCategory.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.buttonDatePicker.setOnClickListener {
            CustomDatePicker(binding.buttonDatePicker, this)
        }
    }

    private fun observer() {

    }

    private fun getAdapter(itemsList: MutableList<String>): ArrayAdapter<String>? {
        val adapter = activity?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.style_spinner, itemsList
            )
        }
        return adapter
    }


}