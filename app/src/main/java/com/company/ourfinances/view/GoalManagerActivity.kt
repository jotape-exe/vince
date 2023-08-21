package com.company.ourfinances.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityGoalManagerBinding
import com.company.ourfinances.model.entity.GoalEntity
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
    }

    private fun loadGoal() {

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

                TextUtils.equals(
                    binding.buttonDatePickerGoal.text,
                    getString(R.string.select_date)
                ) -> {
                    binding.buttonDatePickerGoal.error = getString(R.string.date_cannot_be_empty)
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

                }
            }
        }

    }
}