package io.robe.hibernate.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Ticket extends BaseEntity<Ticket> {

    enum Type {
        CHANGE_PASSWORD,
        INIT_PASSWORD,
        ACTIVATE,
        FORGOT_PASSWORD
    }

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private Date expirationDate;

    @ManyToOne(targetEntity = User.class)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
