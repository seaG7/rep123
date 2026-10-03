package ru.mirea.samsonova.cloudid;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ru.mirea.samsonova.cloudid.data.firebase.FirebaseAuthDataSource;
import ru.mirea.samsonova.cloudid.data.network.NetworkApi;
import ru.mirea.samsonova.cloudid.data.repository.AtlasRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.AuthRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.CloudClassifierRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.CloudRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.storage.client.sharedprefs.SharedPrefClientStorage;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.domain.repository.AtlasRepository;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;
import ru.mirea.samsonova.cloudid.domain.repository.CloudClassifierRepository;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

/** Сборка репозиториев. Экраны берут их только через ViewModel и use-case. */
public class CloudIdApp extends Application {
    private static CloudIdApp instance;
    private AuthRepository authRepository;
    private CloudRepository cloudRepository;
    private AtlasRepositoryImpl atlasRepository;
    private CloudClassifierRepository classifierRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        authRepository = new AuthRepositoryImpl(new FirebaseAuthDataSource(), new SharedPrefClientStorage(this));
        cloudRepository = new CloudRepositoryImpl(new NetworkApi());
        atlasRepository = new AtlasRepositoryImpl(this);
        classifierRepository = new CloudClassifierRepositoryImpl(this);
    }

    public static CloudIdApp get() {
        return instance;
    }

    public AuthRepository auth() {
        return authRepository;
    }

    public CloudRepository clouds() {
        return cloudRepository;
    }

    public AtlasRepository atlas() {
        return atlasRepository;
    }

    public LiveData<List<Sighting>> observeSightings() {
        return atlasRepository.observe();
    }

    public CloudClassifierRepository classifier() {
        return classifierRepository;
    }
}
