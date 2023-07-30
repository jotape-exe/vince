package com.company.ourfinances.view.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.R

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleComponent: TextView = itemView.findViewById(R.id.text_title)
    val iconComponent: ImageView = itemView.findViewById(R.id.image_component_home)
    val buttonComponent: Button = itemView.findViewById(R.id.button_component_home)

}