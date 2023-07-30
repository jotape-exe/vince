package com.company.ourfinances.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_types")
data class PaymentTypeEntity(
    @PrimaryKey(autoGenerate = true)
    var paymentId: Long = 0,
    val name: String
)
