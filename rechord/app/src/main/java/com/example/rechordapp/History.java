package com.example.rechordapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.example.rechordapp.ResultActivity.HISTORY_NAME;

public class History extends AppCompatActivity {

    private Button back_button;
    private Button reset_button;

    private HistoryViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.back_button = findViewById(R.id.back_button);
        this.reset_button = findViewById(R.id.reset_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toMainMenu();
            }
        });
        reset_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eraseHistory();
                readHistory();
            }
        });
        readHistory();

    }

    private void eraseHistory(){
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove(HISTORY_NAME).apply();
    }
    private void readHistory() {
        try {
            JSONArray historyJson = new JSONArray(PreferenceManager.
                    getDefaultSharedPreferences(this).getString(HISTORY_NAME, "[]"));
            ArrayList<String> historyList = new ArrayList<>(historyJson.length());
            for (int i = historyJson.length() - 1 ; i >= 0; i--) {
                historyList.add(historyJson.getString(i));
            }
            if (historyList.isEmpty()) {
                historyList.add("Votre historique est vide.");
            }
            // set up the RecyclerView
            RecyclerView recyclerView = findViewById(R.id.rvHistory);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new HistoryViewAdapter(this, historyList);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void toMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}