package com.company.ourfinances.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.HomeListItemBinding
import com.company.ourfinances.view.HomeComponent
import com.company.ourfinances.view.fragments.imageId
import com.company.ourfinances.view.listener.OnComponentHomeListener

class HomeViewHolder(private var bind: HomeListItemBinding, private val listener: OnComponentHomeListener) : RecyclerView.ViewHolder(bind.root) {
    fun bind(component: HomeComponent) {
        bind.textTitle.text = component.title
        bind.imageComponentHome.setImageResource(component.icon)
        bind.buttonComponentHome.text = component.buttonText

        bind.buttonComponentHome.setOnClickListener {
            listener.onClick(component.title)
        }
    }

}