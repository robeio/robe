package io.robe.hibernate.criteria.api.cache;

import java.util.Map;

/**
 * Created by kamilbukum on 12/01/2017.
 */
public class EntityMeta {
    private final String identityName;
    private final Map<String, FieldMeta> fieldMap;
    private final Map<String, String> relationMap;

    public EntityMeta(String identityName, Map<String, FieldMeta> fieldMap, Map<String, String> relationMap) {
        this.identityName = identityName;
        this.fieldMap = fieldMap;
        this.relationMap = relationMap;
    }

    public String getIdentityName() {
        return identityName;
    }

    public Map<String, String> getRelationMap() {
        return relationMap;
    }

    public Map<String, FieldMeta> getFieldMap() {
        return fieldMap;
    }

}
