package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;

/** Зарегистрироваться */
public class RegisterUseCase {
    private AuthRepository authRepository;

    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public boolean execute(String login, String password) {
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        return authRepository.register(login, password);
    }
}
