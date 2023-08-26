package com.company.ourfinances.model.remote.service

import com.company.ourfinances.model.entity.CurrencyEntity
import retrofit2.Call
import retrofit2.http.GET


interface CurrencyService {
    @GET("last/USD-BRL,EUR-BRL,BTC-BRL")
    fun getLastCurrencyData(): Call<CurrencyEntity>
}