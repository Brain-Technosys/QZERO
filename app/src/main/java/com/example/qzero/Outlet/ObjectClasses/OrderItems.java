package com.example.qzero.Outlet.ObjectClasses;


public class OrderItems {

    int orderId;
    int itemId;

    String itemCode;
    String itemName;
    String timing;
    String itemStatus;
    String itemPrice;
    String remarks;
    String quantitiy;
    String discount;
    String discountAmount;
    String totalAmount;
    String netAmount;

    public OrderItems(int orderId, int itemId, String itemCode, String itemName, String timing, String itemStatus, String itemPrice, String remarks, String quantitiy,String discount, String discountAmount, String totalAmount, String netAmount) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.timing = timing;
        this.itemStatus = itemStatus;
        this.itemPrice = itemPrice;
        this.remarks = remarks;
        this.quantitiy = quantitiy;
        this.discount = discount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.netAmount = netAmount;

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getQuantitiy() {
        return quantitiy;
    }

    public void setQuantitiy(String quantitiy) {
        this.quantitiy = quantitiy;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }
}
