package ru.mirea.samsonova.cloudid.presentation.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.auth.AuthActivity;
import ru.mirea.samsonova.cloudid.presentation.vm.CloudViewModelFactory;
import ru.mirea.samsonova.cloudid.presentation.vm.DetailsViewModel;

public class DetailsFragment extends Fragment {
    private static final String ARG_CODE = "code";
    private static final String ARG_PHOTO = "photo";

    public static DetailsFragment newInstance(String code, String photo) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CODE, code);
        args.putString(ARG_PHOTO, photo);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View back = view.findViewById(R.id.buttonBack);
        ViewCompat.setOnApplyWindowInsetsListener(back, (target, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            target.setTranslationY(bars.top);
            return insets;
        });
        back.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        ImageView hero = view.findViewById(R.id.imageHero);
        TextView title = view.findViewById(R.id.textTitle);
        TextView latin = view.findViewById(R.id.textLatin);
        TextView extract = view.findViewById(R.id.textExtract);
        TextInputEditText editNote = view.findViewById(R.id.editNote);
        View noteBox = (View) editNote.getParent();
        while (noteBox != null && !(noteBox instanceof com.google.android.material.textfield.TextInputLayout)) {
            noteBox = noteBox.getParent() instanceof View ? (View) noteBox.getParent() : null;
        }
        View noteField = noteBox;
        View buttonSave = view.findViewById(R.id.buttonSave);
        View cardGuest = view.findViewById(R.id.cardGuest);
        TextView notice = view.findViewById(R.id.textNotice);
        DetailsViewModel viewModel = new ViewModelProvider(this, new CloudViewModelFactory())
                .get(DetailsViewModel.class);
        buttonSave.setOnClickListener(v -> {
            String note = editNote.getText() == null ? "" : editNote.getText().toString();
            viewModel.save(note);
        });
        view.findViewById(R.id.buttonOpenAuth).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        viewModel.card().observe(getViewLifecycleOwner(), card -> {
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
        Bundle args = getArguments();
        String code = args == null ? null : args.getString(ARG_CODE);
        String photo = args == null ? null : args.getString(ARG_PHOTO);
        viewModel.open(code, photo);
        ScreenRise.play(view.findViewById(R.id.detailsRoot));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRemoving() && getActivity() instanceof ru.mirea.samsonova.cloudid.presentation.home.HomeActivity) {
            ((ru.mirea.samsonova.cloudid.presentation.home.HomeActivity) getActivity()).onDetailsClosed();
        }
    }
}
