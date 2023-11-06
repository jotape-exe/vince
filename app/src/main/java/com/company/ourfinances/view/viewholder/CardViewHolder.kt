package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.R
import com.company.ourfinances.databinding.CardItemBinding
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.view.listener.OnCardListener
import com.company.ourfinances.view.utils.CryptoUtils

class CardViewHolder(
    private val bind: CardItemBinding,
    private val listener: OnCardListener
) : RecyclerView.ViewHolder(bind.root) {

    private val cripto = CryptoUtils()
    fun bind(card: CardEntity) {

        bind.textCardNameItem.text = card.name
        bind.textCardNumberItem.text = cripto.decrypt(card.cardNumber)
        bind.textCardTypeItem.text = card.cardType

        bind.textCardNameItem.setTextColor(Color.parseColor(card.cardTextColor))
        bind.textCardNumberItem.setTextColor(Color.parseColor(card.cardTextColor))
        bind.textCardTypeItem.setTextColor(Color.parseColor(card.cardTextColor))

        bind.cardContent.background.setTint(Color.parseColor(card.cardColor))
        bind.cardContent.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(itemView.context.getString(R.string.delete_card))
                .setMessage(itemView.context.getString(R.string.really_delete_card))
                .setPositiveButton(itemView.context.getString(R.string.yes)) { dialog, which ->
                    listener.onLongClick(card.cardId)
                }
                .setNeutralButton(itemView.context.getString(R.string.cancel), null)
                .create()
                .show()
            true
        }

    }
}

