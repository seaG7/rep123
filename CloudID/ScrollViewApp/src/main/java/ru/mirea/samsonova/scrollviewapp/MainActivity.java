package ru.mirea.samsonova.scrollviewapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {
    private static final int TERMS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout wrapper = findViewById(R.id.wrapper);
        LayoutInflater inflater = getLayoutInflater();
        for (int index = 0; index < TERMS; index++) {
            View row = inflater.inflate(R.layout.item, wrapper, false);
            TextView text = row.findViewById(R.id.textValue);
            BigInteger value = BigInteger.ONE.shiftLeft(index);
            text.setText("a" + (index + 1) + " = " + value.toString());
            wrapper.addView(row);
        }
    }
}
