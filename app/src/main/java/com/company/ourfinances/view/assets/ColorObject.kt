package com.company.ourfinances.view.assets

class ColorObject(
    var name: String,
    var hex: String,
    var contrastHex: String
) {
    val hexHash: String = "#$hex"
    val contrastHexHash: String = "#$contrastHex"
}