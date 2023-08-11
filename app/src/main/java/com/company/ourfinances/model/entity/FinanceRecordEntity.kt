package com.company.ourfinances.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "finance_records",
    foreignKeys = [
        ForeignKey(
            entity = CategoryExpenseEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryExpenseId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PaymentTypeEntity::class,
            parentColumns = arrayOf("paymentId"),
            childColumns = arrayOf("paymentTypeId"),
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
    val originAccount: String?,
    val destinationAccount: String?,
    val typeRecord: String,
    @ColumnInfo(index = true)
    val categoryExpenseId: Long?,
    @ColumnInfo(index = true)
    val paymentTypeId: Long?
) {
    class Builder {
        private var recordId: Long = 0
        private lateinit var title: String
        private var value: Double = 0.0
        private lateinit var dateRecord: String
        private var originAccount: String? = null
        private var destinationAccount: String? = null
        private lateinit var typeRecord: String
        private var categoryExpenseId: Long? = null
        private var paymentTypeId: Long? = null

        fun setRecordId(recordId: Long) = apply { this.recordId = recordId }
        fun setTitle(title: String) = apply { this.title = title }
        fun setValue(value: Double) = apply { this.value = value }
        fun setDateRecord(dateRecord: String) = apply { this.dateRecord = dateRecord }
        fun setOriginAccount(originAccount: String?) = apply { this.originAccount = originAccount }
        fun setDestinationAccount(destinationAccount: String?) = apply { this.destinationAccount = destinationAccount }

        fun setTypeRecord(typeRecord: String) = apply { this.typeRecord = typeRecord }
        fun setCategoryExpenseId(categoryExpenseId: Long?) = apply { this.categoryExpenseId = categoryExpenseId }

        fun setPaymentTypeId(paymentTypeId: Long?) = apply { this.paymentTypeId = paymentTypeId }

        fun build() = FinanceRecordEntity(
            recordId,
            title,
            value,
            dateRecord,
            originAccount,
            destinationAccount,
            typeRecord,
            categoryExpenseId,
            paymentTypeId
        )
    }
}
