package ru.mirea.samsonova.cloudid.data.repository;

import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;

import ru.mirea.samsonova.cloudid.data.firebase.FirebaseAuthDataSource;
import ru.mirea.samsonova.cloudid.data.storage.client.ClientStorage;
import ru.mirea.samsonova.cloudid.data.storage.client.models.ClientRecord;
import ru.mirea.samsonova.cloudid.domain.models.User;
import ru.mirea.samsonova.cloudid.domain.repository.AuthCallback;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuthDataSource firebaseAuthDataSource;
    private final ClientStorage clientStorage;

    public AuthRepositoryImpl(FirebaseAuthDataSource firebaseAuthDataSource, ClientStorage clientStorage) {
        this.firebaseAuthDataSource = firebaseAuthDataSource;
        this.clientStorage = clientStorage;
        FirebaseUser remote = firebaseAuthDataSource.currentUser();
        if (remote != null && remote.getEmail() != null) {
            clientStorage.save(new ClientRecord(1, remote.getEmail(), false, String.valueOf(LocalDate.now())));
        }
    }

    @Override
    public void login(String email, String password, AuthCallback callback) {
        firebaseAuthDataSource.signIn(email, password, new AuthCallback() {
            @Override
            public void onSuccess() {
                persist(email, false);
                callback.onSuccess();
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    @Override
    public void register(String email, String password, AuthCallback callback) {
        firebaseAuthDataSource.signUp(email, password, new AuthCallback() {
            @Override
            public void onSuccess() {
                persist(email, false);
                callback.onSuccess();
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    @Override
    public void continueAsGuest() {
        clientStorage.save(new ClientRecord(0, "guest", true, String.valueOf(LocalDate.now())));
    }

    @Override
    public User getProfile() {
        return mapToDomain(clientStorage.get());
    }

    @Override
    public void logout() {
        firebaseAuthDataSource.signOut();
        clientStorage.save(new ClientRecord(0, "guest", true, String.valueOf(LocalDate.now())));
    }

    private void persist(String email, boolean guest) {
        clientStorage.save(new ClientRecord(1, email, guest, String.valueOf(LocalDate.now())));
    }

    private User mapToDomain(ClientRecord record) {
        return new User(record.getId(), record.getEmail(), record.isGuest(), record.getLocalDate());
    }
}
