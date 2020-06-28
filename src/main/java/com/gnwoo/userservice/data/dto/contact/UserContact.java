package com.gnwoo.userservice.data.dto.contact;

import com.gnwoo.userservice.data.database.table.entity.UserEntity;

public class UserContact extends ContactBase {
    private final String username;
    private final String displayName;

    public UserContact(UserEntity user, String rID) {
        super(rID);
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }
}
