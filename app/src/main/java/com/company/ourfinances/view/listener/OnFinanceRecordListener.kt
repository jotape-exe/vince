package com.company.ourfinances.view.listener

import com.company.ourfinances.model.entity.CardEntity

interface OnFinanceRecordListener {
    fun onDelete(id: Long)

    fun onClick(id: Long)

    fun getPaymentNameById(id: Long?):String

    fun getCategoryNameById(id: Long?):String

    fun getCardById(id: Long?): CardEntity?
}