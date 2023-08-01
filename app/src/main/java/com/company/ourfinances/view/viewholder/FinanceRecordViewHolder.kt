package com.company.ourfinances.view.viewholder

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.FinaceRecordViewItemBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.view.listener.OnFinanceRecordListener
import com.company.ourfinances.viewmodel.RevenueFragmentViewModel

class FinanceRecordViewHolder(
    private val bind: FinaceRecordViewItemBinding,
    private val viewModel: RevenueFragmentViewModel,
    private val listener: OnFinanceRecordListener
) : RecyclerView.ViewHolder(bind.root) {

    fun bind(financeRecordEntity: FinanceRecordEntity) {

        financeRecordEntity.categoryExpenseId?.let { viewModel.getCategoryById(it) }

        bind.textTitleView.text = financeRecordEntity.title
        bind.textDateView.text = financeRecordEntity.dateRecord
        bind.textValueView.text = financeRecordEntity.value.toString()
        //bind.textTagRecord.text = financeRecordEntity

        bind.buttonDeleteView.setOnClickListener {
            listener.onDelete(financeRecordEntity.recordId)
        }
    }

}
