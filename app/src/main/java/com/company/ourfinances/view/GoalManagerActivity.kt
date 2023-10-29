package com.company.ourfinances.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityGoalManagerBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.entity.GoalEntity
import com.company.ourfinances.view.utils.CustomDatePicker
import com.company.ourfinances.viewmodel.GoalViewModel

class GoalManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalManagerBinding
    private lateinit var viewModel: GoalViewModel

    private var goalId:Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalManagerBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[GoalViewModel::class.java]
        setContentView(binding.root)

        loadGoal()

        listeners()

        observers()

    }

    private fun observers() {
        viewModel.goal.observe(this) {goal->
            binding.inputGoalName.setText(goal.name)
            binding.inputCurrentRevenue.setText(goal.currentValue.toString())
            binding.inputGoalRevenue.setText(goal.finalValue.toString())
            binding.buttonDatePickerGoal.setText(goal.limitDate.toString())
        }
    }

    private fun loadGoal() {
        val bundle = intent.extras
        bundle?.let { bundleObj ->
                goalId = bundleObj.getLong(DatabaseConstants.FinanceRecord.recordId)
                viewModel.finGoalById(goalId)
            }
        }


    private fun resetGoalId(){
        goalId = 0L
    }

    private fun listeners() {
        binding.inputGoalName.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editGoalName.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.inputCurrentRevenue.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.numberCurrentRevenue.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.inputGoalRevenue.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.numberGoalRevenue.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.imageCloseGoal.setOnClickListener {
            finish()
        }

        binding.buttonSaveGoal.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.inputGoalName.text) -> {
                    binding.editGoalName.error = "Nome vazio!"
                }
                TextUtils.isEmpty(binding.inputCurrentRevenue.text) -> {
                    binding.numberCurrentRevenue.error = "Valor vazio!"
                }
                TextUtils.isEmpty(binding.inputGoalRevenue.text) -> {
                    binding.numberGoalRevenue.error = "Valor vazio!"
                }
                TextUtils.equals(binding.buttonDatePickerGoal.text, getString(R.string.select_date)) -> {
                    binding.buttonDatePickerGoal.error = getString(R.string.date_cannot_be_empty)
                }

                binding.inputGoalRevenue.text.toString().toDouble() <= 0.0 -> {
                    binding.numberGoalRevenue.error = "Meta deve ser maior que zero!"
                }

                binding.inputCurrentRevenue.text.toString().toDouble() > binding.inputGoalRevenue.text.toString().toDouble() ->{
                    binding.numberCurrentRevenue.error = "Valor maior que a meta!"
                }

                else -> {
                    val goal = GoalEntity(
                        goalId,
                        binding.inputGoalName.text.toString(),
                        binding.inputCurrentRevenue.text.toString().toDouble(),
                        binding.inputGoalRevenue.text.toString().toDouble(),
                        binding.buttonDatePickerGoal.text.toString()
                    )

                    viewModel.save(goal)
                    resetGoalId()

                    Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()

                    finish()

                }
            }
        }

        binding.buttonDatePickerGoal.setOnClickListener {
            //CustomDatePicker(binding.buttonDatePickerGoal, supportFragmentManager)
        }

    }
}