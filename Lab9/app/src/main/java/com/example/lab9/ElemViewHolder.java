package com.example.lab9;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ElemViewHolder extends RecyclerView.ViewHolder {
    private TextView contentItemView;
    public String data;
    public long id;

    public ElemViewHolder(View contentItemView, String data) {
        super(contentItemView);
        this.contentItemView = (TextView) contentItemView.findViewById(R.id.item_text_view);
        this.data = data;
    }

    public void updateElem(long id, String data) {
        String textViewContent = data + " (" + String.valueOf(id) +")";
        contentItemView.setText(textViewContent);
        this.id = id;
        this.data = data;
    }

    public void setData(String data) {
        contentItemView.setText(data);
        this.data = data;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
