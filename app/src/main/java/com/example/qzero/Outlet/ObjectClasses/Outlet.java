package com.example.qzero.Outlet.ObjectClasses;

public class Outlet  {

    String outlet_id;
    String outlet_name;
    Boolean isActive;

    public Outlet(String outlet_id, String outlet_name, Boolean isActive) {
        this.outlet_id = outlet_id;
        this.outlet_name = outlet_name;
        this.isActive = isActive;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
