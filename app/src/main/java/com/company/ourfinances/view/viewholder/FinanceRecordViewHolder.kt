package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.FinaceRecordViewItemBinding
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.view.listener.OnFinanceRecordListener

class FinanceRecordViewHolder(
    private val bind: FinaceRecordViewItemBinding,
    private val listener: OnFinanceRecordListener
) : RecyclerView.ViewHolder(bind.root) {
    fun bind(financeRecordEntity: FinanceRecordEntity) {

        bind.textTitleView.text = financeRecordEntity.title
        bind.textDateView.text = financeRecordEntity.dateRecord
        bind.textValueView.text = "R$ ${financeRecordEntity.value}"
        bind.textTagRecord.text = listener.getEntityNameById(
            financeRecordEntity.categoryExpenseId, CategoryExpenseEntity::class.java
        )
        bind.textTypePayCard.text = listener.getEntityNameById(
            financeRecordEntity.paymentTypeId, PaymentTypeEntity::class.java
        )

        bind.buttonDeleteView.setOnClickListener {

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

