package ru.mirea.samsonova.fragmentapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static final int STUDENT_NUMBER = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("my_number_student", STUDENT_NUMBER);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainer, TasksFragment.class, bundle)
                    .commit();
        }
    }
}
