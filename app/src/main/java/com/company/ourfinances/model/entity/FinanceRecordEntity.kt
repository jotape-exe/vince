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
    val recordId: Long = 0,
    val title: String,
    val value: Double,
    val originAccount: String?,
    val destinationAccount: String?,
    val typeRecord: String,
    @ColumnInfo(index = true)
    val categoryExpenseId: Long?,
    @ColumnInfo(index = true)
    val paymentTypeId: Long?
)


