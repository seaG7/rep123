package ru.mirea.samsonova.cloudid.presentation.sky;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.home.HomeActivity;
import ru.mirea.samsonova.cloudid.presentation.vm.CloudViewModelFactory;
import ru.mirea.samsonova.cloudid.presentation.vm.SkyViewModel;

public class SkyFragment extends Fragment {
    private ImageView imagePreview;
    private Bitmap frame;
    private String photoUri = "";
    private String lastCode = "";
    private SkyViewModel viewModel;

    private final ActivityResultLauncher<Void> camera =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    showFrame(bitmap, writeJpeg(bitmap));
                }
            });

    private final ActivityResultLauncher<String> gallery =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri == null || getContext() == null) {
                    return;
                }
                try {
                    Bitmap bitmap = decode(uri);
                    if (bitmap != null) {
                        showFrame(bitmap, writeJpeg(bitmap));
                    }
                } catch (IOException ignored) {
                    imagePreview.setImageURI(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sky, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imagePreview = view.findViewById(R.id.imagePreview);
        MaterialButton buttonIdentify = view.findViewById(R.id.buttonIdentify);
        LinearLayout cardResult = view.findViewById(R.id.cardResult);
        viewModel = new ViewModelProvider(this, new CloudViewModelFactory()).get(SkyViewModel.class);
        if (imagePreview.getDrawable() instanceof BitmapDrawable) {
            frame = ((BitmapDrawable) imagePreview.getDrawable()).getBitmap();
        }
        if (frame == null) {
            frame = BitmapFactory.decodeResource(getResources(), R.drawable.art_sky);
        }
        view.findViewById(R.id.buttonCamera).setOnClickListener(v -> camera.launch(null));
        view.findViewById(R.id.buttonGallery).setOnClickListener(v -> gallery.launch("image/*"));
        buttonIdentify.setOnClickListener(v -> identify(buttonIdentify));
        viewModel.photoUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri == null || uri.isEmpty()) {
                return;
            }
            photoUri = uri;
            try {
                frame = decode(Uri.parse(uri));
                imagePreview.setImageBitmap(frame);
            } catch (IOException ignored) {
                frame = BitmapFactory.decodeResource(getResources(), R.drawable.art_sky);
            }
        });
        viewModel.result().observe(getViewLifecycleOwner(), result -> applyResult(buttonIdentify, cardResult, view, result));
        cardResult.setOnClickListener(v -> {
            if (lastCode.isEmpty()) {
                return;
            }
            ((HomeActivity) requireActivity()).openDetails(lastCode, photoUri);
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

    private void showFrame(Bitmap bitmap, String uri) {
        frame = bitmap;
        photoUri = uri == null ? "" : uri;
        imagePreview.setImageBitmap(bitmap);
        if (viewModel != null) {
            viewModel.rememberPhoto(photoUri);
        }
    }

    private void identify(MaterialButton button) {
        if (frame == null) {
            return;
        }
        button.setText("Считаем…");
        viewModel.classify(SkyPixels.from(frame));
    }

    private void applyResult(MaterialButton button, LinearLayout card, View view, SkyViewModel.Result result) {
        if (result == null) {
            return;
        }
        button.setEnabled(!result.busy);
        button.setText(result.busy ? "Считаем…" : "Определить");
        if (!result.visible) {
            return;
        }
        boolean firstShow = card.getVisibility() != View.VISIBLE;
        lastCode = result.code;
        card.setVisibility(View.VISIBLE);
        if (firstShow) {
            card.setAlpha(0f);
            card.setTranslationY(18f * card.getResources().getDisplayMetrics().density);
            card.animate().alpha(1f).translationY(0f).setDuration(420).start();
        }
        ((TextView) view.findViewById(R.id.textResultTitle)).setText(result.title);
        ((TextView) view.findViewById(R.id.textResultMeta)).setText(result.latin);
        ((TextView) view.findViewById(R.id.textResultScore)).setText(result.score);
        ((TextView) view.findViewById(R.id.textTop)).setText(result.rest);
    }

    private Bitmap decode(Uri uri) throws IOException {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        try (InputStream input = requireContext().getContentResolver().openInputStream(uri)) {
            BitmapFactory.decodeStream(input, null, bounds);
        }
        int sample = 1;
        int largest = Math.max(bounds.outWidth, bounds.outHeight);
        while (largest / sample > 1280) {
            sample *= 2;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sample;
        try (InputStream input = requireContext().getContentResolver().openInputStream(uri)) {
            return BitmapFactory.decodeStream(input, null, options);
        }
    }

    private String writeJpeg(Bitmap bitmap) {
        if (getContext() == null) {
            return "";
        }
        File file = new File(requireContext().getCacheDir(), "frame.jpg");
        try (FileOutputStream stream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            return Uri.fromFile(file).toString();
        } catch (IOException exception) {
            return "";
        }
    }
}
