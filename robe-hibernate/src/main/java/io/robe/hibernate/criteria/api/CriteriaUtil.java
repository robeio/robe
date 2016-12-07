package io.robe.hibernate.criteria.api;


import io.robe.common.service.search.SearchFrom;
import io.robe.common.service.search.SearchIgnore;
import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.criteria.api.criterion.*;
import io.robe.hibernate.criteria.api.query.SearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CriteriaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchQuery.class);

    /**
     *
     * @param criteria
     * @param qValue
     */
    public static void configureQCriterions(RootCriteria criteria, String qValue) {
        Map<String, Field> parentFieldMap = SearchQuery.CacheFields.getCachedFields(criteria.getEntityClass());
        for(Map.Entry<String, Field>  entry: parentFieldMap.entrySet()) {
            Field field =  entry.getValue();

            // if field is not entity member or not searchable than pass
            if(
                    !field.getType().equals(String.class) ||
                    field.getAnnotation(Transient.class) != null ||
                            field.getAnnotation(SearchIgnore.class) != null ||
                            field.isSynthetic()
                    ) continue;


            SearchFrom from = field.getAnnotation(SearchFrom.class);

            if(from == null) { // Searchable field found
                criteria.addOrFilter(field.getName(), Operators.CONTAINS.getOp(), qValue);
            } else { // Searchable target entity found
                JoinCriteria joinCriteria = createOrGetJoinCriteriaByField(
                        criteria,
                        field,
                        field.getName()
                );
                if(joinCriteria != null) {
                    for(String select: from.select()) {
                        joinCriteria.addOrFilter(select, Operators.CONTAINS.getOp(), qValue);
                    }
                }
            }
        }
    }

    /**
     *
     * @param rootCriteria
     * @param filters
     */
    public static void configureFilters(RootCriteria rootCriteria, String[][] filters) {
        Map<String, Field> parentFieldMap = SearchQuery.CacheFields.getCachedFields(rootCriteria.getEntityClass());
        for(String[] filter: filters) {
            String[] names = filter[0].split("\\.");
            String name = names[0];
            String operator = filter[1];
            String value = filter.length > 2 ? filter[2] : null;
            Field field = parentFieldMap.get(name);

            if(field == null) {
                LOGGER.error(name + " field not found in " + rootCriteria.getEntityClass().getName());
                continue;
            }

            switch (names.length) {
                case 1:
                    rootCriteria.addFilter(name, operator, value);
                    break;
                case 2:
                    String targetName = names[1];
                    JoinCriteria joinCriteria = createOrGetJoinCriteriaByField(
                            rootCriteria,
                            field,
                            name
                    );
                    if(joinCriteria != null) {
                        joinCriteria.addFilter(targetName, operator, value);
                    }
            }
        }
    }

    /**
     *
     * @param criteria
     * @param sortings
     */
    public static void configureSortings(RootCriteria criteria, String[] sortings){
        Map<String, Field> parentFieldMap = SearchQuery.CacheFields.getCachedFields(criteria.getEntityClass());
        for(String sorting: sortings) {
            if(sorting.length() == 0) continue;

            SearchCriteria.SortType sortType = SearchCriteria.SortType.getSortType(sorting.charAt(0));
            if(sortType == null) {
                sortType = SearchCriteria.SortType.ASC;
            } else {
                sorting = sorting.substring(1);
            }

            String[] sorts = sorting.split("\\.");
            String sort = sorts[0];
            if(sort.length() == 0) continue;


            Field field = parentFieldMap.get(sort);

            if(field == null) {
                LOGGER.error(sort + " field not found in " + criteria.getEntityClass().getName());
                continue;
            }

            switch (sorts.length) {
                case 1:
                    criteria.getOrderedSortMap().put(sorting, sortType);
                    break;
                case 2:
                    JoinCriteria joinCriteria = createOrGetJoinCriteriaByField(
                            criteria,
                            field,
                            sort
                    );
                    if(joinCriteria != null) {
                        criteria.getOrderedSortMap().put(sorting, sortType);
                    }
                    break;
            }
        }
    }

    /**
     *
     * @param criteria
     * @param selectFields
     */
    public static void configureSelectFields(RootCriteria criteria, String[] selectFields){
        Map<String, Field> parentFieldMap = SearchQuery.CacheFields.getCachedFields(criteria.getEntityClass());
        for(String selectField: selectFields) {
            if(selectField.length() == 0) continue;
            String[] selects = selectField.split("\\.");
            String select = selects[0];
            if(select.length() == 0) continue;
            Field field = parentFieldMap.get(select);
            if(field == null) {
                LOGGER.error(field.getName() + " field not found in " + criteria.getEntityClass().getName());
                continue;
            }

            switch (selects.length) {
                case 1:
                    criteria.getOrderedSelects().add(select);
                    break;
                case 2:
                    JoinCriteria joinCriteria = createOrGetJoinCriteriaByField(
                            criteria,
                            field,
                            select
                    );
                    if(joinCriteria != null) {
                        criteria.getOrderedSelects().add(select);
                    }
                    break;
            }
        }
    }

    /**
     *
     * @param criteria
     * @param field
     * @param alias
     * @return
     */
    public static JoinCriteria createOrGetJoinCriteriaByField(RootCriteria criteria, Field field, String alias){
        JoinCriteria joinCriteria = criteria.getCriteria(alias);
        if(joinCriteria  == null) {
            SearchFrom searchFrom = field.getAnnotation(SearchFrom.class);
            if (searchFrom == null) {
                LOGGER.error("@SearchFrom annotation not found on " + field.getName() + " field ! ");
            } else {
                joinCriteria = criteria.createJoinQuery(
                        field.getName(),
                        searchFrom.entity(),
                        searchFrom.id(),
                        field.getName()
                );
            }
        }
        return joinCriteria;
    }

    /**
     *
     * @param entityClass
     * @return
     */
    public static List<String> fromEntityFields2SearchFields(Class<?> entityClass) {
        List<String> fieldList = new LinkedList<>();
        for(Field field: entityClass.getDeclaredFields()) {
            if(field.isSynthetic()) continue;
            Transient notPersistence = field.getAnnotation(Transient.class);
            if(notPersistence == null) {
                fieldList.add(field.getName());
            }
        }
        return fieldList;
    }

    /**
     *
     * @param entityClass
     * @return
     */
    public static String[] fromEntityFields2SearchFieldArray(Class<?> entityClass) {
        return fromEntityFields2SearchFields(entityClass).toArray(new String[]{});
    }

}
