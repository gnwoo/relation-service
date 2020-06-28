package com.gnwoo.userservice.data.dto.searchresult;

import com.gnwoo.userservice.data.database.table.entity.UserEntity;

public class UserSearchResult {
    private final String username;
    private final String displayName;

    public UserSearchResult(UserEntity user) {
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
