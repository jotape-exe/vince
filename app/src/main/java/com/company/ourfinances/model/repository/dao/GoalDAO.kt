package com.company.ourfinances.model.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.company.ourfinances.model.entity.GoalEntity

@Dao
interface GoalDAO {
    @Insert
    fun insert(goalEntity: GoalEntity)

    @Update
    fun update(goalEntity: GoalEntity)

    @Delete
    fun delete(goalEntity: GoalEntity)

    @Query("SELECT * FROM goals")
    fun getAllGoals(): List<GoalEntity>

    @Query("SELECT * FROM goals WHERE goalId = :id")
    fun getGoalById(id: Long): GoalEntity
}