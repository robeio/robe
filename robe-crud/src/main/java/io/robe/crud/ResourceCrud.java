package io.robe.crud;

import io.robe.crud.helper.CrudUtility;
import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.expr.BinaryExpr.Operator;
import japa.parser.ast.stmt.*;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceCrud {

    private static String daoName;

    private static String entityName;

    private static Boolean auth;

    private static String idGetFunction;

    private static List<String> uniqueFields;
    private static List<String> fields;

    public static void setUniqueFields(List<String> uniqueFields) {
        ResourceCrud.uniqueFields = uniqueFields;
    }

    public static void setFields(List<String> fields) {
        ResourceCrud.fields = fields;
    }

    public static void setIdGetFunction(String idGetFunction) {
        ResourceCrud.idGetFunction = idGetFunction;
    }

    public static void setAuth(Boolean auth) {
        ResourceCrud.auth = auth;
    }

    public static void setEntityName(String entityName) {
        ResourceCrud.entityName = entityName;
    }

    public static void setDaoName(String daoName) {
        ResourceCrud.daoName = daoName;
    }

    public static String resourceGenerate(List<ImportDeclaration> importDeclarations, String packageName,
                                          Boolean inject) {

        List<BodyDeclaration> bodyDeclarationsList = new ArrayList<BodyDeclaration>();

        bodyDeclarationsList.add(ResourceCrud.getAll());
        bodyDeclarationsList.add(ResourceCrud.get());
        bodyDeclarationsList.add(ResourceCrud.create());
        bodyDeclarationsList.add(ResourceCrud.update());
        bodyDeclarationsList.add(ResourceCrud.delete());

        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setImports(importDeclarations);

        compilationUnit.setPackage(CrudUtility.getPackage(packageName));

        FieldDeclaration injectFieldDeclaration = ASTHelper.createFieldDeclaration(0, new ClassOrInterfaceType(CrudUtility.capitalizeToUpper(daoName)), CrudUtility.createVariableDeclarator(CrudUtility.capitalizeToLower(daoName), null));
        bodyDeclarationsList.add(0, injectFieldDeclaration);

        if (inject) {
            List<AnnotationExpr> fieldAnnotationExprs = new ArrayList<AnnotationExpr>();
            fieldAnnotationExprs.add(new MarkerAnnotationExpr(ASTHelper.createNameExpr("Inject")));
            injectFieldDeclaration.setAnnotations(fieldAnnotationExprs);

        } else {
            ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(ModifierSet.PUBLIC, entityName + "Resource");
            BlockStmt conBlock = new BlockStmt();

            ThisExpr thisExpr = new ThisExpr();
            FieldAccessExpr accessExpr = new FieldAccessExpr(thisExpr, CrudUtility.capitalizeToLower(daoName));
            AssignExpr assignExpr = new AssignExpr(accessExpr, new NameExpr(CrudUtility.capitalizeToLower(daoName)), AssignExpr.Operator.assign);
            ASTHelper.addStmt(conBlock, assignExpr);

            constructorDeclaration.setParameters(Arrays.asList(CrudUtility.generateParameter(daoName, null, null, null, null)));
            constructorDeclaration.setBlock(conBlock);
            bodyDeclarationsList.add(1, constructorDeclaration);
        }


        ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(1, Arrays.asList(CrudUtility.generateAnnotation("Path", CrudUtility.capitalizeToLower(entityName), null), CrudUtility
                .generateAnnotation("Consumes", "MediaType", "APPLICATION_JSON"), CrudUtility.generateAnnotation("Produces", "MediaType", "APPLICATION_JSON")), false, entityName + "Resource", null,
                null, null,
                bodyDeclarationsList);
        ASTHelper.addTypeDeclaration(compilationUnit, type);

        return compilationUnit.toString();
    }

    public static MethodDeclaration delete() {

        String entityVariableName = CrudUtility.capitalizeToLower(entityName);
        daoName = CrudUtility.capitalizeToLower(daoName);

        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "delete");

        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(CrudUtility.generateParameter(entityName, "Valid", null, null, null));
        if (auth) {
            parameterList.add(CrudUtility.generateParameter("Credentials", "Auth", null, null, null));
        }
        method.setParameters(parameterList);

        method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("DELETE", null, null), CrudUtility.generateAnnotation("UnitOfWork", null, null)));

        BlockStmt body = new BlockStmt();

        MethodCallExpr methodFindBy = new MethodCallExpr(new NameExpr(daoName), idGetFunction);
        ASTHelper.addArgument(methodFindBy, new MethodCallExpr(new NameExpr(entityVariableName), "getOid"));

        AssignExpr assignExpr = new AssignExpr(new NameExpr(entityVariableName), methodFindBy, AssignExpr.Operator.assign);

        MethodCallExpr methodDelete = new MethodCallExpr(new NameExpr(daoName), "delete");
        ASTHelper.addArgument(methodDelete, new NameExpr(entityVariableName));

        ASTHelper.addStmt(body, assignExpr);
        ASTHelper.addStmt(body, new ReturnStmt(methodDelete));

        method.setBody(body);

        return method;

    }

    public static MethodDeclaration update() {

        String entityVariableName = CrudUtility.capitalizeToLower(entityName);
        daoName = CrudUtility.capitalizeToLower(daoName);
        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "update");


        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(CrudUtility.generateParameter(entityName, "Valid", null, null, null));
        if (auth) {
            parameterList.add(CrudUtility.generateParameter("Credentials", "Auth", null, null, null));
        }
        method.setParameters(parameterList);

        method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("POST", null, null), CrudUtility.generateAnnotation("UnitOfWork", null, null)));

        BlockStmt body = new BlockStmt();

        MethodCallExpr detach = new MethodCallExpr(new NameExpr(daoName), "detach");

        ASTHelper.addArgument(detach, new NameExpr(entityVariableName));

        MethodCallExpr findBy = new MethodCallExpr(new NameExpr(daoName), idGetFunction);
        ASTHelper.addArgument(findBy, new MethodCallExpr(new NameExpr(entityVariableName), "getOid"));

        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType(entityName, 0), Arrays.asList(CrudUtility.createVariableDeclarator("entity", findBy)));

        MethodCallExpr update = new MethodCallExpr(new NameExpr(daoName), "update");
        ASTHelper.addArgument(update, new NameExpr("entity"));

        ASTHelper.addStmt(body, detach);
        ASTHelper.addStmt(body, variableDeclarationExpr);
        for (String string : fields) {
            ASTHelper.addStmt(body, CrudUtility.generateUpdateRow("entity", entityVariableName, "set" + CrudUtility.capitalizeToUpper(string), "get" + CrudUtility.capitalizeToUpper(string)));
        }
        ASTHelper.addStmt(body, new ReturnStmt(update));

        method.setBody(body);

        return method;

    }

    public static MethodDeclaration create() {

        String entityVariableName = CrudUtility.capitalizeToLower(entityName);
        daoName = CrudUtility.capitalizeToLower(daoName);
        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "create");

        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(CrudUtility.generateParameter(entityName, "Valid", null, null, null));
        if (auth) {
            parameterList.add(CrudUtility.generateParameter("Credentials", "Auth", null, null, null));
        }
        method.setParameters(parameterList);

        method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("PUT", null, null), CrudUtility.generateAnnotation("UnitOfWork", null, null)));

        BlockStmt body = new BlockStmt();
        if (uniqueFields != null) {
            for (String field : uniqueFields) {

                MethodCallExpr callFindBy = new MethodCallExpr(new NameExpr(daoName), idGetFunction);
                ASTHelper.addArgument(callFindBy, new MethodCallExpr(new NameExpr(entityVariableName), "get" + CrudUtility.capitalizeToUpper(field)));

                VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType("Optional<" + entityName + ">", 0), Arrays.asList(CrudUtility.createVariableDeclarator(CrudUtility.capitalizeToLower(field), callFindBy)));
                ASTHelper.addStmt(body, variableDeclarationExpr);
                BinaryExpr binaryExpr = new BinaryExpr(new MethodCallExpr(new NameExpr(entityVariableName), "get" + CrudUtility.capitalizeToUpper(field)), new StringLiteralExpr("already used by " +
                        "another " + entityVariableName + ". Please use different " + field), Operator.plus);

                MethodCallExpr callException = new MethodCallExpr(null, "RobeRuntimeException", Arrays.asList(new StringLiteralExpr("Error"), binaryExpr));

                List<Statement> ifStatements = new ArrayList<Statement>();

                ThrowStmt throwStmt = new ThrowStmt(callException);
                ifStatements.add(throwStmt);

                IfStmt ifStmt = new IfStmt(new MethodCallExpr(new NameExpr(CrudUtility.capitalizeToLower(field)), "isPresent"), new BlockStmt(ifStatements), null);
                ASTHelper.addStmt(body, ifStmt);
            }
        }

        MethodCallExpr callCreateFunction = new MethodCallExpr(new NameExpr(daoName), "create");
        ASTHelper.addArgument(callCreateFunction, new NameExpr(entityVariableName));
        ASTHelper.addStmt(body, new ReturnStmt(callCreateFunction));
        method.setBody(body);

        return method;

    }

    public static MethodDeclaration get() {

        String entityVariableName = CrudUtility.capitalizeToLower(entityName);
        String pathParamName = entityVariableName + "Id";
        daoName = CrudUtility.capitalizeToLower(daoName);

        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "get");

        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(CrudUtility.generateParameter("String", "PathParam", pathParamName, pathParamName, null));
        if (auth) {
            parameterList.add(CrudUtility.generateParameter("Credentials", "Auth", null, null, null));
        }
        method.setParameters(parameterList);
        method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("Path", "{" + pathParamName + "}", null), CrudUtility.generateAnnotation("GET", null, null), CrudUtility.generateAnnotation("UnitOfWork", null, null)));
        BlockStmt body = new BlockStmt();
        MethodCallExpr callFindAll = new MethodCallExpr(new NameExpr(daoName), idGetFunction);
        ASTHelper.addArgument(callFindAll, new NameExpr(pathParamName));
        ReturnStmt returnStmt = new ReturnStmt(callFindAll);

        ASTHelper.addStmt(body, returnStmt);

        method.setBody(body);

        return method;

    }

    public static MethodDeclaration getAll() {
        String name = "get" + entityName + "s";
        daoName = CrudUtility.capitalizeToLower(daoName);
        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType("List<" + entityName + ">", 0), name);
        if (auth) {
            method.setParameters(Arrays.asList(CrudUtility.generateParameter("Credentials", "Auth", null, null, null)));
        }
        method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("Path", "all", null), CrudUtility.generateAnnotation("GET", null, null), CrudUtility.generateAnnotation("UnitOfWork", null, null)));
        BlockStmt body = new BlockStmt();

        FieldAccessExpr field = new FieldAccessExpr(new NameExpr(entityName), "class");
        MethodCallExpr callFindAll = new MethodCallExpr(new NameExpr(daoName), "findAll");
        ASTHelper.addArgument(callFindAll, field);

        ReturnStmt returnStmt = new ReturnStmt(callFindAll);

        ASTHelper.addStmt(body, returnStmt);
        method.setBody(body);

        return method;
    }


}
