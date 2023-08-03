package com.company.ourfinances.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards",
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    var cardId: Long = 0,
    val name: String,
    val cardType: String,
    val cardColor: String,
    val cardNumber: String,
    val cardTextColor: String
)
