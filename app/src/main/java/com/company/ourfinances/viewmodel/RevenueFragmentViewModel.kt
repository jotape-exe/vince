package com.company.ourfinances.viewmodel

import android.app.Application
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

    private val _financeRecord = MutableLiveData<List<FinanceRecordEntity>>()
    var financeRecord:LiveData<List<FinanceRecordEntity>> = _financeRecord

    private val _categoryExpense = MutableLiveData<List<CategoryExpenseEntity>>()
    var categoryExpense:LiveData<List<CategoryExpenseEntity>> = _categoryExpense

    private val _typePayment = MutableLiveData<List<PaymentTypeEntity>>()
    val typePay: LiveData<List<PaymentTypeEntity>> = _typePayment


    fun getAllCategories(){
        _categoryExpense.value = categoryRepository.getAll()
    }

    fun getAllTypePayments(){
        _typePayment.value = paymentTypeRepository.getAll()
    }


}