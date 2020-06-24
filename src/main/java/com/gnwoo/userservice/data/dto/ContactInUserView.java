package com.gnwoo.userservice.data.dto;

import com.gnwoo.userservice.data.table.User;

public class ContactInUserView {
    private String username;
    private String displayName;
    private Long rID;

    public ContactInUserView(User user, Long rID) {
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.rID = rID;
    }
}
