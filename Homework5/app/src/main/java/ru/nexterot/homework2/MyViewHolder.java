package ru.nexterot.homework2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView mTextView;

    MyViewHolder(@NonNull final View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.text);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "you clicked RV's item", Toast.LENGTH_SHORT).show();
                Context ctx = mTextView.getContext();
                Intent i = new Intent(ctx, ThirdActivity.class);
                i.putExtra("info", mTextView.getText());
                ctx.startActivity(i);
            }
        });
    }
}