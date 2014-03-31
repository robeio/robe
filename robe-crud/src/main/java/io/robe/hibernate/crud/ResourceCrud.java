package io.robe.hibernate.crud;

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

	public static String ResourceGenerate(String name,String entityName,String daoName,List<BodyDeclaration> bodyDeclarationsList,List<ImportDeclaration> importDeclarations,String packageName){

		CompilationUnit compilationUnit = new CompilationUnit();
		compilationUnit.setImports(importDeclarations);

		compilationUnit.setPackage(CrudUtility.getPackage(packageName));

		List<BodyDeclaration> bodyDeclarations = new ArrayList<BodyDeclaration>();
		 
		FieldDeclaration injectFieldDeclaration = ASTHelper.createFieldDeclaration(0,new ClassOrInterfaceType(daoName), CrudUtility.createVariableDeclarator(CrudUtility.capitalizeToLower(daoName), null));
		List<AnnotationExpr> fieldAnnotationExprs= new ArrayList<AnnotationExpr>();
		  	
		fieldAnnotationExprs.add(new MarkerAnnotationExpr(ASTHelper.createNameExpr("Inject")));
		injectFieldDeclaration.setAnnotations(fieldAnnotationExprs);
		  	
		bodyDeclarations.add(injectFieldDeclaration);
		bodyDeclarations.addAll(bodyDeclarationsList);
		
		ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(1,Arrays.asList(CrudUtility.generateAnnotation("PATH", entityName, null),CrudUtility.generateAnnotation("Consumes", "MediaType", "APPLICATION_JSON"),CrudUtility.generateAnnotation("Produces", "MediaType", "APPLICATION_JSON")), false, name, null,null , null, bodyDeclarations);
	    ASTHelper.addTypeDeclaration(compilationUnit, type);
		
		return compilationUnit.toString();
	}
	
	public static MethodDeclaration delete(String entityName,String daoName,String idGetFunction,String findByFunction,String deleteFunction){
		
		String entityVariableName=CrudUtility.capitalizeToLower(entityName);
		daoName=CrudUtility.capitalizeToLower(daoName);
	
		MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "delete");
		
	    method.setParameters(Arrays.asList(CrudUtility.generateParameter("Credentials", "Auth", null,null,null),CrudUtility.generateParameter(entityName, null, null,null,null)));
	    method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("DELETE", null,null),CrudUtility.generateAnnotation("UnitOfWork", null,null)));
	    
	    BlockStmt body = new BlockStmt();
	    
        MethodCallExpr call1 = new MethodCallExpr(new  NameExpr(daoName), findByFunction);
        ASTHelper.addArgument(call1,new MethodCallExpr(new  NameExpr(entityVariableName), idGetFunction));
        
        AssignExpr assignExpr = new AssignExpr(new NameExpr(entityVariableName), call1, AssignExpr.Operator.assign);
        
        MethodCallExpr call2 = new MethodCallExpr(new  NameExpr(daoName), deleteFunction);
        ASTHelper.addArgument(call2,new NameExpr(entityVariableName));
	    
        ASTHelper.addStmt(body, assignExpr);
        ASTHelper.addStmt(body, call2);
        
	    ASTHelper.addStmt(body, new ReturnStmt(ASTHelper.createNameExpr(entityVariableName)));
	    
	    method.setBody(body);
	    
	    return method;
		
	}

	public static MethodDeclaration update(String entityName,String daoName,List<String> fields,String idGetFunction,String findByFunction,String updateFunction,String detachFunction){
		
		String entityVariableName=CrudUtility.capitalizeToLower(entityName);
		daoName=CrudUtility.capitalizeToLower(daoName);
		MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "update");
		
	    method.setParameters(Arrays.asList(CrudUtility.generateParameter("Credentials", "Auth", null,null,null),CrudUtility.generateParameter(entityName, null, null,null,null)));
	    method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("PUT", null,null),CrudUtility.generateAnnotation("UnitOfWork", null,null)));
	    
	    BlockStmt body = new BlockStmt();
	    
	    MethodCallExpr call1 = new MethodCallExpr(new  NameExpr(daoName), detachFunction);
        
	    ASTHelper.addArgument(call1,new NameExpr(entityVariableName));
	    
        MethodCallExpr call2 = new MethodCallExpr(new NameExpr(daoName), findByFunction);
        ASTHelper.addArgument(call2,new MethodCallExpr(new NameExpr(entityVariableName),idGetFunction));

	    VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType(entityName, 0), Arrays.asList(CrudUtility.createVariableDeclarator("entity", call2)));
        
        MethodCallExpr call5 = new MethodCallExpr(new  NameExpr(daoName), updateFunction);
        ASTHelper.addArgument(call5,new NameExpr("entity"));
        
        AssignExpr assignExpr = new AssignExpr(new NameExpr("entity"), call5, AssignExpr.Operator.assign);
        
        ASTHelper.addStmt(body, call1);
        ASTHelper.addStmt(body, variableDeclarationExpr);
        for (String string : fields) {
        	ASTHelper.addStmt(body, CrudUtility.generateUpdateRow("entity",entityVariableName,"set"+string,"get"+string));
		}
        ASTHelper.addStmt(body, assignExpr);
        ASTHelper.addStmt(body, new ReturnStmt(ASTHelper.createNameExpr("entity")));
        
	    method.setBody(body);
	    
	    return method;
	    
	}
	
	public static MethodDeclaration create(String entityName,String daoName,List<String> idGetFunction,String findByFunction,String createFunction){
		
		String entityVariableName=CrudUtility.capitalizeToLower(entityName);
		daoName=CrudUtility.capitalizeToLower(daoName);
		MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "create");
		
	    method.setParameters(Arrays.asList(CrudUtility.generateParameter("Credentials","Auth",null,null,null),CrudUtility.generateParameter(entityName, "Valid",null,null,null)));
	    method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("POST", null,null),CrudUtility.generateAnnotation("UnitOfWork", null,null)));
	    
	    BlockStmt body = new BlockStmt();

	    for (String string : idGetFunction) {

	    	 MethodCallExpr callFindBy = new MethodCallExpr(new NameExpr(daoName), "findBy"+string);
	         ASTHelper.addArgument(callFindBy,new MethodCallExpr(new NameExpr(entityVariableName),"get"+string));
	         
	         VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType("Optional<"+entityName+">", 0), Arrays.asList(CrudUtility.createVariableDeclarator(CrudUtility.capitalizeToLower(string), callFindBy)));
	         ASTHelper.addStmt(body, variableDeclarationExpr);
	         
	         
	         BinaryExpr binaryExpr = new BinaryExpr(new MethodCallExpr(new NameExpr(entityVariableName),"get"+string), new StringLiteralExpr("already used by another "+ entityVariableName +". Please use different code.") ,Operator.plus);
	         
	         MethodCallExpr callException = new MethodCallExpr(null, "RobeRuntimeException", Arrays.asList(new StringLiteralExpr("Error"),binaryExpr));
	         
	         List<Statement> ifStatements =new ArrayList<Statement>();
	         
	         ThrowStmt throwStmt = new ThrowStmt(callException);
	         ifStatements.add(throwStmt);

	         IfStmt ifStmt = new IfStmt(new MethodCallExpr(new NameExpr(CrudUtility.capitalizeToLower(string)),"isPresent"),new BlockStmt(ifStatements) , null);
	         ASTHelper.addStmt(body, ifStmt);
		}
	 
        MethodCallExpr callCreateFunction = new MethodCallExpr(new  NameExpr(daoName), createFunction);
        ASTHelper.addArgument(callCreateFunction,new NameExpr(entityVariableName));
        
        AssignExpr assignExpr = new AssignExpr(new NameExpr(entityVariableName), callCreateFunction, AssignExpr.Operator.assign);
        ASTHelper.addStmt(body, assignExpr);
        
        
        
	    ASTHelper.addStmt(body, new ReturnStmt(ASTHelper.createNameExpr(entityVariableName)));
	    
	    method.setBody(body);
	    
	    return method;
		
	}
	
	public static MethodDeclaration get(String entityName,String daoName,String idGetFunction,String findByFunction){
		
		String entityVariableName=CrudUtility.capitalizeToLower(entityName);
		String pathParamName=entityVariableName+"Id";
		daoName=CrudUtility.capitalizeToLower(daoName);
		
		MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), "get");
		
	    method.setParameters(Arrays.asList(CrudUtility.generateParameter("Credentials", "Auth", null,null,null),CrudUtility.generateParameter("String", "PathParam", "{"+pathParamName+"}",pathParamName,null)));
	    method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("PATH", "{"+pathParamName+"}",null),CrudUtility.generateAnnotation("GET", null,null),CrudUtility.generateAnnotation("UnitOfWork", null,null)));
	    
	    BlockStmt body = new BlockStmt();
	    
	    MethodCallExpr callIdGetFunction = new MethodCallExpr(new NameExpr(daoName), idGetFunction);
	    ASTHelper.addArgument(callIdGetFunction,new NameExpr(pathParamName));

	    VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType(entityName, 0), Arrays.asList(CrudUtility.createVariableDeclarator(entityVariableName, callIdGetFunction)));

        MethodCallExpr callHibernate = new MethodCallExpr(new NameExpr("Hibernate"), "initialize");
        ASTHelper.addArgument(callHibernate,new MethodCallExpr(new NameExpr(entityVariableName),findByFunction));
        
        ASTHelper.addStmt(body, variableDeclarationExpr);
        ASTHelper.addStmt(body, callHibernate);
	    ASTHelper.addStmt(body, new ReturnStmt(ASTHelper.createNameExpr(entityVariableName)));
	    
	    method.setBody(body);
		
		return method;
		
	}
	
	public static MethodDeclaration getAll(String entityName,String daoName,String findAllFunction) {
		
		String name="get"+entityName+"s";
		daoName=CrudUtility.capitalizeToLower(daoName);

	    List<Statement> statements = new ArrayList<Statement>();
		
	    MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType("List<"+entityName+">", 0),name);
	    
		method.setParameters(Arrays.asList(CrudUtility.generateParameter("Credentials", "Auth", null, null,null)));
	    method.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("PATH", "all",null),CrudUtility.generateAnnotation("GET", null,null),CrudUtility.generateAnnotation("UnitOfWork",null,null)));
	    BlockStmt body = new BlockStmt();
	    

	    FieldAccessExpr field = new FieldAccessExpr(new NameExpr(entityName), "class");
	    MethodCallExpr callFindAll = new MethodCallExpr(new NameExpr(daoName), findAllFunction);
	    ASTHelper.addArgument(callFindAll,field);
	        
	    ReturnStmt returnStmt = new ReturnStmt(callFindAll);
	    statements.add(returnStmt);
	    body.setStmts(statements);
	    
	    method.setBody(body);
	    
	    return method;
	}
	
	

}
