package com.company.ourfinances.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.company.ourfinances.R
import com.company.ourfinances.view.utils.CurrencyObject

class CurrencySpinnerAdapter(context: Context, resource: Int, objects: MutableList<CurrencyObject>) : ArrayAdapter<CurrencyObject>(context, resource, objects) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: layoutInflater.inflate(R.layout.style_currency_spinner, parent, false)
        val currencyObject = getItem(position)

        val textView = rowView.findViewById<TextView>(R.id.text_local_currency)
        val imageView = rowView.findViewById<ImageView>(R.id.currency_local_flag)

        textView.text = currencyObject!!.value
        imageView.setImageResource(currencyObject.flag)

        return rowView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
