package com.company.ourfinances.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.CardItemBinding
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.view.listener.OnCardListener
import com.company.ourfinances.view.viewholder.CardViewHolder


class CardItemAdapter(): RecyclerView.Adapter<CardViewHolder>() {

    private var _componentsList: List<CardEntity> = arrayListOf()
    private lateinit var listener: OnCardListener

    fun updateList(componentList: List<CardEntity>) {
        _componentsList = componentList
        notifyDataSetChanged()
    }

    fun attachToListener(componentListener: OnCardListener){
        listener = componentListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return _componentsList.count()
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(_componentsList[position])
    }
}