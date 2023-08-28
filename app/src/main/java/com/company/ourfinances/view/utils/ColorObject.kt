package com.company.ourfinances.view.utils

class ColorObject(
    var name: String,
    var hex: String,
    var contrastHex: String
) {
    val hexHash: String = "#$hex"
    val contrastHexHash: String = "#$contrastHex"
}