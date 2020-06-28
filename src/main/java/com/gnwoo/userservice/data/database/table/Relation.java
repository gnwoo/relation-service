package com.gnwoo.userservice.data.database.table;

import javax.persistence.*;

@Entity
public class Relation {
    @Id
    private String rID;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String entityIDA;
    @Column(nullable = false)
    private String entityIDB;

    public Relation() { }

    public Relation(Invite invite) {
        this.type = invite.getType();
        this.entityIDA = invite.getEntityIDA();
        this.entityIDB = invite.getEntityIDB();
    }

    public String getrID() {
        return rID;
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
