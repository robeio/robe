package io.robe.admin.hibernate.entity;

import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Ticket extends BaseEntity {

    @Column(name = "ticketType")
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column
    private Date expirationDate;
    @Column(length = 32)
    private String userOid;

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public enum Type {
        CHANGE_PASSWORD,
        INIT_PASSWORD,
        ACTIVATE,
        FORGOT_PASSWORD,
        REGISTER
    }
}
