package com.company.ourfinances.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    var goalId: Long = 0,
    val name: String,
    val currentValue: Double,
    val finalValue: Double,
    val limitDate: String,
)