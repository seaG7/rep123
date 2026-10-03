package ru.mirea.samsonova.cloudid.presentation.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;

import ru.mirea.samsonova.cloudid.CloudIdApp;
import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.GetCloudDetailsUseCase;
import ru.mirea.samsonova.cloudid.domain.SaveSightingUseCase;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.auth.AuthActivity;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_CODE = "code";
    public static final String EXTRA_PHOTO = "photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        View back = findViewById(R.id.buttonBack);
        ViewCompat.setOnApplyWindowInsetsListener(back, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            view.setTranslationY(bars.top);
            return insets;
        });
        back.setOnClickListener(v -> finish());

        String code = getIntent().getStringExtra(EXTRA_CODE);
        String photo = getIntent().getStringExtra(EXTRA_PHOTO);
        CloudType type = new GetCloudDetailsUseCase(CloudIdApp.get().clouds()).execute(code == null ? "" : code);
        ImageView hero = findViewById(R.id.imageHero);
        TextView title = findViewById(R.id.textTitle);
        TextView latin = findViewById(R.id.textLatin);
        TextView extract = findViewById(R.id.textExtract);
        if (type == null) {
            title.setText("Нет карточки");
            return;
        }
        title.setText(type.getName());
        latin.setText(type.getLatin());
        extract.setText(type.getExtract());
        Object source = photo != null && !photo.isEmpty() ? photo : type.getImageUrl();
        Glide.with(hero).load(source).centerCrop().into(hero);

        TextInputEditText editNote = findViewById(R.id.editNote);
        View noteBox = (View) editNote.getParent();
        while (noteBox != null && !(noteBox instanceof com.google.android.material.textfield.TextInputLayout)) {
            noteBox = noteBox.getParent() instanceof View ? (View) noteBox.getParent() : null;
        }
        View buttonSave = findViewById(R.id.buttonSave);
        View cardGuest = findViewById(R.id.cardGuest);
        TextView notice = findViewById(R.id.textNotice);
        boolean guest = CloudIdApp.get().auth().getProfile().isGuest();
        if (guest) {
            if (noteBox != null) {
                noteBox.setVisibility(View.GONE);
            }
            buttonSave.setVisibility(View.GONE);
            cardGuest.setVisibility(View.VISIBLE);
            notice.setText("Атлас и заметки доступны после входа. Гость смотрит небо и каталог.");
            findViewById(R.id.buttonOpenAuth).setOnClickListener(v -> {
                Intent intent = new Intent(this, AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
            ScreenRise.play(findViewById(R.id.detailsRoot));
            return;
        }
        String photoUri = photo == null ? "" : photo;
        buttonSave.setOnClickListener(v -> {
            String note = editNote.getText() == null ? "" : editNote.getText().toString().trim();
            Sighting sighting = new Sighting(0, type.getCode(), type.getName(), note,
                    LocalDate.now().toString(), photoUri);
            boolean saved = new SaveSightingUseCase(CloudIdApp.get().atlas(), CloudIdApp.get().auth()).execute(sighting);
            if (saved) {
                buttonSave.setEnabled(false);
                ((com.google.android.material.button.MaterialButton) buttonSave).setText("В атласе");
            } else {
                cardGuest.setVisibility(View.VISIBLE);
                notice.setText("Сохранить не удалось: нужен вход.");
            }
        });
        ScreenRise.play(findViewById(R.id.detailsRoot));
    }
}
