package com.company.ourfinances.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.HomeListItemBinding
import com.company.ourfinances.view.assets.HomeComponent
import com.company.ourfinances.view.listener.OnComponentHomeListener
import com.company.ourfinances.view.viewholder.HomeViewHolder

class HomeComponentAdapter(): RecyclerView.Adapter<HomeViewHolder>() {

    private var _componentsList: List<HomeComponent> = arrayListOf()
    private lateinit var listener: OnComponentHomeListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = HomeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return _componentsList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(_componentsList[position])
    }

    fun updateList(componentList: List<HomeComponent>) {
        _componentsList = componentList
    }

    fun attachToListener(componentListener: OnComponentHomeListener){
        listener = componentListener
    }
}