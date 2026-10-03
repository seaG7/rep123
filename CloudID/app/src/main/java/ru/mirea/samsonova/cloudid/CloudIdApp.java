package ru.mirea.samsonova.cloudid;

import android.app.Application;

import ru.mirea.samsonova.cloudid.data.firebase.FirebaseAuthDataSource;
import ru.mirea.samsonova.cloudid.data.network.NetworkApi;
import ru.mirea.samsonova.cloudid.data.repository.AtlasRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.AuthRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.CloudClassifierRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.CloudRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.storage.client.sharedprefs.SharedPrefClientStorage;
import ru.mirea.samsonova.cloudid.domain.repository.AtlasRepository;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;
import ru.mirea.samsonova.cloudid.domain.repository.CloudClassifierRepository;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

/** Сборка репозиториев. Presentation вызывает только use-case. */
public class CloudIdApp extends Application {
    private static CloudIdApp instance;
    private AuthRepository authRepository;
    private CloudRepository cloudRepository;
    private AtlasRepository atlasRepository;
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

    public CloudClassifierRepository classifier() {
        return classifierRepository;
    }
}
