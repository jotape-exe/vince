package com.company.ourfinances.view.assets

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.company.ourfinances.R

class LoadingDialog(private val activity: AppCompatActivity) {
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builderDialog = AlertDialog.Builder(activity)
        builderDialog.setView(activity.layoutInflater.inflate(R.layout.dialog_loading, null))
        builderDialog.setCancelable(true)

        dialog = builderDialog.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}