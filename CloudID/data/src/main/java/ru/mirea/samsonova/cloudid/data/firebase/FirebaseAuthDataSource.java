package ru.mirea.samsonova.cloudid.data.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.samsonova.cloudid.domain.repository.AuthCallback;

public class FirebaseAuthDataSource {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void signIn(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(messageOf(task.getException()));
                    }
                });
    }

    public void signUp(String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(messageOf(task.getException()));
                    }
                });
    }

    public FirebaseUser currentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    private String messageOf(Exception exception) {
        if (exception == null || exception.getMessage() == null) {
            return "Не удалось войти";
        }
        return exception.getMessage();
    }
}
