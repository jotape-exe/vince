package com.company.ourfinances.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.GoalItemBinding
import com.company.ourfinances.model.entity.GoalEntity
import com.company.ourfinances.view.listener.OnGoalListener
import com.company.ourfinances.view.viewholder.GoalViewHolder

class GoalAdapter : RecyclerView.Adapter<GoalViewHolder>() {

    private var _list: List<GoalEntity> = listOf()
    private lateinit var listener: OnGoalListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val itemView = GoalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return _list.count()
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(_list[position])
    }

    fun updateList(list: List<GoalEntity>) {
        _list = list
        notifyDataSetChanged()
    }

    fun attachToListener(itemListener: OnGoalListener) {
        listener = itemListener
    }
}