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

    fun delete(financeRecordEntity: FinanceRecordEntity){
        database.delete(financeRecordEntity)
    }

    fun findById(id: Long): FinanceRecordEntity{
        return database.getRecordById(id)
    }

    fun findAll(): List<FinanceRecordEntity>{
        return database.getAllRecords()
    }
}