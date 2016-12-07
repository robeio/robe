package io.robe.hibernate.criteria.api.criterion;

/**
 * Holds join criteria information.
 */
public class JoinCriteria extends SearchCriteria {
    /**
     * Holds Parent {@link SearchCriteria}
     */
    private final SearchCriteria parent;
    /**
     * Holds name of @{@link javax.persistence.Id} Entity
     */
    private final String idColumn;
    /**
     * Holds reference id ( foreign key name) of entity in Relation Entity.
     */
    private final String referenceId;

    /**
     *
     * @param searchCriteria
     * @param alias
     * @param entityClass
     * @param idColumn
     * @param referenceId
     */
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
