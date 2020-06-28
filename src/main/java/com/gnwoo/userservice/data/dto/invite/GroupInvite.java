package com.gnwoo.userservice.data.dto.invite;

import com.gnwoo.userservice.data.database.table.entity.GroupEntity;

public class GroupInvite extends InviteBase {
    private final String groupID;
    private final String groupName;

    public GroupInvite(GroupEntity group, Long inviteID) {
        super(inviteID);
        this.groupID = group.getEntityID();
        this.groupName = group.getGroupName();
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }
}
