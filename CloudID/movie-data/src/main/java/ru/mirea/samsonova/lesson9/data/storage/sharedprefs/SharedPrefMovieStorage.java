package ru.mirea.samsonova.lesson9.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;

import ru.mirea.samsonova.lesson9.data.storage.MovieStorage;
import ru.mirea.samsonova.lesson9.data.storage.models.Movie;

public class SharedPrefMovieStorage implements MovieStorage {
    private static final String SHARED_PREFS_NAME = "shared_prefs_name";
    private static final String KEY = "movie_name";
    private static final String DATE_KEY = "movie_date";
    private static final String ID_KEY = "movie_id";
    private final SharedPreferences sharedPreferences;

    public SharedPrefMovieStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public Movie get() {
        String movieName = sharedPreferences.getString(KEY, "unknown");
        String movieDate = sharedPreferences.getString(DATE_KEY, LocalDate.now().toString());
        int movieId = sharedPreferences.getInt(ID_KEY, -1);
        return new Movie(movieId, movieName, movieDate);
    }

    @Override
    public boolean save(Movie movie) {
        sharedPreferences.edit()
                .putString(KEY, movie.getName())
                .putString(DATE_KEY, movie.getLocalDate())
                .putInt(ID_KEY, movie.getId())
                .commit();
        return true;
    }
}
