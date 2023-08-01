package com.company.ourfinances.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.repository.FinanceRecordRepository

class FinanceActivityViewModel(application: Application) : AndroidViewModel(application) {

    val repository = FinanceRecordRepository(application.applicationContext)

    private val _financeRecord = MutableLiveData<List<FinanceRecordEntity>>()
    val financeRecord: LiveData<List<FinanceRecordEntity>> = _financeRecord
    fun insert(recordEntity: FinanceRecordEntity) {
        repository.save(recordEntity)
    }

    fun getAll():List<FinanceRecordEntity>{
        return repository.findAll()
    }

    fun getAllByExpenseCategory(typeRecord: String): List<FinanceRecordEntity> {
        return repository.getAllByExpenseCategory(typeRecord)
    }

    fun delete(id: Long) {
        repository.delete(id)
    }

    fun getById(id: Long): FinanceRecordEntity {
        return repository.findById(id)
    }

}