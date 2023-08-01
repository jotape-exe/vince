package com.company.ourfinances.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.repository.CategoryExpenseRepository
import com.company.ourfinances.model.repository.FinanceRecordRepository
import com.company.ourfinances.model.repository.PaymentTypeRepository


class RevenueFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val financeRepository = FinanceRecordRepository(application.applicationContext)
    private val categoryRepository = CategoryExpenseRepository(application.applicationContext)
    private val paymentTypeRepository = PaymentTypeRepository(application.applicationContext)

    private val _categoryExpenseList = MutableLiveData<List<CategoryExpenseEntity>>()
    val categoryExpenseList:LiveData<List<CategoryExpenseEntity>> = _categoryExpenseList

    private val _categoryExpense = MutableLiveData<CategoryExpenseEntity>()
    val categoryExpense:LiveData<CategoryExpenseEntity> = _categoryExpense

    private val _typePayment = MutableLiveData<List<PaymentTypeEntity>>()
    val typePay: LiveData<List<PaymentTypeEntity>> = _typePayment


    private val _financeRecord = MutableLiveData<List<FinanceRecordEntity>>()
    val financeRecord: LiveData<List<FinanceRecordEntity>> = _financeRecord

    fun getAllRecords(){
        _financeRecord.value = financeRepository.findAll()
   }

    fun getAllCategories(){
        _categoryExpenseList.value = categoryRepository.getAll()
    }

    fun getAllTypePayments(){
        _typePayment.value = paymentTypeRepository.getAll()
    }

    fun getCategoryById(id: Long){
        _categoryExpense.value = categoryRepository.findById(id)
    }

    fun getAllByExpenseCategory(typeRecord: String) {
        _financeRecord.value = financeRepository.getAllByExpenseCategory(typeRecord)
    }

    fun delete(id: Long) {
        financeRepository.delete(id)
    }

    fun getById(id: Long): FinanceRecordEntity {
        return financeRepository.findById(id)
    }



}