package com.company.ourfinances.model.remote

import com.company.ourfinances.model.constants.RemoteConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object{
        private lateinit var INTANCE: Retrofit
        fun getRetrofitInstance(): Retrofit{

            if (!::INTANCE.isInitialized){
                synchronized(RetrofitClient::class){
                    INTANCE = Retrofit.Builder()
                        .baseUrl(RemoteConstants.API_URL)
                        .client(OkHttpClient.Builder().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }

            return INTANCE
        }

        fun <T> getService(serviceClass: Class<T>): T {
            return getRetrofitInstance().create(serviceClass)
        }
    }

}
