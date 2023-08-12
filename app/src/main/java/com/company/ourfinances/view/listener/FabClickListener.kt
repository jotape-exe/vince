package com.company.ourfinances.view.listener

import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity


interface FabClickListener {
    fun doSave()
    fun clearAll()
    fun getIdCategoryExpenseFromName(spinnerItemName: String, list: List<CategoryExpenseEntity>):Long?
    fun getIdPaymentTypeFromName(spinnerItemName: String, list: List<PaymentTypeEntity>): Long?
}
