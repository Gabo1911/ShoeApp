package com.example.shoeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();

        Toast.makeText(this, "Welcome to ShoeApp", Toast.LENGTH_SHORT).show();
    }

    private void initializeViews() {
        btnGetStarted = findViewById(R.id.btnGetStarted);
        if (btnGetStarted == null) {
            Toast.makeText(this, "Get Started button not found in layout", Toast.LENGTH_LONG).show();
        }
    }

    private void setupClickListeners() {
        if (btnGetStarted != null) {
            btnGetStarted.setOnClickListener(v -> {
                Toast.makeText(this, "Redirecting to Login Page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginPage.class));
                finish(); // Prevent returning to splash
            });
        }
    }
}
