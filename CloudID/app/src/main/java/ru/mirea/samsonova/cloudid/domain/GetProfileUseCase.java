package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.models.User;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;

/** Просмотреть профиль */
public class GetProfileUseCase {
    private AuthRepository authRepository;

    public GetProfileUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public User execute() {
        return authRepository.getProfile();
    }
}
