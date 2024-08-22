package com.example.salesapp.Models;

public class DBCartItem {
    private String itemName;
    private String stock;
    private String itemId;
    private String price;

    public DBCartItem(String itemId, String itemName, String stock, String price) {
        this.itemName = itemName;
        this.stock = stock;
        this.itemId = itemId;
        this.price = price;
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

    public String getPrice() {
        return price;
    }
}
