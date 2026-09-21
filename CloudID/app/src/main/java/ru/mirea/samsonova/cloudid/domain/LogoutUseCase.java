package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;

/** Выйти */
public class LogoutUseCase {
    private AuthRepository authRepository;

    public LogoutUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute() {
        authRepository.logout();
    }
}
