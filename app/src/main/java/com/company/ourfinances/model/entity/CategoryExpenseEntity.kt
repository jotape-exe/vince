package com.company.ourfinances.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_records")
data class CategoryExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
) {
}