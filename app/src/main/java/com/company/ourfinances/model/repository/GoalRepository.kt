package com.company.ourfinances.model.repository

import android.content.Context
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.GoalEntity
import com.company.ourfinances.model.repository.db.VinceDatabase

class GoalRepository(context: Context) {
    private val database = VinceDatabase.getInstance(context).getGoalDAO()

    fun save(goalEntity: GoalEntity){
        database.insert(goalEntity)
    }
    fun update(goalEntity: GoalEntity){
        database.update(goalEntity)
    }
    fun delete(id: Long){
        val entity = findById(id)
        database.delete(entity)
    }
    fun findById(id: Long): GoalEntity {
        return database.getGoalById(id)
    }
    fun findAll(): List<GoalEntity>{
        return database.getAllGoals()
    }
}