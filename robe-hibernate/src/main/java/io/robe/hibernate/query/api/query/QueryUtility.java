package io.robe.hibernate.query.api.query;

import io.robe.common.utils.Strings;
import io.robe.common.utils.Validations;
import io.robe.common.utils.reflection.Fields;
import io.robe.hibernate.query.api.criteria.CriteriaJoin;
import io.robe.hibernate.query.api.criteria.CriteriaParent;
import io.robe.hibernate.query.api.criteria.Order;
import io.robe.hibernate.query.api.criteria.cache.EntityMeta;
import io.robe.hibernate.query.api.criteria.cache.FieldMeta;
import io.robe.hibernate.query.api.criteria.cache.FieldReference;
import io.robe.hibernate.query.api.criteria.criterion.Restriction;
import io.robe.hibernate.query.api.criteria.criterion.Restrictions;
import io.robe.hibernate.query.api.criteria.projection.ProjectionList;
import io.robe.hibernate.query.api.criteria.projection.Projections;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kamilbukum on 17/01/2017.
 */
public class QueryUtility {
    /**
     *
     * @param criteria
     * @param meta
     * @param transformer
     * @param queries
     * @param joinedClassNames
     * @param reference
     * @param <E>
     */
    static <E> void configureQ(CriteriaParent criteria, EntityMeta meta, Transformer<E> transformer, String[] queries, Set<String> joinedClassNames, FieldReference reference) {
        List<Restriction> restrictions = new LinkedList<>();
        if(reference == null) {
            for(Map.Entry<String, FieldMeta> entry:  meta.getFieldMap().entrySet()) {
                FieldMeta fieldMeta = entry.getValue();
                if(!fieldMeta.isSearchIgnore() && fieldMeta.getType().equals(String.class)) {
                    String fieldName = entry.getKey();
                    configureQForField(fieldName, fieldMeta, criteria, transformer, queries, joinedClassNames, restrictions);
                }
            }
        } else if(reference.getFilters() != null && reference.getFilters().length > 0) {
            for(String filter: reference.getFilters()) {
                String fieldName = filter;
                FieldMeta fieldMeta =meta.getFieldMap().get(filter);
                if(!fieldMeta.isSearchIgnore() && fieldMeta.getType().equals(String.class)) {
                    configureQForField(fieldName, fieldMeta, criteria, transformer, queries, joinedClassNames, restrictions);
                }
            }
        }
        if(restrictions.size() > 0) {
            criteria.add(restrictions.size() == 1 ? restrictions.get(0): Restrictions.or(restrictions));
        }
    }

    /**
     *
     * @param fieldName
     * @param fieldMeta
     * @param criteria
     * @param transformer
     * @param queries
     * @param joinedClassNames
     * @param restrictions
     * @param <E>
     */
    static <E> void configureQForField(String fieldName, FieldMeta fieldMeta, CriteriaParent criteria, Transformer<E> transformer, String[] queries, Set<String> joinedClassNames, List<Restriction> restrictions) {
        if(fieldMeta.getReference() == null) { // @SearchFrom is not defined.
            // create filter
            for(int i = 0 ; i < queries.length; i++) {
                Operator op = Operator.Q;
                String variableAlias = "$_query_" + i;
                String rawValue = queries[i];
                Object value = getValue(op, rawValue, fieldMeta.getType());
                Restriction restriction = Restrictions.filter(fieldName, op, value, variableAlias);
                restrictions.add(restriction);
            }
        } else if(fieldMeta.getReference().getFilters() != null && fieldMeta.getReference().getFilters().length > 0) {
            String className = fieldMeta.getReference().getTargetEntity().getName();
            if(joinedClassNames.contains(className)) {
                return;
            }

            joinedClassNames.add(className);
            EntityMeta joinMeta  = Query.CachedEntity.getEntityMeta(fieldMeta.getReference().getTargetEntity(), transformer.getFinder());
            CriteriaJoin criteriaJoin = addOrGetJoin(fieldName, fieldMeta, criteria);
            configureQ(criteriaJoin, joinMeta, transformer, queries, joinedClassNames, fieldMeta.getReference());
        }
    }

    /**
     *
     * @param criteria
     * @param meta
     * @param transformer
     * @param filters
     * @param <E>
     */
    static <E> void configureFilters(CriteriaParent criteria, EntityMeta meta, Transformer<E> transformer, String[][] filters) {
        if(filters == null || filters.length == 0) return;
        List<Restriction> restrictions = new LinkedList<>();
        for(String[] filter: filters) {
            String name = filter[0];
            Operator operator = Operator.value(filter[1]);
            String rawValue = filter[2];
            Parent parent = new Parent(criteria, meta, name);
            createCriteriaByGivenName(parent, transformer);
            FieldMeta fieldMeta = parent.meta.getFieldMap().get(parent.name);
            String valueAlias = name.replace("\\.", "_");
            Object value = getValue(operator, rawValue, fieldMeta.getType());
            Restriction restriction = Restrictions.filter(parent.name, operator, value, valueAlias);
            restrictions.add(restriction);
        }
        if(restrictions.size() > 0) {
            criteria.add(restrictions.size() == 1 ? restrictions.get(0): Restrictions.and(restrictions));
        }
    }

