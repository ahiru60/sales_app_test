package com.example.salesapp.Models;

import android.graphics.Bitmap;

import org.jetbrains.annotations.Nullable;

public class SessionItem {

    private String itemId;
    private String itemName;
    private String quantity;
    private String price;
    private Bitmap image;

    public SessionItem(String itemId,Bitmap image,String itemName, String quantity, String price) {
        this.itemId = itemId;
        this.image = image;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }
    public String getItemName() {
        return itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Bitmap getImage() {
        return image;
    }
}
