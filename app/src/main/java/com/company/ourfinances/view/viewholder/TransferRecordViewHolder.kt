package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.TransferRecordViewItemBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.view.listener.OnFinanceRecordListener

class TransferRecordViewHolder(
    private val bind: TransferRecordViewItemBinding,
    private val listener: OnFinanceRecordListener
) : RecyclerView.ViewHolder(bind.root) {
    fun bind(financeRecordEntity: FinanceRecordEntity) {

        bind.textTitleViewTransfer.text = financeRecordEntity.title
        bind.textDateViewTransfer.text = financeRecordEntity.dateRecord
        bind.textValueViewTransfer.text = "R$ ${financeRecordEntity.value}"
        bind.textReceiverViewTransfer.text = financeRecordEntity.destinationAccount

        bind.textTypePayCardTransfer.text = listener.getPaymentNameById(financeRecordEntity.paymentTypeId)

        bind.buttonDeleteViewTransfer.setOnClickListener {

            AlertDialog.Builder(itemView.context)
                .setTitle("Remoção de Registro")
                .setMessage("Tem certeza que deseja excluir o registro? Isso não pode ser revertido.")
                .setPositiveButton("Sim") { dialog, which ->
                    listener.onDelete(financeRecordEntity.recordId)
                }
                .setNeutralButton("Cancelar", null)
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
