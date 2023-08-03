package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.CardItemBinding
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.view.CardComponent
import com.company.ourfinances.view.HomeComponent
import com.company.ourfinances.view.listener.OnCardListener
import com.company.ourfinances.view.listener.OnComponentHomeListener

class CardViewHolder(
    private val bind: CardItemBinding,
    private val listener: OnCardListener
) : RecyclerView.ViewHolder(bind.root) {

    fun bind(card: CardEntity) {
        bind.textCardNameItem.text = card.name
        bind.textCardNumberItem.text = card.cardNumber
        bind.textCardTypeItem.text = card.cardType

        bind.textCardNameItem.setTextColor(Color.parseColor(card.cardTextColor))
        bind.textCardNumberItem.setTextColor(Color.parseColor(card.cardTextColor))
        bind.textCardTypeItem.setTextColor(Color.parseColor(card.cardTextColor))
        bind.cardContent.background.setTint(Color.parseColor(card.cardColor))

        bind.cardContent.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("Remoção de Registro")
                .setMessage("Tem certeza que deseja excluir o cartão? Isso não pode ser revertido.")
                .setPositiveButton("Sim") { dialog, which ->
                    listener.onLongClick(card.cardId)
                }
                .setNegativeButton("Não", null)
                .setNeutralButton("Cancelar", null)
                .create()
                .show()
            true

        }

    }
}

