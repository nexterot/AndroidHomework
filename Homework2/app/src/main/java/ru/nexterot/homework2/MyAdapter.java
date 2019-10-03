package ru.nexterot.homework2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<String> mData;

    MyAdapter() {
        mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.recycler_element, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String str = mData.get(i);
        myViewHolder.mTextView.setText(str);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    void setData(ArrayList<String> l) {
        mData = l;
        notifyDataSetChanged();
    }
}