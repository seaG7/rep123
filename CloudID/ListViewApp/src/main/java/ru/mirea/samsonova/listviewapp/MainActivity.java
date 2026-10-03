package ru.mirea.samsonova.listviewapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Book> books = Books.planned();
        TextView count = findViewById(R.id.textCount);
        count.setText(books.size() + " книг: автор и название");
        ListView list = findViewById(R.id.listBooks);
        list.setAdapter(new BookAdapter(getLayoutInflater(), books));
    }
}
