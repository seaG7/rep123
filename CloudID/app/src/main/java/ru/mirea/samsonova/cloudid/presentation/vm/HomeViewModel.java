package ru.mirea.samsonova.cloudid.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.samsonova.cloudid.domain.GetCloudCatalogUseCase;
import ru.mirea.samsonova.cloudid.domain.GetMyAtlasUseCase;
import ru.mirea.samsonova.cloudid.domain.GetProfileUseCase;
import ru.mirea.samsonova.cloudid.domain.LogoutUseCase;
import ru.mirea.samsonova.cloudid.domain.models.CatalogLoad;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.domain.models.User;

/**
 * Каталог приходит из замоканного JSON, наблюдения — из Room.
 * MediatorLiveData собирает оба источника в один снимок для каталога, атласа и профиля.
 */
public class HomeViewModel extends ViewModel {
    public static class AtlasRow {
        public final String code;
        public final String name;
        public final String note;
        public final String image;
        public final String photoUri;

        public AtlasRow(String code, String name, String note, String image, String photoUri) {
            this.code = code;
            this.name = name;
            this.note = note;
            this.image = image;
            this.photoUri = photoUri;
        }
    }

    public static class GenusCount {
        public final String name;
        public final String image;
        public final int count;

        public GenusCount(String name, String image, int count) {
            this.name = name;
            this.image = image;
            this.count = count;
        }
    }

    public static class ProfileState {
        public final boolean guest;
        public final String avatar;
        public final String name;
        public final String email;
        public final String summary;
        public final List<GenusCount> genera;

        public ProfileState(boolean guest, String avatar, String name, String email, String summary,
                            List<GenusCount> genera) {
            this.guest = guest;
            this.avatar = avatar;
            this.name = name;
            this.email = email;
            this.summary = summary;
            this.genera = genera;
        }
    }

    public static class Library {
        public final List<CloudType> catalog;
        public final List<AtlasRow> atlas;
        public final String atlasEmpty;
        public final ProfileState profile;
        public final String warning;

        public Library(List<CloudType> catalog, List<AtlasRow> atlas, String atlasEmpty,
                       ProfileState profile, String warning) {
            this.catalog = catalog;
            this.atlas = atlas;
            this.atlasEmpty = atlasEmpty;
            this.profile = profile;
            this.warning = warning == null ? "" : warning;
        }
    }

    private final GetProfileUseCase profileUseCase;
    private final GetMyAtlasUseCase atlasUseCase;
    private final LogoutUseCase logoutUseCase;
    private final MediatorLiveData<Library> library = new MediatorLiveData<>();
    private final MutableLiveData<UiEvent<Boolean>> loggedOut = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<CloudType>> catalog = new MutableLiveData<>();
    private List<CloudType> types = new ArrayList<>();
    private List<Sighting> sightings = new ArrayList<>();
    private volatile String catalogWarning = "";
    private volatile boolean cleared;

    public HomeViewModel(GetCloudCatalogUseCase catalogUseCase, GetProfileUseCase profileUseCase,
                         GetMyAtlasUseCase atlasUseCase, LogoutUseCase logoutUseCase,
                         LiveData<List<Sighting>> roomSightings) {
        this.profileUseCase = profileUseCase;
        this.atlasUseCase = atlasUseCase;
        this.logoutUseCase = logoutUseCase;
        library.addSource(roomSightings, value -> {
            sightings = atlasUseCase.execute();
            emit();
        });
        library.addSource(catalog, value -> {
            types = value == null ? new ArrayList<>() : value;
            emit();
        });
        executor.execute(() -> {
            CatalogLoad loaded = catalogUseCase.execute();
            catalogWarning = loaded.getWarning();
            if (!cleared) {
                catalog.postValue(loaded.getTypes());
            }
        });
        Log.d(tag(), "created");
    }

    public LiveData<Library> library() {
        return library;
    }

    public LiveData<UiEvent<Boolean>> loggedOut() {
        return loggedOut;
    }

    public void logout() {
        logoutUseCase.execute();
        loggedOut.setValue(new UiEvent<>(true));
    }

    private void emit() {
        User user = profileUseCase.execute();
        boolean guest = user.isGuest();
        List<AtlasRow> rows = guest ? new ArrayList<>() : rowsOf(sightings);
        String empty = null;
        if (rows.isEmpty()) {
            empty = guest
                    ? "Гость смотрит небо и каталог. Атлас откроется после входа."
                    : "Пока пусто. Определите облако и сохраните кадр.";
        }
        library.setValue(new Library(types, rows, empty, profileOf(user, guest), catalogWarning));
    }

    private List<AtlasRow> rowsOf(List<Sighting> source) {
        Map<String, String> images = imagesByCode();
        List<AtlasRow> rows = new ArrayList<>();
        for (Sighting sighting : source) {
            String photo = sighting.getPhotoUri() == null ? "" : sighting.getPhotoUri();
            String image = photo.isEmpty() ? images.get(sighting.getCloudCode()) : photo;
            String note = sighting.getNote() == null ? "" : sighting.getNote().trim();
            rows.add(new AtlasRow(sighting.getCloudCode(), sighting.getCloudName(), note,
                    image == null ? "" : image, photo));
        }
        return rows;
    }

    private ProfileState profileOf(User user, boolean guest) {
        if (guest) {
            return new ProfileState(true, "Г", "Гость", "без аккаунта",
                    "Атлас и заметки доступны после входа.", new ArrayList<>());
        }
        String login = user.getLogin() == null ? "" : user.getLogin();
        int at = login.indexOf('@');
        String title = at > 0 ? login.substring(0, at) : login;
        String avatar = title.isEmpty() ? "•" : title.substring(0, 1).toUpperCase();
        List<GenusCount> genera = generaOf();
        String summary = sightings.isEmpty()
                ? "Пока пусто. Определите облако и сохраните кадр."
                : sightings.size() + " " + observations(sightings.size());
        return new ProfileState(false, avatar, title.isEmpty() ? "Профиль" : title, login, summary, genera);
    }

    private List<GenusCount> generaOf() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Map<String, CloudType> byCode = new LinkedHashMap<>();
        for (CloudType type : types) {
            counts.put(type.getCode(), 0);
            byCode.put(type.getCode(), type);
        }
        for (Sighting sighting : sightings) {
            String code = sighting.getCloudCode();
            counts.put(code, counts.getOrDefault(code, 0) + 1);
        }
        List<GenusCount> genera = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 0) {
                continue;
            }
            CloudType type = byCode.get(entry.getKey());
            genera.add(new GenusCount(
                    type == null ? entry.getKey() : type.getName(),
                    type == null ? "" : type.getImageUrl(),
                    entry.getValue()));
        }
        return genera;
    }

    private Map<String, String> imagesByCode() {
        Map<String, String> images = new LinkedHashMap<>();
        for (CloudType type : types) {
            images.put(type.getCode(), type.getImageUrl());
        }
        return images;
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

    @Override
    protected void onCleared() {
        cleared = true;
        executor.shutdownNow();
        Log.d(tag(), "cleared");
        super.onCleared();
    }

    private static String tag() {
        return HomeViewModel.class.getSimpleName();
    }
}
