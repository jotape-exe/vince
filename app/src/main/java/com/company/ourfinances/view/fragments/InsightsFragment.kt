package com.company.ourfinances.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.company.ourfinances.databinding.FragmentInsightsBinding

class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentInsightsBinding.inflate(inflater, container, false)




        return binding.root
    }

}