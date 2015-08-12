package com.example.qzero.Outlet.ObjectClasses;

import java.io.Serializable;

/**
 * Created by Braintech on 8/12/2015.
 */
public class Order{
    int orderId;
    int itemsCount;

    boolean isShipped;

    String purchaseDate;
    String orderStatus;
    String customer;
    String shippingAddress;
    String orderBillingAddress;

    // Default Constructor
    public Order() {
    }

    // Parametrised constructor
    public Order(int orderId, String purchaseDate, boolean isShipped, String orderStatus, int itemsCount, String customer, String shippingAdress, String orderBillingAdress) {
        this.orderId = orderId;
        this.purchaseDate = purchaseDate;
        this.isShipped = isShipped;
        this.orderStatus = orderStatus;
        this.itemsCount = itemsCount;
        this.customer = customer;
        this.shippingAddress = shippingAdress;
        this.orderBillingAddress = orderBillingAdress;
    }

    // Setters

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setIsShipped(boolean isShipped) {
        this.isShipped = isShipped;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setOrderBillingAddress(String orderBillingAddress) {
        this.orderBillingAddress = orderBillingAddress;
    }


    // Getters
    public int getOrderId() {
        return this.orderId;
    }

    public String getPurchaseDate() {
        return this.purchaseDate;
    }

    public boolean isShipped() {
        return this.isShipped;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public int getItemsCount() {
        return this.itemsCount;
    }

    public String getCustomer() {
        return this.customer;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public String getOrderBillingAddress() {
        return this.orderBillingAddress;
    }
}
