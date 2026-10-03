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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class CountriesFragment extends Fragment {
    static final List<Country> COUNTRIES = Arrays.asList(
            new Country("Россия", "Москва", "Зимой небо часто закрывают слоистые облака."),
            new Country("Исландия", "Рейкьявик", "Низкая облачность и резкий ветер держатся у побережья."),
            new Country("Япония", "Токио", "Летом кучевые облака быстро вырастают в грозу."),
            new Country("Норвегия", "Осло", "Перистые облака появляются перед сменой погоды."),
            new Country("Чили", "Сантьяго", "Над Андами облака задерживаются у хребта."),
            new Country("Кения", "Найроби", "Кучевые облака стоят высоко над саванной."),
            new Country("Франция", "Париж", "Осенью обычны поля слоисто-кучевых облаков."),
            new Country("Австралия", "Канберра", "Сухое небо чередуется с редкими кучевыми.")
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ShareViewModel viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        RecyclerView recycler = view.findViewById(R.id.recyclerCountries);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        CountryAdapter adapter = new CountryAdapter(country -> viewModel.select(country));
        recycler.setAdapter(adapter);
        viewModel.selected().observe(getViewLifecycleOwner(), adapter::setSelected);
        if (viewModel.selected().getValue() == null) {
            viewModel.select(COUNTRIES.get(0));
        }
    }
}
