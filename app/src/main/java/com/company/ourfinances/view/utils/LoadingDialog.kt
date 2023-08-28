package com.company.ourfinances.view.utils

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.company.ourfinances.R

class LoadingDialog(activity: AppCompatActivity) {
    private var dialog: AlertDialog
    init {
        val builderDialog = AlertDialog.Builder(activity)

        builderDialog.setView(activity.layoutInflater.inflate(R.layout.dialog_loading, null))
        builderDialog.setCancelable(true)

        dialog = builderDialog.create()
    }

    fun startLoadingDialog() {
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}