package com.company.ourfinances.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.repository.CardRepository
import com.company.ourfinances.model.repository.CategoryExpenseRepository
import com.company.ourfinances.model.repository.FinanceRecordRepository
import com.company.ourfinances.model.repository.PaymentTypeRepository

class FinanceActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val financeRepository = FinanceRecordRepository(application.applicationContext)
    private val categoryRepository = CategoryExpenseRepository(application.applicationContext)
    private val paymentTypeRepository = PaymentTypeRepository(application.applicationContext)

    private val _categoryExpenseList = MutableLiveData<List<CategoryExpenseEntity>>()
    val categoryExpenseList:LiveData<List<CategoryExpenseEntity>> = _categoryExpenseList

    private val _typePaymentList = MutableLiveData<List<PaymentTypeEntity>>()
    val typePaymentList: LiveData<List<PaymentTypeEntity>> = _typePaymentList

    private val _financeRecordList = MutableLiveData<List<FinanceRecordEntity>>()
    val financeRecordList: LiveData<List<FinanceRecordEntity>> = _financeRecordList

    private val _financeRecord = MutableLiveData<FinanceRecordEntity>()
    val financeRecord: LiveData<FinanceRecordEntity> = _financeRecord

    fun save(recordEntity: FinanceRecordEntity) {
        if (recordEntity.recordId == 0L){
            financeRepository.save(recordEntity)
        } else{
            financeRepository.update(recordEntity)
        }
    }

    fun delete(id: Long) {
        financeRepository.delete(id)
    }

    fun getAllCategories(){
        _categoryExpenseList.value = categoryRepository.getAll()
    }

    fun getAllTypePayments(){
        _typePaymentList.value = paymentTypeRepository.getAll()
    }

    fun getCategoryById(id: Long): CategoryExpenseEntity {
        return categoryRepository.findById(id)
    }

    fun getTypePaymentById(id: Long): PaymentTypeEntity{
        return paymentTypeRepository.findById(id)
    }

    fun getAllByExpenseCategory(typeRecord: String) {
        _financeRecordList.value = financeRepository.getAllByExpenseCategory(typeRecord)
    }

    fun getAllFinanceRecords(){
       _financeRecordList.value = financeRepository.findAll()
    }

    fun getAllRecords(): List<FinanceRecordEntity> {
        return financeRepository.findAll()
    }

    fun getRecordById(id: Long) {
        _financeRecord.value = financeRepository.findById(id)
    }


}