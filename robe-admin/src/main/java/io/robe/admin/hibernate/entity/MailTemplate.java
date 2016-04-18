package io.robe.admin.hibernate.entity;

import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table
public class MailTemplate extends BaseEntity {

    @NotEmpty
    @Length(max = 32)
    @Column(name = "code", length = 32, nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Type language;

    @NotEmpty
    @Column(name = "mailTemplate", nullable = false)
    @Lob
    private char[] template;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Type getLanguage() {
        return language;
    }

    public void setLanguage(Type language) {
        this.language = language;
    }

    public char[] getTemplate() {
        return template;
    }

    public void setTemplate(char[] template) {
        this.template = template;
    }

    public enum Type {
        TR,
        EN;
    }
}
