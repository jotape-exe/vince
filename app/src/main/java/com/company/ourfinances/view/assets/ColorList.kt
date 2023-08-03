package com.company.ourfinances.view.assets

class ColorList {

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
            ColorObject("Prata", "C0C0C0", textBlack),
            ColorObject("Preto", textBlack, textWhite),
            ColorObject("Cinza", "808080", textWhite),
            ColorObject("Marrom", "800000", textWhite),
            ColorObject("Vermelho", "FF0000", textWhite),
            ColorObject("Fúcsia", "FF00FF", textWhite),
            ColorObject("Verde", "008000", textWhite),
            ColorObject("Lima", "00FF00", textBlack),
            ColorObject("Oliva", "808000", textWhite),
            ColorObject("Amarelo", "FFFF00", textBlack),
            ColorObject("Marinho", "000080", textWhite),
            ColorObject("Azul", "0000FF", textWhite),
            ColorObject("Verde-azulado", "008080", textWhite),
            ColorObject("Aqua", "00FFFF", textBlack),
            ColorObject("Roxo", "9803FC", textWhite)

        )
    }

}