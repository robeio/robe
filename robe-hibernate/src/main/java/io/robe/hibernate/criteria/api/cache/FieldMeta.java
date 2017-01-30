package io.robe.hibernate.criteria.api.cache;

import java.lang.reflect.Field;

/**
 * Created by kamilbukum on 12/01/2017.
 */
public class FieldMeta {
    private final boolean searchIgnore;
    private final boolean hasRelation;
    private final boolean isTransient;
    private final boolean collection;
    private final Field field;
    private final FieldReference reference;
    private String relationName;

    public FieldMeta(Field field, boolean isTransient, boolean searchIgnore, boolean hasRelation, boolean collection){
        this(field, null, isTransient, searchIgnore, hasRelation, collection);
    }

    public FieldMeta(Field field, FieldReference reference, boolean isTransient, boolean searchIgnore, boolean hasRelation, boolean collection){
        this.field = field;
        this.reference = reference;
        this.isTransient = isTransient;
        this.searchIgnore = searchIgnore;
        this.hasRelation = hasRelation;
        this.collection = collection;

    }

    public FieldReference getReference() {
        return reference;
    }

    public boolean isTransient() {
        return isTransient;
    }

    public boolean isSearchIgnore() {
        return searchIgnore;
    }

    public boolean hasRelation(){
        return this.hasRelation;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getRelationName() {
        return relationName;
    }

    public Field getField() {
        return field;
    }

    public boolean isCollection() {
        return collection;
    }
}
