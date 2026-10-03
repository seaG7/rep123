package ru.mirea.samsonova.cloudid.presentation.atlas;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.details.DetailsActivity;
import ru.mirea.samsonova.cloudid.presentation.vm.CloudViewModelFactory;
import ru.mirea.samsonova.cloudid.presentation.vm.HomeViewModel;

public class AtlasFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_atlas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recyclerAtlas);
        View empty = view.findViewById(R.id.groupEmpty);
        TextView textEmpty = view.findViewById(R.id.textEmpty);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        HomeViewModel viewModel = new ViewModelProvider(requireActivity(), new CloudViewModelFactory())
                .get(HomeViewModel.class);
        viewModel.library().observe(getViewLifecycleOwner(), library -> {
            if (library == null) {
                return;
            }
            boolean showEmpty = library.atlas.isEmpty();
            empty.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
            recycler.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
            if (showEmpty) {
                textEmpty.setText(library.atlasEmpty);
            }
            java.util.List<Sighting> sightings = new ArrayList<>();
            java.util.Map<String, String> images = new HashMap<>();
            for (HomeViewModel.AtlasRow row : library.atlas) {
                sightings.add(new Sighting(0, row.code, row.name, row.note, "", row.photoUri));
                images.put(row.code, row.image);
            }
            recycler.setAdapter(new SightingRowAdapter(sightings, images, this::open));
        });
        ScreenRise.play(view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && getView() != null) {
            ScreenRise.play(getView());
        }
    }

    private void open(Sighting sighting) {
        Intent intent = new Intent(requireContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_CODE, sighting.getCloudCode());
        intent.putExtra(DetailsActivity.EXTRA_PHOTO, sighting.getPhotoUri());
        startActivity(intent);
    }
}
