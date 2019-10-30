package ru.nexterot.homework2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private ArrayList<Pair<String,Double>> mData;

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
        String str = mData.get(i).first;
        Double d = mData.get(i).second;
        myViewHolder.mTextView.setText(str + " " + d);
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    void clear() {
        if (mData == null) {
            return;
        }
        mData.clear();
        notifyDataSetChanged();
    }

    void setData(ArrayList<Pair<String,Double>> s) {
        mData = s;
        notifyDataSetChanged();
    }
}