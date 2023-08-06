package com.company.ourfinances.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.FinaceRecordViewItemBinding
import com.company.ourfinances.databinding.TransferRecordViewItemBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.view.viewholder.FinanceRecordViewHolder
import com.company.ourfinances.view.viewholder.TransferRecordViewHolder
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class TransferRecordAdapter(private val viewModel: FinanceActivityViewModel): RecyclerView.Adapter<TransferRecordViewHolder>() {

    private var _list: List<FinanceRecordEntity> = listOf()
    private lateinit var listener: OnFinanceRecordListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransferRecordViewHolder {
        val itemView = TransferRecordViewItemBinding
            .inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return TransferRecordViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return _list.count()
    }

    override fun onBindViewHolder(holder: TransferRecordViewHolder, position: Int) {
        val paymentName =
            _list[position].paymentTypeId?.let { id -> viewModel.getTypePaymentById(id).name }

        holder.bind(_list[position], paymentName as String)
    }

    fun updateList(list: List<FinanceRecordEntity>) {
        _list = list
        notifyDataSetChanged()
    }

    fun attachToListener(itemListener: OnFinanceRecordListener) {
        listener = itemListener
    }
}