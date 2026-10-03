package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.repository.AuthCallback;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;

/** Зарегистрироваться */
public class RegisterUseCase {
    private AuthRepository authRepository;

    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthCallback callback) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            callback.onError("Укажите почту");
            return;
        }
        if (password == null || password.length() < 6) {
            callback.onError("Пароль от 6 символов");
            return;
        }
        authRepository.register(email.trim(), password, callback);
    }
}
