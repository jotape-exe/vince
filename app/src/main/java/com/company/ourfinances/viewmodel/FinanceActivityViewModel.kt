package com.company.ourfinances.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.repository.FinanceRecordRepository

class FinanceActivityViewModel(application: Application) : AndroidViewModel(application) {

    val repository = FinanceRecordRepository(application.applicationContext)
    fun insert(recordEntity: FinanceRecordEntity) {
        repository.save(recordEntity)
    }

    fun getAll():List<FinanceRecordEntity>{
        return repository.findAll()
    }

}