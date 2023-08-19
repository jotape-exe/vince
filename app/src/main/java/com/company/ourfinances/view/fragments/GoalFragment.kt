package com.company.ourfinances.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentGoalBinding


class GoalFragment : Fragment() {

    private lateinit var binding: FragmentGoalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalBinding.inflate(inflater, container, false)



        return binding.root
    }

}