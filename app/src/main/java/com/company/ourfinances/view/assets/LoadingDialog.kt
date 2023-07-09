package com.company.ourfinances.view.assets

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.company.ourfinances.R

class LoadingDialog(val context: Context) {
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builderDialog = AlertDialog.Builder(context)
        builderDialog.setView(LayoutInflater.from(context).inflate(R.layout.custom_dialog, null))
        builderDialog.setCancelable(true)

        dialog = builderDialog.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}