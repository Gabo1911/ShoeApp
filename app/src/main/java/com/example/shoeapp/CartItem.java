package com.example.shoeapp;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String productName;
    private double productPrice;
    private int productImageResId;
    private String selectedColor;
    private String selectedSize;
    private int quantity;

    public CartItem(String productName, double productPrice, int productImageResId, String selectedColor, String selectedSize, int quantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageResId = productImageResId;
        this.selectedColor = selectedColor;
        this.selectedSize = selectedSize;
        this.quantity = quantity;
    }

    // Getters
    public String getProductName() {
        return productName;
    }
    public double getProductPrice() {
        return productPrice;
    }
    public int getProductImageResId() {
        return productImageResId;
    }
    public String getSelectedColor() {
        return selectedColor;
    }
    public String getSelectedSize() {
        return selectedSize;
    }
    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }
    // You might not need setters for other fields if they are fixed upon creation
}