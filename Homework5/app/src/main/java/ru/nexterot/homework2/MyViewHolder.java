package ru.nexterot.homework2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView mTextView;
    TextView mText2View;

    MyViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.text);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "you clicked RV's item", Toast.LENGTH_SHORT).show();
            }
        });
        mText2View = itemView.findViewById(R.id.text2);
    }
}