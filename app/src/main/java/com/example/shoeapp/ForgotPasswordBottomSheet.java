package com.example.shoeapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordBottomSheet extends BottomSheetDialogFragment {

    private TextInputEditText etEmailForgot, etNewPassword, etConfirmNewPassword;
    private TextInputLayout tilEmailForgot, tilNewPassword, tilConfirmNewPassword;
    private Button btnSubmitForgotPassword;
    private DatabaseHelper databaseHelper;

    private String verifiedEmail = null;

    public static ForgotPasswordBottomSheet newInstance() {
        return new ForgotPasswordBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_forgot_password, container, false);
        databaseHelper = new DatabaseHelper(getContext());

        etEmailForgot = view.findViewById(R.id.etEmailForgot);
        tilEmailForgot = view.findViewById(R.id.tilEmailForgot);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        tilNewPassword = view.findViewById(R.id.tilNewPassword);
        etConfirmNewPassword = view.findViewById(R.id.etConfirmNewPassword);
        tilConfirmNewPassword = view.findViewById(R.id.tilConfirmNewPassword);
        btnSubmitForgotPassword = view.findViewById(R.id.btnSubmitForgotPassword);

        btnSubmitForgotPassword.setOnClickListener(v -> {
            if (verifiedEmail == null) {
                handleVerifyEmail();
            } else {
                handleResetPassword();
            }
        });

        return view;
    }

    private void handleVerifyEmail() {
        String email = etEmailForgot.getText().toString().trim();
        tilEmailForgot.setError(null); // Clear previous error

        if (TextUtils.isEmpty(email)) {
            tilEmailForgot.setError("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmailForgot.setError("Enter a valid email");
            return;
        }

        if (databaseHelper.checkEmailExists(email)) {
            verifiedEmail = email; // Store verified email
            Toast.makeText(getContext(), "Email verified. Please enter new password.", Toast.LENGTH_SHORT).show();

            // Show password fields and change button text
            tilEmailForgot.setEnabled(false); // Disable email field
            tilNewPassword.setVisibility(View.VISIBLE);
            tilConfirmNewPassword.setVisibility(View.VISIBLE);
            btnSubmitForgotPassword.setText("Reset Password");
        } else {
            tilEmailForgot.setError("Email not found in our records.");
        }
    }

    private void handleResetPassword() {
        String newPassword = etNewPassword.getText().toString();
        String confirmNewPassword = etConfirmNewPassword.getText().toString();

        tilNewPassword.setError(null);
        tilConfirmNewPassword.setError(null);

        boolean isValid = true;
        if (TextUtils.isEmpty(newPassword)) {
            tilNewPassword.setError("New password is required");
            isValid = false;
        } else if (newPassword.length() < 6) {
            tilNewPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmNewPassword)) {
            tilConfirmNewPassword.setError("Confirm your new password");
            isValid = false;
        } else if (!newPassword.equals(confirmNewPassword)) {
            tilConfirmNewPassword.setError("Passwords do not match");
            isValid = false;
        }

        if (isValid && verifiedEmail != null) {
            if (databaseHelper.updatePassword(verifiedEmail, newPassword)) {
                Toast.makeText(getContext(), "Password reset successfully!", Toast.LENGTH_LONG).show();
                dismiss(); // Close the bottom sheet
            } else {
                Toast.makeText(getContext(), "Failed to reset password. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }
}