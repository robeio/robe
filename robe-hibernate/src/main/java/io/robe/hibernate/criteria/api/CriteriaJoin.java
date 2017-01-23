package io.robe.hibernate.criteria.api;

import io.robe.common.utils.Validations;
import io.robe.hibernate.criteria.api.criterion.Restriction;
import io.robe.hibernate.criteria.api.projection.Projection;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public final class CriteriaJoin<E> extends CriteriaParent<E> {
    private final CriteriaParent<E> parent;
    private final String referenceId;
    private final List<JoinRelation> joinRelations = new LinkedList<>();
    /**
     * @param alias
     * @param entityClass
     */
    CriteriaJoin(CriteriaParent<E> parentCriteriaParent, String alias, Class<?> entityClass, Transformer<E> transformer) {
        this(parentCriteriaParent, alias, entityClass, transformer, null);
    }
    /**
     * @param alias
     * @param entityClass
     */
    CriteriaJoin(CriteriaParent parent, String alias, Class<?> entityClass, Transformer<E> transformer, String referenceId) {
        super(alias, entityClass, parent.getTransformer());
        this.parent = parent;
        this.referenceId = referenceId;
        if(!Validations.isEmptyOrNull(this.referenceId)) {
            this.addRelation(this.getMeta().getIdentityName(), referenceId, parent);
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

    @Override
    public CriteriaJoin<E> add(Restriction criterion) {
        super.add(criterion);
        return this;
    }

    @Override
    public CriteriaJoin<E> addOrder(Order order) {
        if(order.getAlias() == null) {
            order.setAlias(this.getAlias());
        }
        parent.addOrder(order);
        return this;
    }

    @Override
    public CriteriaJoin<E> setProjection(Projection projection) {
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
}
