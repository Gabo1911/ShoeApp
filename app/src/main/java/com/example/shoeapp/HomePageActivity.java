package com.example.shoeapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {

    private TextView tvProductName, tvProductRating, tvProductDescription, tvProductStock, tvProductPrice, tvQuantity;
    private ImageButton btnBack, btnCart, btnDecrementQuantity, btnIncrementQuantity;
    private Button btnAddToCart;
    private ImageView productImageView, brandLogoView;
    private LinearLayout sizeContainer, colorContainer;
    private List<Button> sizeButtons = new ArrayList<>();
    private List<ImageView> colorImageViews = new ArrayList<>();

    public static int PRODUCT_STOCK = 3;

    private final String PRODUCT_NAME = "Air Max 200 SE";
    private final String PRODUCT_DESCRIPTION = "Air Max 200 SE is an American brand of basketball shoes, athletic and style clothing produced by Nike. Designed for Nike by Peter Moore.";
    private final double PRODUCT_PRICE = 2599.99;
    private final int PRODUCT_IMAGE_RES = R.drawable.img_sneaker;
    private final int BRAND_LOGO_RES = R.drawable.nike;
    private final String PRODUCT_RATING = "4.8 ★";
    private final String[] AVAILABLE_SIZES = {"8", "8.5", "9", "9.5", "10"};
    private final Map<Integer, String> AVAILABLE_COLORS = new HashMap<Integer, String>() {{
        put(R.id.colorPink, "#EFCECE");
        put(R.id.colorBlack, "#000000");
        put(R.id.colorLightBlue, "#E5E5FF");
        put(R.id.colorBlue, "#7CB9E8");
        put(R.id.colorYellow, "#F0E68C");
    }};

    private String selectedSize = "";
    private String selectedColorHex = "";
    private int currentQuantity = 1;
    private ImageView selectedColorView = null;
    private Button selectedSizeButton = null;

    public static ArrayList<CartItem> cartItemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        loadProductData();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tvProductStock != null) {
            tvProductStock.setText("Available: " + PRODUCT_STOCK);
        }

    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnCart = findViewById(R.id.btnCart);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnDecrementQuantity = findViewById(R.id.btnDecrementQuantity);
        btnIncrementQuantity = findViewById(R.id.btnIncrementQuantity);

        productImageView = findViewById(R.id.productImage);
        brandLogoView = findViewById(R.id.brandLogo);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductRating = findViewById(R.id.tvProductRating);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvProductStock = findViewById(R.id.tvProductStock);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvQuantity = findViewById(R.id.tvQuantity);

        sizeContainer = findViewById(R.id.sizeContainer);
        colorContainer = findViewById(R.id.colorContainer);

        // Populate size buttons
        sizeButtons.add(findViewById(R.id.btnSize8));
        sizeButtons.add(findViewById(R.id.btnSize8_5));
        sizeButtons.add(findViewById(R.id.btnSize9));
        sizeButtons.add(findViewById(R.id.btnSize9_5));
        sizeButtons.add(findViewById(R.id.btnSize10));

        // Populate color ImageViews
        for (int id : AVAILABLE_COLORS.keySet()) {
            ImageView colorView = findViewById(id);
            if (colorView != null) {
                colorImageViews.add(colorView);
            }
        }
    }

    private void loadProductData() {
        productImageView.setImageResource(PRODUCT_IMAGE_RES);
        brandLogoView.setImageResource(BRAND_LOGO_RES);
        tvProductName.setText(PRODUCT_NAME);
        tvProductRating.setText(PRODUCT_RATING);
        tvProductDescription.setText(PRODUCT_DESCRIPTION);
        tvProductStock.setText("Available: " + PRODUCT_STOCK);
        tvProductPrice.setText("₱" + String.format("%.2f", PRODUCT_PRICE));
        tvQuantity.setText(String.valueOf(currentQuantity));

        if (!sizeButtons.isEmpty() && sizeButtons.get(0) != null) {
            selectSizeButton(sizeButtons.get(0));
        }
        if (!colorImageViews.isEmpty() && colorImageViews.get(0) != null) {
            selectColorView(colorImageViews.get(0));
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, CartPage.class));
        });

        btnAddToCart.setOnClickListener(v -> handleAddToCart());

        btnIncrementQuantity.setOnClickListener(v -> {
            if (currentQuantity < PRODUCT_STOCK) {
                currentQuantity++;
                tvQuantity.setText(String.valueOf(currentQuantity));
            } else {
                Toast.makeText(this, "Maximum stock reached for selection", Toast.LENGTH_SHORT).show();
            }
        });

        btnDecrementQuantity.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                tvQuantity.setText(String.valueOf(currentQuantity));
            }
        });

        for (Button sizeButton : sizeButtons) {
            if (sizeButton != null) { // Null check
                sizeButton.setOnClickListener(v -> selectSizeButton((Button) v));
            }
        }

        for (ImageView colorView : colorImageViews) {
            if (colorView != null) { // Null check
                colorView.setOnClickListener(v -> selectColorView((ImageView) v));
            }
        }
    }

    private void selectSizeButton(Button button) {
        if (selectedSizeButton != null) {
            selectedSizeButton.setSelected(false);
            selectedSizeButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        }
        button.setSelected(true);
        button.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        selectedSizeButton = button;
        selectedSize = button.getText().toString();
    }

    private void selectColorView(ImageView colorView) {
        // Reset previous selection
        if (selectedColorView != null) {
            selectedColorView.setBackgroundResource(R.drawable.color_circle); // Ensure this drawable exists
            String originalColorHex = AVAILABLE_COLORS.get(selectedColorView.getId());
            if (originalColorHex != null && selectedColorView.getBackground() != null) {
                selectedColorView.getBackground().setColorFilter(Color.parseColor(originalColorHex), PorterDuff.Mode.SRC_ATOP);
            }
            selectedColorView.setPadding(selectedColorView.getPaddingLeft(), selectedColorView.getPaddingTop(), selectedColorView.getPaddingRight(), selectedColorView.getPaddingBottom());
        }

        // Apply new selection
        colorView.setBackgroundResource(R.drawable.color_circle_selected); // Ensure this drawable exists
        String colorHex = AVAILABLE_COLORS.get(colorView.getId());
        if (colorHex != null) {
            Drawable selectedBackground = colorView.getBackground();
            if (selectedBackground instanceof android.graphics.drawable.LayerDrawable) {
                android.graphics.drawable.LayerDrawable layerDrawable = (android.graphics.drawable.LayerDrawable) selectedBackground;
                if (layerDrawable.getNumberOfLayers() > 1) {
                    Drawable innerCircle = layerDrawable.getDrawable(1); // Assuming inner circle is at index 1
                    innerCircle.setColorFilter(Color.parseColor(colorHex), PorterDuff.Mode.SRC_ATOP);
                }
            }
            selectedColorHex = colorHex;
        }
        colorView.setPadding(colorView.getPaddingLeft(), colorView.getPaddingTop(), colorView.getPaddingRight(), colorView.getPaddingBottom());
        selectedColorView = colorView;
    }


    private void handleAddToCart() {
        if (selectedSize.isEmpty()) {
            Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedColorHex.isEmpty()) {
            Toast.makeText(this, "Please select a color", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentQuantity <= 0) {
            Toast.makeText(this, "Please select a valid quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantityAlreadyInCartForThisVariant = 0;
        CartItem existingCartItem = null;

        for (CartItem item : cartItemsList) {
            if (item.getProductName().equals(PRODUCT_NAME) &&
                    item.getSelectedSize().equals(selectedSize) &&
                    item.getSelectedColor().equals(selectedColorHex)) {
                quantityAlreadyInCartForThisVariant = item.getQuantity();
                existingCartItem = item;
                break;
            }
        }

        if ((quantityAlreadyInCartForThisVariant + currentQuantity) > PRODUCT_STOCK) {
            int canAdd = PRODUCT_STOCK - quantityAlreadyInCartForThisVariant;
            Toast.makeText(this, "Not enough stock. You can add " + (canAdd > 0 ? canAdd : 0) + " more.", Toast.LENGTH_LONG).show();
            return;
        }

        if (existingCartItem != null) {
            existingCartItem.setQuantity(quantityAlreadyInCartForThisVariant + currentQuantity);
        } else {
            CartItem newItem = new CartItem(
                    PRODUCT_NAME,
                    PRODUCT_PRICE,
                    PRODUCT_IMAGE_RES,
                    selectedColorHex,
                    selectedSize,
                    currentQuantity
            );
            cartItemsList.add(newItem);
        }
        Toast.makeText(this, currentQuantity + " " + PRODUCT_NAME + " added/updated in cart!", Toast.LENGTH_SHORT).show();
    }
}