package com.company.ourfinances.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.company.ourfinances.R
import com.company.ourfinances.view.utils.ColorObject

class ColorSpinnerAdapter(context: Context, list: List<ColorObject>) :
    ArrayAdapter<ColorObject>(context, 0, list) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = layoutInflater.inflate(R.layout.color_spinner_bg, null, true)
        return view(view, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null){
            view = layoutInflater.inflate(R.layout.color_spinner_item, parent, false)
        }

        return view(view!!, position)
    }

    private fun view(view: View, position: Int): View {

        val colorObject : ColorObject = getItem(position) ?: return view

        val colorNameItem = view.findViewById<TextView>(R.id.colorName)
        val colorNameBG = view.findViewById<TextView>(R.id.colorNameBg)
        val colorBlob = view.findViewById<View>(R.id.colorBlob)

        colorNameBG?.text = colorObject.name
        colorNameBG?.setTextColor(Color.parseColor(colorObject.contrastHexHash))

        colorNameItem?.text = colorObject.name

        colorBlob?.background?.setTint(Color.parseColor(colorObject.hexHash))

        return view
    }

}