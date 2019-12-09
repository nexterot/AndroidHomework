package com.example.lab9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ElemViewHolder> {
    private int countItems;

    private List<TableEntity> itemsList;

    public ListAdapter(List<TableEntity> itemsList) {
        countItems = 0;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ElemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ElemViewHolder viewHolder = new ElemViewHolder(view, "");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ElemViewHolder holder, int position) {
        // Обновляем элемент
        holder.updateElem(itemsList.get(position).id, itemsList.get(position).text);
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        }
        return itemsList.size();
    }

    public void deleteElem(long i) {
        itemsList.remove(i);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (itemsList != null) {
            itemsList.clear();
        }
        notifyDataSetChanged();
    }

    public void setData(List<TableEntity> elems) {
        itemsList = elems;
        notifyDataSetChanged();
    }
}
