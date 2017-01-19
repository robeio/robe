package io.robe.hibernate.query.api.criteria;

import io.robe.hibernate.query.api.criteria.criterion.Restriction;
import io.robe.hibernate.query.api.criteria.projection.Projection;
import io.robe.hibernate.query.api.query.Transformer;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public abstract class CriteriaParent {
    /**
     * Entity Alias
     */
    private final String alias;
    /**
     * Entity class Type
     */
    private final Class<?> entityClass;
    private Map<String, CriteriaJoin> joins = new LinkedHashMap<>();
    private List<Restriction> restrictions = new LinkedList<>();
    protected String identityName;
    private Projection projection;

    /**
     * @param alias
     * @param entityClass
     */
    protected CriteriaParent(String alias, Class<?> entityClass){
        this.alias = alias;
        this.entityClass = entityClass;
    }

    /**
     *
     * creates {@link CriteriaParent}
     * @param alias
     * @param entityClass
     * @return
     */
    public CriteriaJoin createJoin(String alias, Class<?> entityClass) {
        return createJoin(alias, entityClass, null);
    }
    /**
     *
     * creates {@link CriteriaParent}
     * @param alias
     * @param entityClass
     * @return
     */
    public CriteriaJoin createJoin(String alias, Class<?> entityClass, String referenceId) {
        this.identityName = getTransformer().getFinder().getEntityMeta(entityClass).getIdentityName();
        CriteriaJoin join = new CriteriaJoin(this, alias, entityClass, identityName, referenceId);
        joins.put(alias, join);
        return join;
    }

    public CriteriaJoin getJoin(String alias) {
        return joins.get(alias);
    }

    public Map<String, CriteriaJoin> getJoins() {
        return joins;
    }

    public Projection getProjection() {
        return projection;
    }

    public <T extends CriteriaParent> T add(Restriction criterion){
        restrictions.add(criterion);
        return (T)this;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }

    public String getAlias() {
        return alias;
    }


    public <T extends CriteriaParent> T addOrder(Order order) {
        order.setAlias(this.alias);
        addOrder(this, order);
        return (T)this;
    }
    private <T extends CriteriaParent> void addOrder(CriteriaParent criteria, Order order){
        if(criteria instanceof Criteria) {
            ((Criteria<T>)criteria).getOrders().add(order);
        } else {
            addOrder(((CriteriaJoin)criteria).getParent(), order);
        }
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getIdentityName() {
        return identityName;
    }

    public <T extends CriteriaParent> T setProjection(Projection projection) {
        this.projection = projection;
        return (T)this;
    }

    abstract <E>Transformer<E> getTransformer();
}


