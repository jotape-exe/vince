package com.company.ourfinances.model.repository

import com.company.ourfinances.model.constants.RemoteConstants
import com.company.ourfinances.model.entity.CurrencyEntity
import com.company.ourfinances.model.remote.RetrofitClient
import com.company.ourfinances.model.remote.service.CurrencyService
import com.company.ourfinances.view.listener.CurrencyAPIListener
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyRepository {

    private val remoteService = RetrofitClient.getService(CurrencyService::class.java)

    fun getLastCurrencyData(listener: CurrencyAPIListener<CurrencyEntity>) {
        val request = remoteService.getLastCurrencyData()

        request.enqueue(object : Callback<CurrencyEntity>{
            override fun onResponse(call: Call<CurrencyEntity>, response: Response<CurrencyEntity>) {
                if (response.code() == RemoteConstants.HTTP.OK){
                    response.body()?.let { entity->
                        listener.onResponse(entity)
                    }
                } else{
                    listener.onFailure(convertErrorJson(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<CurrencyEntity>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

        })
    }

    private fun convertErrorJson(json: String): String {
        return Gson().fromJson(json, String::class.java)
    }

}