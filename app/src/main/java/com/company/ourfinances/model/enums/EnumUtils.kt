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
}
