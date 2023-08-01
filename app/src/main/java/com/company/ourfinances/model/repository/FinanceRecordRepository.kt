package com.company.ourfinances.model.repository

import android.content.Context
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.repository.db.VinceDatabase

class FinanceRecordRepository(context: Context){

    private val database = VinceDatabase.getInstance(context).getFinanceRecordDAO()

    fun save(financeRecordEntity: FinanceRecordEntity){
        database.insert(financeRecordEntity)
    }
    fun update(financeRecordEntity: FinanceRecordEntity){
        database.update(financeRecordEntity)
    }
    fun delete(id: Long){
        val entity = findById(id)
        database.delete(entity)
    }
    fun findById(id: Long): FinanceRecordEntity{
        return database.getRecordById(id)
    }
    fun findAll(): List<FinanceRecordEntity>{
        return database.getAllRecords()
    }
    fun getAllByExpenseCategory(typeRecord: String): List<FinanceRecordEntity> {
        return database.getAllByExpenseCategory(typeRecord)
    }
}