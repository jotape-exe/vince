package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentGoalBinding
import com.company.ourfinances.model.constants.DatabaseConstants
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.FinanceActivity
import com.company.ourfinances.view.GoalManagerActivity
import com.company.ourfinances.view.adapters.GoalAdapter
import com.company.ourfinances.view.listener.OnGoalListener
import com.company.ourfinances.viewmodel.GoalViewModel


class GoalFragment : Fragment() {

    private lateinit var binding: FragmentGoalBinding
    private lateinit var viewModel: GoalViewModel
    private lateinit var adapter: GoalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GoalViewModel::class.java]

        adapter = GoalAdapter()

        binding.recyclerGoal.layoutManager = LinearLayoutManager(context)
        binding.recyclerGoal.adapter = adapter

        viewModel.findAll()

        listeners()

        observers()

        val listener = object : OnGoalListener {
            override fun onClick(id: Long) {
                val bundle = Bundle()

                bundle.putLong(DatabaseConstants.FinanceRecord.recordId, id)

                startActivity(Intent(context, GoalManagerActivity::class.java).putExtras(bundle))
            }

            override fun onDelete(id: Long) {
                viewModel.delete(id)
                viewModel.findAll()
            }

        }

        adapter.attachToListener(listener)

        return binding.root
    }

    private fun observers(){
        viewModel.goalList.observe(viewLifecycleOwner){
            binding.root.findViewById<TextView>(R.id.text_not_data).isVisible = it.isEmpty()
            adapter.updateList(it)
        }

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