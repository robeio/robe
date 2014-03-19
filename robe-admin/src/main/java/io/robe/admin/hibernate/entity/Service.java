package io.robe.admin.hibernate.entity;

import io.robe.auth.data.entry.ServiceEntry;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Service extends BaseEntity implements ServiceEntry {

    @Column(length = 100)
    private String path;

    @Enumerated(EnumType.STRING)
    private Method method; // 0-GET,1-PUT,2-POST,3-DELETE,4-OPTIONS,


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
