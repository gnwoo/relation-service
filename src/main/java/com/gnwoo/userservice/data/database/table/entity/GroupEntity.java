package com.gnwoo.userservice.data.database.table.entity;

import javax.persistence.*;

@Entity
public class GroupEntity extends EntityBase {
    @Column(nullable = false)
    protected String groupName;
    @Column(nullable = false)
    protected String groupOwner;

    public GroupEntity() { super(null); }

    public String getGroupID() {
        return super.getEntityID();
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupOwner() {
        return groupOwner;
    }
}
