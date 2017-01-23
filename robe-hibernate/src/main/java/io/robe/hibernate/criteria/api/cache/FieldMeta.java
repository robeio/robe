package io.robe.hibernate.criteria.api.cache;

/**
 * Created by kamilbukum on 12/01/2017.
 */
public class FieldMeta {
    private final boolean searchIgnore;
    private final boolean hasRelation;
    private final boolean isTransient;
    private final Class<?> type;
    private final FieldReference reference;

    public FieldMeta(Class<?> type, boolean isTransient, boolean searchIgnore, boolean hasRelation){
        this(type, null, isTransient, searchIgnore, hasRelation);
    }

    public FieldMeta(Class<?> type, FieldReference reference, boolean isTransient, boolean searchIgnore, boolean hasRelation){
        this.type = type;
        this.reference = reference;
        this.isTransient = isTransient;
        this.searchIgnore = searchIgnore;
        this.hasRelation = hasRelation;

    }

    public FieldReference getReference() {
        return reference;
    }

    public Class<?> getType() {
        return this.type;
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
}
