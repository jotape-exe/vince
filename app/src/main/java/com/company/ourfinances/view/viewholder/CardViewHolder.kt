package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.CardItemBinding
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.view.listener.OnCardListener

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
                .setTitle("Remoção de Cartão")
                .setMessage("Tem certeza que deseja excluir o cartão? Todos os registros com este cartão" +
                        " serão apagados!")
                .setPositiveButton("Sim") { dialog, which ->
                    listener.onLongClick(card.cardId)
                }
                .setNeutralButton("Cancelar", null)
                .create()
                .show()
            true
        }

    }
}

