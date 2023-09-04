package com.company.ourfinances.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "finance_records",
    foreignKeys = [
        ForeignKey(
            entity = CategoryRecordEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryRecordId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PaymentTypeEntity::class,
            parentColumns = arrayOf("paymentId"),
            childColumns = arrayOf("paymentTypeId"),
            onDelete = ForeignKey.CASCADE
        ),
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
    var recordId: Long = 0,
    val title: String,
    val value: Double,
    val dateRecord: String,
    val destinationAccount: String?,
    val typeRecord: String,
    @ColumnInfo(index = true)
    val categoryRecordId: Long?,
    @ColumnInfo(index = true)
    val paymentTypeId: Long?,
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
        private var categoryRecordId: Long? = null
        private var paymentTypeId: Long? = null
        private var cardId: Long? = null

        fun setRecordId(recordId: Long) = apply { this.recordId = recordId }

        fun setTitle(title: String) = apply { this.title = title }

        fun setValue(value: Double) = apply { this.value = value }

        fun setDateRecord(dateRecord: String) = apply { this.dateRecord = dateRecord }

        fun setDestinationAccount(destinationAccount: String?) = apply { this.destinationAccount = destinationAccount }

        fun setTypeRecord(typeRecord: String) = apply { this.typeRecord = typeRecord }

        fun setCategoryRecordId(categoryRecordId: Long?) = apply { this.categoryRecordId = categoryRecordId }

        fun setPaymentTypeId(paymentTypeId: Long?) = apply { this.paymentTypeId = paymentTypeId }

        fun setCardId(cardId: Long?) = apply { this.cardId = cardId }

        fun build() = FinanceRecordEntity(
            recordId,
            title,
            value,
            dateRecord,
            destinationAccount,
            typeRecord,
            categoryRecordId,
            paymentTypeId,
            cardId
        )
    }
}
