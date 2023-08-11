package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.ourfinances.databinding.FragmentCardBinding
import com.company.ourfinances.view.CardCreateActivity
import com.company.ourfinances.view.adapters.CardItemAdapter
import com.company.ourfinances.view.listener.OnCardListener
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class CardFragment : Fragment() {

    private lateinit var binding: FragmentCardBinding
    private lateinit var viewModel: FinanceActivityViewModel
    private lateinit var adapter: CardItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FinanceActivityViewModel::class.java]

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

        viewModel.getAllCards()

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
        viewModel.cardRecord.observe(viewLifecycleOwner){
            adapter.updateList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCards()
    }



}