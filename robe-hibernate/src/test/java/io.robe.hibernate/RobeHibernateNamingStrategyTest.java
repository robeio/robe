package io.robe.hibernate;

import io.robe.hibernate.conf.RobeHibernateNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by adem on 19/10/2016.
 */
public class RobeHibernateNamingStrategyTest {

    @Test
    public void namingStrategy() {
        RobeHibernateNamingStrategy strategy = new RobeHibernateNamingStrategy("V_");
        Identifier identifier = Identifier.toIdentifier("A_NAME");
        Identifier expectedIdentifier = Identifier.toIdentifier("V_A_NAME");

        Identifier none = strategy.toPhysicalTableName(null, null);
        Identifier physicalCatalogName = strategy.toPhysicalCatalogName(identifier, null);
        Identifier physicalColumnName = strategy.toPhysicalColumnName(identifier, null);
        Identifier physicalSchemaName = strategy.toPhysicalSchemaName(identifier, null);
        Identifier physicalSequenceName = strategy.toPhysicalSequenceName(identifier, null);
        Identifier physicalTableName = strategy.toPhysicalTableName(identifier, null);

        Assert.assertEquals(null, none);
        Assert.assertEquals(expectedIdentifier, physicalCatalogName);
        Assert.assertEquals(expectedIdentifier, physicalColumnName);
        Assert.assertEquals(expectedIdentifier, physicalSchemaName);
        Assert.assertEquals(expectedIdentifier, physicalSequenceName);
        Assert.assertEquals(expectedIdentifier, physicalTableName);
    }

}
