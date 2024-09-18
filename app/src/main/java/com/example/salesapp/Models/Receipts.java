package com.example.salesapp.Models;

import java.util.ArrayList;

public class Receipts {
    private ArrayList<Receipt> receipts;

    public Receipts(ArrayList<Receipt> receipts) {
        this.receipts = receipts;
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }


}
