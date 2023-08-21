package com.company.ourfinances.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivityGoalManagerBinding

class GoalManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalManagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()
    }

    private fun listeners() {
        binding.imageCloseGoal.setOnClickListener {
            finish()
        }
    }
}