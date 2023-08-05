package com.company.ourfinances.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.FinaceRecordViewItemBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.view.viewholder.FinanceRecordViewHolder
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class FinanceRecordAdapter(
    private val viewModel: FinanceActivityViewModel
) : RecyclerView.Adapter<FinanceRecordViewHolder>() {

    private var _list: List<FinanceRecordEntity> = listOf()
    private lateinit var listener: OnFinanceRecordListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceRecordViewHolder {
        val itemView = FinaceRecordViewItemBinding
            .inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return FinanceRecordViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: FinanceRecordViewHolder, position: Int) {
        val tag =
            _list[position].categoryExpenseId?.let { id -> viewModel.getCategoryById(id).name }

        val paymentName =
            _list[position].paymentTypeId?.let { id -> viewModel.getTypePaymentById(id).name }

        holder.bind(_list[position], tag as String, paymentName as String)
    }

    override fun getItemCount(): Int {
        return _list.count()
    }

    fun updateList(list: List<FinanceRecordEntity>) {
        _list = list
        notifyDataSetChanged()
    }

    fun attachToListener(itemListener: OnFinanceRecordListener) {
        listener = itemListener
    }
}