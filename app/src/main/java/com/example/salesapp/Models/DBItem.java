package com.example.salesapp.Models;

import android.graphics.Bitmap;

import com.example.salesapp.Tools.BitmapReader;

import java.io.Serializable;

public class DBItem implements Serializable {
    private String itemName;
    private String stock;
    private String itemId;
    private String cost;
    private String selling_price;
    private Bitmap imageBtmp;

    private int quality = 90;
    private int maxWidth = 250;
    private int maxHeight = 250;

    public DBItem(String imageURL, String itemId, String itemName, String stock, String cost, String selling_price) {
        this.imageBtmp = BitmapReader.readImage(imageURL,quality,maxWidth,maxHeight);
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
