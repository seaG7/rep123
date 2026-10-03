package ru.mirea.samsonova.cloudid.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

import ru.mirea.samsonova.cloudid.domain.GetCloudDetailsUseCase;
import ru.mirea.samsonova.cloudid.domain.GetProfileUseCase;
import ru.mirea.samsonova.cloudid.domain.SaveSightingUseCase;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;

public class DetailsViewModel extends ViewModel {
    public static class Card {
        public final String code;
        public final String title;
        public final String latin;
        public final String extract;
        public final String image;
        public final boolean guest;
        public final boolean saved;
        public final String saveError;

        public Card(String code, String title, String latin, String extract, String image,
                    boolean guest, boolean saved, String saveError) {
            this.code = code;
            this.title = title;
            this.latin = latin;
            this.extract = extract;
            this.image = image;
            this.guest = guest;
            this.saved = saved;
            this.saveError = saveError;
        }
    }

    private final GetCloudDetailsUseCase detailsUseCase;
    private final SaveSightingUseCase saveUseCase;
    private final GetProfileUseCase profileUseCase;
    private final MutableLiveData<Card> card = new MutableLiveData<>();
    private String photo = "";
    private String name = "";

    public DetailsViewModel(GetCloudDetailsUseCase detailsUseCase, SaveSightingUseCase saveUseCase,
                            GetProfileUseCase profileUseCase) {
        this.detailsUseCase = detailsUseCase;
        this.saveUseCase = saveUseCase;
        this.profileUseCase = profileUseCase;
        Log.d(tag(), "created");
    }

    public LiveData<Card> card() {
        return card;
    }

    public void open(String code, String photoUri) {
        if (card.getValue() != null) {
            return;
        }
        String safeCode = code == null ? "" : code;
        photo = photoUri == null ? "" : photoUri;
        CloudType type = detailsUseCase.execute(safeCode);
        boolean guest = profileUseCase.execute().isGuest();
        if (type == null) {
            card.setValue(new Card(safeCode, "Нет карточки", "", "", photo, guest, false, null));
            return;
        }
        name = type.getName();
        String image = photo.isEmpty() ? type.getImageUrl() : photo;
        card.setValue(new Card(type.getCode(), type.getName(), type.getLatin(), type.getExtract(),
                image, guest, false, null));
    }

    public void save(String note) {
        Card current = card.getValue();
        if (current == null || current.guest || current.saved) {
            return;
        }
        String text = note == null ? "" : note.trim();
        boolean ok = saveUseCase.execute(new Sighting(0, current.code, name, text,
                LocalDate.now().toString(), photo));
        if (ok) {
            card.setValue(new Card(current.code, current.title, current.latin, current.extract,
                    current.image, false, true, null));
        } else {
            card.setValue(new Card(current.code, current.title, current.latin, current.extract,
                    current.image, false, false, "Сохранить не удалось: нужен вход."));
        }
    }

    @Override
    protected void onCleared() {
        Log.d(tag(), "cleared");
        super.onCleared();
    }

    private static String tag() {
        return DetailsViewModel.class.getSimpleName();
    }
}
