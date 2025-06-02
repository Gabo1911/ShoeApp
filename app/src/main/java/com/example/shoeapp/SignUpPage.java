package com.example.shoeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpPage extends AppCompatActivity {

    private EditText etFullName, etEmail, etMobile, etPassword, etConfirmPassword;
    private CheckBox cbTerms;
    private Button btnSignUp;
    private ImageButton btnBack;
    private TextView tvLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);

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
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnBack = findViewById(R.id.btnBack);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupClickListeners() {
        btnSignUp.setOnClickListener(v -> {
            if (validateInputs()) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();
                String password = etPassword.getText().toString();

                if (databaseHelper.checkEmailExists(email)) {
                    Toast.makeText(SignUpPage.this, "Email already registered. Please use a different email.", Toast.LENGTH_LONG).show();
                    etEmail.setError("Email already exists");
                    etEmail.requestFocus();
                } else {
                    boolean isInserted = databaseHelper.addUser(fullName, email, mobile, password);
                    if (isInserted) {
                        Toast.makeText(SignUpPage.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpPage.this, LoginPage.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpPage.this, "Account creation failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpPage.this, LoginPage.class));
            finish(); // Finish SignUpPage
        });
    }

    private boolean validateInputs() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        boolean isValid = true;

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            isValid = false;
        } else {
            etFullName.setError(null);
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        if (mobile.isEmpty()) {
            etMobile.setError("Mobile number is required");
            isValid = false;
        } else if (mobile.length() < 10) { // Basic validation, can be improved
            etMobile.setError("Please enter a valid 10-digit mobile number");
            isValid = false;
        } else {
            etMobile.setError(null);
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

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Confirm your password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            isValid = false;
        } else {
            etConfirmPassword.setError(null);
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!isValid) {
        }
        return isValid;
    }
}