package com.example.shoeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckoutPage extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnPlaceOrder;
    private EditText etFullName, etAddress, etCity, etZipCode;
    private RadioGroup rgPaymentMethod;
    private TextView tvSubtotalValue, tvShippingValue, tvTotalValue;

    private DatabaseHelper databaseHelper;
    private String loggedInUserEmail;
    private double currentSubtotal = 0.0;
    private final double SHIPPING_COST = 99.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);
        retrieveLoggedInUser();

        initializeViews();
        loadUserDetails();
        calculateAndDisplayTotals();
        setupClickListeners();

    }

    private void retrieveLoggedInUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginPage.PREFS_NAME, Context.MODE_PRIVATE);
        loggedInUserEmail = sharedPreferences.getString(LoginPage.KEY_LOGGED_IN_EMAIL, null);
        if (loggedInUserEmail == null) {
            Toast.makeText(this, "Error: User not logged in. Please login again.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CheckoutPage.this, LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etZipCode = findViewById(R.id.etZipCode);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        tvSubtotalValue = findViewById(R.id.tvSubtotal);
        tvShippingValue = findViewById(R.id.tvShipping);
        tvTotalValue = findViewById(R.id.tvTotal);
    }

    private void loadUserDetails() {
        if (loggedInUserEmail != null) {
            String fullName = databaseHelper.getUserFullNameByEmail(loggedInUserEmail);
            if (fullName != null) {
                etFullName.setText(fullName);
            } else {
                etFullName.setHint("Full Name (Not Found in DB)");
            }
        }
    }

    private void calculateAndDisplayTotals() {
        currentSubtotal = 0;
        boolean isCartEmpty = HomePageActivity.cartItemsList == null || HomePageActivity.cartItemsList.isEmpty();

        if (!isCartEmpty) {
            for (CartItem item : HomePageActivity.cartItemsList) {
                currentSubtotal += item.getProductPrice() * item.getQuantity();
            }
            tvSubtotalValue.setText(String.format(Locale.getDefault(), "₱%.2f", currentSubtotal));
            tvShippingValue.setText(String.format(Locale.getDefault(), "₱%.2f", SHIPPING_COST));
            tvTotalValue.setText(String.format(Locale.getDefault(), "₱%.2f", currentSubtotal + SHIPPING_COST));
            btnPlaceOrder.setEnabled(true);
            btnPlaceOrder.setAlpha(1.0f);
            btnPlaceOrder.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
        } else {
            tvSubtotalValue.setText("₱0.00");
            tvShippingValue.setText("₱0.00");
            tvTotalValue.setText("₱0.00");
            btnPlaceOrder.setEnabled(false);
            btnPlaceOrder.setAlpha(0.5f);
            btnPlaceOrder.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.darker_gray)));
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnPlaceOrder.setOnClickListener(v -> {
            if (validateForm()) {
                processOrder();
            }
        });
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(etFullName.getText().toString().trim())) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etCity.getText().toString().trim())) {
            etCity.setError("City is required");
            etCity.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etZipCode.getText().toString().trim())) {
            etZipCode.setError("ZIP Code is required");
            etZipCode.requestFocus();
            return false;
        }

        if (rgPaymentMethod.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (HomePageActivity.cartItemsList.isEmpty()) {
            Toast.makeText(this, "Your cart is empty. Cannot place order.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private String getSelectedPaymentMethod() {
        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
        RadioButton selectedButton = findViewById(selectedId);
        return selectedButton != null ? selectedButton.getText().toString() : "Unknown";
    }

    private void processOrder() {
        String shippingFullName = etFullName.getText().toString().trim();
        String shippingAddress = etAddress.getText().toString().trim();
        String shippingCity = etCity.getText().toString().trim();
        String shippingZipCode = etZipCode.getText().toString().trim();
        String paymentMethod = getSelectedPaymentMethod();
        String orderDate = DatabaseHelper.getCurrentTimestamp();
        double shippingForOrder = HomePageActivity.cartItemsList.isEmpty() ? 0 : SHIPPING_COST;
        double totalAmount = currentSubtotal + shippingForOrder;

        Order newOrder = new Order(
                loggedInUserEmail, shippingFullName, shippingAddress, shippingCity,
                shippingZipCode, paymentMethod, currentSubtotal, shippingForOrder,
                totalAmount, orderDate
        );


        List<CartItem> orderedItems = new ArrayList<>(HomePageActivity.cartItemsList);

        long orderId = databaseHelper.addOrder(newOrder, orderedItems);

        if (orderId != -1) {
            // UPDATE PRODUCT STOCK
            for (CartItem item : orderedItems) {
                HomePageActivity.PRODUCT_STOCK -= item.getQuantity();
                if (HomePageActivity.PRODUCT_STOCK < 0) {
                    HomePageActivity.PRODUCT_STOCK = 0;
                }
            }

            HomePageActivity.cartItemsList.clear();

            new AlertDialog.Builder(this)
                    .setTitle("Order Placed Successfully!")
                    .setMessage("Your Order ID is #" + orderId + ".\nPayment Method: " + paymentMethod + "\nThank you for shopping!")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        Intent intent = new Intent(CheckoutPage.this, HomePageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finishAffinity();
                    })
                    .show();
        } else {
            Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}