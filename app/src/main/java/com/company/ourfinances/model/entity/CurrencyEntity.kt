package com.company.ourfinances.model.entity

import com.google.gson.annotations.SerializedName

class CurrencyEntity {
    @SerializedName("USDBRL")
    lateinit var usdBRL: CurrencyInfo

    @SerializedName("EURBRL")
    lateinit var eurBRL: CurrencyInfo

    @SerializedName("BTCBRL")
    lateinit var btcBRL: CurrencyInfo

    class CurrencyInfo {
        lateinit var code: String
        lateinit var codein: String
        lateinit var name: String

        @SerializedName("high")
        lateinit var highValue: String

        @SerializedName("low")
        lateinit var lowValue: String

        @SerializedName("varBid")
        lateinit var variationBid: String

        @SerializedName("pctChange")
        lateinit var percentChange: String

        lateinit var bid: String
        lateinit var ask: String
        lateinit var timestamp: String

        @SerializedName("create_date")
        lateinit var createDate: String
        override fun toString(): String {
            return "CurrencyInfo(code='$code', codein='$codein', name='$name', highValue='$highValue', lowValue='$lowValue', variationBid='$variationBid', percentChange='$percentChange', bid='$bid', ask='$ask', timestamp='$timestamp', createDate='$createDate')"
        }




    }

    override fun toString(): String {
        return "CurrencyEntity(usdBRL=$usdBRL, eurBRL=$eurBRL, btcBRL=$btcBRL)"
    }


}