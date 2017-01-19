package io.robe.hibernate.query.impl.hql.util;

import io.robe.hibernate.query.api.criteria.CriteriaJoin;
import io.robe.hibernate.query.api.criteria.CriteriaParent;
import io.robe.hibernate.query.api.criteria.JoinRelation;

import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by kamilbukum on 17/01/2017.
 */
public class JoinUtil {
    /**
     * Generates all Join Queries for the given criteria
     * @param criteria
     * @return
     */
    public static String generateJoinQuery(CriteriaParent criteria, StringJoiner restrictionJoiner, StringJoiner qJoiner, Map<String, Object> parameterMap) {
        if(criteria.getJoins().size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, CriteriaJoin> joinEntry: criteria.getJoins().entrySet()) {
            String joinProjectionResult = joinToString(joinEntry.getValue(), restrictionJoiner, qJoiner, parameterMap);
            builder.append(joinProjectionResult);
            if(joinEntry.getValue().getJoins().size() > 0) {
                builder.append(generateJoinQuery(joinEntry.getValue(), restrictionJoiner, qJoiner, parameterMap));
            }
        }
        return builder.toString();
    }

    /**
     * Generates Join Query for the given JoinCriteria
     * @param criteriaJoin
     * @return
     */
    private static String joinToString(CriteriaJoin criteriaJoin, StringJoiner restrictionJoiner, StringJoiner qJoiner, Map<String, Object> parameterMap) {
        StringBuilder builder = new StringBuilder("\nLEFT OUTER JOIN ")
                .append(criteriaJoin.getEntityClass().getName())
                .append(" ")
                .append(criteriaJoin.getAlias())
                .append(" ")
                .append(" ON ");

        if(criteriaJoin.getJoinRelations().size() == 0) {
            throw new RuntimeException("Not found any Join Relations in " + criteriaJoin.getAlias() + " Join Criteria ! ");
        }


        StringJoiner joiner = new StringJoiner(" AND ");
        for(JoinRelation joinRelation: criteriaJoin.getJoinRelations()) {
            StringBuilder relationBuilder = new StringBuilder("\n")
                    .append(joinRelation.getRelationCriteria().getAlias())
                    .append(".")
                    .append(joinRelation.getRelationField())
                    .append("=")
                    .append(joinRelation.getJoinedCriteria().getAlias())
                    .append(".")
                    .append(joinRelation.getJoinedField());
            joiner.add(relationBuilder.toString());
        }

        if(criteriaJoin.getRestrictions().size() > 0) {
            RestrictionUtil.generateRestrictions(criteriaJoin, criteriaJoin.getRestrictions(), restrictionJoiner, qJoiner, parameterMap);
        }
        if(joiner.length() > 0) {
            builder.append(joiner.toString());
        }
        return builder.toString();
    }
}
