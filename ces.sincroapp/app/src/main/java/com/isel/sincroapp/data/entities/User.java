package com.isel.sincroapp.data.entities;

public class User {
    String username;
    String password;
    String cc_number;

    public User() {
    }

    public User(String username, String password, String cc_number) {
        this.username = username;
        this.password = password;
        this.cc_number = cc_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCc_number() {
        return cc_number;
    }

    public void setCc_number(String cc_number) {
        this.cc_number = cc_number;
    }
}
