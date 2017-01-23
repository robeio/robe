package io.robe.hibernate.criteria.query;

import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.Validations;
import io.robe.common.utils.reflection.Fields;
import io.robe.hibernate.criteria.api.*;
import io.robe.hibernate.criteria.api.cache.FieldReference;
import io.robe.hibernate.criteria.api.criterion.Restrictions;
import io.robe.hibernate.criteria.api.projection.Projection;
import io.robe.hibernate.criteria.api.projection.ProjectionList;
import io.robe.hibernate.criteria.api.cache.EntityMeta;
import io.robe.hibernate.criteria.api.cache.FieldMeta;
import io.robe.hibernate.criteria.api.criterion.Restriction;
import io.robe.hibernate.criteria.api.projection.Projections;

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
     * @param queries
     * @param joinedClassNames
     * @param reference
     */
    static <E> void configureCriteriaByQ(CriteriaParent<E> criteria, String[] queries, Set<String> joinedClassNames, FieldReference reference){
        List<Restriction> restrictions = new LinkedList<>();
        if(reference == null) {
            for(Map.Entry<String, FieldMeta> entry:  criteria.getMeta().getFieldMap().entrySet()) {
                FieldMeta fieldMeta = entry.getValue();
                if(!fieldMeta.isSearchIgnore() && !fieldMeta.isTransient() && fieldMeta.getField().getType().equals(String.class)) {
                    String fieldName = entry.getKey();
                    configureQForField(criteria, fieldName, fieldMeta, queries, joinedClassNames, restrictions);
                }
            }
        } else if(reference.getFilters() != null && reference.getFilters().length > 0) {
            for(String filter: reference.getFilters()) {
                FieldMeta fieldMeta =criteria.getMeta().getFieldMap().get(filter);
                if(!fieldMeta.isSearchIgnore() && !fieldMeta.isTransient() && fieldMeta.getField().getType().equals(String.class)) {
                    configureQForField(criteria, filter, fieldMeta, queries, joinedClassNames, restrictions);
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
     * @param queries
     * @param joinedClassNames
     * @param restrictions
     * @param <E>
     */
    private static <E> void configureQForField(CriteriaParent<E> criteria, String fieldName, FieldMeta fieldMeta, String[] queries, Set<String> joinedClassNames, List<Restriction> restrictions) {
        if(fieldMeta.getReference() == null) { // @SearchFrom is not defined.
            // create filter
            for(int i = 0 ; i < queries.length; i++) {
                Operator op = Operator.Q;
                String variableAlias = "$_query_" + i;
                String rawValue = queries[i];
                Object value = getValue(op, rawValue, fieldMeta.getField().getType());
                Restriction restriction = Restrictions.filter(fieldName, op, value, variableAlias);
                restrictions.add(restriction);
            }
        } else if(fieldMeta.getReference().getFilters() != null && fieldMeta.getReference().getFilters().length > 0) {
            String className = fieldMeta.getReference().getTargetEntity().getName();
            if(joinedClassNames.contains(className)) {
                return;
            }
            joinedClassNames.add(className);
            CriteriaJoin<E> criteriaJoin = addOrGetJoin(fieldName, fieldMeta, criteria);
            configureCriteriaByQ(criteriaJoin, queries, joinedClassNames, fieldMeta.getReference());
        }
    }

    /**
     *
     * @param criteria
     * @param filters
     * @param <E>
     */
    static <E> void configureFilters(CriteriaParent<E> criteria, String[][] filters) {
        if(filters == null || filters.length == 0) return;
        List<Restriction> restrictions = new LinkedList<>();
        for(String[] filter: filters) {
            String name = filter[0];
            Operator operator = Operator.value(filter[1]);
            String rawValue = filter[2];
            Parent<E> parent = new Parent<>(criteria, name);
            if(!createCriteriaByGivenName(parent)) continue;
            FieldMeta fieldMeta = parent.criteria.getMeta().getFieldMap().get(parent.name);
            String valueAlias = name.replace("\\.", "_");
            Object value = getValue(operator, rawValue, fieldMeta.getField().getType());
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
     * @param sorts
     * @param <E>
     */
    public static <E> void configureSorts(CriteriaParent<E> criteria, String[] sorts){
        if( sorts == null || sorts.length == 0) return;
        for(String sort: sorts) {
            if(Validations.isEmptyOrNull(sort)) continue;
            sort = sort.trim();
            if(sort.length() < 2) continue;
            String op = sort.substring(0, 1);
            Order.Type type = Order.Type.value(op);
            sort = sort.substring(1);
            Parent<E> parent = new Parent<>(criteria, sort);
            if(!createCriteriaByGivenName(parent)) continue;
            Order order = type == Order.Type.ASC ? Order.asc(parent.name): Order.desc(parent.name);
            parent.criteria.addOrder(order);
        }
    }

    public static <E> void configureSelectFields(Criteria<E> criteria, SearchModel search){
        switch (criteria.getTransformer().getTransformType()) {
            case ENTITY:
                return;
            case DTO:
                QueryUtility.configureDtoSelects(criteria);
                break;
            case MAP:
                if(search.getFields() != null && search.getFields().length > 0) {
                    QueryUtility.configureFields(criteria, search.getFields());
                }
                break;
        }
    }

    public static <E> void configureDtoSelects(Criteria<E> criteria){
        EntityMeta transformerMeta = criteria.getTransformer().getMeta();
        for(Map.Entry<String, FieldMeta> entry: transformerMeta.getFieldMap().entrySet()) {
            FieldMeta fieldMeta = entry.getValue();
            String fieldName  = fieldMeta.hasRelation() ? fieldMeta.getRelationName() : entry.getKey();
            Parent<E> parent = new Parent<>(criteria, fieldName);
            ProjectionList projectionList = configureField(parent);
            if(projectionList != null) {
                projectionList.add(Projections.alias(Projections.property(parent.name), entry.getKey()));
            }
        }
    }
    /**
     *
     * @param criteria
     * @param fields
     * @param <E>
     */
    static <E> void configureFields(CriteriaParent<E> criteria, String[] fields){
        if( fields == null || fields.length == 0) return;
        for(String field: fields) {
            if(Validations.isEmptyOrNull(field)) continue;
            field = field.trim();
            Parent<E> parent = new Parent<>(criteria, field);
            ProjectionList projectionList = configureField(parent);
            if(projectionList != null) {
                projectionList.add(Projections.property(parent.name));
            }
        }
    }

    static <E> ProjectionList configureField(Parent<E> parent) {
        if(!createCriteriaByGivenName(parent)) return null;
        ProjectionList projection = (ProjectionList) parent.criteria.getProjection();
        if(parent.criteria.getProjection() == null) {
            projection = Projections.projectionList();
            parent.criteria.setProjection(projection);
        }
        return projection;
    }

    /**
     *
     */
    private static class Parent<E> {
        CriteriaParent<E> criteria;
        String name;

        public Parent(CriteriaParent<E> criteria, String name){
            this.criteria = criteria;
            this.name = name;
        }
    }

    /**
     *
     * @param parent
     * @param <E>
     * @return
     */
    private static <E> boolean createCriteriaByGivenName(Parent<E> parent){
        EntityMeta meta = parent.criteria.getMeta();
        String[] names;
        if(meta.getFieldMap().get(parent.name) != null ) {
            names = new String[] {parent.name};
        } else {
            names = parent.name.split("\\.");
        }
        int lastParentIndex = names.length -1;
        int step = 0;

        FieldMeta fieldMeta;
        if(lastParentIndex <= step) {
            fieldMeta = meta.getFieldMap().get(names[0]);
            if(fieldMeta == null || fieldMeta.isTransient() ) {
                return false;
            }
        } else {
            while (lastParentIndex > step) {
                String fieldName = names[step];
                fieldMeta = meta.getFieldMap().get(fieldName);
                if(fieldMeta == null) return false;
                if(fieldMeta.getReference() == null) {
                    throw new RuntimeException("Field is parent field. @SearchFrom is not defined on field ! ");
                }
                Class<?> joinClass = fieldMeta.getReference().getTargetEntity();
                EntityMeta joinMeta  = parent.criteria.getTransformer().getMeta(joinClass);
                parent.criteria = addOrGetJoin(names[step], fieldMeta, parent.criteria);
                meta = joinMeta;
                step++;
            }
        }
        parent.name = names[step];
        return true;
    }

    /**
     *
     * @param name
     * @param meta
     * @param criteriaParent
     * @return
     */
    private static <E> CriteriaJoin<E> addOrGetJoin(String name, FieldMeta meta, CriteriaParent<E> criteriaParent){
        Class<?> joinClass = meta.getReference().getTargetEntity();
        CriteriaJoin<E> join = criteriaParent.getJoin(name);
        if(join == null) {
            join = criteriaParent.createJoin(name, joinClass, name);
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
