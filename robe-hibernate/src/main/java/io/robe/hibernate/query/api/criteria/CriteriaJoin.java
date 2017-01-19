package io.robe.hibernate.query.api.criteria;

import io.robe.common.utils.Validations;
import io.robe.hibernate.query.api.query.Transformer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public final class CriteriaJoin extends CriteriaParent {
    private final CriteriaParent parent;
    private final String referenceId;
    private final List<JoinRelation> joinRelations = new LinkedList<>();
    /**
     * @param alias
     * @param entityClass
     */
    CriteriaJoin(CriteriaParent parentCriteriaParent, String alias, Class<?> entityClass, String identityName) {
        this(parentCriteriaParent, alias, entityClass, identityName, null);
    }
    /**
     * @param alias
     * @param entityClass
     */
    CriteriaJoin(CriteriaParent parent, String alias, Class<?> entityClass, String identityName, String referenceId) {
        super(alias, entityClass);
        this.parent = parent;
        this.referenceId = referenceId;
        if(!Validations.isEmptyOrNull(this.referenceId)) {
            this.addRelation(identityName, referenceId, parent);
        }
    }

    public CriteriaJoin addRelation(String name, String relationName){
        addRelation(name, relationName, parent);
        return this;
    }

    public CriteriaJoin addRelation(String joinField, String referenceField, CriteriaParent referenceCriteria){
        joinRelations.add(new JoinRelation(this, joinField, referenceCriteria, referenceField));
        return this;
    }

    public List<JoinRelation> getJoinRelations() {
        return joinRelations;
    }

    public CriteriaParent getParent() {
        return parent;
    }

    public String getReferenceId() {
        return referenceId;
    }

    @Override
    <E> Transformer<E> getTransformer() {
        return parent.getTransformer();
    }
}
