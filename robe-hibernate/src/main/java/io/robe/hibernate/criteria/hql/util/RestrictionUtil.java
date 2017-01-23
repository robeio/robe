package io.robe.hibernate.criteria.hql.util;

import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.CriteriaParent;
import io.robe.hibernate.criteria.api.criterion.RestrictionList;
import io.robe.hibernate.criteria.api.criterion.Restriction;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by kamilbukum on 17/01/2017.
 */

public class RestrictionUtil {
    /**
     *
     * @param criteria
     * @param <E>
     * @return
     */
    public static <E> String generateRestrictionsQuery(Criteria<E> criteria, StringJoiner restrictionJoiner, StringJoiner qJoiner, Map<String, Object> parameterMap){
        if(criteria.getRestrictions().size() > 0) {
            RestrictionUtil.generateRestrictions(criteria, criteria.getRestrictions(), restrictionJoiner, qJoiner, parameterMap);
        }
        if(restrictionJoiner.length() > 0) {
            if(qJoiner.length() > 0) {
                restrictionJoiner.add( " ( "+ qJoiner.toString() + " ) ");
            }
            return "\nWHERE " + restrictionJoiner.toString();

        } else if(qJoiner.length() > 0) {
            return "\nWHERE " + qJoiner.toString();
        }
        return "";
    }
    /**
     *
     * @param criteria
     * @param restrictions
     * @param joiner
     */
    public static void generateRestrictions(CriteriaParent criteria, List<Restriction> restrictions, StringJoiner joiner, StringJoiner qJoiner, Map<String, Object> parameterMap){
        for(Restriction restriction: restrictions) {
            String result = null;
            switch (restriction.getOperator()) {
                case EQUALS:
                    result = restrictionToString(criteria, restriction, restriction.getValue(), "=", parameterMap);
                    break;
                case IS_NULL:
                    result = criteria.getAlias() + "." + restriction.getName() + " IS NULL";
                    break;
                case NOT_EQUALS:
                    result = criteria.getAlias() + "." + restrictionToString(criteria, restriction, restriction.getValue(), "!=", parameterMap);
                    break;
                case IS_NOT_NULL:
                    result = restriction.getName() + " IS NOT NULL";
                    break;
                case LESS_THAN:
                    result = restrictionToString(criteria, restriction, restriction.getValue(), " < ", parameterMap);
                    break;
                case LESS_OR_EQUALS_THAN:
                    result = restrictionToString(criteria, restriction, restriction.getValue(), " <= ", parameterMap);
                    break;
                case GREATER_THAN:
                    result = restrictionToString(criteria, restriction, restriction.getValue(), " > ", parameterMap);
                    break;
                case GREATER_OR_EQUALS_THAN:
                    result = restrictionToString(criteria, restriction, restriction.getValue(), " >= ", parameterMap);
                    break;
                case Q:
                    result = null;
                    qJoiner.add(criteria.getAlias() + "." + restriction.getName() + " LIKE " + ":" + restriction.getValueAlias());
                    parameterMap.putIfAbsent(restriction.getValueAlias(), getPercentValue(restriction));
                    break;
                case CONTAINS:
                    Object containsValue = getPercentValue(restriction);
                    result = restrictionToString(criteria, restriction, containsValue, " LIKE ", parameterMap);
                    break;
                case IN:
                    result = restrictionToString(criteria, restriction, restriction.getValue(), " IN ", parameterMap);
                    break;
                case AND:
                    StringJoiner andJoiner =  new StringJoiner(" AND ");
                    generateRestrictions(criteria, ((RestrictionList)restriction).getRestrictions(), andJoiner, qJoiner, parameterMap);
                    if(!"".equals(andJoiner.toString())) {
                        result = "( " +  andJoiner.toString() + " )";
                    }
                    break;
                case OR:
                    StringJoiner orJoiner = new StringJoiner(" OR ");
                    generateRestrictions(criteria, ((RestrictionList)restriction).getRestrictions(), orJoiner, qJoiner, parameterMap);
                    if(!"".equals(orJoiner.toString())) {
                        result = "( " +  orJoiner.toString() + " )";
                    }
            }
            if(result != null) {
                joiner.add(result);
            }
        }
    }
    /**
     *
     * @param criteria
     * @param restriction
     * @param operator
     * @return
     */
    private static String restrictionToString(CriteriaParent criteria, Restriction restriction, Object value, String operator, Map<String, Object> parameterMap){
        String valueAlias =  "$" + restriction.getValueAlias().replaceAll("\\.", "_");

        parameterMap.put(valueAlias, value);
        return criteria.getAlias() + "." + restriction.getName() + operator + ":" + valueAlias;
    }

    private static Object getPercentValue(Restriction restriction) {
        if(restriction.getValue() != null) {
            return "%" + restriction.getValue() + "%";
        }
        return  "%%";
    }
}
