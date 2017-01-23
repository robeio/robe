package io.robe.hibernate.criteria.api.projection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamilbukum on 13/01/2017.
 */
public class ProjectionList implements Projection {

    private List<Projection> elements = new ArrayList<>();

    /**
     * Constructs a ProjectionList
     *
     * @see org.hibernate.criterion.Projections#projectionList()
     */
    protected ProjectionList() {
    }

     /**
     * Add a projection to this list of projections
     *
     * @param projection The projection to add
     * @return {@code this}, for method chaining
     */
    public ProjectionList add(Projection projection) {
        elements.add(projection);
        return this;
    }

    /**
     * Adds a projection to this list of projections after wrapping it with an alias
     *
     * @param projection The projection to add
     * @param alias      The alias to apply to the projection
     * @return {@code this}, for method chaining
     * @see org.hibernate.criterion.Projections#alias
     */
    public ProjectionList add(Projection projection, String alias) {
        return add(Projections.alias(projection, alias));
    }

    public boolean isGrouped() {
        for (Projection projection : elements) {
            if (projection.isGrouped()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Access a wrapped projection by index
     *
     * @param i The index of the projection to return
     * @return The projection
     */
    @SuppressWarnings("UnusedDeclaration")
    public Projection getProjection(int i) {
        return elements.get(i);
    }

    public int getLength() {
        return elements.size();
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectionList)) return false;

        ProjectionList that = (ProjectionList) o;

        return elements != null ? elements.equals(that.elements) : that.elements == null;
    }

    @Override
    public int hashCode() {
        return elements != null ? elements.hashCode() : 0;
    }
}
