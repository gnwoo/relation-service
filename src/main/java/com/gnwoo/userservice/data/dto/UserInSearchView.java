package com.gnwoo.userservice.data.dto;

import com.gnwoo.userservice.data.table.User;

public class UserInSearchView {
    private String username;
    private String displayName;

    public UserInSearchView() {
    }

    public UserInSearchView(User user) {
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
    }
}
