package io.robe.hibernate.criteria.impl.hql;

import io.robe.common.utils.Strings;
import io.robe.common.utils.reflection.Fields;
import io.robe.common.utils.Validations;
import io.robe.hibernate.criteria.api.ResultPair;
import io.robe.hibernate.criteria.api.criterion.Filter;
import io.robe.hibernate.criteria.api.criterion.JoinCriteria;
import io.robe.hibernate.criteria.api.criterion.RootCriteria;
import io.robe.hibernate.criteria.api.criterion.SearchCriteria;
import io.robe.hibernate.criteria.api.query.SearchQuery;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by kamilbukum on 30/11/16.
 */
public abstract class HqlConverterUtil {

    private static final HqlRestrictions RESTRICTIONS = new HqlRestrictions();
    private static final String ALIAS_SUFFIX = "_alias";

    public static  ResultPair<String, Map<String, Object>> list(RootCriteria criteria) {

        ResultPair<String, Map<String, Object>> resultPair = new ResultPair<>();

        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> paramMap = new LinkedHashMap<>();

        queryBuilder
                .append(getSelections(criteria))
                .append(getFromAndWhere(criteria, paramMap))
                .append(" ")
                .append(getSorts(criteria));

        // set list query
        resultPair.setLeft(queryBuilder.toString());
        // set parameters
        resultPair.setRight(paramMap);
        return  resultPair;
    }

    public static  ResultPair<ResultPair<String, String>, Map<String, Object>> listWithCount(RootCriteria criteria){

        ResultPair<ResultPair<String, String>, Map<String, Object>> resultPair = new ResultPair<>();

        StringBuilder listBuilder = new StringBuilder();
        StringBuilder countBuilder = new StringBuilder();
        // add selection to query
        listBuilder.append(getSelections(criteria));
        countBuilder.append("SELECT count(*) ");

        Map<String, Object> paramMap = new LinkedHashMap<>();

        String fromAndWhere = getFromAndWhere(criteria, paramMap);
        listBuilder.append(fromAndWhere);
        countBuilder.append(fromAndWhere);

        listBuilder.append(" ").append(getSorts(criteria));

        ResultPair<String, String> queryResultPair = new ResultPair<>();
        queryResultPair.setLeft(listBuilder.toString());
        queryResultPair.setRight(countBuilder.toString());
        resultPair.setLeft(queryResultPair);
        resultPair.setRight(paramMap);
        return resultPair;
    }


    public static String getSelections(RootCriteria criteria){
        StringJoiner joiner = new StringJoiner(",");

        String result = "SELECT ";
        if(criteria.getOrderedSelects().size() == 0) {
            return result + criteria.getAlias();
        }

        for(String select: criteria.getOrderedSelects()) {
            String[] selectFields = select.split("\\.");
            switch (selectFields.length) {
                case 1:
                    joiner.add(criteria.getAlias() + "." + selectFields[0] + " AS " + selectFields[0]);
                    break;
                case 2:
                    String fieldAlias = selectFields[0] + Strings.capitalizeFirstChar(selectFields[1]);
                    joiner.add(selectFields[0] + ALIAS_SUFFIX + "." + selectFields[1] + " AS " + fieldAlias);
                    break;
            }
        }
        return result + joiner.toString();
    }

