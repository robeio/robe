package io.robe.hibernate.criteria.api.criterion;

/**
 * Created by kamilbukum on 30/11/16.
 */
public class JoinCriteria extends SearchCriteria {
    private final SearchCriteria parent;
    private final String idColumn;
    private final String referenceId;

    public JoinCriteria(SearchCriteria searchCriteria, String alias, Class<?> entityClass, String idColumn, String referenceId) {
        super(alias, entityClass);
        this.parent = searchCriteria;
        this.idColumn = idColumn;
        this.referenceId = referenceId;
    }

    public SearchCriteria getParent() {
        return parent;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public String getReferenceId() {
        return referenceId;
    }

}
