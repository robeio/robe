package io.robe.hibernate.criteria.hql.util;

import io.robe.common.dto.Pair;
import io.robe.common.utils.Validations;
import io.robe.hibernate.criteria.api.CriteriaJoin;
import io.robe.hibernate.criteria.api.CriteriaParent;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.projection.*;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by kamilbukum on 17/01/2017.
 */
public class SelectUtil {
    /**
     *
     * @param criteria
     * @param <E>
     * @return
     */
    public static <E> Pair<String, String> generateSelectQueryForList(Criteria<E> criteria) {
        Pair<String, String> pair = new Pair<>();
        StringJoiner groupJoiner = new StringJoiner(", ");
        String select = selectForListRecursively(criteria, groupJoiner);
        String groupBy = groupJoiner.toString();
        if(groupJoiner.length() > 0) {
            groupBy = "\nGROUP BY " + groupBy;
        }
        if("".equals(select)) {
            pair.setLeft(criteria.getAlias());
            pair.setRight(groupBy);
            return pair;
        }
        pair.setLeft(select);
        pair.setRight(groupBy);
        return pair;
    }

    /**
     *
     * @param criteria
     * @return
     */
    private static <E> String selectForListRecursively(CriteriaParent<E> criteria, StringJoiner groupJoiner){
        StringJoiner joiner = new StringJoiner(", ");
        if(criteria.getProjection() != null) {
            joiner.add(selectForListByProjection(criteria, criteria.getProjection(), null, groupJoiner));
        }
        for(Map.Entry<String, CriteriaJoin<E>> joinEntry: criteria.getJoins().entrySet()) {
            String joinProjectionResult = selectForListRecursively(joinEntry.getValue(), groupJoiner);
            if(!Validations.isEmptyOrNull(joinProjectionResult)) {
                joiner.add(joinProjectionResult);
            }
        }
        return joiner.toString();
    }

    /**
     *
     * @param criteria
     * @param projection
     * @return
     */
    private static String selectForListByProjection(CriteriaParent criteria, Projection projection, String alias, StringJoiner groupJoiner){
        String result;
        if(projection instanceof IdentifierProjection) {
            result = criteria.getAlias() + "." + criteria.getMeta().getIdentityName() + getAsKey(criteria, criteria.getMeta().getIdentityName(), alias);
        } else if(projection instanceof PropertyProjection) {
            PropertyProjection p = (PropertyProjection)projection;
            result = criteria.getAlias() + "." + p.getProperty() + getAsKey(criteria, p.getProperty(), alias);
            if(p.isGrouped()) {
                groupJoiner.add( criteria.getAlias() + "." + p.getProperty());
            }
        } else if(projection instanceof FunctionProjection) {
            FunctionProjection pp = (FunctionProjection)projection;
            if(FunctionProjection.Type.COUNT == pp.getFnType() && Validations.isEmptyOrNull(pp.getProperty())) {
                return pp.getFnType().name() + "(1)" + getAsKey(criteria, "fn_" + pp.getFnType().name().toLowerCase(), alias);
            } else {
                return pp.getFnType().name() + "(" + criteria.getAlias() + "." + pp.getProperty() + ")" + getAsKey(criteria, "fn_" + pp.getProperty() + "_" + pp.getFnType().name().toLowerCase() , alias);
            }
        } else if(projection instanceof EnhancedProjection) {
            EnhancedProjection p = (EnhancedProjection)projection;
            result = selectForListByProjection(criteria, p.getProjection(), p.getAlias(), groupJoiner);
        } else if(projection instanceof ProjectionList){
            ProjectionList p = (ProjectionList)projection;
            StringJoiner joiner = new StringJoiner(", ");
            for(int i = 0 ; i < p.getLength(); i++) {
                Projection childProjection = p.getProjection(i);
                joiner.add(selectForListByProjection(criteria, childProjection, null, groupJoiner));
            }
            result = joiner.toString();
        } else {
            throw new RuntimeException("Unknown Projection !"+ projection);
        }
        return result;
    }

    private static String getAsKey(CriteriaParent criteria, String selectAlias, String asAlias){
        String alias;
        if(asAlias != null) {
            alias = asAlias.replaceAll("\\.", "_0_");
        } else if(criteria.isRoot()) {
            alias = selectAlias;
        } else {
           alias =  criteria.getAlias() + "_0_" + selectAlias;
        }
        return  " AS "  + alias;
    }

}
