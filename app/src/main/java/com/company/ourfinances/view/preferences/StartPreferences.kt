package com.company.ourfinances.view.preferences

import android.content.Context

const val START_TIMES = "start"
class StartPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(START_TIMES, Context.MODE_PRIVATE)

    fun setState(state: Int){
        preferences.edit().putInt(START_TIMES, state).apply()
    }

    fun getState(): Int{
        return preferences.getInt(START_TIMES, 0)
    }
}