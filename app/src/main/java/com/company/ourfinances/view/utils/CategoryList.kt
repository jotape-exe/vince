package com.company.ourfinances.view.utils

import android.content.Context
import com.company.ourfinances.R

class CategoryList(val context: Context) {
    fun getCategories(): List<String> {
        return listOf(
            context.getString(R.string.categoria_alimentacao),
            context.getString(R.string.categoria_moradia),
            context.getString(R.string.categoria_transporte),
            context.getString(R.string.categoria_lazer),
            context.getString(R.string.categoria_investimentos),
            context.getString(R.string.categoria_compras),
            context.getString(R.string.categoria_saude),
            context.getString(R.string.categoria_veiculo),
            context.getString(R.string.categoria_outro)
        )
    }
}
