package com.gnwoo.userservice.data.table;

import javax.persistence.*;

@Entity
public class Invite {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long uuidA;
    @Column(nullable = false)
    private Long uuidB;

    public Invite() { }

    public Invite(Long uuidA, Long uuidB) {
        this.uuidA = uuidA;
        this.uuidB = uuidB;
    }

    public Long getId() {
        return id;
    }

    public Long getUuidA() {
        return uuidA;
    }

    public Long getUuidB() {
        return uuidB;
    }
}
