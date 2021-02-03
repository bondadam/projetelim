package com.example.rechordapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private ImageView success_image;
    private TextView success_text;
    private Button back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        this.success_image = findViewById(R.id.success_image);
        this.success_text = findViewById(R.id.success_text);
        this.back_button = findViewById(R.id.back_button);


        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toMainMenu();
            }
        });
        // Get the Intent that started this activity and extract the string

        Intent intent = getIntent();
        String message = intent.getStringExtra("successs");
        if (message == "{\"correct\":1.0}\n"){
            this.success_image.setImageResource(R.drawable.check_mark);
            this.success_text.setText("Bravo ! Vous avez réussi cet accord avec une probabilité de 100%.");
        } else {
            this.success_image.setImageResource(R.drawable.cross);
            this.success_text.setText("Aïe ! Vous avez raté cet accord avec une probabilité de 100%.");
        }
        //this.success_text.setText(message);
    }

    public void toMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}