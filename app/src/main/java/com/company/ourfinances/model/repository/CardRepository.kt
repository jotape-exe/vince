package com.company.ourfinances.model.repository

import android.content.Context
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.repository.db.VinceDatabase

class CardRepository(context: Context) {
    private val database = VinceDatabase.getInstance(context).getCardDAO()

    fun getAll(): List<CardEntity> {
        return database.getAllCards()
    }

    fun getCardById(id: Long): CardEntity{
        return database.getCardById(id)
    }

    fun insert(cardEntity: CardEntity){
        database.insert(cardEntity)
    }

    fun deleteCard(id: Long){
        val card = getCardById(id)
        database.delete(card)
    }

    fun getNameById(id: Long): String {
        return database.getNameById(id)
    }
}