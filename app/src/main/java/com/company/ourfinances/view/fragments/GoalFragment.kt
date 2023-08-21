package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentGoalBinding
import com.company.ourfinances.view.GoalManagerActivity


class GoalFragment : Fragment() {

    private lateinit var binding: FragmentGoalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalBinding.inflate(inflater, container, false)

        linsteners()

        return binding.root
    }

    private fun linsteners() {
        binding.buttonAddGoal.setOnClickListener {
            startActivity(Intent(requireContext(), GoalManagerActivity::class.java))
        }
    }

}