package com.company.ourfinances.model.preferences

import android.content.Context
import android.content.SharedPreferences
import com.company.ourfinances.model.constants.DatabaseConstants

class UserPreferences(context: Context) {
    private val username: SharedPreferences =
        context.getSharedPreferences(DatabaseConstants.User.name, Context.MODE_PRIVATE)

    fun putName(key: String, name: String) {
        username.edit().putString(key, name).apply()
    }

    fun getName(key: String): String {
        return username.getString(key, "Nome não definido") ?: ""
    }

}