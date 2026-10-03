package ru.mirea.samsonova.cloudid.presentation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.ScrollView;

/**
 * При открытии экрана блоки коротко поднимаются на свои места.
 */
public final class ScreenRise {
    private ScreenRise() {
    }

    public static void play(View root) {
        ViewGroup group = contentGroup(root);
        if (group == null) {
            return;
        }
        float dy = 18f * root.getResources().getDisplayMetrics().density;
        PathInterpolator ease = new PathInterpolator(0.16f, 0.84f, 0.32f, 1f);
        int shown = 0;
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child.getVisibility() != View.VISIBLE) {
                continue;
            }
            child.animate().cancel();
            child.setAlpha(0f);
            child.setTranslationY(dy);
            child.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(30L + shown * 55L)
                    .setDuration(460)
                    .setInterpolator(ease)
                    .start();
            shown++;
        }
    }

    private static ViewGroup contentGroup(View root) {
        View current = root;
        if (current instanceof ViewGroup && ((ViewGroup) current).getChildCount() > 0
                && !(current instanceof ScrollView)) {
            View first = ((ViewGroup) current).getChildAt(0);
            if (first instanceof ScrollView) {
                current = first;
            }
        }
        if (current instanceof ScrollView && ((ScrollView) current).getChildCount() > 0) {
            current = ((ScrollView) current).getChildAt(0);
        }
        if (current instanceof ViewGroup) {
            return (ViewGroup) current;
        }
        return null;
    }
}
