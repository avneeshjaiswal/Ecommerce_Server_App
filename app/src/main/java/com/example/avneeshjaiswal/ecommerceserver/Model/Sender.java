package com.example.avneeshjaiswal.ecommerceserver.Model;

import android.app.Notification;

/**
 * Created by avneesh jaiswal on 15-Mar-18.
 */

public class Sender {
    public String to;
    public Notification notification;

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public Sender(String token, com.example.avneeshjaiswal.ecommerceserver.Model.Notification notification) {

    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
