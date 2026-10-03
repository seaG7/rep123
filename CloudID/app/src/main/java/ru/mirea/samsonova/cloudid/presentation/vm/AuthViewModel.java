package ru.mirea.samsonova.cloudid.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.samsonova.cloudid.domain.GetProfileUseCase;
import ru.mirea.samsonova.cloudid.domain.LoginUseCase;
import ru.mirea.samsonova.cloudid.domain.RegisterUseCase;
import ru.mirea.samsonova.cloudid.domain.repository.AuthCallback;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    public static class Form {
        public final boolean loading;
        public final boolean register;
        public final String error;

        public Form(boolean loading, boolean register, String error) {
            this.loading = loading;
            this.register = register;
            this.error = error;
        }
    }

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final AuthRepository authRepository;
    private final MutableLiveData<Form> form = new MutableLiveData<>(new Form(false, false, null));
    private final MutableLiveData<UiEvent<Boolean>> openHome = new MutableLiveData<>();
    private final boolean signedIn;

    public AuthViewModel(LoginUseCase loginUseCase, RegisterUseCase registerUseCase,
                         GetProfileUseCase profileUseCase, AuthRepository authRepository) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
        this.authRepository = authRepository;
        this.signedIn = !profileUseCase.execute().isGuest();
        Log.d(tag(), "created");
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public LiveData<Form> form() {
        return form;
    }

    public LiveData<UiEvent<Boolean>> openHome() {
        return openHome;
    }

    public void login(String email, String password) {
        form.setValue(new Form(true, false, null));
        loginUseCase.execute(email, password, callback(false));
    }

    public void register(String email, String password) {
        form.setValue(new Form(true, true, null));
        registerUseCase.execute(email, password, callback(true));
    }

    public void continueAsGuest() {
        authRepository.continueAsGuest();
        openHome.setValue(new UiEvent<>(true));
    }

    private AuthCallback callback(boolean register) {
        return new AuthCallback() {
            @Override
            public void onSuccess() {
                form.postValue(new Form(false, register, null));
                openHome.postValue(new UiEvent<>(true));
            }

            @Override
            public void onError(String message) {
                form.postValue(new Form(false, register, message));
            }
        };
    }

    @Override
    protected void onCleared() {
        Log.d(tag(), "cleared");
        super.onCleared();
    }

    private static String tag() {
        return AuthViewModel.class.getSimpleName();
    }
}
