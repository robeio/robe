package io.robe.crud.helper;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.expr.NameExpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasanmumin on 25/12/14.
 */
public class Constants {

    public static final List<ImportDeclaration> resourceImports = new ArrayList<ImportDeclaration>();

    static {
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.inject.Inject"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("io.dropwizard.auth.Auth"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("io.dropwizard.hibernate.UnitOfWork"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("io.robe.auth.Credentials"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.validation.Valid"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.Consumes"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.DELETE"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.POST"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.PUT"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.GET"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.Path"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.PathParam"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.Produces"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("javax.ws.rs.MediaType"), false, false));
        resourceImports.add(new ImportDeclaration(new NameExpr("java.util.List"), false, false));
    }

    public static final List<ImportDeclaration> daoImports = new ArrayList<ImportDeclaration>();

    static {
        daoImports.add(new ImportDeclaration(new NameExpr("javax.inject.Inject"), false, false));
        daoImports.add(new ImportDeclaration(new NameExpr("org.hibernate.SessionFactory"), false, false));
        daoImports.add(new ImportDeclaration(new NameExpr("io.robe.hibernate.dao.BaseDao"), false, false));
        daoImports.add(new ImportDeclaration(new NameExpr("org.hibernate.criterion.Restrictions"), false, false));
        daoImports.add(new ImportDeclaration(new NameExpr("org.hibernate.Criteria"), false, false));
        daoImports.add(new ImportDeclaration(new NameExpr("com.google.common.base.Optional"), false, false));
    }
}
