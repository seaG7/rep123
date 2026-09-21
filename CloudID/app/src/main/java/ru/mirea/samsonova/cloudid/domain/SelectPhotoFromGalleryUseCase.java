package ru.mirea.samsonova.cloudid.domain;

/** Выбрать фото из галереи */
public class SelectPhotoFromGalleryUseCase {
    public String execute() {
        return "content://media/external/images/media/test_sky.jpg";
    }
}
