package com.example.shoeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge; // You can keep or remove this if not actively using its specific features beyond insets
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginPage extends AppCompatActivity {
    private TextView tvForgotPassword, tvSignUp;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private DatabaseHelper databaseHelper;

    public static final String PREFS_NAME = "ShoeAppPrefs";
    public static final String KEY_LOGGED_IN_EMAIL = "loggedInEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString();

                if (databaseHelper.checkUser(email, password)) {
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_LOGGED_IN_EMAIL, email);
                    editor.apply();

                    Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPage.this, HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginPage.this, "Login failed. Invalid email or password.", Toast.LENGTH_LONG).show();
                    etPassword.setText("");
                    etPassword.requestFocus();
                }
            }
        });

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginPage.this, SignUpPage.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            ForgotPasswordBottomSheet bottomSheet = ForgotPasswordBottomSheet.newInstance();
            bottomSheet.show(getSupportFragmentManager(), "ForgotPasswordBottomSheetTag");
        });
    }

    private boolean validateInputs() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        boolean isValid = true;

        // Email field validation
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        return isValid;
    }
}