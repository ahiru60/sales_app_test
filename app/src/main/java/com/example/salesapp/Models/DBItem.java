package com.example.salesapp.Models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class DBItem implements Serializable {
    private String itemName;
    private String stock;
    private String itemId;
    private String cost;
    private String selling_price;
    private Bitmap imageBtmp;

    public DBItem(Bitmap imageBtmp, String itemId, String itemName, String stock, String cost, String selling_price) {
        this.imageBtmp = imageBtmp;
        this.itemName = itemName;
        this.stock = stock;
        this.itemId = itemId;
        this.cost = cost;
        this.selling_price = selling_price;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStock() {
        return stock;
    }

    public String getItemId() {
        return itemId;
    }

    public String getSellingPrice() {
        return selling_price;
    }

    public String getCost() {
        return cost;
    }

    public Bitmap getImageBtmp() {
        return imageBtmp;
    }
}
