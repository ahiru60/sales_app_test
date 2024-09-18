package com.example.salesapp.Models;

public class Receipt{
    private String receiptId;
    private String total_amount;
    private String userId;
    private String timestamp;
    private boolean isOnlyDate = false;

    public Receipt(String receiptId, String total_amount, String userId, String timestamp) {
        this.receiptId = receiptId;
        this.total_amount = total_amount;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isOnlyDate() {
        return isOnlyDate;
    }

    public void setOnlyDate(boolean onlyDate) {
        isOnlyDate = onlyDate;
    }
}
