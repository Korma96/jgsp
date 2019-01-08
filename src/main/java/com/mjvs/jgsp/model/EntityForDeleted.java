package com.mjvs.jgsp.model;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass // za ovu klasu nece biti napravljen tabela u bazi
@Where(clause="deleted = 0")
public abstract class EntityForDeleted {
    @Column(name = "deleted", unique = false, nullable = false)
    protected boolean deleted;

    protected EntityForDeleted() { this.deleted = false; }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
