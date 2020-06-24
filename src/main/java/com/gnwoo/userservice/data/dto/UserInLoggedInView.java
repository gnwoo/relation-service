package com.gnwoo.userservice.data.dto;

import com.gnwoo.userservice.data.table.User;

public class UserInLoggedInView {
    private String username;
    private String displayName;
    private String email;

    public UserInLoggedInView() {
    }

    public UserInLoggedInView(User user) {
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
    }
}
