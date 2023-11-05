package com.company.ourfinances.view

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.company.ourfinances.view.preferences.THEME_PREFERENCES_KEY
import com.company.ourfinances.view.preferences.THEME_PREFERENCES_NAME
import com.company.ourfinances.view.preferences.ThemePreferences

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
        val isDarkModeEnabled = ThemePreferences(this).getState()

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
