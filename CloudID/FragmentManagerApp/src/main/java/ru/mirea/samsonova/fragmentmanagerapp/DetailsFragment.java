package ru.mirea.samsonova.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView name = view.findViewById(R.id.textName);
        TextView capital = view.findViewById(R.id.textCapital);
        TextView fact = view.findViewById(R.id.textFact);
        name.setText(getString(R.string.pick));
        ShareViewModel viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        viewModel.selected().observe(getViewLifecycleOwner(), country -> {
            if (country == null) {
                return;
            }
            name.setText(country.name);
            capital.setText(country.capital);
            fact.setText(country.fact);
        });
    }
}
