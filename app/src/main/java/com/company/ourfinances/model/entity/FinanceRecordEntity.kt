package com.company.ourfinances.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "finance_records",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = arrayOf("cardId"),
            childColumns = arrayOf("cardId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FinanceRecordEntity(
    @PrimaryKey(autoGenerate = true)
    var recordId: Long,
    val title: String,
    val value: Double,
    val dateRecord: String,
    val destinationAccount: String?,
    val typeRecord: String,
    val categoryRecord: String,
    val paymentType: String,
    @ColumnInfo(index = true)
    val cardId: Long?
) {
    class Builder {
        private var recordId: Long = 0
        private lateinit var title: String
        private var value: Double = 0.0
        private lateinit var dateRecord: String
        private var destinationAccount: String? = null
        private lateinit var typeRecord: String
        private var categoryRecord: String = ""
        private var paymentType: String = ""
        private var cardId: Long? = null

        fun setRecordId(recordId: Long) = apply { this.recordId = recordId }

        fun setTitle(title: String) = apply { this.title = title }

        fun setValue(value: Double) = apply { this.value = value }

        fun setDateRecord(dateRecord: String) = apply { this.dateRecord = dateRecord }

        fun setDestinationAccount(destinationAccount: String?) = apply { this.destinationAccount = destinationAccount }

        fun setTypeRecord(typeRecord: String) = apply { this.typeRecord = typeRecord }

        fun setCategoryRecord(categoryRecord: String) = apply { this.categoryRecord = categoryRecord }

        fun setPaymentType(paymentType: String) = apply { this.paymentType = paymentType }

        fun setCardId(cardId: Long?) = apply { this.cardId = cardId }

        fun build() = FinanceRecordEntity(
            recordId,
            title,
            value,
            dateRecord,
            destinationAccount,
            typeRecord,
            categoryRecord,
            paymentType,
            cardId
        )
    }
}
