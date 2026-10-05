package ru.mirea.samsonova.lesson9.data.storage;

import ru.mirea.samsonova.lesson9.data.storage.models.Movie;

public interface MovieStorage {
    Movie get();

    boolean save(Movie movie);
}
