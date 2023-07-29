package com.company.ourfinances.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_types")
data class PaymentTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val paymentId: Long = 0,
    val name: String
)
