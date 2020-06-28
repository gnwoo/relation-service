package com.gnwoo.userservice.data.database.table.entity;

import com.gnwoo.userservice.rpc.database.table.User;

public class UserEntity extends EntityBase {
    private final User user;

    public UserEntity(User user) {
        super("u");
        this.user = user;
    }

    @Override
    public String getEntityID() {
        return user.getUuid();
    }

    public String getUUID() {
        return user.getUuid();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getDisplayName() {
        return user.getDisplayName();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
