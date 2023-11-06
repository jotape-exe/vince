package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.R
import com.company.ourfinances.databinding.TransferRecordViewItemBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.view.listener.OnFinanceRecordListener

class TransferRecordViewHolder(
    private val bind: TransferRecordViewItemBinding,
    private val listener: OnFinanceRecordListener
) : RecyclerView.ViewHolder(bind.root) {
    fun bind(financeRecordEntity: FinanceRecordEntity) {

        bind.textTitleViewTransfer.text = financeRecordEntity.title
        bind.textDateViewTransfer.text = financeRecordEntity.dateRecord
        bind.textValueViewTransfer.text = "${itemView.context.getString(R.string.coin)} ${financeRecordEntity.value}"
        bind.textReceiverViewTransfer.text = financeRecordEntity.destinationAccount

        bind.textTypePayCardTransfer.text = financeRecordEntity.paymentType

        bind.buttonDeleteViewTransfer.setOnClickListener {

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

        bind.buttonEditViewTransfer.setOnClickListener {
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
