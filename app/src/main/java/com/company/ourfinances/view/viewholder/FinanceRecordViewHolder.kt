package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FinaceRecordViewItemBinding
import com.company.ourfinances.model.entity.CategoryRecordEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.view.listener.OnFinanceRecordListener

class FinanceRecordViewHolder(
    private val bind: FinaceRecordViewItemBinding,
    private val listener: OnFinanceRecordListener
) : RecyclerView.ViewHolder(bind.root) {
    fun bind(financeRecordEntity: FinanceRecordEntity) {

        bind.textTitleView.text = financeRecordEntity.title
        bind.textDateView.text = financeRecordEntity.dateRecord
        bind.textValueView.text = "R$ ${financeRecordEntity.value}"
        bind.textTagRecord.text = financeRecordEntity.categoryRecord
        bind.textTypePayCard.text = financeRecordEntity.paymentType

        bind.buttonDeleteView.setOnClickListener {

            AlertDialog.Builder(itemView.context)
                .setTitle(itemView.context.getString(R.string.delete_record))
                .setMessage(itemView.context.getString(R.string.really_delete_record))
                .setPositiveButton(itemView.context.getString(R.string.yes)) { dialog, which ->
                    listener.onDelete(financeRecordEntity.recordId)
                }
                .setNeutralButton(itemView.context.getString(R.string.cancel), null)
                .create()
                .show()
        }

        bind.buttonEditView.setOnClickListener {
            listener.onClick(financeRecordEntity.recordId)
        }

        financeRecordEntity.cardId?.let { cardId ->
            listener.getCardById(cardId)?.let { card ->
                with(bind.textCardName) {
                    isVisible = true
                    text = card.name
                    background.setTint(Color.parseColor(card.cardColor))
                    setTextColor(Color.parseColor(card.cardTextColor))
                }
            }
        }

    }
}

