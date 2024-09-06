package com.example.salesapp.Models;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private String userId;
    private String timeStamp;
    private ArrayList<OrderItem> orderItems;

    public Order(String orderId,String userId, String timeStamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.timeStamp = timeStamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
