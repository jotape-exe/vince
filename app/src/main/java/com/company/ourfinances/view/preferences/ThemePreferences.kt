package com.company.ourfinances.view.preferences

import android.content.Context

const val THEME_PREFERENCES_NAME = "theme_state"
const val THEME_PREFERENCES_KEY = "theme_key"
class ThemePreferences(context: Context) {
    private val preferences = context.getSharedPreferences(THEME_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setState(state: Boolean){
        preferences.edit().putBoolean(THEME_PREFERENCES_KEY, state).apply()
    }

    fun getState(): Boolean{
        return preferences.getBoolean(THEME_PREFERENCES_KEY, false)
    }
}