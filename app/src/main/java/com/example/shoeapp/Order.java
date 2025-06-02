package com.example.shoeapp;

public class Order {
    private long id;
    private String userEmail;
    private String shippingFullName;
    private String shippingAddress;
    private String shippingCity;
    private String shippingZipCode;
    private String paymentMethod;
    private double subtotal;
    private double shippingCost;
    private double totalAmount;
    private String orderDate;

    public Order(String userEmail, String shippingFullName, String shippingAddress,
                 String shippingCity, String shippingZipCode, String paymentMethod,
                 double subtotal, double shippingCost, double totalAmount, String orderDate) {
        this.userEmail = userEmail;
        this.shippingFullName = shippingFullName;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingZipCode = shippingZipCode;
        this.paymentMethod = paymentMethod;
        this.subtotal = subtotal;
        this.shippingCost = shippingCost;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    // Getters
    public long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public String getShippingFullName() { return shippingFullName; }
    public String getShippingAddress() { return shippingAddress; }
    public String getShippingCity() { return shippingCity; }
    public String getShippingZipCode() { return shippingZipCode; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getSubtotal() { return subtotal; }
    public double getShippingCost() { return shippingCost; }
    public double getTotalAmount() { return totalAmount; }
    public String getOrderDate() { return orderDate; }

    // Setter for ID (used after inserting into DB)
    public void setId(long id) { this.id = id; }
}