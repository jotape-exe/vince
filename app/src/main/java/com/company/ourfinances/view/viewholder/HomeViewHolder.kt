package com.company.ourfinances.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.HomeListItemBinding
import com.company.ourfinances.view.utils.HomeComponent
import com.company.ourfinances.view.listener.OnComponentHomeListener

class HomeViewHolder(private var bind: HomeListItemBinding, private val listener: OnComponentHomeListener) : RecyclerView.ViewHolder(bind.root) {
    fun bind(component: HomeComponent) {

        bind.textTitle.text = component.title
        bind.imageComponentHome.setImageResource(component.icon)
        bind.textComponentHome.text = component.descriptionText

        bind.buttonNext.setOnClickListener {
            listener.onClick(component.title)
        }

    }

}