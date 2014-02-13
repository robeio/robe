package io.robe.hibernate.entity;

import javax.persistence.*;
import java.sql.Blob;

/**
 * Created by kaanalkim on 11/02/14.
 */
@Entity
@Table
public class MailTemplate extends BaseEntity {

    public enum Type {
        TR,
        EN;
    }

    @Column(name = "code", nullable = true, length = 25)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang", nullable = false)
    private Type lang;

    @Column(name = "template", nullable = false, columnDefinition = "Text")
    private String template;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Type getLang() {
        return lang;
    }

    public void setLang(Type lang) {
        this.lang = lang;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
