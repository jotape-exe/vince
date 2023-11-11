package com.company.ourfinances.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.FinaceRecordViewItemBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.view.viewholder.FinanceRecordViewHolder
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class FinanceRecordAdapter(private val list: List<FinanceRecordEntity>) : RecyclerView.Adapter<FinanceRecordViewHolder>() {

    private lateinit var listener: OnFinanceRecordListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceRecordViewHolder {
        val itemView = FinaceRecordViewItemBinding
            .inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return FinanceRecordViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: FinanceRecordViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    fun attachToListener(itemListener: OnFinanceRecordListener) {
        listener = itemListener
    }

    fun removeItemById(id: Long) {
        val position = list.indexOfFirst {
            it.recordId == id
        }
        if (position != -1) {
            (list as ArrayList).removeAt(position)
            notifyItemRemoved(position)
        }
    }

}