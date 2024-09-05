package com.example.salesapp.Models;

public class Discount {
    private Double percentage = null;
    private Double value = null;
    private boolean isValue = true;

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        value = null;
        isValue = false;
        this.percentage = percentage;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        percentage = null;
        isValue = true;
        this.value = value;
    }
    public Double getDiscount(){
        if(isValue){
            return value;
        }
        if(!isValue){
            return percentage;
        }
        return null;
    }

    public boolean isValue() {
        return isValue;
    }
}
