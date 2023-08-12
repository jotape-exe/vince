package com.company.ourfinances.view.listener

interface OnFinanceRecordListener {
    fun onDelete(id: Long)
    fun onClick(id: Long)
    fun getPaymentNameById(id: Long?):String
    fun getCategoryNameById(id: Long?):String
}