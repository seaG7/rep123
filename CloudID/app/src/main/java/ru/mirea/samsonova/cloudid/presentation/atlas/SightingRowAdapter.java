package ru.mirea.samsonova.cloudid.presentation.atlas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;

public class SightingRowAdapter extends RecyclerView.Adapter<SightingRowAdapter.Holder> {
    interface OnSightingClick {
        void onClick(Sighting sighting);
    }

    private final List<Sighting> items;
    private final Map<String, String> images;
    private final OnSightingClick click;

    public SightingRowAdapter(List<Sighting> items, Map<String, String> images, OnSightingClick click) {
        this.items = items;
        this.images = images;
        this.click = click;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sighting, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Sighting sighting = items.get(position);
        holder.name.setText(sighting.getCloudName());
        String note = sighting.getNote() == null ? "" : sighting.getNote().trim();
        holder.meta.setText(note);
        holder.meta.setVisibility(note.isEmpty() ? View.GONE : View.VISIBLE);
        String photo = sighting.getPhotoUri();
        Object source = photo != null && !photo.isEmpty() ? photo : images.get(sighting.getCloudCode());
        Glide.with(holder.image).load(source).centerCrop().into(holder.image);
        holder.itemView.setOnClickListener(v -> click.onClick(sighting));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView name;
        final TextView meta;

        Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageThumb);
            name = itemView.findViewById(R.id.textName);
            meta = itemView.findViewById(R.id.textMeta);
        }
    }
}
