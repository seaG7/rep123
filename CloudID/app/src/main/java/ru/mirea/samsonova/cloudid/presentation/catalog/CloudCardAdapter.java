package ru.mirea.samsonova.cloudid.presentation.catalog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;

public class CloudCardAdapter extends RecyclerView.Adapter<CloudCardAdapter.Holder> {
    interface OnCloudClick {
        void onClick(CloudType type);
    }

    private final List<CloudType> items;
    private final OnCloudClick click;

    public CloudCardAdapter(List<CloudType> items, OnCloudClick click) {
        this.items = items;
        this.click = click;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cloud, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CloudType type = items.get(position);
        holder.name.setText(type.getName());
        holder.latin.setText(type.getLatin());
        Glide.with(holder.image).load(type.getImageUrl()).centerCrop().into(holder.image);
        holder.itemView.setOnClickListener(v -> click.onClick(type));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView name;
        final TextView latin;

        Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageCloud);
            name = itemView.findViewById(R.id.textName);
            latin = itemView.findViewById(R.id.textLatin);
        }
    }
}
