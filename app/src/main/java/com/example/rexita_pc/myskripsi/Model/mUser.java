package com.example.rexita_pc.myskripsi.Model;

public class mUser {
    String uid, name, address, number, email;

    public mUser() {
    }

    public mUser(String uid, String name, String address, String number, String email) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.number = number;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
