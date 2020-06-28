package com.gnwoo.userservice.rpc.dto.request;

public class SignUpRequest {
    private String username;
    private String displayName;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
