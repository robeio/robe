package io.robe.hibernate.conf;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Custom named strategy for table names
 * read from robe.yml -hibernate.prefix
 */
public class RobeHibernateNamingStrategy implements PhysicalNamingStrategy {

    private String prefix;

    public RobeHibernateNamingStrategy(String prefix) {
        this.prefix = (prefix == null ? "" : prefix).trim();
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convert(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convert(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convert(identifier);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convert(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convert(identifier);
    }

    private String transformToPluralForm(String tableNameInSingularForm) {
        StringBuilder pluralForm = new StringBuilder();
        pluralForm.append(this.prefix);
        pluralForm.append(tableNameInSingularForm);
        return pluralForm.toString();
    }

    private Identifier convert(Identifier identifier) {
        if (identifier == null || StringUtils.isBlank(identifier.getText())) {
            return identifier;
        }
        return Identifier.toIdentifier(transformToPluralForm(identifier.getText()));
    }
}
