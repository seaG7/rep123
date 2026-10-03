package ru.mirea.samsonova.cloudid.presentation.vm;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.samsonova.cloudid.CloudIdApp;
import ru.mirea.samsonova.cloudid.domain.ClassifyCloudUseCase;
import ru.mirea.samsonova.cloudid.domain.GetCloudCatalogUseCase;
import ru.mirea.samsonova.cloudid.domain.GetCloudDetailsUseCase;
import ru.mirea.samsonova.cloudid.domain.GetMyAtlasUseCase;
import ru.mirea.samsonova.cloudid.domain.GetProfileUseCase;
import ru.mirea.samsonova.cloudid.domain.LoginUseCase;
import ru.mirea.samsonova.cloudid.domain.LogoutUseCase;
import ru.mirea.samsonova.cloudid.domain.RegisterUseCase;
import ru.mirea.samsonova.cloudid.domain.SaveSightingUseCase;

/**
 * Собирает ViewModel и её use-case. Сама модель не видит Context и вёрстку.
 * Репозитории берутся из {@link CloudIdApp}, чтобы не открывать вторую базу Room.
 */
public class CloudViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        CloudIdApp app = CloudIdApp.get();
        if (modelClass == AuthViewModel.class) {
            return modelClass.cast(new AuthViewModel(
                    new LoginUseCase(app.auth()),
                    new RegisterUseCase(app.auth()),
                    new GetProfileUseCase(app.auth()),
                    app.auth()));
        }
        if (modelClass == SkyViewModel.class) {
            return modelClass.cast(new SkyViewModel(
                    new ClassifyCloudUseCase(app.classifier()),
                    new GetCloudDetailsUseCase(app.clouds())));
        }
        if (modelClass == HomeViewModel.class) {
            return modelClass.cast(new HomeViewModel(
                    new GetCloudCatalogUseCase(app.clouds()),
                    new GetProfileUseCase(app.auth()),
                    new GetMyAtlasUseCase(app.atlas(), app.auth()),
                    new LogoutUseCase(app.auth()),
                    app.observeSightings()));
        }
        if (modelClass == DetailsViewModel.class) {
            return modelClass.cast(new DetailsViewModel(
                    new GetCloudDetailsUseCase(app.clouds()),
                    new SaveSightingUseCase(app.atlas(), app.auth()),
                    new GetProfileUseCase(app.auth())));
        }
        Log.e("CloudViewModelFactory", "Нет конструктора для " + modelClass.getName());
        throw new IllegalArgumentException(modelClass.getName());
    }
}
