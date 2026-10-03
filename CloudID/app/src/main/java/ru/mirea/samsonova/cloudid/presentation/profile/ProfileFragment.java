package ru.mirea.samsonova.cloudid.presentation.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.auth.AuthActivity;
import ru.mirea.samsonova.cloudid.presentation.vm.CloudViewModelFactory;
import ru.mirea.samsonova.cloudid.presentation.vm.HomeViewModel;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        HomeViewModel viewModel = new ViewModelProvider(requireActivity(), new CloudViewModelFactory())
                .get(HomeViewModel.class);
        view.findViewById(R.id.buttonLogout).setOnClickListener(v -> viewModel.logout());
        viewModel.library().observe(getViewLifecycleOwner(), library -> {
            if (library == null || library.profile == null) {
                return;
            }
            bind(view, library.profile);
        });
        viewModel.loggedOut().observe(getViewLifecycleOwner(), event -> {
            Boolean go = event == null ? null : event.take();
            if (!Boolean.TRUE.equals(go)) {
                return;
            }
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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

    private void bind(View view, HomeViewModel.ProfileState profile) {
        TextView avatar = view.findViewById(R.id.textAvatar);
        TextView name = view.findViewById(R.id.textName);
        TextView email = view.findViewById(R.id.textEmail);
        TextView textCount = view.findViewById(R.id.textCount);
        LinearLayout genusList = view.findViewById(R.id.genusList);
        avatar.setText(profile.avatar);
        name.setText(profile.name);
        email.setText(profile.email);
        textCount.setText(profile.summary);
        genusList.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        for (HomeViewModel.GenusCount genus : profile.genera) {
            View row = inflater.inflate(R.layout.item_genus, genusList, false);
            ((TextView) row.findViewById(R.id.textGenus)).setText(genus.name);
            ((TextView) row.findViewById(R.id.textGenusCount)).setText(String.valueOf(genus.count));
            ImageView image = row.findViewById(R.id.imageGenus);
            if (genus.image != null && !genus.image.isEmpty()) {
                Glide.with(image).load(genus.image).centerCrop().into(image);
            }
            genusList.addView(row);
        }
    }
}
