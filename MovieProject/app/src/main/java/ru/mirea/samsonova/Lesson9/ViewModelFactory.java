package ru.mirea.samsonova.Lesson9;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.samsonova.Lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.samsonova.Lesson9.data.storage.MovieStorage;
import ru.mirea.samsonova.Lesson9.data.storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.samsonova.Lesson9.domain.repository.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(context);
        MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);
        return modelClass.cast(new MainViewModel(movieRepository));
    }
}
