package ru.mirea.samsonova.cloudid.presentation.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.home.HomeActivity;
import ru.mirea.samsonova.cloudid.presentation.vm.CloudViewModelFactory;
import ru.mirea.samsonova.cloudid.presentation.vm.HomeViewModel;

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
        TextView note = view.findViewById(R.id.textCatalogNote);
        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        CloudCardAdapter adapter = new CloudCardAdapter(this::open);
        recycler.setAdapter(adapter);
        HomeViewModel viewModel = new ViewModelProvider(requireActivity(), new CloudViewModelFactory())
                .get(HomeViewModel.class);
        viewModel.library().observe(getViewLifecycleOwner(), library -> {
            if (library == null) {
                return;
            }
            boolean showNote = library.warning != null && !library.warning.isEmpty();
            note.setVisibility(showNote ? View.VISIBLE : View.GONE);
            note.setText(showNote ? library.warning : "");
            adapter.setItems(library.catalog);
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

    private void open(CloudType type) {
        ((HomeActivity) requireActivity()).openDetails(type.getCode(), null);
    }
}
