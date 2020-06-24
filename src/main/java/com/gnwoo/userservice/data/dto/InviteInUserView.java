package com.gnwoo.userservice.data.dto;

import com.gnwoo.userservice.data.table.User;

public class InviteInUserView {
    private Long id;
    private String username;
    private String displayName;

    public InviteInUserView() {
    }

    public InviteInUserView(Long id, User user) {
        this.id = id;
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
    }
}
