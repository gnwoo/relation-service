package com.gnwoo.userservice.data.dto.invite;

public class InviteBase {
    private final Long inviteID;

    public InviteBase(Long inviteID) {
        this.inviteID = inviteID;
    }

    public Long getInviteID() {
        return inviteID;
    }
}
