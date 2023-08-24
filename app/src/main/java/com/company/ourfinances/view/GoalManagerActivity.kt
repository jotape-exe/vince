package com.company.ourfinances.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityGoalManagerBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.entity.GoalEntity
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.assets.CustomDatePicker
import com.company.ourfinances.view.listener.OnGoalListener
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
            binding.editGoalName.setText(goal.name)
            binding.numberCurrentRevenue.setText(goal.currentValue.toString())
            binding.numberGoalRevenue.setText(goal.finalValue.toString())
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
        binding.imageCloseGoal.setOnClickListener {
            finish()
        }

        binding.buttonSaveGoal.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.editGoalName.text) -> {
                    binding.editGoalName.error = "Nome vazio!"
                }

                TextUtils.isEmpty(binding.numberCurrentRevenue.text) -> {
                    binding.numberCurrentRevenue.error = "Valor vazio!"
                }

                TextUtils.isEmpty(binding.numberGoalRevenue.text) -> {
                    binding.numberGoalRevenue.error = "Valor vazio!"
                }

                TextUtils.equals(binding.buttonDatePickerGoal.text, getString(R.string.select_date)) -> {
                    binding.buttonDatePickerGoal.error = getString(R.string.date_cannot_be_empty)
                }

                binding.numberGoalRevenue.text.toString().toInt() <= 0 -> {
                    binding.numberGoalRevenue.error = "Meta deve ser maior que zero!"
                }

                binding.numberCurrentRevenue.text.toString().toDouble() > binding.numberGoalRevenue.text.toString().toDouble() ->{
                    binding.numberCurrentRevenue.error = "Valor maior que a meta!"
                }

                else -> {
                    val goal = GoalEntity(
                        goalId,
                        binding.editGoalName.text.toString(),
                        binding.numberCurrentRevenue.text.toString().toDouble(),
                        binding.numberGoalRevenue.text.toString().toDouble(),
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
            CustomDatePicker(binding.buttonDatePickerGoal, supportFragmentManager)
        }

    }
}