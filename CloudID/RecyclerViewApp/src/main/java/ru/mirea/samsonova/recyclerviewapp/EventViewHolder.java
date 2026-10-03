package ru.mirea.samsonova.recyclerviewapp;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventViewHolder extends RecyclerView.ViewHolder {
    private final ImageView image;
    private final TextView year;
    private final TextView title;
    private final TextView description;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.imageEvent);
        year = itemView.findViewById(R.id.textYear);
        title = itemView.findViewById(R.id.textTitle);
        description = itemView.findViewById(R.id.textDescription);
    }

    public void bind(Event event) {
        year.setText(event.year);
        title.setText(event.title);
        description.setText(event.description);
        image.setBackgroundTintList(ColorStateList.valueOf(event.color));
    }
}
