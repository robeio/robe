package io.robe.hibernate.crud;

import japa.parser.ASTHelper;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CrudUtility {
	
	public static PackageDeclaration getPackage(String dirPath){
		return new PackageDeclaration(ASTHelper.createNameExpr(dirPath));
	}

	public static List<ImportDeclaration> getImports(String...imports) {
		List<ImportDeclaration> importDeclarations = new LinkedList<ImportDeclaration>();

		for (String pak : imports) {
			ImportDeclaration imp = new ImportDeclaration();
			imp.setName(ASTHelper.createNameExpr(pak));
			importDeclarations.add(imp);
		}
		return importDeclarations;
	}
	

	public static AnnotationExpr generateAnnotation(String annotation,String annotationParameter,String parameterAccess){
		if(isEmpty(annotationParameter)){
			return new MarkerAnnotationExpr(ASTHelper.createNameExpr(annotation));
		}else if(isEmpty(parameterAccess)) {
			return new SingleMemberAnnotationExpr(new NameExpr(annotation), new StringLiteralExpr(annotationParameter));
		} else {
			return new SingleMemberAnnotationExpr(ASTHelper.createNameExpr(annotation),new FieldAccessExpr(ASTHelper.createNameExpr(annotationParameter),parameterAccess));
		}
	}
	
	public static MethodCallExpr generateUpdateRow(String entity,String entityVariableName,String setMethod,String getMethod){
		MethodCallExpr call = new MethodCallExpr(new  NameExpr(entity), setMethod);
        ASTHelper.addArgument(call,new MethodCallExpr(new NameExpr(entityVariableName),getMethod));
        return call;
	}
	
	public static Parameter generateParameter(String type,String annotation,String annotationParameter,String paramName,String parameterAccess){
		Parameter parameter = ASTHelper.createParameter(ASTHelper.createReferenceType(type, 0),(isEmpty(paramName)?capitalizeToLower(type):paramName));
		
		if(!isEmpty(annotation)){
				if(isEmpty(annotationParameter)){
					parameter.setAnnotations(Arrays.asList(generateAnnotation(annotation, null,null)));
				}else if(isEmpty(parameterAccess)){
					parameter.setAnnotations(Arrays.asList(generateAnnotation(annotation, annotationParameter,null)));
				} else {
					parameter.setAnnotations(Arrays.asList(generateAnnotation(annotation, annotationParameter, parameterAccess)));
				}
		}
		return parameter;
	}
	

	public static VariableDeclarator createVariableDeclarator(String variableDeclaratorIdName,Expression init){
		
		 VariableDeclarator variableDeclarator = new VariableDeclarator(new VariableDeclaratorId(variableDeclaratorIdName));
		 if(init!=null){
			 variableDeclarator.setInit(init);
		 }
		return variableDeclarator;
	}
	
	public static String capitalizeToLower(String line) {
		return Character.toLowerCase(line.charAt(0)) + line.substring(1);
	}
	
	public static String capitalizeToUpper(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

    public static boolean isEmpty(String line){
        return line==null||line.trim().equals("");
    }
	
}
