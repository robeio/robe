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

    @Enumerated(EnumType.STRING)
    @Column(name = "tLang", nullable = false)
    private Type lang;

    @Column(name = "template", nullable = false)
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private Blob template;

    public Type getLang() {
        return lang;
    }

    public void setLang(Type lang) {
        this.lang = lang;
    }

    public Blob getTemplate() {
        return template;
    }

    public void setTemplate(Blob template) {
        this.template = template;
    }

}
