package com.company.ourfinances.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.view.isVisible
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivitySettingsBinding
import com.company.ourfinances.model.repository.GeneralRepository
import com.company.ourfinances.view.preferences.ThemePreferences

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private lateinit var themePreferences: ThemePreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        themePreferences = ThemePreferences(this)

        setup()

        listeners()
    }

    private fun setup() {
        binding.textAppVersion.text = "Versão ${getAppVersion(this)?.versionNumber.toString()}"

    }

    private fun listeners() {
        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.buttonDeleteAccount.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_all))
                .setMessage(getString(R.string.really_delete))
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    doDeleteAll()
                }
                .setNeutralButton(getString(R.string.cancel), null)
                .create()
                .show()
        }
        binding.switchTheme.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            themePreferences.setState(isChecked)
        }

        binding.switchTheme.isChecked = themePreferences.getState()
    }

    private fun doDeleteAll() {

        GeneralRepository(this).deleteAll()

        Toast.makeText(this, getString(R.string.success), Toast.LENGTH_LONG).show()

    }

    fun getAppVersion(
        context: Context,
    ): AppVersion? {
        return try {
            val packageManager = context.packageManager
            val packageName = context.packageName
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            AppVersion(
                versionName = packageInfo.versionName,
                versionNumber = PackageInfoCompat.getLongVersionCode(packageInfo),
            )
        } catch (e: Exception) {
            null
        }
    }
}

data class AppVersion(
    val versionName: String,
    val versionNumber: Long,
)