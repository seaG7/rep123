package ru.mirea.samsonova.cloudid.presentation.auth;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ru.mirea.samsonova.cloudid.CloudIdApp;
import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.domain.LoginUseCase;
import ru.mirea.samsonova.cloudid.domain.RegisterUseCase;
import ru.mirea.samsonova.cloudid.domain.repository.AuthCallback;
import ru.mirea.samsonova.cloudid.presentation.home.HomeActivity;

/**
 * Один экран неба. Фон чуть плывёт и не сменяется.
 * «Дальше» поднимает плашку входа. «Создать аккаунт» растягивает ту же плашку,
 * углы остаются скруглёнными, небо над ней не уходит.
 */
public class AuthActivity extends AppCompatActivity {
    private View root;
    private View imageSky;
    private View intro;
    private LinearLayout sheet;
    private View handle;
    private View pageLogin;
    private View pageRegister;
    private TextView textError;
    private TextView textErrorRegister;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editEmailRegister;
    private TextInputEditText editPasswordRegister;
    private boolean sheetOpen;
    private boolean registerMode;
    private boolean dragging;
    private float dragStartY;
    private float dragStartTranslation;
    private int imeBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CloudIdApp.get().auth().getProfile().isGuest()) {
            openHome();
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        root = findViewById(R.id.root);
        imageSky = findViewById(R.id.imageSky);
        intro = findViewById(R.id.intro);
        sheet = findViewById(R.id.sheet);
        handle = findViewById(R.id.handle);
        pageLogin = findViewById(R.id.pageLogin);
        pageRegister = findViewById(R.id.pageRegister);
        textError = findViewById(R.id.textError);
        textErrorRegister = findViewById(R.id.textErrorRegister);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editEmailRegister = findViewById(R.id.editEmailRegister);
        editPasswordRegister = findViewById(R.id.editPasswordRegister);

        sheet.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        sheet.setClipToOutline(true);
        driftSky();
        placeSheetOffscreen();
        listenInsets();
        listenSwipe();
        listenSheetDrag();
        intro.setAlpha(0f);
        intro.setTranslationY(dp(18));
        intro.animate().alpha(1f).translationY(0f).setStartDelay(40).setDuration(560).setInterpolator(ease()).start();

        findViewById(R.id.buttonNext).setOnClickListener(v -> openSheet());
        findViewById(R.id.buttonLogin).setOnClickListener(v -> login());
        findViewById(R.id.buttonToRegister).setOnClickListener(v -> showRegister());
        findViewById(R.id.buttonBackLogin).setOnClickListener(v -> showLoginPage());
        findViewById(R.id.buttonDoRegister).setOnClickListener(v -> register());
        findViewById(R.id.buttonGuest).setOnClickListener(v -> {
            CloudIdApp.get().auth().continueAsGuest();
            openHome();
        });
    }

    private void driftSky() {
        imageSky.setScaleX(1.12f);
        imageSky.setScaleY(1.12f);
        imageSky.animate().cancel();
        android.animation.ObjectAnimator x = android.animation.ObjectAnimator.ofFloat(imageSky, View.TRANSLATION_X, 0f, -56f);
        android.animation.ObjectAnimator y = android.animation.ObjectAnimator.ofFloat(imageSky, View.TRANSLATION_Y, 0f, -32f);
        x.setDuration(16000);
        y.setDuration(21000);
        x.setRepeatCount(android.animation.ValueAnimator.INFINITE);
        y.setRepeatCount(android.animation.ValueAnimator.INFINITE);
        x.setRepeatMode(android.animation.ValueAnimator.REVERSE);
        y.setRepeatMode(android.animation.ValueAnimator.REVERSE);
        x.setInterpolator(new LinearInterpolator());
        y.setInterpolator(new LinearInterpolator());
        x.start();
        y.start();
    }

    private void placeSheetOffscreen() {
        sheet.setVisibility(View.INVISIBLE);
        root.post(() -> {
            resizeSheet(false);
            sheet.setVisibility(View.VISIBLE);
        });
    }

    private void listenInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            ViewGroup.MarginLayoutParams introParams = (ViewGroup.MarginLayoutParams) intro.getLayoutParams();
            introParams.bottomMargin = bars.bottom + dp(16);
            intro.setLayoutParams(introParams);
            sheet.setPadding(0, 0, 0, Math.max(bars.bottom, imeBottom));
            root.post(() -> resizeSheet(sheetOpen || imeBottom > 0));
            return insets;
        });
    }

    private int desiredSheetHeight() {
        if (imeBottom > 0) {
            return Math.max(dp(280), root.getHeight() - dp(48));
        }
        float fraction = registerMode ? 0.74f : 0.52f;
        return Math.round(root.getHeight() * fraction);
    }

    private void resizeSheet(boolean reveal) {
        if (root.getHeight() == 0) {
            return;
        }
        int height = desiredSheetHeight();
        if (reveal && sheetOpen) {
            animateHeight(height);
        } else {
            setSheetHeight(height);
        }
        if (reveal) {
            sheet.setTranslationY(0f);
            intro.setAlpha(0f);
            sheetOpen = true;
        } else if (!dragging && !sheetOpen) {
            sheet.setTranslationY(height);
        }
    }

    private void setSheetHeight(int height) {
        if (sheet.getLayoutParams().height != height) {
            sheet.getLayoutParams().height = height;
            sheet.setLayoutParams(sheet.getLayoutParams());
        }
    }

    private void animateHeight(int to) {
        int from = sheet.getLayoutParams().height;
        if (from <= 0) {
            from = sheet.getHeight();
        }
        if (from == to) {
            return;
        }
        ValueAnimator anim = ValueAnimator.ofInt(from, to);
        anim.setDuration(480);
        anim.setInterpolator(ease());
        anim.addUpdateListener(value -> setSheetHeight((int) value.getAnimatedValue()));
        anim.start();
    }

    private void listenSwipe() {
        GestureDetector detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent start, @NonNull MotionEvent end, float velocityX, float velocityY) {
                if (start == null || sheetOpen) {
                    return false;
                }
                if (start.getY() - end.getY() > 72f) {
                    openSheet();
                    return true;
                }
                return false;
            }
        });
        imageSky.setOnTouchListener((view, event) -> {
            detector.onTouchEvent(event);
            return true;
        });
    }

    private void listenSheetDrag() {
        handle.setOnTouchListener((view, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dragging = true;
                    dragStartY = event.getRawY();
                    dragStartTranslation = sheet.getTranslationY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float next = dragStartTranslation + (event.getRawY() - dragStartY);
                    next = Math.max(0f, Math.min(sheet.getHeight(), next));
                    sheet.setTranslationY(next);
                    intro.setAlpha(sheet.getHeight() == 0 ? 1f : next / sheet.getHeight());
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    dragging = false;
                    if (sheet.getTranslationY() < sheet.getHeight() * 0.45f) {
                        openSheet();
                    } else {
                        closeSheet();
                    }
                    return true;
                default:
                    return false;
            }
        });
    }

    private void openSheet() {
        sheetOpen = true;
        sheet.animate().translationY(0f).setDuration(480).setInterpolator(ease()).start();
        intro.animate().alpha(0f).translationY(dp(12)).setDuration(280).start();
    }

    private void closeSheet() {
        sheetOpen = false;
        registerMode = false;
        sheet.animate().translationY(sheet.getHeight()).setDuration(380).setInterpolator(ease()).withEndAction(() -> {
            pageRegister.setVisibility(View.GONE);
            pageRegister.setAlpha(1f);
            pageLogin.setVisibility(View.VISIBLE);
            pageLogin.setAlpha(1f);
            pageLogin.setTranslationY(0f);
            int height = desiredSheetHeight();
            setSheetHeight(height);
            sheet.setTranslationY(height);
        }).start();
        intro.animate().alpha(1f).translationY(0f).setDuration(320).start();
    }

    private void showRegister() {
        registerMode = true;
        textErrorRegister.setVisibility(View.GONE);
        pageLogin.animate().cancel();
        pageRegister.animate().cancel();
        pageRegister.setVisibility(View.VISIBLE);
        pageRegister.setAlpha(0f);
        pageRegister.setTranslationY(dp(16));
        pageRegister.animate().alpha(1f).translationY(0f).setDuration(420).setInterpolator(ease()).start();
        pageLogin.animate().alpha(0f).setDuration(180).withEndAction(() -> {
            if (registerMode) {
                pageLogin.setVisibility(View.GONE);
            }
        }).start();
        openSheet();
        animateHeight(desiredSheetHeight());
    }

    private void showLoginPage() {
        registerMode = false;
        pageRegister.animate().cancel();
        pageLogin.animate().cancel();
        pageLogin.setVisibility(View.VISIBLE);
        pageLogin.setAlpha(0f);
        pageLogin.setTranslationY(dp(12));
        pageLogin.animate().alpha(1f).translationY(0f).setDuration(380).setInterpolator(ease()).start();
        pageRegister.animate().alpha(0f).setDuration(160).withEndAction(() -> {
            if (!registerMode) {
                pageRegister.setVisibility(View.GONE);
            }
        }).start();
        animateHeight(desiredSheetHeight());
    }

    private void login() {
        textError.setVisibility(View.GONE);
        MaterialButton button = findViewById(R.id.buttonLogin);
        button.setEnabled(false);
        new LoginUseCase(CloudIdApp.get().auth()).execute(textOf(editEmail), textOf(editPassword), new AuthCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> openHome());
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    button.setEnabled(true);
                    textError.setText(message);
                    textError.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void register() {
        textErrorRegister.setVisibility(View.GONE);
        MaterialButton button = findViewById(R.id.buttonDoRegister);
        button.setEnabled(false);
        new RegisterUseCase(CloudIdApp.get().auth()).execute(
                textOf(editEmailRegister), textOf(editPasswordRegister), new AuthCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> openHome());
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> {
                            button.setEnabled(true);
                            textErrorRegister.setText(message);
                            textErrorRegister.setVisibility(View.VISIBLE);
                        });
                    }
                });
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private static String textOf(TextInputEditText edit) {
        return edit.getText() == null ? "" : edit.getText().toString();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private static PathInterpolator ease() {
        return new PathInterpolator(0.16f, 0.84f, 0.32f, 1f);
    }
}
