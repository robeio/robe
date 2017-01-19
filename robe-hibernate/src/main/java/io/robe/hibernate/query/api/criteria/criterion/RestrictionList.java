package io.robe.hibernate.query.api.criteria.criterion;

import io.robe.hibernate.query.api.query.Operator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kamilbukum on 16/01/2017.
 */
public class RestrictionList extends Restriction {
    private final List<Restriction> restrictions;
    public RestrictionList(Operator operator, Restriction[] restrictions) {
        this(operator, Arrays.asList(restrictions));
    }
    public RestrictionList(Operator operator, List<Restriction> restrictions) {
        super(operator, null, null, null);
        this.restrictions = restrictions;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }
}
