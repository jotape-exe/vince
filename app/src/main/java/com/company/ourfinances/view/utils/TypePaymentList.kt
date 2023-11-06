package com.company.ourfinances.view.utils

import android.content.Context
import com.company.ourfinances.R

class TypePaymentList(val context: Context) {
    fun getList(): List<String> {
        return listOf(
            context.getString(R.string.pix),
            context.getString(R.string.money),
            context.getString(R.string.card),
            context.getString(R.string.billet),
        )
    }
}