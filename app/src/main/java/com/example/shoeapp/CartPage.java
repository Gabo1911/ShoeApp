package com.example.shoeapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class CartPage extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnCheckout;
    private ListView lvCartItems;
    private TextView tvSubtotal, tvShipping, tvTotal;

    private CartAdapter adapter;
    private final double SHIPPING_COST = 99.00;
    private Runnable cartUpdateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartUpdateRunnable = this::updateCartState;

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnCheckout = findViewById(R.id.btnCheckout);
        lvCartItems = findViewById(R.id.lvCartItems);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTotal = findViewById(R.id.tvTotal);

        adapter = new CartAdapter(this, HomePageActivity.cartItemsList, cartUpdateRunnable);
        lvCartItems.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCheckout.setOnClickListener(v -> {
            if (HomePageActivity.cartItemsList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                return; // Should be disabled anyway, but good to have a check
            }
            // Toast.makeText(this, "Proceeding to checkout", Toast.LENGTH_SHORT).show(); // Optional
            startActivity(new Intent(CartPage.this, CheckoutPage.class));
        });
    }

    private void updateCartState() {
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Ensures ListView reflects any changes in cartItemsList
        }
        updateTotalsAndCheckoutButton();
    }

    private void updateTotalsAndCheckoutButton() {
        double subtotal = 0;
        for (CartItem item : HomePageActivity.cartItemsList) {
            subtotal += item.getProductPrice() * item.getQuantity();
        }

        tvSubtotal.setText(String.format(Locale.getDefault(),"₱%.2f", subtotal));

        if (!HomePageActivity.cartItemsList.isEmpty() && subtotal > 0) {
            tvShipping.setText(String.format(Locale.getDefault(),"₱%.2f", SHIPPING_COST));
            tvTotal.setText(String.format(Locale.getDefault(),"₱%.2f", subtotal + SHIPPING_COST));

            btnCheckout.setEnabled(true);
            btnCheckout.setAlpha(1.0f);
            btnCheckout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
        } else {
            tvShipping.setText("₱0.00");
            tvTotal.setText("₱0.00");

            btnCheckout.setEnabled(false);
            btnCheckout.setAlpha(0.5f);
            btnCheckout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.darker_gray)));

            if (HomePageActivity.cartItemsList.isEmpty()) {

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartState();
    }
}