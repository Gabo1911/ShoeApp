package com.example.shoeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends ArrayAdapter<CartItem> {

    private Runnable onCartUpdateCallback;

    public CartAdapter(@NonNull Context context, @NonNull List<CartItem> cartItems, Runnable onCartUpdateCallback) {
        super(context, 0, cartItems);
        this.onCartUpdateCallback = onCartUpdateCallback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_cart, parent, false);
        }

        CartItem currentItem = getItem(position); // Get the item for the current position

        ImageView ivCartItemImage = listItemView.findViewById(R.id.ivCartItemImage);
        TextView tvCartItemName = listItemView.findViewById(R.id.tvCartItemName);
        TextView tvCartItemDetails = listItemView.findViewById(R.id.tvCartItemDetails);
        TextView tvCartItemPrice = listItemView.findViewById(R.id.tvCartItemPrice);
        TextView tvCartItemQuantity = listItemView.findViewById(R.id.tvCartItemQuantity);
        ImageButton btnRemoveItem = listItemView.findViewById(R.id.btnRemoveItem);
        ImageButton btnDecrementCartItem = listItemView.findViewById(R.id.btnDecrementCartItem);
        ImageButton btnIncrementCartItem = listItemView.findViewById(R.id.btnIncrementCartItem);

        if (currentItem != null) {
            ivCartItemImage.setImageResource(currentItem.getProductImageResId());
            tvCartItemName.setText(currentItem.getProductName());
            String details = String.format(Locale.getDefault(), "Size: %s, Color: %s",
                    currentItem.getSelectedSize(),
                    currentItem.getSelectedColor());
            tvCartItemDetails.setText(details);
            tvCartItemPrice.setText(String.format(Locale.getDefault(),"₱%.2f", currentItem.getProductPrice()));
            tvCartItemQuantity.setText(String.valueOf(currentItem.getQuantity()));

            // --- DECREMENT BUTTON ---
            btnDecrementCartItem.setOnClickListener(v -> {
                CartItem itemToUpdate = getItem(position);
                if (itemToUpdate != null) {
                    int quantity = itemToUpdate.getQuantity();
                    if (quantity > 1) {
                        itemToUpdate.setQuantity(quantity - 1);

                        notifyDataSetChanged();
                        if (onCartUpdateCallback != null) {
                            onCartUpdateCallback.run();
                        }
                    } else {
                        HomePageActivity.cartItemsList.remove(itemToUpdate);
                        notifyDataSetChanged();
                        if (onCartUpdateCallback != null) {
                            onCartUpdateCallback.run();
                        }
                        Toast.makeText(getContext(), itemToUpdate.getProductName() + " removed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // --- INCREMENT BUTTON ---
            btnIncrementCartItem.setOnClickListener(v -> {
                CartItem itemToUpdate = getItem(position);
                if (itemToUpdate != null) {
                    // Check against available stock in HomePageActivity
                    if (itemToUpdate.getQuantity() < HomePageActivity.PRODUCT_STOCK) {
                        itemToUpdate.setQuantity(itemToUpdate.getQuantity() + 1);
                        // tvCartItemQuantity.setText(String.valueOf(itemToUpdate.getQuantity()));
                        notifyDataSetChanged();
                        if (onCartUpdateCallback != null) {
                            onCartUpdateCallback.run();
                        }
                    } else {
                        Toast.makeText(getContext(), "Max available stock reached for " + itemToUpdate.getProductName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnRemoveItem.setOnClickListener(v -> {
                CartItem itemToRemove = getItem(position);
                if (itemToRemove != null) {
                    HomePageActivity.cartItemsList.remove(itemToRemove);
                    notifyDataSetChanged();
                    if (onCartUpdateCallback != null) {
                        onCartUpdateCallback.run();
                    }
                    Toast.makeText(getContext(), itemToRemove.getProductName() + " removed from cart", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return listItemView;
    }
}