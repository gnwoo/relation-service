package com.gnwoo.userservice.data.dto.contact;

public class ContactBase {
    private final String rID;

    public ContactBase(String rID) {
        this.rID = rID;
    }

    public String getrID() {
        return rID;
    }
}
