package io.robe.hibernate.criteria.api;

import io.robe.hibernate.criteria.api.cache.EntityMeta;
import io.robe.hibernate.criteria.api.criterion.Restriction;
import io.robe.hibernate.criteria.api.projection.Projection;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public abstract class CriteriaParent<E> {
    /**
     * Entity Alias
     */
    private final String alias;
    /**
     * Entity class Type
     */
    private final Class<?> entityClass;
    private Map<String, CriteriaJoin<E>> joins = new LinkedHashMap<>();
    private List<Restriction> restrictions = new LinkedList<>();
    private EntityMeta meta;
    private Projection projection;
    private Transformer<E> transformer;

    /**
     * @param alias
     * @param entityClass
     */
    protected CriteriaParent(String alias, Class<?> entityClass, Transformer<E> transformer){
        this.alias = alias;
        this.entityClass = entityClass;
        this.transformer = transformer;
        this.meta = transformer.getFinder().getEntityMeta(entityClass);
    }

    /**
     *
     * creates {@link CriteriaParent}
     * @param alias
     * @param entityClass
     * @return
     */
    public CriteriaJoin<E> createJoin(String alias, Class<?> entityClass) {
        return createJoin(alias, entityClass, null);
    }
    /**
     *
     * creates {@link CriteriaParent}
     * @param alias
     * @param entityClass
     * @return
     */
    public CriteriaJoin<E> createJoin(String alias, Class<?> entityClass, String referenceId) {
        CriteriaJoin<E> join = new CriteriaJoin<E>(this, alias, entityClass, this.getTransformer(), referenceId);
        joins.put(alias, join);
        return join;
    }

    public CriteriaJoin<E> getJoin(String alias) {
        return joins.get(alias);
    }

    public Map<String, CriteriaJoin<E>> getJoins() {
        return joins;
    }

    public Projection getProjection() {
        return projection;
    }

    public CriteriaParent<E> add(Restriction criterion) {
        restrictions.add(criterion);
        return this;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }

    public String getAlias() {
        return alias;
    }

    public abstract CriteriaParent<E> addOrder(Order order);

    public EntityMeta getMeta() {
        return meta;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public CriteriaParent<E> setProjection(Projection projection) {
        this.projection = projection;
        return this;
    }

    public Transformer<E> getTransformer() {
        return transformer;
    }
}


