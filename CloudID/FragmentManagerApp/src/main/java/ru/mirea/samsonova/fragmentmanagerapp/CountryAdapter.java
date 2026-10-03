package ru.mirea.samsonova.fragmentmanagerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.Holder> {
    interface OnPick {
        void pick(Country country);
    }

    private final OnPick listener;
    private Country selected;

    CountryAdapter(OnPick listener) {
        this.listener = listener;
    }

    void setSelected(Country country) {
        selected = country;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Country country = CountriesFragment.COUNTRIES.get(position);
        boolean on = selected != null && selected.name.equals(country.name);
        holder.title.setText(country.name);
        holder.title.setBackgroundResource(on ? R.drawable.bg_country_on : R.drawable.bg_country);
        holder.itemView.setOnClickListener(v -> listener.pick(country));
    }

    @Override
    public int getItemCount() {
        return CountriesFragment.COUNTRIES.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView title;

        Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textCountry);
        }
    }
}
