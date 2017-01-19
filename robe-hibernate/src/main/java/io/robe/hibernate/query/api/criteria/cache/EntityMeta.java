package io.robe.hibernate.query.api.criteria.cache;

import java.util.Map;

/**
 * Created by kamilbukum on 12/01/2017.
 */
public class EntityMeta {
    private final String identityName;
    private final Map<String, FieldMeta> fieldMap;

    public EntityMeta(String identityName, Map<String, FieldMeta> fieldMap) {
        this.identityName = identityName;
        this.fieldMap = fieldMap;
    }

    public String getIdentityName() {
        return identityName;
    }

    public Map<String, FieldMeta> getFieldMap() {
        return fieldMap;
    }
}
