package com.company.ourfinances.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "record_payment",
    primaryKeys = ["recordId", "paymentId"]
)

data class RecordPaymentCrossRef(
    var recordId: Long,
    var paymentId: Long
)
