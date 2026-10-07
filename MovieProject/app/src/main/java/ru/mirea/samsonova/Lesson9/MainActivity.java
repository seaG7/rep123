package ru.mirea.samsonova.Lesson9;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.samsonova.Lesson9.domain.models.Movie;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelFactory(this))
                .get(MainViewModel.class);
        mainViewModel.getFavoriteMovie().observe(this, textView::setText);
        findViewById(R.id.buttonSaveMovie).setOnClickListener(view ->
                mainViewModel.setText(new Movie(2, text.getText().toString())));
        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> mainViewModel.getText());
    }
}
