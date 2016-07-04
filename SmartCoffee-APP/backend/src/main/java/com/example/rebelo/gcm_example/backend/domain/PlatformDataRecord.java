package com.example.rebelo.gcm_example.backend.domain;

/**
 * Created by rebelo on 16/05/2016.
 */
public class PlatformDataRecord {

    private String statusCoffee;

    private String date;

    private String hour;

    public PlatformDataRecord() {
    }

    public PlatformDataRecord(String statusCoffee, String date, String hour) {

        this.statusCoffee = statusCoffee;
        this.date = date;
        this.hour = hour;
    }

    public String getStatusCoffee() {
        return statusCoffee;
    }

    public void setStatusCoffee(String statusCoffee) {
        this.statusCoffee = statusCoffee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
