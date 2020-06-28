package com.gnwoo.userservice.data.database.table;

import com.gnwoo.userservice.data.database.table.entity.EntityBase;

import javax.persistence.*;

@Entity
public class Invite {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long inviteID;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String entityIDA;
    @Column(nullable = false)
    private String entityIDB;

    public Invite() { }

    public Invite(String type, String entityIDA, String entityIDB) {
        this.type = type;
        this.entityIDA = entityIDA;
        this.entityIDB = entityIDB;
    }

    public Long getInviteID() {
        return inviteID;
    }

    public String getType() {
        return type;
    }

    public String getEntityIDA() {
        return entityIDA;
    }

    public String getEntityIDB() {
        return entityIDB;
    }
}
