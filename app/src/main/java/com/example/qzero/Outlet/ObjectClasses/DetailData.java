package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 9/25/2015.
 */
public class DetailData {

    int quantity;
    String price;

    public DetailData(int quantity, String price) {
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
