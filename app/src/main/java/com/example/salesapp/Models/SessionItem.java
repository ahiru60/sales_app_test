package com.example.salesapp.Models;

import org.jetbrains.annotations.Nullable;

public class SessionItem {
    private String itemName;
    private String quantity;
    private String price;

    public SessionItem(String itemName, String quantity, String price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
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

    public DBCartItem getDBCartItem (@Nullable String itemId){
       return new DBCartItem(itemId,itemName,quantity,price);
    }
}
