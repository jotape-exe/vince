package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.TransferRecordViewItemBinding
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.view.listener.OnFinanceRecordListener

class TransferRecordViewHolder(
    private val bind: TransferRecordViewItemBinding,
    private val listener: OnFinanceRecordListener
): RecyclerView.ViewHolder(bind.root) {
    fun bind(financeRecordEntity: FinanceRecordEntity, paymentName: String) {

        bind.textTitleViewTransfer.text = financeRecordEntity.title
        bind.textDateViewTransfer.text = financeRecordEntity.dateRecord
        bind.textValueViewTransfer.text = "R$ ${financeRecordEntity.value}"
        bind.textReceiverViewTransfer.text = financeRecordEntity.destinationAccount

        bind.textTypePayCarTransfer.text = paymentName

        bind.buttonDeleteViewTransfer.setOnClickListener {

            AlertDialog.Builder(itemView.context)
                .setTitle("Remoção de Registro")
                .setMessage("Tem certeza que deseja excluir o registro? Isso não pode ser revertido.")
                .setPositiveButton("Sim") { dialog, which ->
                    listener.onDelete(financeRecordEntity.recordId)
                }
                .setNegativeButton("Não", null)
                .setNeutralButton("Cancelar", null)
                .create()
                .show()

        }

        bind.buttonEditViewTransfer.setOnClickListener {
            listener.onClick(financeRecordEntity.recordId)
        }
    }
}
