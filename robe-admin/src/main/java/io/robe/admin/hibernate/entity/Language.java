package io.robe.admin.hibernate.entity;

import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity(name = "Language")
@Table
public class Language extends BaseEntity {

    @NotEmpty
    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false)
    private Type code;
    @Length(min = 2, max = 30)
    @NotEmpty
    @Column(name = "name", nullable = false,length = 30)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getCode() {
        return code;
    }

    public void setCode(Type lang) {
        this.code = lang;
    }

    public enum Type {
        TR,
        EN;
    }
}
