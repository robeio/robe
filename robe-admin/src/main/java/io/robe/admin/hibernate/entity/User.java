package io.robe.admin.hibernate.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.robe.auth.data.entry.UserEntry;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class User extends BaseEntity implements UserEntry {

    @Column(unique = true, length = 50)
    private String email;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String surname;

    @JsonIgnore
    @Column(length = 64, nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private int failCount = 0;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleOid")
    private Role role;

    @JsonIgnore
    @JsonManagedReference("ticket")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Ticket.class, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<Ticket>();

    @Column
    private Date lastLoginTime;
    private Date lastLogoutTime;

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    @JsonIgnore
    @Override
    public String getUserId() {
        return getOid();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    @Override
    public String getUsername() {
        return getEmail();
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("email='").append(email).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", password='").append("******").append('\'');
        sb.append(", active=").append(active);
        sb.append(", failCount=").append(failCount);
        sb.append(", role=").append(role);
        sb.append(", tickets=").append(tickets);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", lastLogoutTime=").append(lastLogoutTime);
        sb.append('}');
        return sb.toString();
    }
}
