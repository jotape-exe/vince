package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentCardBinding
import com.company.ourfinances.view.CardCreateActivity
import com.company.ourfinances.view.adapters.CardItemAdapter
import com.company.ourfinances.view.listener.OnCardListener
import com.company.ourfinances.viewmodel.CardViewModel
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class CardFragment : Fragment() {

    private lateinit var binding: FragmentCardBinding
    private lateinit var viewModel: CardViewModel
    private lateinit var adapter: CardItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CardViewModel::class.java]

        adapter = CardItemAdapter()

        binding.recyclerCard.layoutManager = LinearLayoutManager(context)
        binding.recyclerCard.adapter = adapter

        val listener = object : OnCardListener {
            override fun onLongClick(id: Long) {
                viewModel.deleteCard(id)
                viewModel.getAllCards()
            }
        }

        adapter.attachToListener(listener)

        observe()

        listener()

        return binding.root
    }

    private fun listener() {
        binding.buttonAddCard.setOnClickListener {
            startActivity(Intent(context, CardCreateActivity::class.java))
        }
    }

    private fun observe(){
        viewModel.cardRecordList.observe(viewLifecycleOwner){
            binding.root.findViewById<TextView>(R.id.text_not_data).isVisible = it.isEmpty()
            adapter.updateList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCards()
    }



}