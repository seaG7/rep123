package ru.mirea.samsonova.retrofitapp;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    final ImageView image;
    final TextView title;
    final CheckBox done;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.imageTodo);
        title = itemView.findViewById(R.id.textTitle);
        done = itemView.findViewById(R.id.checkDone);
    }
}
