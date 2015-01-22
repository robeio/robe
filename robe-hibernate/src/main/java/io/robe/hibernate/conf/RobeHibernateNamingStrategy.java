package io.robe.hibernate.conf;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Custom named strategy for table names
 * read from robe.yml -hibernate.prefix
 */
public class RobeHibernateNamingStrategy extends ImprovedNamingStrategy {
    private static final long serialVersionUID = 1L;
    private String prefix;

    public RobeHibernateNamingStrategy(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String classToTableName(final String className) {
        return this.addPrefix(super.classToTableName(className));
    }

    @Override
    public String collectionTableName(final String ownerEntity,
                                      final String ownerEntityTable, final String associatedEntity,
                                      final String associatedEntityTable, final String propertyName) {
        return this.addPrefix(super.collectionTableName(ownerEntity,
                ownerEntityTable, associatedEntity, associatedEntityTable,
                propertyName));
    }

    @Override
    public String logicalCollectionTableName(final String tableName,
                                             final String ownerEntityTable, final String associatedEntityTable,
                                             final String propertyName) {
        return this.addPrefix(super.logicalCollectionTableName(tableName,
                ownerEntityTable, associatedEntityTable, propertyName));
    }

    private String addPrefix(final String composedTableName) {
        return this.prefix + composedTableName;
    }
}
