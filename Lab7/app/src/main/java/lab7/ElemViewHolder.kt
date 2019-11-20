package com.example.koshelev.labs.javalab7

import android.view.View
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

class ElemViewHolder(contentItemView: View, var data: String) : RecyclerView.ViewHolder(contentItemView) {
    private val contentItemView: TextView

    init {
        this.contentItemView = contentItemView.findViewById<View>(R.id.item_text_view) as TextView
    }

    fun setDataAndUpdate(data: String) {
        contentItemView.text = data
        this.data = data
    }
}
