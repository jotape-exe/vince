package com.company.ourfinances.view

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.view.isVisible
import com.company.ourfinances.R
import com.company.ourfinances.databinding.ActivitySettingsBinding
import com.company.ourfinances.model.repository.GeneralRepository

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                .setTitle("Apagar Tudo")
                .setMessage("Tem certeza que deseja excluir tudo? Isso não pode ser revertido.")
                .setPositiveButton("Sim") { dialog, which ->
                    doDeleteAll()
                }
                .setNeutralButton("Cancelar", null)
                .create()
                .show()
        }
    }

    private fun doDeleteAll() {
        binding.progressDelete.isVisible = true
        Handler().postDelayed({
            GeneralRepository(this).deleteAll()
        }, 1000)
        binding.progressDelete.isVisible = false

        Toast.makeText(this, "Sucesso!", Toast.LENGTH_LONG).show()

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
        }}
}

data class AppVersion(
    val versionName: String,
    val versionNumber: Long,
)