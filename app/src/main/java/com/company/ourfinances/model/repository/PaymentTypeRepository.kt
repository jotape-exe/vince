package com.company.ourfinances.model.repository

import android.content.Context
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.repository.db.VinceDatabase

class PaymentTypeRepository(context: Context) {
    private val database = VinceDatabase.getInstance(context).getPaymentTypeDAO()

    fun getAll(): List<PaymentTypeEntity>{
        return database.getAllPaymentTypes()
    }

    fun findById(id: Long): PaymentTypeEntity {
        return database.findById(id)
    }
}