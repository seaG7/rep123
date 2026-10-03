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

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.auth.AuthActivity;
import ru.mirea.samsonova.cloudid.presentation.vm.CloudViewModelFactory;
import ru.mirea.samsonova.cloudid.presentation.vm.DetailsViewModel;

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

        ImageView hero = findViewById(R.id.imageHero);
        TextView title = findViewById(R.id.textTitle);
        TextView latin = findViewById(R.id.textLatin);
        TextView extract = findViewById(R.id.textExtract);
        TextInputEditText editNote = findViewById(R.id.editNote);
        View noteBox = (View) editNote.getParent();
        while (noteBox != null && !(noteBox instanceof com.google.android.material.textfield.TextInputLayout)) {
            noteBox = noteBox.getParent() instanceof View ? (View) noteBox.getParent() : null;
        }
        View noteField = noteBox;
        View buttonSave = findViewById(R.id.buttonSave);
        View cardGuest = findViewById(R.id.cardGuest);
        TextView notice = findViewById(R.id.textNotice);
        DetailsViewModel viewModel = new ViewModelProvider(this, new CloudViewModelFactory())
                .get(DetailsViewModel.class);
        buttonSave.setOnClickListener(v -> {
            String note = editNote.getText() == null ? "" : editNote.getText().toString();
            viewModel.save(note);
        });
        findViewById(R.id.buttonOpenAuth).setOnClickListener(v -> {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        viewModel.card().observe(this, card -> {
            if (card == null) {
                return;
            }
            title.setText(card.title);
            latin.setText(card.latin);
            extract.setText(card.extract);
            if (card.image != null && !card.image.isEmpty()) {
                Glide.with(hero).load(card.image).centerCrop().into(hero);
            }
            if (card.guest) {
                if (noteField != null) {
                    noteField.setVisibility(View.GONE);
                }
                buttonSave.setVisibility(View.GONE);
                cardGuest.setVisibility(View.VISIBLE);
                notice.setText("Атлас и заметки доступны после входа. Гость смотрит небо и каталог.");
                return;
            }
            if (noteField != null) {
                noteField.setVisibility(View.VISIBLE);
            }
            buttonSave.setVisibility(View.VISIBLE);
            if (card.saved) {
                buttonSave.setEnabled(false);
                ((com.google.android.material.button.MaterialButton) buttonSave).setText("В атласе");
            }
            if (card.saveError != null) {
                cardGuest.setVisibility(View.VISIBLE);
                notice.setText(card.saveError);
            }
        });
        viewModel.open(getIntent().getStringExtra(EXTRA_CODE), getIntent().getStringExtra(EXTRA_PHOTO));
        ScreenRise.play(findViewById(R.id.detailsRoot));
    }
}
