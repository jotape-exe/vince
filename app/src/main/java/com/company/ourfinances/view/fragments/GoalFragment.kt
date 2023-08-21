package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentGoalBinding
import com.company.ourfinances.view.GoalManagerActivity
import com.company.ourfinances.viewmodel.GoalViewModel


class GoalFragment : Fragment() {

    private lateinit var binding: FragmentGoalBinding
    private lateinit var viewModel: GoalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GoalViewModel::class.java]

        listeners()

        observers()

        return binding.root
    }

    private fun observers() {

    }

    override fun onResume() {
        super.onResume()
        viewModel.findAll()
    }

    private fun listeners() {
        binding.buttonAddGoal.setOnClickListener {
            startActivity(Intent(requireContext(), GoalManagerActivity::class.java))
        }
    }

}