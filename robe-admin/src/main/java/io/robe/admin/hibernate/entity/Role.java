package io.robe.admin.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.robe.auth.data.entry.RoleEntry;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Role extends BaseEntity implements RoleEntry {

    @Length(min = 2, max = 32)
    @NotEmpty
    @Column(length = 32, unique = true, nullable = false)
    private String code;

    @Length(min = 3, max = 50)
    @NotEmpty
    @Column(length = 50, nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return getOid();
    }
}
