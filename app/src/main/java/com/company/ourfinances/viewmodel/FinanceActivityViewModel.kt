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

    private val cardRepository = CardRepository(application.applicationContext)
    private val financeRepository = FinanceRecordRepository(application.applicationContext)
    private val categoryRepository = CategoryExpenseRepository(application.applicationContext)
    private val paymentTypeRepository = PaymentTypeRepository(application.applicationContext)

    private val _categoryExpenseList = MutableLiveData<List<CategoryExpenseEntity>>()
    val categoryExpenseList:LiveData<List<CategoryExpenseEntity>> = _categoryExpenseList

    private val _categoryExpense = MutableLiveData<CategoryExpenseEntity>()
    val categoryExpense:LiveData<CategoryExpenseEntity> = _categoryExpense

    private val _typePayment = MutableLiveData<List<PaymentTypeEntity>>()
    val typePay: LiveData<List<PaymentTypeEntity>> = _typePayment

    private val _financeRecordList = MutableLiveData<List<FinanceRecordEntity>>()
    val financeRecordList: LiveData<List<FinanceRecordEntity>> = _financeRecordList

    private val _financeRecord = MutableLiveData<FinanceRecordEntity>()
    val financeRecord: LiveData<FinanceRecordEntity> = _financeRecord

    private val _cardRecord = MutableLiveData<List<CardEntity>>()
    val cardRecord: LiveData<List<CardEntity>> = _cardRecord

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
        _typePayment.value = paymentTypeRepository.getAll()
    }

    fun getCategoryById(id: Long): CategoryExpenseEntity {
        return categoryRepository.findById(id)
    }

    fun getAllByExpenseCategory(typeRecord: String) {
        _financeRecordList.value = financeRepository.getAllByExpenseCategory(typeRecord)
    }

    fun insertCard(cardEntity: CardEntity){
        cardRepository.insert(cardEntity)
    }

    fun getAllCards(){
        _cardRecord.value = cardRepository.getAll()
    }

    fun deleteCard(id:Long) {
        cardRepository.deleteCard(id)
    }

    fun getRecordById(id: Long) {
        _financeRecord.value = financeRepository.findById(id)
    }

}