package io.robe.hibernate.criteria.api.cache;

import java.util.Map;

/**
 * Created by kamilbukum on 12/01/2017.
 */
public class EntityMeta {
    private final String identityName;
    private final Map<String, FieldMeta> fieldMap;
    private final Map<String, String> fieldRelationMap;
    private final Map<String, FieldMeta> relationMap;

    public EntityMeta(String identityName, Map<String, FieldMeta> fieldMap, Map<String, FieldMeta> relationMap, Map<String, String> fieldRelationMap) {
        this.identityName = identityName;
        this.fieldMap = fieldMap;
        this.relationMap = relationMap;
        this.fieldRelationMap = fieldRelationMap;
    }

    public String getIdentityName() {
        return identityName;
    }

    public Map<String, FieldMeta> getFieldMap() {
        return fieldMap;
    }

    public Map<String, String> getFieldRelationMap() {
        return fieldRelationMap;
    }

    public Map<String, FieldMeta> getRelationMap(){
        return this.relationMap;
    }

}
