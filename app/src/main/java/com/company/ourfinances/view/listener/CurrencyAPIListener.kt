package com.company.ourfinances.view.listener

interface CurrencyAPIListener<T> {
    fun onResponse(result: T)
    fun onFailure(error: String)
}