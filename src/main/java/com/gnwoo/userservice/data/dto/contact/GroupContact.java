package com.gnwoo.userservice.data.dto.contact;

import com.gnwoo.userservice.data.database.table.entity.GroupEntity;

public class GroupContact extends ContactBase {
    private final String groupName;

    public GroupContact(GroupEntity group, String rID) {
        super(rID);
        this.groupName = group.getGroupName();
    }

    public String getGroupName() {
        return groupName;
    }
}
