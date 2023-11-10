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
import com.company.ourfinances.databinding.FragmentExpenseBinding
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

class ExpenseFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentExpenseBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var cardViewModel: CardViewModel

    private var cards: List<CardEntity> = listOf()

    private var recordId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)
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

        val selectedName = binding.spinnerTypePayExpense.text.toString()

        val visibilityBySpinnerSelected = selectedName == getString(R.string.card) && cards.isNotEmpty()

        setCardSpinnerVisibility(visibilityBySpinnerSelected)

    }

    private fun setupLists() {
        val namePaymentList: List<String> = TypePaymentList(requireContext()).getList()
        binding.spinnerTypePayExpense.setAdapter(getAdapter(namePaymentList))

        val nameCategoriesList: List<String> = CategoryList(requireContext()).getCategories()
        binding.spinnerCategoryExpense.setAdapter(getAdapter(nameCategoriesList))
    }

    override fun doSave() {
        if (validateFields()) {
            val financeRecord = FinanceRecordEntity.Builder()
                .setRecordId(recordId)
                .setTitle(binding.inputTitleExpense.text.toString())
                .setValue(binding.inputValueExpense.text.toString().toDouble())
                .setDateRecord(binding.buttonDatePickerExpense.text.toString())
                .setTypeRecord(
                    EnumUtils.getRegisterType(
                        RegisterTypeEnum.DESPESA,
                        requireContext()
                    )
                )
                .setCategoryRecord(binding.spinnerCategoryExpense.text.toString())
                .setPaymentType(binding.spinnerTypePayExpense.text.toString())

            if (binding.spinnerCardExpense.isVisible) {
                val cardListener = object : OnSpinnerListener<CardEntity> {
                    override fun getIdByName(name: String): Long {
                        return cards.find { it.name == name }!!.cardId
                    }
                }
                financeRecord.setCardId(cardListener.getIdByName(binding.spinnerCardExpense.text.toString()))
            }

            viewModel.save(financeRecord.build())

            Log.i("info", financeRecord.build().toString())

            clearAll()

            val bundle = Bundle()
            bundle.putString(
                getString(R.string.fragmentIdentifier),
                EnumUtils.getRegisterType(RegisterTypeEnum.DESPESA, context)
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
        binding.inputTitleExpense.text?.clear()
        binding.inputValueExpense.text?.clear()
        binding.buttonDatePickerExpense.setText(activity?.getString(R.string.select_date))
        binding.spinnerCategoryExpense.setText(requireContext().getString(R.string.select), false)
        binding.spinnerTypePayExpense.setText(requireContext().getString(R.string.select), false)
    }

    private fun listeners() {

        binding.inputTitleExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editTitleExpense.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.inputValueExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editValueExpense.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.spinnerCategoryExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.spinnerCategoryExpenseLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.spinnerTypePayExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val selectedName = binding.spinnerTypePayExpense.text.toString()

                val visibilityBySpinnerSelected =
                    selectedName == getString(R.string.card) && cards.isNotEmpty()

                setCardSpinnerVisibility(visibilityBySpinnerSelected)

                if (visibilityBySpinnerSelected) {
                    binding.spinnerTypePayExpenseLayout.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.spinnerCardExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.spinnerCardExpenseLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonDatePickerExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonDatePickerExpenseLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.buttonDatePickerExpense.isFocusable = false
        binding.buttonDatePickerExpense.isClickable = false

        binding.buttonDatePickerExpense.setOnClickListener {
            CustomDatePicker(binding.buttonDatePickerExpense, parentFragmentManager)
        }
    }

    private fun observe() {
        cardViewModel.cardRecordList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                cards = it
                val cardNames: List<String> = cards.map { cardEntity -> cardEntity.name }

                binding.spinnerCardExpense.setAdapter(getAdapter(cardNames))
            }
        }

        viewModel.financeRecord.observe(viewLifecycleOwner) { financeRecord ->

            binding.inputTitleExpense.setText(financeRecord.title)
            binding.inputValueExpense.setText(financeRecord.value.toString())
            binding.buttonDatePickerExpense.setText(financeRecord.dateRecord)

            val cardName = financeRecord.cardId?.let { id ->
                cardViewModel.getCardById(id)?.name
            }

            binding.spinnerCategoryExpense.setText(financeRecord.categoryRecord, false)

            binding.spinnerTypePayExpense.setText(financeRecord.paymentType, false)

            financeRecord.cardId?.let {
                binding.spinnerCardExpense.setText(cardName, false)
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
                    RegisterTypeEnum.DESPESA,
                    requireContext()
                )
            ) {
                recordId = bundleObj.getLong(DatabaseConstants.FinanceRecord.recordId)
                viewModel.getRecordById(recordId)
            }
        }
    }

    private fun setCardSpinnerVisibility(visibility: Boolean) {
        binding.textCardExpense.isVisible = visibility
        binding.spinnerCardExpense.isVisible = visibility
        binding.spinnerCardExpenseLayout.isVisible = visibility
    }

    private fun validateFields(): Boolean {

        var isValid = false

        if (TextUtils.isEmpty(binding.inputTitleExpense.text)) {
            binding.editTitleExpense.error = getString(R.string.title_cannot_be_empty)
        } else if (TextUtils.equals(binding.spinnerCategoryExpense.text, getString(R.string.select))) {
            binding.spinnerCategoryExpenseLayout.error = getString(R.string.select_a_category)
        } else if (TextUtils.equals(
                binding.buttonDatePickerExpense.text,
                requireContext().getString(R.string.select_date)
            )
        ) {
            binding.buttonDatePickerExpenseLayout.error = getString(R.string.date_cannot_be_empty)
        } else if (TextUtils.equals(binding.spinnerTypePayExpense.text, getString(R.string.select))) {
            binding.spinnerTypePayExpenseLayout.error = getString(R.string.select_a_type)
        } else if (cardViewModel.cardRecordList.value!!.isEmpty() and TextUtils.equals(
                binding.spinnerTypePayExpense.text,
                getString(R.string.card)
            )
        ) {
            binding.spinnerTypePayExpenseLayout.error = getString(R.string.no_card)

            Snackbar.make(
                binding.constraintExpense,
                getString(R.string.dont_have_cards),
                Snackbar.LENGTH_SHORT
            ).setAction(getString(R.string.add_card)) {
                startActivity(Intent(context, CardCreateActivity::class.java))
            }.show()

            binding.spinnerTypePayExpense.setText(context?.getString(R.string.select), false)

        } else if (TextUtils.equals(
                binding.spinnerCardExpense.text,
                getString(R.string.select)
            ) and TextUtils.equals(binding.spinnerTypePayExpense.text, getString(R.string.card))
        ) {
            binding.spinnerCardExpenseLayout.error = getString(R.string.choose_a_card)
        } else if (TextUtils.isEmpty(binding.inputValueExpense.text)) {
            binding.editValueExpense.error = getString(R.string.value_cannot_be_empty)
        } else {
            isValid = true
        }

        return isValid
    }


}