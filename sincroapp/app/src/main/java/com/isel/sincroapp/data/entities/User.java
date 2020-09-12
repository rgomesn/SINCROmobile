package com.isel.sincroapp.data.entities;

import java.io.Serializable;

public class User implements Serializable {
    String username;
    String pwd;
    String cc_number;

    public User() {
    }

    public User(String username, String pwd, String cc_number) {
        this.username = username;
        this.pwd = pwd;
        this.cc_number = cc_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCc_number() {
        return cc_number;
    }

    public void setCc_number(String cc_number) {
        this.cc_number = cc_number;
    }
}
