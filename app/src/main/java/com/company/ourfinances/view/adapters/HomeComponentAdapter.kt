package com.company.ourfinances.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.R
import com.company.ourfinances.view.HomeComponent
import com.company.ourfinances.view.viewholder.HomeViewHolder

class HomeComponentAdapter(private val componentsList: List<HomeComponent>): RecyclerView.Adapter<HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return HomeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return componentsList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = componentsList[position]

        holder.titleComponent.text = currentItem.title
        holder.buttonComponent.text = currentItem.buttonText
        holder.iconComponent.setImageResource(currentItem.icon)
    }
}