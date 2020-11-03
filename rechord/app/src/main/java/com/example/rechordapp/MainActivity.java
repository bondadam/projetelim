package com.example.rechordapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countdown_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        TextView tv1 = findViewById(R.id.countdownText);
        tv1.setText(spinner.getSelectedItem().toString());

        FloatingActionButton record = findViewById(R.id.playButton);
        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new CountDownTimer(Integer.parseInt(spinner.getSelectedItem().toString())*1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tv1.setText(""+millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        tv1.setText("Jouez");
                        tv1.setTextColor(Color.RED);
                        tv1.setTextSize(60);
                    }
                }.start();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Spinner spinner = findViewById(R.id.spinner);
        TextView tv1 = findViewById(R.id.countdownText);
        tv1.setText(spinner.getSelectedItem().toString());
        tv1.setTextColor(Color.WHITE);
        tv1.setTextSize(120);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}