package com.example.qzero.Outlet.ObjectClasses;

import java.io.Serializable;

/**
 * Created by Braintech on 7/9/2015.
 */
public class Venue implements Serializable {

    String venue_id;
    String venue_name;
    String venue_address;
    String venue_city;
    String venue_zip;
    String venue_phone;
    String venue_mobile;

    String outlet_id;


    public Venue(String venue_id, String outlet_id, String venue_name, String venue_address, String venue_city, String venue_zip, String venue_phone, String venue_mobile) {
        this.venue_id = venue_id;
        this.venue_name = venue_name;
        this.venue_address = venue_address;
        this.venue_city = venue_city;

        this.venue_zip = venue_zip;
        this.venue_phone = venue_phone;
        this.venue_mobile = venue_mobile;

        this.outlet_id = outlet_id;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getVenue_address() {
        return venue_address;
    }

    public void setVenue_address(String venue_address) {
        this.venue_address = venue_address;
    }

    public String getVenue_phone() {
        return venue_phone;
    }

    public void setVenue_phone(String venue_phone) {
        this.venue_phone = venue_phone;
    }

    public String getVenue_zip() {
        return venue_zip;
    }

    public void setVenue_zip(String venue_zip) {
        this.venue_zip = venue_zip;
    }

    public String getVenue_mobile() {
        return venue_mobile;
    }

    public void setVenue_mobile(String venue_mobile) {
        this.venue_mobile = venue_mobile;
    }

    public String getVenue_city() {
        return venue_city;
    }

    public void setVenue_city(String venue_city) {
        this.venue_city = venue_city;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }
}
