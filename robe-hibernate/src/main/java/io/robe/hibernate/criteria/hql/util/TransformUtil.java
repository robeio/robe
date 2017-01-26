package io.robe.hibernate.criteria.hql.util;

import io.robe.common.dto.Pair;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Order;
import io.robe.hibernate.criteria.hql.TransformerImpl;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class TransformUtil {

    public static <E> String generateDataQuery(Criteria<E> criteria, TransformerImpl<E> transformer, Map<String, Object> parameterMap) {
        // select
        Pair<String, String> selectAndGroupBy = SelectUtil.generateSelectQueryForList(criteria);
        StringBuilder builder = new StringBuilder("SELECT ").append(selectAndGroupBy.getLeft());
        // common quires -> { from, joins, restrictions }
        builder.append(generateCommonQueires(criteria, parameterMap));

        // GROUP BY QUERIES
        builder.append(selectAndGroupBy.getRight());
        // ORDER BY QUERIES
        builder.append(generateOrderQuery(criteria));
        return builder.toString();
    }

    public static <E> String generateCount(Criteria<E> criteria, Map<String, Object> parameterMap) {
        // select
        StringBuilder builder = new StringBuilder("SELECT ").append("count(*)");
        // common quires -> { from, joins, restrictions }
        builder.append(generateCommonQueires(criteria, parameterMap));
        return builder.toString();
    }

    public static <E> Pair<String, String> generatePairResult(Criteria criteria, Map<String, Object> parameterMap) {
        Pair<String, String> pairQuery = new Pair<>();
        // select
        Pair<String, String> selectAndGroupBy = SelectUtil.generateSelectQueryForList(criteria);
        StringBuilder listBuilder = new StringBuilder("SELECT ").append(selectAndGroupBy.getLeft());
        // select
        StringBuilder countBuilder = new StringBuilder("SELECT ").append("count(*)");

        // common quires -> { from, joins, restrictions }
        String commonQueries = generateCommonQueires(criteria, parameterMap);
        listBuilder.append(commonQueries);
        countBuilder.append(commonQueries);
        // GROUP BY QUERIES
        listBuilder.append(selectAndGroupBy.getRight());
        // ORDER BY QUERIES
        listBuilder.append(generateOrderQuery(criteria));
        pairQuery.setLeft(listBuilder.toString());
        pairQuery.setRight(countBuilder.toString());
        return pairQuery;
    }

    private static <E> String generateCommonQueires(Criteria<E> criteria, Map<String, Object> parameterMap) {
        StringBuilder builder = new StringBuilder();
        // from
        builder.append("\nFROM ").append(criteria.getEntityClass().getName()).append(" ").append(criteria.getAlias()).append(" ");
        StringJoiner restrictionJoiner = new StringJoiner(" AND ");
        StringJoiner qJoiner = new StringJoiner(" OR ");
        // append joins
        builder.append(JoinUtil.generateJoinQuery(criteria, restrictionJoiner, qJoiner, parameterMap));
        // add restrictions
        builder.append(RestrictionUtil.generateRestrictionsQuery(criteria, restrictionJoiner, qJoiner, parameterMap));
        return builder.toString();
    }

    public static String generateOrderQuery(Criteria criteria){
        if(criteria.getOrders().size() > 0) {
            StringJoiner joiner = new StringJoiner(",");
            List<Order> orders = criteria.getOrders();
            for(Order order: orders) {
                String name = order.isAlias() ? order.getName() : (order.getCriteriaAlias() + "." + order.getName());
                joiner.add(name + " " +  (order.getType().name()));
            }
            return " ORDER BY " + joiner.toString();
        }
        return "";
    }

}
