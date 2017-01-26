package io.robe.hibernate.criteria.api;

import io.robe.common.utils.Validations;
import io.robe.hibernate.criteria.api.criterion.Restriction;
import io.robe.hibernate.criteria.api.projection.Projection;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public final class CriteriaJoin<E> extends CriteriaParent<E> {
    private final CriteriaParent<E> parent;
    private final String referenceId;
    private final List<JoinRelation> joinRelations = new LinkedList<>();

    /**
     *
     * @param parent
     * @param entityClass
     * @param transformer
     * @param aliasesMap
     */
    CriteriaJoin(CriteriaParent<E> parent, Class<?> entityClass, Transformer<E> transformer, Map<String, Integer> aliasesMap) {
        this(parent, null , entityClass, transformer, null, aliasesMap);
    }

    /**
     *
     * @param parent
     * @param entityClass
     * @param transformer
     * @param referenceId
     * @param aliasesMap
     */
    CriteriaJoin(CriteriaParent<E> parent, String alias, Class<?> entityClass, Transformer<E> transformer, String referenceId, Map<String, Integer> aliasesMap) {
        super(alias, entityClass, transformer, aliasesMap);
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
    public CriteriaJoin<E> add(Restriction restriction) {
        super.add(restriction);
        return this;
    }

    @Override
    public CriteriaJoin<E> addOrder(Order order) {
        if(order.getCriteriaAlias() == null) {
            order.setCriteriaAlias(this.getAlias());
        }
        parent.addOrder(order);
        return this;
    }

    @Override
    public CriteriaJoin<E> setProjection(Projection projection) {
        super.setProjection(projection);
        return this;
    }

    public List<JoinRelation> getJoinRelations() {
        return joinRelations;
    }

    public CriteriaParent<E> getParent() {
        return parent;
    }

    public Criteria<E> getRoot() {
        return this.parent instanceof Criteria ? (Criteria<E>) this.parent : ((CriteriaJoin<E>)this.parent).getRoot();
    }

    public String getReferenceId() {
        return referenceId;
    }

    @Override
    public boolean isRoot() {
        return false;
    }
}
