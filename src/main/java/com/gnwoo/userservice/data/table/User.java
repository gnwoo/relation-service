package com.gnwoo.userservice.data.table;

import javax.persistence.*;

@Entity
public class User {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long uuid;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String displayName;
    @Column(nullable = false)
    private String email;

    public User() { }

    public User(String username, String displayName, String email) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
    }

    public Long getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
