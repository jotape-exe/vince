package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentCardBinding
import com.company.ourfinances.view.CardCreateActivity

class CardFragment : Fragment() {

    private lateinit var binding: FragmentCardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardBinding.inflate(inflater, container, false)

        listener()

        return binding.root
    }

    private fun listener() {
        binding.buttonAddCard.setOnClickListener {
            startActivity(Intent(context, CardCreateActivity::class.java))
        }
    }


}