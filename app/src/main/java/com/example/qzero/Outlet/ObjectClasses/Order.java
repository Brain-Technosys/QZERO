package com.example.qzero.Outlet.ObjectClasses;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Braintech on 8/12/2015.
 */
public class Order {
    int orderId;
    int itemsCount;

    boolean isShipped;

    String purchaseDate;
    String orderStatus;
    String customer;
    String shippingAddress;
    String orderBillingAddress;
    String discount;
    String amount;
    String deliveryType;
    String seatNo;

    // Default Constructor
    public Order() {
    }

    // Parametrised constructor
    public Order(int orderId, String purchaseDate, boolean isShipped, String orderStatus, int itemsCount, String customer, String shippingAdress, String orderBillingAdress, String discount, String amount,String deliveryType, String seatNo) {
        this.orderId = orderId;
        this.purchaseDate = purchaseDate;
        this.isShipped = isShipped;
        this.orderStatus = orderStatus;
        this.itemsCount = itemsCount;
        this.customer = customer;
        this.shippingAddress = shippingAdress;
        this.orderBillingAddress = orderBillingAdress;
        this.discount = discount;
        this.amount = amount;
        this.seatNo=seatNo;
        this.deliveryType=deliveryType;
    }

   /* @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("Order Class ", "writeToParcel..." + flags);
        dest.writeInt(orderId);
        dest.writeString(purchaseDate);
        dest.writeByte((byte) (isShipped ? 1 : 0));
        dest.writeString(orderStatus);
        dest.writeInt(itemsCount);
        dest.writeString(customer);
        dest.writeString(shippingAddress);
        dest.writeString(orderBillingAddress);
        dest.writeString(discount);
        dest.writeString(amount);
    }*/

    /*@Override
    public int describeContents() {
        return 0;
    }
*/
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

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getDiscount() {
        return this.discount;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
}
