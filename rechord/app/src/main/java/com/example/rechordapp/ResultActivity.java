package com.example.rechordapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.FillEventHistory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ResultActivity extends AppCompatActivity {

    private ImageView success_image;
    private TextView success_text;
    private Button back_button;
    private Button history_button;
    private Button listen;
    private File wav_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        this.success_image = findViewById(R.id.success_image);
        this.success_text = findViewById(R.id.success_text);
        this.back_button = findViewById(R.id.back_button);
        this.history_button = findViewById(R.id.history_button);
        this.listen = findViewById(R.id.listen);


        history_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startHistory();
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listenToRecording();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toMainMenu();
            }
        });
        // Get the Intent that started this activity and extract the string

        Intent intent = getIntent();
        String message = intent.getStringExtra("success");
        wav_file = (File)intent.getSerializableExtra("wav");

        String[] message_dissect = message.split(":");
        String score_str = message_dissect[1].replaceAll("[^\\d.]", "");
        float score = Float.parseFloat(score_str);


        if (score == 1.0){
            this.success_image.setImageResource(R.drawable.check_mark);
            this.success_text.setText("Bravo ! Vous avez réussi cet accord avec une probabilité de 100%.");
        } else {
            this.success_image.setImageResource(R.drawable.cross);
            this.success_text.setText("Aïe ! Vous avez raté cet accord avec une probabilité de 100%.");
        }
        //this.success_text.setText(message);
    }

    public void listenToRecording(){
        MediaPlayer mp = MediaPlayer.create(this, Uri.fromFile(wav_file));
        mp.start();
    }

    public void startHistory(){
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }

    public void toMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}