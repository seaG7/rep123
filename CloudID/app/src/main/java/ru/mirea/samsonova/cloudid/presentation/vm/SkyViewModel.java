package ru.mirea.samsonova.cloudid.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.samsonova.cloudid.domain.ClassifyCloudUseCase;
import ru.mirea.samsonova.cloudid.domain.GetCloudDetailsUseCase;
import ru.mirea.samsonova.cloudid.domain.models.Classification;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;

public class SkyViewModel extends ViewModel {
    public static class Result {
        public final boolean busy;
        public final boolean visible;
        public final String code;
        public final String title;
        public final String latin;
        public final String score;
        public final String rest;

        public Result(boolean busy, boolean visible, String code, String title, String latin,
                      String score, String rest) {
            this.busy = busy;
            this.visible = visible;
            this.code = code;
            this.title = title;
            this.latin = latin;
            this.score = score;
            this.rest = rest;
        }

        static Result idle() {
            return new Result(false, false, "", "", "", "", "");
        }
    }

    private final ClassifyCloudUseCase classifyUseCase;
    private final GetCloudDetailsUseCase detailsUseCase;
    private final MutableLiveData<Result> result = new MutableLiveData<>(Result.idle());
    private final MutableLiveData<String> photoUri = new MutableLiveData<>("");
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean cleared;

    public SkyViewModel(ClassifyCloudUseCase classifyUseCase, GetCloudDetailsUseCase detailsUseCase) {
        this.classifyUseCase = classifyUseCase;
        this.detailsUseCase = detailsUseCase;
        Log.d(tag(), "created");
    }

    public LiveData<Result> result() {
        return result;
    }

    public LiveData<String> photoUri() {
        return photoUri;
    }

    public void rememberPhoto(String uri) {
        photoUri.setValue(uri == null ? "" : uri);
    }

    public void classify(float[] pixels) {
        Result current = result.getValue();
        if (current != null && current.busy) {
            return;
        }
        result.setValue(new Result(true, current != null && current.visible,
                current == null ? "" : current.code,
                current == null ? "" : current.title,
                current == null ? "" : current.latin,
                current == null ? "" : current.score,
                current == null ? "" : current.rest));
        executor.execute(() -> {
            List<Classification> top = classifyUseCase.execute(pixels);
            String code = top.isEmpty() ? "Cu" : top.get(0).getCode();
            CloudType type = detailsUseCase.execute(code);
            if (cleared) {
                return;
            }
            result.postValue(new Result(false, true, code,
                    type == null ? code : type.getName(),
                    type == null ? "" : type.getLatin(),
                    scoreOf(top),
                    restOf(top)));
        });
    }

    private static String scoreOf(List<Classification> top) {
        if (top.isEmpty()) {
            return "";
        }
        return Math.round(top.get(0).getConfidence() * 100f) + "%";
    }

    private static String restOf(List<Classification> top) {
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
        return builder.length() == 0 ? "" : "ещё  " + builder;
    }

    @Override
    protected void onCleared() {
        cleared = true;
        executor.shutdownNow();
        Log.d(tag(), "cleared");
        super.onCleared();
    }

    private static String tag() {
        return SkyViewModel.class.getSimpleName();
    }
}
