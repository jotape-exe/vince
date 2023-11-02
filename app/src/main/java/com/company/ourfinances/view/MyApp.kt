package com.company.ourfinances.view

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.company.ourfinances.view.preferences.THEME_PREFERENCES_KEY
import com.company.ourfinances.view.preferences.THEME_PREFERENCES_NAME

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        applyTheme()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun applyTheme() {
        val isDarkModeEnabled = getSharedPreferences(THEME_PREFERENCES_NAME, MODE_PRIVATE)
            .getBoolean(THEME_PREFERENCES_KEY, false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
