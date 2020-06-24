package com.gnwoo.userservice.data.table;

import javax.persistence.*;

@Entity
public class Relation {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long rID;
    @Column(nullable = false)
    private Long uuidA;
    @Column(nullable = false)
    private Long uuidB;

    public Relation() { }

    public Relation(Long uuidA, Long uuidB) {
        this.uuidA = uuidA;
        this.uuidB = uuidB;
    }

    public Long getrID() {
        return rID;
    }

    public Long getUuidA() {
        return uuidA;
    }

    public Long getUuidB() {
        return uuidB;
    }
}
