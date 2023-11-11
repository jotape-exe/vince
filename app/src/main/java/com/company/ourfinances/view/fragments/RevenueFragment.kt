package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentRevenueBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.enums.EnumUtils
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.view.CardCreateActivity
import com.company.ourfinances.view.ShowRecordListActivity
import com.company.ourfinances.view.listener.FabClickListener
import com.company.ourfinances.view.listener.OnSpinnerListener
import com.company.ourfinances.view.utils.CategoryList
import com.company.ourfinances.view.utils.CustomDatePicker
import com.company.ourfinances.view.utils.TypePaymentList
import com.company.ourfinances.viewmodel.CardViewModel
import com.company.ourfinances.viewmodel.FinanceActivityViewModel
import com.google.android.material.snackbar.Snackbar

class RevenueFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private var cards: List<CardEntity> = listOf()

    private var recordId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRevenueBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadRecord()

        setupLists()

        observe()

        listeners()

    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllCategories()
        viewModel.getAllTypePayments()
        cardViewModel.getAllCards()

        val selectedName = binding.spinnerTypePay.text.toString()

        val visibilityBySpinnerSelected = selectedName == getString(R.string.card) && cards.isNotEmpty()

        setCardSpinnerVisibility(visibilityBySpinnerSelected)

    }

    private fun setupLists() {
        val namePaymentList: List<String> = TypePaymentList(requireContext()).getList()
        binding.spinnerTypePay.setAdapter(getAdapter(namePaymentList))

        val nameCategoriesList: List<String> = CategoryList(requireContext()).getCategories()
        binding.spinnerCategory.setAdapter(getAdapter(nameCategoriesList))
    }

    override fun doSave() {
        if (validateFields()) {
            val financeRecord = FinanceRecordEntity.Builder()
                .setRecordId(recordId)
                .setTitle(binding.inputTitle.text.toString())
                .setValue(binding.inputValue.text.toString().toDouble())
                .setDateRecord(binding.buttonDatePicker.text.toString())
                .setTypeRecord(
                    EnumUtils.getRegisterType(
                        RegisterTypeEnum.RECEITA,
                        requireContext()
                    )
                )
                .setCategoryRecord(binding.spinnerCategory.text.toString())
                .setPaymentType(binding.spinnerTypePay.text.toString())

            if (binding.spinnerCard.isVisible) {
                val cardListener = object : OnSpinnerListener<CardEntity> {
                    override fun getIdByName(name: String): Long {
                        return cards.find { it.name == name }!!.cardId
                    }
                }
                financeRecord.setCardId(cardListener.getIdByName(binding.spinnerCard.text.toString()))
            }

            viewModel.save(financeRecord.build())

            Log.i("info", financeRecord.build().toString())

            clearAll()

            val bundle = Bundle()
            bundle.putString(
                getString(R.string.fragmentIdentifier),
                EnumUtils.getRegisterType(RegisterTypeEnum.RECEITA, context)
            )

            val extras = activity?.intent?.extras?.let {
                it.getString(activity?.getString(R.string.fragmentIdentifier)) ?: ""
            }

            activity?.findViewById<View>(R.id.finance_main)?.let { view ->
                Snackbar.make(view, getString(R.string.saved_successfully), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.view)) {
                        if (extras.isNullOrBlank()) {
                            activity?.startActivity(
                                Intent(
                                    context,
                                    ShowRecordListActivity::class.java
                                ).putExtras(bundle)
                            )
                        }

                        activity?.finish()
                    }.show()
            }

            resetRecordId()
        }
    }

    private fun resetRecordId() {
        recordId = 0
    }

    override fun clearAll() {
        binding.inputTitle.text?.clear()
        binding.inputValue.text?.clear()
        binding.buttonDatePicker.setText(activity?.getString(R.string.select_date))
        binding.spinnerCategory.setText(requireContext().getString(R.string.select), false)
        binding.spinnerTypePay.setText(requireContext().getString(R.string.select), false)
    }

    private fun listeners() {

        binding.inputTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editTitle.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.inputValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editValue.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.spinnerCategory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.spinnerCategoryLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.spinnerTypePay.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val selectedName = binding.spinnerTypePay.text.toString()

                val visibilityBySpinnerSelected =
                    selectedName == getString(R.string.card) && cards.isNotEmpty()

                setCardSpinnerVisibility(visibilityBySpinnerSelected)

                //if (visibilityBySpinnerSelected) {
                    binding.spinnerTypePayLayout.error = null
                //}
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.spinnerCard.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.spinnerCardLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonDatePicker.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonDatePickerLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonDatePicker.isFocusable = false
        binding.buttonDatePicker.isClickable = false

        binding.buttonDatePicker.setOnClickListener {
            CustomDatePicker(binding.buttonDatePicker, parentFragmentManager)
        }
    }

    private fun observe() {
        cardViewModel.cardRecordList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                cards = it
                val cardNames: List<String> = cards.map { cardEntity -> cardEntity.name }

                binding.spinnerCard.setAdapter(getAdapter(cardNames))
            }
        }

        viewModel.financeRecord.observe(viewLifecycleOwner) { financeRecord ->

            binding.inputTitle.setText(financeRecord.title)
            binding.inputValue.setText(financeRecord.value.toString())
            binding.buttonDatePicker.setText(financeRecord.dateRecord)

            val cardName = financeRecord.cardId?.let { id ->
                cardViewModel.getCardById(id)?.name
            }

            binding.spinnerCategory.setText(financeRecord.categoryRecord, false)

            binding.spinnerTypePay.setText(financeRecord.paymentType, false)

            financeRecord.cardId?.let {
                binding.spinnerCard.setText(cardName, false)
            }

        }
    }

    private fun getAdapter(itemsList: List<String>): ArrayAdapter<String>? {
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.style_spinner, itemsList
            )
        }
        return adapter
    }

    private fun loadRecord() {
        val bundle = activity?.intent?.extras
        //Log.i("info", "DATA -> ${bundle!!.getString(activity?.getString(R.string.fragmentIdentifier))}")
        bundle?.let { bundleObj ->
            if (bundleObj.getString(activity?.getString(R.string.fragmentIdentifier)) == EnumUtils.getRegisterType(
                    RegisterTypeEnum.RECEITA,
                    requireContext()
                )
            ) {
                recordId = bundleObj.getLong(DatabaseConstants.FinanceRecord.recordId)
                viewModel.getRecordById(recordId)
            }
        }
    }

    private fun setCardSpinnerVisibility(visibility: Boolean) {
        binding.textCard.isVisible = visibility
        binding.spinnerCard.isVisible = visibility
        binding.spinnerCardLayout.isVisible = visibility
    }

    private fun validateFields(): Boolean {

        var isValid = false

        if (TextUtils.isEmpty(binding.inputTitle.text)) {
            binding.editTitle.error = getString(R.string.title_cannot_be_empty)
        } else if (TextUtils.equals(binding.spinnerCategory.text, getString(R.string.select))) {
            binding.spinnerCategoryLayout.error = getString(R.string.select_a_category)
        } else if (TextUtils.equals(binding.buttonDatePicker.text, requireContext().getString(R.string.select_date))) {
            binding.buttonDatePickerLayout.error = getString(R.string.date_cannot_be_empty)
        } else if (TextUtils.equals(binding.spinnerTypePay.text, getString(R.string.select))) {
            binding.spinnerTypePayLayout.error = getString(R.string.select_a_type)
        } else if (cardViewModel.cardRecordList.value!!.isEmpty() and TextUtils.equals(binding.spinnerTypePay.text, getString(R.string.card))) {
            binding.spinnerTypePayLayout.error = getString(R.string.no_card)

            Snackbar.make(
                binding.constraintRevenue,
                getString(R.string.dont_have_cards),
                Snackbar.LENGTH_SHORT
            ).setAction(getString(R.string.add_card)) {
                startActivity(Intent(context, CardCreateActivity::class.java))
            }.show()

            binding.spinnerTypePay.setText(context?.getString(R.string.select), false)

        } else if (TextUtils.equals(binding.spinnerCard.text, getString(R.string.select)) and TextUtils.equals(binding.spinnerTypePay.text, getString(R.string.card))) {
            binding.spinnerCardLayout.error = getString(R.string.choose_a_card)
        } else if (TextUtils.isEmpty(binding.inputValue.text)) {
            binding.editValue.error = getString(R.string.value_cannot_be_empty)
        } else {
            isValid = true
        }

        return isValid
    }


}