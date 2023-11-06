package com.company.ourfinances.model.enums

import android.content.Context
import com.company.ourfinances.R

object EnumUtils {
    fun getCardTypeName(cardType: CardTypeEnum, context: Context): String {
        return when (cardType) {
            CardTypeEnum.CREDIT -> context.getString(R.string.credit)
            CardTypeEnum.DEBIT -> context.getString(R.string.debit)
        }
    }

    fun getRegisterType(registerType: RegisterTypeEnum, context: Context?): String {
        return when (registerType) {
            RegisterTypeEnum.RECEITA -> context!!.getString(R.string.revenue)
            RegisterTypeEnum.DESPESA-> context!!.getString(R.string.expense)
            RegisterTypeEnum.TRANSFERENCIA -> context!!.getString(R.string.transfer)
        }
    }
}
