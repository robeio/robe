package io.robe.hibernate.criteria.api.projection;

/**
 * Created by kamilbukum on 13/01/2017.
 */
public class Projections {
    /**
     * A property value projection
     *
     * @param propertyName The name of the property whose values should be projected
     *
     * @return The property projection
     *
     * @see PropertyProjection
     */
    public static PropertyProjection property(String propertyName) {
        return new PropertyProjection(propertyName, false);
    }
    /**
     * A grouping property value projection
     *
     * @param propertyName The name of the property to group
     *
     * @return The grouped projection
     *
     * @see PropertyProjection
     */
    public static PropertyProjection groupProperty(String propertyName) {
        return new PropertyProjection( propertyName, true);
    }

    /**
     * An identifier value projection.
     *
     * @return The identifier projection
     *
     * @see IdentifierProjection
     */
    public static IdentifierProjection id() {
        return new IdentifierProjection();
    }

    /**
     * Create a new projection list.
     *
     * @return The projection list
     */
    public static ProjectionList projectionList() {
        return new ProjectionList();
    }

    /**
     * The query row count, ie. <tt>count(*)</tt>
     *
     * @return The projection representing the row count
     *
     * @see FunctionProjection
     */
    public static FunctionProjection rowCount() {
        return new FunctionProjection(null, FunctionProjection.Type.COUNT);
    }

    /**
     * A property value count projection
     *
     * @param property The name of the property to count over
     *
     * @return The count projection
     *
     * @see FunctionProjection
     */
    public static FunctionProjection count(String property) {
        return new FunctionProjection(property, FunctionProjection.Type.COUNT);
    }

    /**
     * A property maximum value projection
     *
     * @param propertyName The property for which to find the max
     *
     * @return the max projection
     *
     * @see FunctionProjection
     */
    public static FunctionProjection max(String propertyName) {
        return new FunctionProjection(propertyName, FunctionProjection.Type.MAX);
    }

    /**
     * A property minimum value projection
     *
     * @param propertyName The property for which to find the min
     *
     * @return the min projection
     *
     * @see FunctionProjection
     */
    public static FunctionProjection min(String propertyName) {
        return new FunctionProjection(propertyName, FunctionProjection.Type.MIN);
    }

    /**
     * A property average value projection
     *
     * @param propertyName The property over which to find the average
     *
     * @return the avg projection
     *
     * @see FunctionProjection
     */
    public static FunctionProjection avg(String propertyName) {
        return new FunctionProjection(propertyName, FunctionProjection.Type.AVG);
    }

    /**
     * A property value sum projection
     *
     * @param propertyName The property over which to sum
     *
     * @return the sum projection
     *
     * @see FunctionProjection
     */
    public static FunctionProjection sum(String propertyName) {
        return new FunctionProjection(propertyName, FunctionProjection.Type.SUM);
    }

    /**
     * Assign an alias to a projection, by wrapping it
     *
     * @param projection The projection to be aliased
     * @param alias The alias to apply
     *
     * @return The aliased projection
     *
     * @see Projection
     */
    public static EnhancedProjection alias(Projection projection, String alias) {
        return new EnhancedProjection(projection, alias);
    }


    public static ProjectionElements elements(String propertyName) {
        return new ProjectionElements(propertyName);
    }

    private Projections() {
        //cannot be instantiated
    }
}
