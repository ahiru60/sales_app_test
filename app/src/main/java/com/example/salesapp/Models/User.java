package com.example.salesapp.Models;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class User {
    private boolean isUserSet = false;
    private String userName;
    private String gender;
    private String location;
    private String PaymentMethod;
    private ArrayList<SessionItem> userCartItems;

    public User(@Nullable String userName,@Nullable String gender,@Nullable String location,@Nullable ArrayList<SessionItem> userCartItems) {
        this.userName = userName;
        this.gender = gender;
        this.location = location;
        this.userCartItems = userCartItems;
        if(userName != null && gender != null && location != null && userCartItems != null){
            isUserSet = true;
        }
        this.PaymentMethod = null;

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

    public ArrayList<SessionItem> getUserCartItems() {
        return userCartItems;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }
}