    public static String getFromAndWhere(RootCriteria criteria, Map<String, Object> variableMap){
        StringBuilder builder = new StringBuilder("");
        builder
                .append(" FROM ")
                .append(criteria.getEntityClass().getName())
                .append(" ")
                .append(criteria.getAlias())
                .append(" \n");
        if(criteria.getCriteriaMap() !=  null && criteria.getCriteriaMap().size() > 0 ) {
            for(Map.Entry<String, JoinCriteria> entry : criteria.getCriteriaMap().entrySet()) {
                JoinCriteria joinCriteria = entry.getValue();
                String alias = joinCriteria.getAlias() + ALIAS_SUFFIX;

                builder
                        .append(" JOIN ")
                        .append(joinCriteria.getEntityClass().getName())
                        .append(" ")
                        .append(alias)
                        .append(" ON ")
                        .append(criteria.getAlias()).append(".").append(joinCriteria.getReferenceId())
                        .append("=")
                        .append(alias).append(".").append(joinCriteria.getIdColumn()).append("\n");


                // if filter is exist
                if(criteria.getFilters().size() > 0) {
                    builder.append(" AND ( " ).append(getCriterions(alias, joinCriteria, variableMap)).append(" ) ");
                }
            }
        }

        if(criteria.getFilters().size() > 0 ){
            builder.append("WHERE ")
                    .append(getCriterions(criteria.getAlias(), criteria, variableMap));
        }

        return builder.toString();
    }

    private static String getCriterions(String alias,Class<?> entityClass, Map<String, Filter> filterMap,Map<String, Object> variableMap, String delimiter, String suffix) {
        StringJoiner joiner = new StringJoiner(delimiter);
        Map<String, Field> fieldMap = SearchQuery.CacheFields.getCachedFields(entityClass);
        for(Map.Entry<String, Filter> filterEntry: filterMap.entrySet()) {
            String key = alias + "." + filterEntry.getKey();
            String variable = key.replace(".", suffix);

            String criterion = RESTRICTIONS.filter(
                    key,
                    filterEntry.getValue().getOperator(),
                    filterEntry.getValue().getValue(),
                    variable);

            if(criterion != null) {
                if(!Validations.isEmptyOrNull(filterEntry.getValue().getValue())) {
                    Field field = fieldMap.get(filterEntry.getKey());
                    variableMap.put(variable, getValue(
                            filterEntry.getValue().getOperator(),
                            filterEntry.getValue().getValue(),
                            field
                    ));
                }
                joiner.add(criterion);
            }
        }
        return joiner.toString();
    }

    public static String getCriterions(String alias, SearchCriteria criteria, Map<String, Object> variableMap) {

        // AND Criterion;
        String criterion = getCriterions(
                alias,
                criteria.getEntityClass(),
                criteria.getFilters(),
                variableMap,
                "AND",
                "_"
        );

        // search on q field
        if(criteria.getOrFilterMap().size() > 0) {
            String orCondition = getCriterions(
                    alias,
                    criteria.getEntityClass(),
                    criteria.getOrFilterMap(),
                    variableMap,
                    " OR ",
                    "__"
            );

            if(!Validations.isEmptyOrNull(orCondition)) {
                if(Validations.isEmptyOrNull(criterion)) {
                   criterion =  ( " + orCondition + " );
                } else {
                    criterion = criterion + " AND ( " + orCondition + " )";
                }
            }

        }
        return criterion;
    }

    public static String getSorts(RootCriteria criteria){
        StringJoiner joiner = new StringJoiner(",");

        if(criteria.getOrderedSortMap() == null || criteria.getOrderedSortMap().size() == 0) {
            return "";
        }

        for(Map.Entry<String, SearchCriteria.SortType> sortTypeEntry: criteria.getOrderedSortMap().entrySet()) {
            String[] sorts = sortTypeEntry.getKey().split("\\.");
            switch (sorts.length) {
                case 1:
                    joiner.add(criteria.getAlias() + "." + sorts[0]);
                    break;
                case 2:
                    joiner.add(sorts[0] + ALIAS_SUFFIX + "." + sorts[1]);
                    break;
            }
        }
        return "ORDER BY " + joiner.toString();
    }

    public static Object getValue(String operator, String rawValue, Field field){
        if(operator.equals("|=")) {
            String[] svalues = rawValue.split("\\|");
            List<Object> lvalues = new LinkedList<>();
            for (String svalue : svalues) {
                lvalues.add(Fields.castValue(field, svalue));
            }
            return lvalues;
        }
        return Fields.castValue(field, rawValue);
    }
}
