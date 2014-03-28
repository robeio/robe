package com.robe.hibernate.crud;

import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DaoCrud {
	
	public static String createDao(String entityName,String packageName,List<ImportDeclaration> importDeclarations,List<String> fields,String findBy)
	{
	    CompilationUnit compilationUnit = new CompilationUnit();
	    compilationUnit.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(packageName)));

	    List<ClassOrInterfaceType> extedsList = new ArrayList<ClassOrInterfaceType>();
	    extedsList.add(new ClassOrInterfaceType("BaseDao<"+entityName+">"));
	    
	    compilationUnit.setImports(importDeclarations);
	    
	    List<BodyDeclaration> members= new ArrayList<BodyDeclaration>();
	  
	    ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(ModifierSet.PUBLIC,entityName);
	    constructorDeclaration.setAnnotations(Arrays.asList(CrudUtility.generateAnnotation("Inject",null,null)));
	    BlockStmt conBlock = new BlockStmt();
	    List<Expression> args = new LinkedList<Expression>();
	  	args.add(ASTHelper.createNameExpr("sessionFactory"));
	  	
	  	ASTHelper.addStmt(conBlock, new MethodCallExpr(null,"super",args));
	  	
	    constructorDeclaration.setParameters(Arrays.asList(CrudUtility.generateParameter("SessionFactory", null, null, null, null)));
	    constructorDeclaration.setBlock(conBlock);
	    members.add(constructorDeclaration);

	    members.add(findby(entityName, fields,findBy));
	    
	    ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(1,null, false, entityName+"Dao", null,extedsList , null, members);
	    ASTHelper.addTypeDeclaration(compilationUnit, type);

	    return compilationUnit.toString();
	  }
	
	private static MethodDeclaration findby(String entityName,List<String> fields,String findBy){
		
		MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(entityName, 0), findBy);
		
	    method.setParameters(Arrays.asList(CrudUtility.generateParameter("String", null, null,"code",null)));
	    
	    BlockStmt body = new BlockStmt();
	    MethodCallExpr callCurrentSession = new MethodCallExpr(null, "currentSession");
	    
	    MethodCallExpr callClass = new MethodCallExpr(callCurrentSession, "createCriteria");
	    
	    FieldAccessExpr fieldClass = new FieldAccessExpr(new NameExpr(entityName), "class");
	    ASTHelper.addArgument(callClass,fieldClass);
	    VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(ASTHelper.createReferenceType("Criteria", 0), Arrays.asList(CrudUtility.createVariableDeclarator("criteria", callClass)));
	    
	    ASTHelper.addStmt(body, variableDeclarationExpr);
	    
	    MethodCallExpr callRestrictions = new MethodCallExpr(new NameExpr("Restrictions"), "disjunction");
	    VariableDeclarationExpr variableDeclarationExpr1 = new VariableDeclarationExpr(ASTHelper.createReferenceType("Disjunction", 0), Arrays.asList(CrudUtility.createVariableDeclarator("disjunction", callRestrictions)));
	    ASTHelper.addStmt(body, variableDeclarationExpr1);
	    

	    for (String string : fields) {
	    	
		    MethodCallExpr callAdd = new MethodCallExpr(new NameExpr("disjunction"), "add");
		    MethodCallExpr callEq = new MethodCallExpr(new NameExpr("Restrictions"), "eq", Arrays.asList(new StringLiteralExpr(string),new NameExpr("code")));
		    ASTHelper.addArgument(callAdd, callEq);
		    ASTHelper.addStmt(body, callAdd);
		}
	    method.setName(findBy);
	    
	    MethodCallExpr callDisjunction = new MethodCallExpr(new NameExpr("disjunction"), "add");
	    ASTHelper.addArgument(callDisjunction, new NameExpr("disjunction"));
	    
	    ASTHelper.addStmt(body, callDisjunction);	
	    
	    MethodCallExpr callUniqueResult = new MethodCallExpr(null, "uniqueResult");
	    ASTHelper.addArgument(callUniqueResult,new NameExpr("criteria"));
	    MethodCallExpr callFromNullable = new MethodCallExpr(new NameExpr("Optional"), "fromNullable");
	    ASTHelper.addArgument(callFromNullable,callUniqueResult);
	        
	    ReturnStmt returnStmt = new ReturnStmt(callFromNullable);
       
	    ASTHelper.addStmt(body, returnStmt);	
	    
	    method.setBody(body);
	    
	    return method;
	    
	}

}
