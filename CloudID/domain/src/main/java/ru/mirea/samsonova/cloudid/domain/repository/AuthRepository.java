package ru.mirea.samsonova.cloudid.domain.repository;

import ru.mirea.samsonova.cloudid.domain.models.User;

public interface AuthRepository {
    void login(String email, String password, AuthCallback callback);

    void register(String email, String password, AuthCallback callback);

    void continueAsGuest();

    User getProfile();

    void logout();
}
