package com.example.koshelev.labs.javalab7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private var itemsList: MutableList<TableEntity>?) : RecyclerView.Adapter<ElemViewHolder>() {
    private val countItems: Int

    init {
        countItems = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElemViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.list_item
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately)

        return ElemViewHolder(view, "")
    }

    override fun onBindViewHolder(holder: ElemViewHolder, position: Int) {
        // Обновляем элемент
        holder.setDataAndUpdate(itemsList!![position].text)
    }

    override fun getItemCount(): Int {
        return if (itemsList == null) {
            0
        } else itemsList!!.size
    }

    fun clearData() {
        if (itemsList != null) {
            itemsList!!.clear()
        }
        notifyDataSetChanged()
    }

    fun setData(elems: MutableList<TableEntity>) {
        itemsList = elems
        notifyDataSetChanged()
    }
}
