package ru.mirea.samsonova.cloudid.domain.repository;

import ru.mirea.samsonova.cloudid.domain.models.User;

public interface AuthRepository {
    boolean login(String login, String password);

    boolean register(String login, String password);

    User getProfile();

    void logout();
}
