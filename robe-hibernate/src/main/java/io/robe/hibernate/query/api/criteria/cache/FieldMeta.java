package io.robe.hibernate.query.api.criteria.cache;

/**
 * Created by kamilbukum on 12/01/2017.
 */
public class FieldMeta {
    private final boolean searchIgnore;
    private final Class<?> type;
    private final FieldReference reference;

    public FieldMeta(Class<?> type, boolean searchIgnore){
        this(type, null, searchIgnore);
    }

    public FieldMeta(Class<?> type, FieldReference reference, boolean searchIgnore){
        this.searchIgnore = searchIgnore;
        this.type = type;
        this.reference = reference;
    }

    public boolean isSearchIgnore() {
        return searchIgnore;
    }

    public FieldReference getReference() {
        return reference;
    }

    public Class<?> getType() {
        return type;
    }
}
