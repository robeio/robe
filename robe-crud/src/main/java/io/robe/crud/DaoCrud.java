package io.robe.crud;

import io.robe.crud.helper.CrudUtility;
import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DaoCrud {

    private static String entityName;
    private static List<String> uniqueFields;
    private static String findBy;

    public static void setEntityName(String entityName) {
        DaoCrud.entityName = entityName;
    }

    public static void setUniqueFields(List<String> uniqueFields) {
        DaoCrud.uniqueFields = uniqueFields;
    }

    public static void setFindBy(String findBy) {
        DaoCrud.findBy = findBy;
    }

    public static String createDao(String packageName, List<ImportDeclaration> importDeclarations) {
        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(packageName)));

        List<ClassOrInterfaceType> extendList = new ArrayList<ClassOrInterfaceType>();
        extendList.add(new ClassOrInterfaceType("BaseDao<" + entityName + ">"));

        compilationUnit.setImports(importDeclarations);

        List<BodyDeclaration> members = new ArrayList<BodyDeclaration>();

        ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(ModifierSet.PUBLIC, entityName + "Dao");

        constructorDeclaration.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("Inject", null, null)));

        BlockStmt conBlock = new BlockStmt();
        List<Expression> args = new LinkedList<Expression>();
        args.add(ASTHelper.createNameExpr("sessionFactory"));

        ASTHelper.addStmt(conBlock, new MethodCallExpr(null, "super", args));

        constructorDeclaration.setParameters(Arrays.asList(CrudUtility.generateParameter("SessionFactory", null, null, null, null)));
        constructorDeclaration.setBlock(conBlock);
        members.add(constructorDeclaration);
        if (!findBy.equals("findById")) {
            members.add(createFindByMethod());
        }
        ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, null, false, entityName + "Dao", null, extendList, null, members);
        ASTHelper.addTypeDeclaration(compilationUnit, type);

        return compilationUnit.toString();
    }

    private static MethodDeclaration createFindByMethod() {

        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), findBy);

        method.setParameters(Arrays.asList(CrudUtility.generateParameter("String", null, null, "code", null)));

        BlockStmt body = new BlockStmt();
        MethodCallExpr callCurrentSession = new MethodCallExpr(null, "currentSession");

        MethodCallExpr callClass = new MethodCallExpr(callCurrentSession, "createCriteria");

        FieldAccessExpr fieldClass = new FieldAccessExpr(new NameExpr(entityName), "class");
        ASTHelper.addArgument(callClass, fieldClass);
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType("Criteria", 0), Arrays.asList(CrudUtility.createVariableDeclarator("criteria", callClass)));

        ASTHelper.addStmt(body, variableDeclarationExpr);

        MethodCallExpr callRestrictions = new MethodCallExpr(new NameExpr("Restrictions"), "disjunction");
        VariableDeclarationExpr variableDeclarationExpr1 = new VariableDeclarationExpr(ASTHelper.createReferenceType("Disjunction", 0), Arrays.asList(CrudUtility.createVariableDeclarator("disjunction", callRestrictions)));
        ASTHelper.addStmt(body, variableDeclarationExpr1);


        MethodCallExpr callOid = new MethodCallExpr(new NameExpr("disjunction"), "add");
        MethodCallExpr callEqOid = new MethodCallExpr(new NameExpr("Restrictions"), "eq", Arrays.asList(new StringLiteralExpr("oid"), new NameExpr("code")));
        ASTHelper.addArgument(callOid, callEqOid);
        ASTHelper.addStmt(body, callOid);

        for (String string : uniqueFields) {

            MethodCallExpr callAdd = new MethodCallExpr(new NameExpr("disjunction"), "add");
            MethodCallExpr callEq = new MethodCallExpr(new NameExpr("Restrictions"), "eq", Arrays.asList(new StringLiteralExpr(string), new NameExpr("code")));
            ASTHelper.addArgument(callAdd, callEq);
            ASTHelper.addStmt(body, callAdd);
        }
        method.setName(findBy);

        MethodCallExpr callDisjunction = new MethodCallExpr(new NameExpr("disjunction"), "add");
        ASTHelper.addArgument(callDisjunction, new NameExpr("disjunction"));

        ASTHelper.addStmt(body, callDisjunction);

        MethodCallExpr callUniqueResult = new MethodCallExpr(null, "uniqueResult");
        ASTHelper.addArgument(callUniqueResult, new NameExpr("criteria"));
        MethodCallExpr callFromNullable = new MethodCallExpr(new NameExpr("Optional"), "fromNullable");
        ASTHelper.addArgument(callFromNullable, callUniqueResult);

        ReturnStmt returnStmt = new ReturnStmt(callFromNullable);

        ASTHelper.addStmt(body, returnStmt);

        method.setBody(body);

        return method;

    }

}
