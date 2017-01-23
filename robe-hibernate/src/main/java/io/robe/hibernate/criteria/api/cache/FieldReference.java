package io.robe.hibernate.criteria.api.cache;
/**
 * Created by kamilbukum on 12/01/2017.
 */
public class FieldReference {
    private final Class<?> targetEntity;
    private String[] selects;
    private String[] filters;
    private final String identityName;
    private final String referenceId;

    public FieldReference(Class<?> targetEntity, String[] selects, String[] filters, String identityName, String referenceId) {
        this.targetEntity = targetEntity;
        this.selects = selects;
        this.filters = filters;
        this.identityName = identityName;
        this.referenceId = referenceId;
    }

    public Class<?> getTargetEntity() {
        return targetEntity;
    }

    public String[] getSelects() {
        return selects;
    }

    public void setSelects(String[] selects) {
        this.selects = selects;
    }

    public void setFilters(String[] filters) {
        this.filters = filters;
    }

    public String[] getFilters() {
        return filters;
    }

    public String getIdentityName() {
        return identityName;
    }

    public String getReferenceId() {
        return referenceId;
    }
}
