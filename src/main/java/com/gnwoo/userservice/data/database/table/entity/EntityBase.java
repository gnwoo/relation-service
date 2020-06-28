package com.gnwoo.userservice.data.database.table.entity;

import javax.persistence.*;

@MappedSuperclass
public class EntityBase {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long entityID;
    @Column(nullable = false)
    private final String type;

    public EntityBase(String type) {
        this.type = type;
    }

    public String getEntityID() {
        return String.format("%s_%s", this.type, this.entityID);
    }

    public String getType() {
        return type;
    }
}
