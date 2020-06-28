package com.gnwoo.userservice.rpc.dto;

import com.gnwoo.userservice.rpc.database.table.User;

public class UserInfo {
    private final String username;
    private final String displayName;
    private final String email;

    public UserInfo(User user) {
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
