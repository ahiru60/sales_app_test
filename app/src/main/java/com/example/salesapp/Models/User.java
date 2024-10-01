package com.example.salesapp.Models;

import android.graphics.Bitmap;

import com.example.salesapp.Tools.BitmapReader;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class User {
    private boolean isUserSet = false;
    private String userId;
    private Bitmap image;
    private String userName;
    private String gender;
    private String location;
    private String PaymentMethod;
    private Discount discount;
    private String orderId;
    private ArrayList<SessionItem> userItems;

    private int quality = 90;
    private int maxWidth = 250;
    private int maxHeight = 250;

    public User(@Nullable String userId,@Nullable String imageURL,@Nullable String userName,@Nullable String gender,@Nullable String location,@Nullable ArrayList<SessionItem> userCartItems ,@Nullable Discount discount) {
        this.userId = userId;
        this.image = BitmapReader.readImage(imageURL,quality,maxWidth,maxHeight);
        this.userName = userName;
        this.gender = gender;
        this.location = location;
        this.userItems = userCartItems;
        this.discount = discount;
        if(userName != null && gender != null && location != null && userCartItems != null){
            isUserSet = true;
        }
        this.PaymentMethod = null;

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(String imageURL) {
        this.image = BitmapReader.readImage(imageURL,quality,maxWidth,maxHeight);
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isUserSet() {
        return isUserSet;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<SessionItem> getUserItems() {
        return userItems;
    }
    public void setUserItems(ArrayList<SessionItem> cartItems) {
        userItems = cartItems;
    }
    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
