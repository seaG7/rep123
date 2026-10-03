package ru.mirea.samsonova.recyclerviewapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private final List<Event> events = new ArrayList<>();

    public void setItems(List<Event> next) {
        events.clear();
        if (next != null) {
            events.addAll(next);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), event.year + " · " + event.title, Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
