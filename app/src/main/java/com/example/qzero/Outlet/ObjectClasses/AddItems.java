package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 9/23/2015.
 */
public class AddItems {

    String[] modifiers;
    String quantity;
    String total_price;

    public AddItems(String[] modifiers, String quantity, String total_price) {
        this.modifiers = modifiers;
        this.quantity = quantity;
        this.total_price = total_price;
    }

    public String[] getModifiers() {
        return modifiers;
    }

    public void setModifiers(String[] modifiers) {
        this.modifiers = modifiers;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
