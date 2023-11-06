package com.company.ourfinances.view.utils

import android.content.Context
import com.company.ourfinances.R

class ColorList(val context: Context) {

    private val textBlack = "000000"
    private val textWhite = "FFFFFF"

    val defaultColor = getColors()[0]

    fun colorPosition(colorObject: ColorObject): Int {
        for (color in getColors().indices) {
            if (colorObject == getColors()[color]) {
                return color
            }
        }
        return 0
    }

    fun getColors(): List<ColorObject> {
        return listOf(
            ColorObject(context.getString(R.string.silver), "C0C0C0", textBlack),
            ColorObject(context.getString(R.string.black), textBlack, textWhite),
            ColorObject(context.getString(R.string.gray), "808080", textWhite),
            ColorObject(context.getString(R.string.brown), "800000", textWhite),
            ColorObject(context.getString(R.string.red), "FF0000", textWhite),
            ColorObject(context.getString(R.string.fuchsia), "FF00FF", textWhite),
            ColorObject(context.getString(R.string.green), "008000", textWhite),
            ColorObject(context.getString(R.string.lime), "00FF00", textBlack),
            ColorObject(context.getString(R.string.olive), "808000", textWhite),
            ColorObject(context.getString(R.string.yellow), "FFFF00", textBlack),
            ColorObject(context.getString(R.string.marine), "000080", textWhite),
            ColorObject(context.getString(R.string.blue), "0000FF", textWhite),
            ColorObject(context.getString(R.string.bluish_green), "008080", textWhite),
            ColorObject(context.getString(R.string.aqua), "00FFFF", textBlack),
            ColorObject(context.getString(R.string.purple), "9803FC", textWhite)

        )
    }

}