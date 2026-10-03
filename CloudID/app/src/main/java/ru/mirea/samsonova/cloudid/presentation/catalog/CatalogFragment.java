package ru.mirea.samsonova.cloudid.presentation.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mirea.samsonova.cloudid.CloudIdApp;
import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.GetCloudCatalogUseCase;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.details.DetailsActivity;

public class CatalogFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recyclerCatalog);
        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        java.util.List<CloudType> catalog = new GetCloudCatalogUseCase(CloudIdApp.get().clouds()).execute();
        recycler.setAdapter(new CloudCardAdapter(catalog, this::open));
        ScreenRise.play(view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && getView() != null) {
            ScreenRise.play(getView());
        }
    }

    private void open(CloudType type) {
        Intent intent = new Intent(requireContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_CODE, type.getCode());
        startActivity(intent);
    }
}
