package com.company.ourfinances.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.FinaceRecordViewItemBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.view.listener.OnComponentHomeListener
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.view.viewholder.FinanceRecordViewHolder
import com.company.ourfinances.viewmodel.RevenueFragmentViewModel

class FinanceRecordAdapter(
    private val viewModel: RevenueFragmentViewModel
): RecyclerView.Adapter<FinanceRecordViewHolder>() {

    private var _list: List<FinanceRecordEntity> = listOf()
    private lateinit var listener: OnFinanceRecordListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceRecordViewHolder {
        val itemView = FinaceRecordViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FinanceRecordViewHolder(itemView, viewModel, listener)
    }

    override fun onBindViewHolder(holder: FinanceRecordViewHolder, position: Int) {
        holder.bind(_list[position])
    }

    override fun getItemCount(): Int {
        return _list.count()
    }

    fun updateList(list: List<FinanceRecordEntity>) {
        _list = list
    }

    fun attachToListener(itemListener: OnFinanceRecordListener){
        listener = itemListener
    }
}