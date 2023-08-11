package com.company.ourfinances.model.preferences

import android.content.Context

import android.content.SharedPreferences



class FinancePreferences(context: Context) {

    private val PREFERENCE_NAME = "Finance"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun storeIdentifier(key: String, str: String) {
        this.sharedPreferences.edit().putString(key, str).apply()
    }

    fun getStoredIdentifier(key: String):String {
        return this.sharedPreferences.getString(key,"") ?: ""
    }
}