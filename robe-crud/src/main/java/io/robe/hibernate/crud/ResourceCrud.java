package io.robe.hibernate.crud;

import io.robe.hibernate.helper.CrudUtility;
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

    public static String ResourceGenerate(String name, String entityName, String daoName, List<BodyDeclaration> bodyDeclarationsList, List<ImportDeclaration> importDeclarations, String packageName, Boolean inject) {

        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setImports(importDeclarations);

        compilationUnit.setPackage(CrudUtility.getPackage(packageName));

        FieldDeclaration injectFieldDeclaration = ASTHelper.createFieldDeclaration(0, new ClassOrInterfaceType(daoName), CrudUtility.createVariableDeclarator(CrudUtility.capitalizeToLower(daoName), null));
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


        ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(1, Arrays.asList(CrudUtility.generateAnnotation("Path", CrudUtility.capitalizeToLower(entityName), null), CrudUtility.generateAnnotation("Consumes", "MediaType", "APPLICATION_JSON"), CrudUtility.generateAnnotation("Produces", "MediaType", "APPLICATION_JSON")), false, name, null, null, null, bodyDeclarationsList);
        ASTHelper.addTypeDeclaration(compilationUnit, type);

        return compilationUnit.toString();
    }

    public static MethodDeclaration delete(String entityName, String daoName, String idGetFunction, String findByFunction, String deleteFunction, Boolean auth) {

        String entityVariableName = CrudUtility.capitalizeToLower(entityName);
        daoName = CrudUtility.capitalizeToLower(daoName);

        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "delete");

        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(CrudUtility.generateParameter(entityName, null, null, null, null));
        if (auth) {
            parameterList.add(CrudUtility.generateParameter("Credentials", "Auth", null, null, null));
        }
        method.setParameters(parameterList);

        method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("DELETE", null, null), CrudUtility.generateAnnotation("UnitOfWork", null, null)));

        BlockStmt body = new BlockStmt();

        MethodCallExpr call1 = new MethodCallExpr(new NameExpr(daoName), findByFunction);
        ASTHelper.addArgument(call1, new MethodCallExpr(new NameExpr(entityVariableName), idGetFunction));

        AssignExpr assignExpr = new AssignExpr(new NameExpr(entityVariableName), call1, AssignExpr.Operator.assign);

        MethodCallExpr call2 = new MethodCallExpr(new NameExpr(daoName), deleteFunction);
        ASTHelper.addArgument(call2, new NameExpr(entityVariableName));

        ASTHelper.addStmt(body, assignExpr);
        ASTHelper.addStmt(body, call2);

        ASTHelper.addStmt(body, new ReturnStmt(ASTHelper.createNameExpr(entityVariableName)));

        method.setBody(body);

        return method;

    }

    public static MethodDeclaration update(String entityName, String daoName, List<String> fields, String idGetFunction, String findByFunction, String updateFunction, String detachFunction, Boolean auth) {

        String entityVariableName = CrudUtility.capitalizeToLower(entityName);
        daoName = CrudUtility.capitalizeToLower(daoName);
        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "update");


        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(CrudUtility.generateParameter(entityName, null, null, null, null));
        if (auth) {
            parameterList.add(CrudUtility.generateParameter("Credentials", "Auth", null, null, null));
        }
        method.setParameters(parameterList);

        method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("POST", null, null), CrudUtility.generateAnnotation("UnitOfWork", null, null)));

        BlockStmt body = new BlockStmt();

        MethodCallExpr call1 = new MethodCallExpr(new NameExpr(daoName), detachFunction);

        ASTHelper.addArgument(call1, new NameExpr(entityVariableName));

        MethodCallExpr call2 = new MethodCallExpr(new NameExpr(daoName), findByFunction);
        ASTHelper.addArgument(call2, new MethodCallExpr(new NameExpr(entityVariableName), idGetFunction));

        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType(entityName, 0), Arrays.asList(CrudUtility.createVariableDeclarator("entity", call2)));

        MethodCallExpr call5 = new MethodCallExpr(new NameExpr(daoName), updateFunction);
        ASTHelper.addArgument(call5, new NameExpr("entity"));

        AssignExpr assignExpr = new AssignExpr(new NameExpr("entity"), call5, AssignExpr.Operator.assign);

        ASTHelper.addStmt(body, call1);
        ASTHelper.addStmt(body, variableDeclarationExpr);
        for (String string : fields) {
            ASTHelper.addStmt(body, CrudUtility.generateUpdateRow("entity", entityVariableName, "set" + CrudUtility.capitalizeToUpper(string), "get" + CrudUtility.capitalizeToUpper(string)));
        }
        ASTHelper.addStmt(body, assignExpr);
        ASTHelper.addStmt(body, new ReturnStmt(ASTHelper.createNameExpr("entity")));

        method.setBody(body);

        return method;

    }

    public static MethodDeclaration create(String entityName, String daoName, List<String> idGetFunction, String createFunction, Boolean auth, String findByFunction) {

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
        if (idGetFunction != null) {
            for (String string : idGetFunction) {

                MethodCallExpr callFindBy = new MethodCallExpr(new NameExpr(daoName), findByFunction);
                ASTHelper.addArgument(callFindBy, new MethodCallExpr(new NameExpr(entityVariableName), "get" + string));

                VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType("Optional<" + entityName + ">", 0), Arrays.asList(CrudUtility.createVariableDeclarator(CrudUtility.capitalizeToLower(string), callFindBy)));
                ASTHelper.addStmt(body, variableDeclarationExpr);


                BinaryExpr binaryExpr = new BinaryExpr(new MethodCallExpr(new NameExpr(entityVariableName), "get" + string), new StringLiteralExpr("already used by another " + entityVariableName + ". Please use different code."), Operator.plus);

                MethodCallExpr callException = new MethodCallExpr(null, "RobeRuntimeException", Arrays.asList(new StringLiteralExpr("Error"), binaryExpr));

                List<Statement> ifStatements = new ArrayList<Statement>();

                ThrowStmt throwStmt = new ThrowStmt(callException);
                ifStatements.add(throwStmt);

                IfStmt ifStmt = new IfStmt(new MethodCallExpr(new NameExpr(CrudUtility.capitalizeToLower(string)), "isPresent"), new BlockStmt(ifStatements), null);
                ASTHelper.addStmt(body, ifStmt);
            }
        }

        MethodCallExpr callCreateFunction = new MethodCallExpr(new NameExpr(daoName), createFunction);
        ASTHelper.addArgument(callCreateFunction, new NameExpr(entityVariableName));

        AssignExpr assignExpr = new AssignExpr(new NameExpr(entityVariableName), callCreateFunction, AssignExpr.Operator.assign);
        ASTHelper.addStmt(body, assignExpr);

        ASTHelper.addStmt(body, new ReturnStmt(ASTHelper.createNameExpr(entityVariableName)));

        method.setBody(body);

        return method;

    }

    public static MethodDeclaration get(String entityName, String daoName, String idGetFunction, Boolean auth) {

        String entityVariableName = CrudUtility.capitalizeToLower(entityName);
        String pathParamName = entityVariableName + "Id";
        daoName = CrudUtility.capitalizeToLower(daoName);

        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "get");

        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(CrudUtility.generateParameter("String", "PathParam", "{" + pathParamName + "}", pathParamName, null));
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

    public static MethodDeclaration getAll(String entityName, String daoName, String findAllFunction, Boolean auth) {

        String name = "get" + entityName + "s";
        daoName = CrudUtility.capitalizeToLower(daoName);

        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType("List<" + entityName + ">", 0), name);
        if (auth) {
            method.setParameters(Arrays.asList(CrudUtility.generateParameter("Credentials", "Auth", null, null, null)));
        }
        method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("Path", "all", null), CrudUtility.generateAnnotation("GET", null, null), CrudUtility.generateAnnotation("UnitOfWork", null, null)));
        BlockStmt body = new BlockStmt();

        FieldAccessExpr field = new FieldAccessExpr(new NameExpr(entityName), "class");
        MethodCallExpr callFindAll = new MethodCallExpr(new NameExpr(daoName), findAllFunction);
        ASTHelper.addArgument(callFindAll, field);

        ReturnStmt returnStmt = new ReturnStmt(callFindAll);

        ASTHelper.addStmt(body, returnStmt);
        method.setBody(body);

        return method;
    }


}
