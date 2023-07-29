package com.company.ourfinances.model.repository

import android.content.Context
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.repository.db.VinceDatabase

class RegisterRepository private constructor(context: Context){

    private val database = VinceDatabase.getInstance(context)

    fun save(financeRecordEntity: FinanceRecordEntity){
    }
}