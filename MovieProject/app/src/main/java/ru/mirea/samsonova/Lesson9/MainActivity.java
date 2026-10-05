package ru.mirea.samsonova.Lesson9;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.samsonova.Lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.samsonova.Lesson9.data.storage.MovieStorage;
import ru.mirea.samsonova.Lesson9.data.storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.samsonova.Lesson9.domain.models.Movie;
import ru.mirea.samsonova.Lesson9.domain.repository.MovieRepository;
import ru.mirea.samsonova.Lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.samsonova.Lesson9.domain.usecases.SaveMovieToFavoriteUseCase;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);
        MovieStorage movieStorage = new SharedPrefMovieStorage(this);
        MovieRepository movieRepository = new MovieRepositoryImpl(movieStorage);
        findViewById(R.id.buttonSaveMovie).setOnClickListener(view -> {
            boolean result = new SaveMovieToFavoriteUseCase(movieRepository)
                    .execute(new Movie(2, text.getText().toString()));
            textView.setText(String.format("Save result %s", result));
        });
        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> {
            Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
            textView.setText(String.format("Save result %s", movie.getName()));
        });
    }
}
