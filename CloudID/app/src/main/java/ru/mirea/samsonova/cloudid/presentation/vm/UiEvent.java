package ru.mirea.samsonova.cloudid.presentation.vm;

/**
 * Одноразовое событие для LiveData: поворот экрана не повторяет переход.
 */
public class UiEvent<T> {
    private final T value;
    private boolean handled;

    public UiEvent(T value) {
        this.value = value;
    }

    public T take() {
        if (handled) {
            return null;
        }
        handled = true;
        return value;
    }
}
