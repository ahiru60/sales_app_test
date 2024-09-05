package com.example.salesapp.Models;

public class OrderItem {
    private String orderItemId;
    private String itemId;
    private String itemName;
    private String quantity;
    private String sellingPrice;

    public OrderItem(String orderItemId, String itemId, String itemName, String quantity, String sellingPrice) {
        this.orderItemId = orderItemId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }

    public String getOrderItemId() {
        return orderItemId;
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

    public String getSellingPrice() {
        return sellingPrice;
    }
}
