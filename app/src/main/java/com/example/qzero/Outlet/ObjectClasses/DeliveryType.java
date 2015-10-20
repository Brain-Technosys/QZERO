package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/20/2015.
 */
public class DeliveryType {

    int deliveryTypeId;

    String deliveryTypeName;

    public DeliveryType(int deliveryTypeId, String deliveryTypeName) {
        this.deliveryTypeId = deliveryTypeId;
        this.deliveryTypeName = deliveryTypeName;
    }

    public int getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(int deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
    }
}
