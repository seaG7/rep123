package ru.mirea.samsonova.Lesson9.data.repository;

import java.time.LocalDate;

import ru.mirea.samsonova.Lesson9.data.storage.MovieStorage;
import ru.mirea.samsonova.Lesson9.data.storage.models.Movie;
import ru.mirea.samsonova.Lesson9.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private final MovieStorage movieStorage;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(ru.mirea.samsonova.Lesson9.domain.models.Movie movie) {
        return movieStorage.save(mapToStorage(movie));
    }

    @Override
    public ru.mirea.samsonova.Lesson9.domain.models.Movie getMovie() {
        return mapToDomain(movieStorage.get());
    }

    private Movie mapToStorage(ru.mirea.samsonova.Lesson9.domain.models.Movie movie) {
        return new Movie(movie.getId(), movie.getName(), LocalDate.now().toString());
    }

    private ru.mirea.samsonova.Lesson9.domain.models.Movie mapToDomain(Movie movie) {
        return new ru.mirea.samsonova.Lesson9.domain.models.Movie(movie.getId(), movie.getName());
    }
}