    /**
     *
     * @param criteria
     * @param meta
     * @param transformer
     * @param sorts
     * @param <E>
     */
    public static <E> void configureSorts(CriteriaParent criteria, EntityMeta meta, Transformer<E> transformer, String[] sorts){
        if( sorts == null || sorts.length == 0) return;
        for(String sort: sorts) {
            if(Validations.isEmptyOrNull(sort)) continue;
            sort = sort.trim();
            if(sort.length() < 2) continue;
            String op = sort.substring(0, 0);
            Order.Type type = Order.Type.value(op);
            sort = sort.substring(1);
            Parent parent = new Parent(criteria, meta, sort);
            createCriteriaByGivenName(parent, transformer);
            Order order = type == Order.Type.ASC ? Order.asc(parent.name): Order.desc(parent.name);
            parent.criteria.addOrder(order);
        }
    }

    /**
     *
     * @param criteria
     * @param meta
     * @param transformer
     * @param fields
     * @param <E>
     */
    static <E> void configureFields(CriteriaParent criteria, EntityMeta meta, Transformer<E> transformer, String[] fields){
        if( fields == null || fields.length == 0) return;
        Parent parent = new Parent(criteria, meta, "");
        for(String field: fields) {
            if(Validations.isEmptyOrNull(field)) continue;
            field = field.trim();
            parent.name = field;
            createCriteriaByGivenName(parent, transformer);
            ProjectionList projection = (ProjectionList) parent.criteria.getProjection();
            if(parent.criteria.getProjection() == null) {
                projection = Projections.projectionList();
                parent.criteria.setProjection(projection);
            }
            projection.add(Projections.property(parent.name));
        }
    }

    /**
     *
     */
    private static class Parent {
        CriteriaParent criteria;
        EntityMeta meta;
        String name;

        public Parent( CriteriaParent criteria, EntityMeta meta, String name){
            this.criteria = criteria;
            this.meta = meta;
            this.name = name;
        }
    }

    /**
     *
     * @param parent
     * @param transformer
     * @param <E>
     * @return
     */
    static <E> Parent createCriteriaByGivenName(Parent parent, Transformer<E> transformer){
        String[] names = null;
        if(parent.meta.getFieldMap().get(parent.name) != null ) {
            names = new String[] {parent.name};
        } else {
            names = parent.name.split("\\.");
        }
        int lastParentIndex = names.length -1;
        int step = 0;
        while (lastParentIndex > step) {
            String fieldName = names[step];
            FieldMeta fieldMeta = parent.meta.getFieldMap().get(fieldName);
            if(fieldMeta.getReference() == null) {
                throw new RuntimeException("Field is parent field. @SearchFrom is not defined on field ! ");
            }
            Class<?> joinClass = fieldMeta.getReference().getTargetEntity();
            EntityMeta joinMeta  = Query.CachedEntity.getEntityMeta(joinClass, transformer.getFinder());
            CriteriaJoin criteriaJoin = addOrGetJoin(names[step], fieldMeta, parent.criteria);
            parent.criteria = criteriaJoin;
            parent.meta = joinMeta;
            step++;
        }
        parent.name = names[step];
        return parent;
    }

    /**
     *
     * @param name
     * @param meta
     * @param criteriaParent
     * @return
     */
    private static CriteriaJoin addOrGetJoin(String name, FieldMeta meta, CriteriaParent criteriaParent){
        Class<?> joinClass = meta.getReference().getTargetEntity();
        CriteriaJoin join = criteriaParent.getJoin(name);
        if(join == null) {
            String alias = Strings.unCapitalizeFirstChar(criteriaParent.getAlias() + "_" + joinClass.getSimpleName());
            join = criteriaParent.createJoin(alias, joinClass, name);
        }
        return join;
    }



    /**
     *
     * @param operator
     * @param rawValue
     * @param fieldType
     * @return
     */
    public static Object getValue(String operator, String rawValue, Class<?> fieldType){
        return getValue(Operator.value(operator), rawValue, fieldType);
    }

    /**
     *
     * @param operator
     * @param rawValue
     * @param fieldType
     * @return
     */
    public static Object getValue(Operator operator, String rawValue, Class<?> fieldType){
        if(Operator.IN.equals(operator)) {
            String[] svalues = rawValue.split("\\|");
            List<Object> lvalues = new LinkedList<>();
            for (String svalue : svalues) {
                lvalues.add(Fields.castValue(fieldType, svalue));
            }
            return lvalues;
        }
        return Fields.castValue(fieldType, rawValue);
    }
}
