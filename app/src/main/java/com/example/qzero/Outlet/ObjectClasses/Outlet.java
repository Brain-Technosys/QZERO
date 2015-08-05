package com.example.qzero.Outlet.ObjectClasses;

public class Outlet {

    String outlet_id;
    String outlet_name;
    String outlet_desc;
    String phone_num;
    String mobile_num;
    Boolean isActive;


    public Outlet(String outlet_id, String outlet_name, String outlet_desc, String phone_num, String mobile_num, Boolean isActive) {
        this.outlet_id = outlet_id;
        this.outlet_name = outlet_name;
        this.outlet_desc = outlet_desc;
        this.isActive = isActive;
        this.phone_num = phone_num;
        this.mobile_num = mobile_num;

    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getOutlet_desc() {
        return outlet_desc;
    }

    public void setOutlet_desc(String outlet_desc) {
        this.outlet_desc = outlet_desc;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
