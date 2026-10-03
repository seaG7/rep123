package ru.mirea.samsonova.cloudid.domain.repository;

public interface AuthCallback {
    void onSuccess();

    void onError(String message);
}
