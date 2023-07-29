package com.company.ourfinances.model.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val userName: SharedPreferences =
        context.getSharedPreferences("name", Context.MODE_PRIVATE)

    fun putName(key: String, name: String) {
        userName.edit().putString(key, name).apply()
    }

    fun getName(key: String): String {
        return userName.getString(key, "Nome não definido") ?: ""
    }

}