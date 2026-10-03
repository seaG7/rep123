package ru.mirea.samsonova.cloudid.presentation.sky;

import android.content.Intent;
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

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import ru.mirea.samsonova.cloudid.CloudIdApp;
import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.ClassifyCloudUseCase;
import ru.mirea.samsonova.cloudid.domain.GetCloudDetailsUseCase;
import ru.mirea.samsonova.cloudid.domain.models.Classification;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.presentation.ScreenRise;
import ru.mirea.samsonova.cloudid.presentation.details.DetailsActivity;

public class SkyFragment extends Fragment {
    private ImageView imagePreview;
    private Bitmap frame;
    private String photoUri = "";
    private String lastCode = "";
    private boolean busy;

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
        if (imagePreview.getDrawable() instanceof BitmapDrawable) {
            frame = ((BitmapDrawable) imagePreview.getDrawable()).getBitmap();
        }
        if (frame == null) {
            frame = BitmapFactory.decodeResource(getResources(), R.drawable.art_sky);
        }
        view.findViewById(R.id.buttonCamera).setOnClickListener(v -> camera.launch(null));
        view.findViewById(R.id.buttonGallery).setOnClickListener(v -> gallery.launch("image/*"));
        buttonIdentify.setOnClickListener(v -> identify(buttonIdentify, cardResult, view));
        cardResult.setOnClickListener(v -> {
            if (lastCode.isEmpty()) {
                return;
            }
            Intent intent = new Intent(requireContext(), DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_CODE, lastCode);
            intent.putExtra(DetailsActivity.EXTRA_PHOTO, photoUri);
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

    private void showFrame(Bitmap bitmap, String uri) {
        frame = bitmap;
        photoUri = uri == null ? "" : uri;
        imagePreview.setImageBitmap(bitmap);
    }

    private void identify(MaterialButton button, LinearLayout card, View view) {
        if (busy || frame == null || getActivity() == null) {
            return;
        }
        busy = true;
        button.setText("Считаем…");
        float[] pixels = SkyPixels.from(frame);
        CloudIdApp app = CloudIdApp.get();
        new Thread(() -> {
            List<Classification> top = new ClassifyCloudUseCase(app.classifier()).execute(pixels);
            String code = top.isEmpty() ? "Cu" : top.get(0).getCode();
            CloudType type = new GetCloudDetailsUseCase(app.clouds()).execute(code);
            if (getActivity() == null) {
                return;
            }
            requireActivity().runOnUiThread(() -> {
                busy = false;
                button.setText("Определить");
                bindResult(view, card, type, top, code);
            });
        }).start();
    }

    private void bindResult(View view, LinearLayout card, CloudType type, List<Classification> top, String code) {
        lastCode = code;
        card.setVisibility(View.VISIBLE);
        card.setAlpha(0f);
        card.setTranslationY(18f * card.getResources().getDisplayMetrics().density);
        card.animate().alpha(1f).translationY(0f).setDuration(420).start();
        TextView title = view.findViewById(R.id.textResultTitle);
        TextView meta = view.findViewById(R.id.textResultMeta);
        TextView score = view.findViewById(R.id.textResultScore);
        TextView rest = view.findViewById(R.id.textTop);
        title.setText(type == null ? code : type.getName());
        meta.setText(type == null ? "" : type.getLatin());
        if (!top.isEmpty()) {
            score.setText(String.format(Locale.getDefault(), "%.0f%%", top.get(0).getConfidence() * 100f));
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < top.size(); i++) {
            if (builder.length() > 0) {
                builder.append("  ·  ");
            }
            Classification item = top.get(i);
            builder.append(item.getName())
                    .append(" ")
                    .append(Math.round(item.getConfidence() * 100f))
                    .append("%");
        }
        rest.setText(builder.length() == 0 ? "" : "ещё  " + builder);
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
