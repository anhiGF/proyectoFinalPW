package com.example.apptutorias;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TutorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        TextView textView = findViewById(R.id.roleTextView);
        textView.setText("Bienvenido, Tutor");
    }
}
