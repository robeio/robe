package io.robe.admin.hibernate.entity;

import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class SystemParameter extends BaseEntity {

    @Length(min = 1, max = 32)
    @NotEmpty
    @Column(unique = true,nullable = false, name = "PARAMETER_KEY",length = 32)
    private String key;

    @Length(min = 1, max = 32)
    @NotEmpty
    @Column(name = "PARAMETER_VALUE",nullable = false, length = 32)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SystemParameters{");
        sb.append("key='").append(key).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
