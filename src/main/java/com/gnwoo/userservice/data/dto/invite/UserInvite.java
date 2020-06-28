package com.gnwoo.userservice.data.dto.invite;

import com.gnwoo.userservice.data.database.table.entity.UserEntity;

public class UserInvite extends InviteBase {
    private final String username;
    private final String displayName;

    public UserInvite(UserEntity user, Long inviteID) {
        super(inviteID);
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
