package com.company.ourfinances.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.ourfinances.model.entity.CurrencyEntity
import com.company.ourfinances.model.repository.CurrencyRepository
import com.company.ourfinances.view.listener.CurrencyAPIListener

class CurrencyViewModel {
    private val repository = CurrencyRepository()

    private var _currencyData = MutableLiveData<List<CurrencyEntity>>()
    val currencyData: LiveData<List<CurrencyEntity>> = _currencyData

    fun getLastCurrencyData(){
        repository.getLastCurrencyData(object : CurrencyAPIListener<CurrencyEntity>{
            override fun onResponse(result: CurrencyEntity) {
                _currencyData.value = listOf(result)
            }

            override fun onFailure(error: String) {

            }

        })
    }
}