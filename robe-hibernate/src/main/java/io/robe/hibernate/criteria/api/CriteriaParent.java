package io.robe.hibernate.criteria.api;

import io.robe.common.utils.Strings;
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
    private final Map<String, CriteriaJoin<E>> joins = new LinkedHashMap<>();
    private final List<Restriction> restrictions = new LinkedList<>();
    private final EntityMeta meta;
    private Projection projection;
    private final Transformer<E> transformer;
    private final Map<String, Integer> aliasesMap;
    /**
     * @param entityClass
     */
    protected CriteriaParent(String alias, Class<?> entityClass, Transformer<E> transformer, Map<String, Integer> aliasesMap){
        this.entityClass = entityClass;
        this.aliasesMap = aliasesMap;
        this.transformer = transformer;

        alias = "$" + (alias != null ? alias : Strings.unCapitalizeFirstChar(entityClass.getSimpleName()));
        if(aliasesMap.containsKey(alias)) {
            Integer aliasCount = aliasesMap.getOrDefault(alias, 0);
            aliasCount++;
            aliasesMap.put(alias, aliasCount);
            alias = alias + "_" + aliasCount;
        }
        this.alias = alias;
        if(transformer.getTransformClass() != null && this.entityClass.getName().equals(transformer.getTransformClass().getName())) {
            this.meta = transformer.getMeta();
        } else {
            this.meta = transformer.getFinder().getEntityMeta(entityClass);
        }
    }

    /**
     *
     * creates {@link CriteriaParent}
     * @param entityClass
     * @return
     */
    public CriteriaJoin<E> createJoin(Class<?> entityClass) {
        return createJoin(null, entityClass, null);
    }
    /**
     *
     * creates {@link CriteriaParent}
     * @param entityClass
     * @return
     */
    public CriteriaJoin<E> createJoin(Class<?> entityClass, String referenceId) {
        return createJoin(null, entityClass, referenceId);
    }
    /**
     *
     * creates {@link CriteriaParent}
     * @param entityClass
     * @return
     */
    public CriteriaJoin<E> createJoin(String alias, Class<?> entityClass) {
        return createJoin(alias, entityClass, null);
    }
    /**
     *
     * creates {@link CriteriaParent}
     * @param entityClass
     * @return
     */
    public CriteriaJoin<E> createJoin(String alias, Class<?> entityClass, String referenceId) {
        CriteriaJoin<E> join = new CriteriaJoin<>(this, alias, entityClass, this.getTransformer(), referenceId, this.aliasesMap);
        joins.put(join.getAlias(), join);
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

    public CriteriaParent<E> add(Restriction restriction) {
        restrictions.add(restriction);
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

    public abstract boolean isRoot();
}


