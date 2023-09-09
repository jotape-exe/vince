package com.company.ourfinances.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_records")
data class CategoryRecordEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name: String
)