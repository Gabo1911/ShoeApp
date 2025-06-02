package com.example.shoeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ShoeApp.db";
    private static final int DATABASE_VERSION = 2;

    // Table Users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_FULL_NAME = "user_full_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_MOBILE = "user_mobile";
    public static final String COLUMN_USER_PASSWORD = "user_password";

    // Table Orders
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_USER_EMAIL_FK = "order_user_email";
    public static final String COLUMN_ORDER_FULL_NAME = "order_full_name";
    public static final String COLUMN_ORDER_ADDRESS = "order_address";
    public static final String COLUMN_ORDER_CITY = "order_city";
    public static final String COLUMN_ORDER_ZIP_CODE = "order_zip_code";
    public static final String COLUMN_ORDER_PAYMENT_METHOD = "order_payment_method";
    public static final String COLUMN_ORDER_SUBTOTAL = "order_subtotal";
    public static final String COLUMN_ORDER_SHIPPING_COST = "order_shipping_cost";
    public static final String COLUMN_ORDER_TOTAL_AMOUNT = "order_total_amount";
    public static final String COLUMN_ORDER_DATE = "order_date";

    // Table Order Items
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COLUMN_ORDER_ITEM_ID = "order_item_id";
    public static final String COLUMN_ORDER_ITEM_ORDER_ID_FK = "order_item_order_id";
    public static final String COLUMN_ORDER_ITEM_PRODUCT_NAME = "order_item_product_name";
    public static final String COLUMN_ORDER_ITEM_PRICE = "order_item_price";
    public static final String COLUMN_ORDER_ITEM_QUANTITY = "order_item_quantity";
    public static final String COLUMN_ORDER_ITEM_COLOR = "order_item_color";
    public static final String COLUMN_ORDER_ITEM_SIZE = "order_item_size";
    public static final String COLUMN_ORDER_ITEM_IMAGE_RES_ID = "order_item_image_res_id";


    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_FULL_NAME + " TEXT NOT NULL,"
            + COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE,"
            + COLUMN_USER_MOBILE + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL" + ")";

    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
            + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ORDER_USER_EMAIL_FK + " TEXT NOT NULL,"
            + COLUMN_ORDER_FULL_NAME + " TEXT NOT NULL,"
            + COLUMN_ORDER_ADDRESS + " TEXT NOT NULL,"
            + COLUMN_ORDER_CITY + " TEXT NOT NULL,"
            + COLUMN_ORDER_ZIP_CODE + " TEXT NOT NULL,"
            + COLUMN_ORDER_PAYMENT_METHOD + " TEXT NOT NULL,"
            + COLUMN_ORDER_SUBTOTAL + " REAL NOT NULL,"
            + COLUMN_ORDER_SHIPPING_COST + " REAL NOT NULL,"
            + COLUMN_ORDER_TOTAL_AMOUNT + " REAL NOT NULL,"
            + COLUMN_ORDER_DATE + " TEXT NOT NULL" + ")";

    private static final String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + TABLE_ORDER_ITEMS + "("
            + COLUMN_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ORDER_ITEM_ORDER_ID_FK + " INTEGER NOT NULL,"
            + COLUMN_ORDER_ITEM_PRODUCT_NAME + " TEXT NOT NULL,"
            + COLUMN_ORDER_ITEM_PRICE + " REAL NOT NULL,"
            + COLUMN_ORDER_ITEM_QUANTITY + " INTEGER NOT NULL,"
            + COLUMN_ORDER_ITEM_COLOR + " TEXT,"
            + COLUMN_ORDER_ITEM_SIZE + " TEXT,"
            + COLUMN_ORDER_ITEM_IMAGE_RES_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_ORDER_ITEM_ORDER_ID_FK + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + ")" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);
        Log.d("DBHelper", "Database tables created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL(CREATE_ORDERS_TABLE);
            db.execSQL(CREATE_ORDER_ITEMS_TABLE);
            Log.d("DBHelper", "Tables ORDERS and ORDER_ITEMS recreated for version 2.");
        }
        // For other version upgrades, add more 'if (oldVersion < X)' blocks
    }

    public boolean addUser(String fullName, String email, String mobile, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FULL_NAME, fullName);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_MOBILE, mobile);
        values.put(COLUMN_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + " = ?", new String[]{email},
                null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?",
                new String[]{email, password}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public String getUserFullNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_FULL_NAME},
                COLUMN_USER_EMAIL + " = ?", new String[]{email},
                null, null, null);
        String fullName = null;
        if (cursor != null && cursor.moveToFirst()) {
            fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULL_NAME));
            cursor.close();
        }
        db.close();
        return fullName;
    }

    public long addOrder(Order order, List<CartItem> items) {
        SQLiteDatabase db = this.getWritableDatabase();
        long orderId = -1;

        db.beginTransaction();
        try {
            ContentValues orderValues = new ContentValues();
            orderValues.put(COLUMN_ORDER_USER_EMAIL_FK, order.getUserEmail());
            orderValues.put(COLUMN_ORDER_FULL_NAME, order.getShippingFullName());
            orderValues.put(COLUMN_ORDER_ADDRESS, order.getShippingAddress());
            orderValues.put(COLUMN_ORDER_CITY, order.getShippingCity());
            orderValues.put(COLUMN_ORDER_ZIP_CODE, order.getShippingZipCode());
            orderValues.put(COLUMN_ORDER_PAYMENT_METHOD, order.getPaymentMethod());
            orderValues.put(COLUMN_ORDER_SUBTOTAL, order.getSubtotal());
            orderValues.put(COLUMN_ORDER_SHIPPING_COST, order.getShippingCost());
            orderValues.put(COLUMN_ORDER_TOTAL_AMOUNT, order.getTotalAmount());
            orderValues.put(COLUMN_ORDER_DATE, order.getOrderDate()); // Use the pre-formatted date string

            orderId = db.insert(TABLE_ORDERS, null, orderValues);

            if (orderId != -1) {
                for (CartItem item : items) {
                    ContentValues itemValues = new ContentValues();
                    itemValues.put(COLUMN_ORDER_ITEM_ORDER_ID_FK, orderId);
                    itemValues.put(COLUMN_ORDER_ITEM_PRODUCT_NAME, item.getProductName());
                    itemValues.put(COLUMN_ORDER_ITEM_PRICE, item.getProductPrice());
                    itemValues.put(COLUMN_ORDER_ITEM_QUANTITY, item.getQuantity());
                    itemValues.put(COLUMN_ORDER_ITEM_COLOR, item.getSelectedColor());
                    itemValues.put(COLUMN_ORDER_ITEM_SIZE, item.getSelectedSize());
                    itemValues.put(COLUMN_ORDER_ITEM_IMAGE_RES_ID, item.getProductImageResId());
                    db.insert(TABLE_ORDER_ITEMS, null, itemValues);
                }
                db.setTransactionSuccessful();
                Log.d("DBHelper", "Order and items added successfully. Order ID: " + orderId);
            } else {
                Log.e("DBHelper", "Failed to insert order header.");
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error adding order to database", e);
            orderId = -1;
        } finally {
            db.endTransaction();
            db.close();
        }
        return orderId;
    }

    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, newPassword);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USER_EMAIL + " = ?", new String[]{email});
        db.close();
        return rowsAffected > 0;
    }
}