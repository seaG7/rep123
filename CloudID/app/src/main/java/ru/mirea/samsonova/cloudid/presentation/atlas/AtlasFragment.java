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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mirea.samsonova.cloudid.CloudIdApp;
import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.GetCloudCatalogUseCase;
import ru.mirea.samsonova.cloudid.domain.GetMyAtlasUseCase;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.details.DetailsActivity;

public class AtlasFragment extends Fragment {
    private RecyclerView recycler;
    private View empty;
    private TextView textEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_atlas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler = view.findViewById(R.id.recyclerAtlas);
        empty = view.findViewById(R.id.groupEmpty);
        textEmpty = view.findViewById(R.id.textEmpty);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind();
        ScreenRise.play(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recycler != null) {
            bind();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && recycler != null) {
            bind();
            ScreenRise.play(requireView());
        }
    }

    private void bind() {
        CloudIdApp app = CloudIdApp.get();
        boolean guest = app.auth().getProfile().isGuest();
        List<Sighting> sightings = new GetMyAtlasUseCase(app.atlas(), app.auth()).execute();
        Map<String, String> images = new HashMap<>();
        for (CloudType type : new GetCloudCatalogUseCase(app.clouds()).execute()) {
            images.put(type.getCode(), type.getImageUrl());
        }
        recycler.setAdapter(new SightingRowAdapter(sightings, images, this::open));
        boolean showEmpty = sightings.isEmpty();
        empty.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
        recycler.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
        if (guest) {
            textEmpty.setText("Гость смотрит небо и каталог. Атлас откроется после входа.");
        } else {
            textEmpty.setText("Пока пусто. Определите облако и сохраните кадр.");
        }
    }

    private void open(Sighting sighting) {
        Intent intent = new Intent(requireContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_CODE, sighting.getCloudCode());
        intent.putExtra(DetailsActivity.EXTRA_PHOTO, sighting.getPhotoUri());
        startActivity(intent);
    }
}
