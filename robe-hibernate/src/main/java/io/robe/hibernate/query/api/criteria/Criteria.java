package io.robe.hibernate.query.api.criteria;

import io.robe.hibernate.query.api.query.Transformer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class Criteria<E> extends CriteriaParent {
    /**
     *
     */
    private Transformer<E> transformer;
    /**
     *
     */
    private List<Order> orders = new LinkedList<>();
    /**
     * Starting index for the paged fetches.
     */
    private Integer offset;
    /**
     * Maximum number of results per page.
     */
    private Integer limit;
    /**
     * @param alias
     * @param entityClass
     */
    protected Criteria(String alias, Class<?> entityClass, Transformer<E> transformer) {
        super(alias, entityClass);
        this.identityName = transformer.getFinder().getEntityMeta(entityClass).getIdentityName();
        this.transformer = transformer;
    }

    /**
     *
     * creates {@link CriteriaParent}
     * @param alias
     * @param entityClass
     * @return
     */
    public static <E> Criteria<E> createCriteria(String alias, Class<?> entityClass, Transformer<E> transformer) {
        return new Criteria<>(alias, entityClass, transformer);
    }

    /**
     * Sets starting index for the paged fetches.
     * @param offset
     */
    public Criteria<E> setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Sets maximum number of results per page.
     * @param limit
     */
    public Criteria<E> setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Gets starting index for the paged fetches.
     * @return
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * Gets maximum number of results per page.
     * @return
     */
    public Integer getLimit() {
        return limit;
    }

    public List<E> list(){
        assertTransformerIsExist();
        return transformer.list(this);
    }

    public Result<E> pairList(){
        assertTransformerIsExist();
        return transformer.pairList(this);
    }

    public Long count(){
        assertTransformerIsExist();
        return transformer.count(this);
    }


    public Object uniqueResult(){
        assertTransformerIsExist();
        return transformer.uniqueResult(this);
    }

    public Transformer<E> getTransformer() {
        return transformer;
    }

    private void assertTransformerIsExist(){
        if(transformer == null) {
            throw new RuntimeException("Transformer not provided for CriteriaParent");
        }
    }


    public List<Order> getOrders() {
        return orders;
    }
}
