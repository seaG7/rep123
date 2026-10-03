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

import com.bumptech.glide.Glide;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.mirea.samsonova.cloudid.CloudIdApp;
import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.GetCloudCatalogUseCase;
import ru.mirea.samsonova.cloudid.domain.GetMyAtlasUseCase;
import ru.mirea.samsonova.cloudid.domain.GetProfileUseCase;
import ru.mirea.samsonova.cloudid.domain.LogoutUseCase;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.domain.models.User;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.auth.AuthActivity;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.buttonLogout).setOnClickListener(v -> {
            new LogoutUseCase(CloudIdApp.get().auth()).execute();
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        bind(view);
        ScreenRise.play(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            bind(getView());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && getView() != null) {
            bind(getView());
            ScreenRise.play(getView());
        }
    }

    private void bind(View view) {
        CloudIdApp app = CloudIdApp.get();
        User user = new GetProfileUseCase(app.auth()).execute();
        List<Sighting> sightings = new GetMyAtlasUseCase(app.atlas(), app.auth()).execute();
        TextView avatar = view.findViewById(R.id.textAvatar);
        TextView name = view.findViewById(R.id.textName);
        TextView email = view.findViewById(R.id.textEmail);
        TextView textCount = view.findViewById(R.id.textCount);
        LinearLayout genusList = view.findViewById(R.id.genusList);
        genusList.removeAllViews();
        if (user.isGuest()) {
            avatar.setText("Г");
            name.setText("Гость");
            email.setText("без аккаунта");
            textCount.setText("Атлас и заметки доступны после входа.");
            return;
        }
        String login = user.getLogin() == null ? "" : user.getLogin();
        int at = login.indexOf('@');
        String title = at > 0 ? login.substring(0, at) : login;
        avatar.setText(title.isEmpty() ? "•" : title.substring(0, 1).toUpperCase());
        name.setText(title.isEmpty() ? "Профиль" : title);
        email.setText(login);
        fillGenera(genusList, sightings);
        if (sightings.isEmpty()) {
            textCount.setText("Пока пусто. Определите облако и сохраните кадр.");
        } else {
            textCount.setText(sightings.size() + " " + observations(sightings.size()));
        }
    }

    private void fillGenera(LinearLayout list, List<Sighting> sightings) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Map<String, CloudType> types = new LinkedHashMap<>();
        for (CloudType type : new GetCloudCatalogUseCase(CloudIdApp.get().clouds()).execute()) {
            counts.put(type.getCode(), 0);
            types.put(type.getCode(), type);
        }
        for (Sighting sighting : sightings) {
            String code = sighting.getCloudCode();
            counts.put(code, counts.getOrDefault(code, 0) + 1);
        }
        LayoutInflater inflater = LayoutInflater.from(list.getContext());
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 0) {
                continue;
            }
            CloudType type = types.get(entry.getKey());
            View row = inflater.inflate(R.layout.item_genus, list, false);
            TextView label = row.findViewById(R.id.textGenus);
            TextView count = row.findViewById(R.id.textGenusCount);
            ImageView image = row.findViewById(R.id.imageGenus);
            label.setText(type == null ? entry.getKey() : type.getName());
            count.setText(String.valueOf(entry.getValue()));
            if (type != null) {
                Glide.with(image).load(type.getImageUrl()).centerCrop().into(image);
            }
            list.addView(row);
        }
    }

    private static String observations(int count) {
        int mod10 = count % 10;
        int mod100 = count % 100;
        if (mod10 == 1 && mod100 != 11) {
            return "наблюдение";
        }
        if (mod10 >= 2 && mod10 <= 4 && (mod100 < 12 || mod100 > 14)) {
            return "наблюдения";
        }
        return "наблюдений";
    }
}
