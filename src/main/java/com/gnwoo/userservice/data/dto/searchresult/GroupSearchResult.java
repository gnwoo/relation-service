package com.gnwoo.userservice.data.dto.searchresult;

import com.gnwoo.userservice.data.database.table.entity.GroupEntity;

public class GroupSearchResult {
    private final String groupID;
    private final String groupName;

    public GroupSearchResult(GroupEntity group) {
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
