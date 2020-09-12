package com.isel.sincroapp.data.entities;

import java.util.Date;

public class Notification {
    long notification_id;
    long infraction_id;
    String cc_number;
    boolean notified;
    Date notification_time;

    public Notification() {
    }

    public Notification(long notification_id, long infraction_id, String cc_number, boolean notified, Date notification_time) {
        this.notification_id = notification_id;
        this.infraction_id = infraction_id;
        this.cc_number = cc_number;
        this.notified = notified;
        this.notification_time = notification_time;
    }

    public long getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(long notification_id) {
        this.notification_id = notification_id;
    }

    public long getInfraction_id() {
        return infraction_id;
    }

    public void setInfraction_id(long infraction_id) {
        this.infraction_id = infraction_id;
    }

    public String getCc_number() {
        return cc_number;
    }

    public void setCc_number(String cc_number) {
        this.cc_number = cc_number;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public Date getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(Date notification_time) {
        this.notification_time = notification_time;
    }
}