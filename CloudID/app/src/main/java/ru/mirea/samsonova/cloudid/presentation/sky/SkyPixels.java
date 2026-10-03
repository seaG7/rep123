package ru.mirea.samsonova.cloudid.presentation.sky;

import android.graphics.Bitmap;

/** Кадр CCSN: 224×224, NHWC, float32 / 255. */
public final class SkyPixels {
    public static final int SIZE = 224;

    private SkyPixels() {
    }

    public static float[] from(Bitmap source) {
        Bitmap scaled = source.getWidth() == SIZE && source.getHeight() == SIZE
                ? source
                : Bitmap.createScaledBitmap(source, SIZE, SIZE, true);
        int[] pixels = new int[SIZE * SIZE];
        scaled.getPixels(pixels, 0, SIZE, 0, 0, SIZE, SIZE);
        float[] tensor = new float[SIZE * SIZE * 3];
        int index = 0;
        for (int color : pixels) {
            tensor[index++] = ((color >> 16) & 0xFF) / 255f;
            tensor[index++] = ((color >> 8) & 0xFF) / 255f;
            tensor[index++] = (color & 0xFF) / 255f;
        }
        if (scaled != source) {
            scaled.recycle();
        }
        return tensor;
    }
}
