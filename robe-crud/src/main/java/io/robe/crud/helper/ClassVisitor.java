package io.robe.crud.helper;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassVisitor extends VoidVisitorAdapter {

    public static List<String> classes = new ArrayList<String>();
    public static Map<String, List<Model>> models = new HashMap<String, List<Model>>();
    public static Map<String, List<String>> allColumns = new HashMap<String, List<String>>();
    public static Map<String, List<String>> uniqueColumns = new HashMap<String, List<String>>();
    public static Map<String, String> imports = new HashMap<String, String>();

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {

        List<AnnotationExpr> list = n.getAnnotations();

        if (list != null) {
            for (AnnotationExpr annotationExpr : list) {
                if (annotationExpr.toString().equals("@Entity")) {

                    List<BodyDeclaration> body = n.getMembers();
                    List<String> allList = new ArrayList<String>();
                    List<String> uniqueList = new ArrayList<String>();

                    imports.put(n.getName(), ((CompilationUnit) n.getParentNode()).getPackage().getName().toString());
                    List<Model> model = new ArrayList<Model>();
                    for (BodyDeclaration bodyDeclaration : body) {

                        if (bodyDeclaration instanceof FieldDeclaration) {
                            FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
                            List<AnnotationExpr> fieldExp = fieldDeclaration.getAnnotations();
                            VariableDeclarator variableDeclarator = fieldDeclaration.getVariables().get(0);

                            Model m = new Model();

                            m.setName(variableDeclarator.getId().toString());
                            //set default value
                            m.setNullable(true);
                            m.setLength("255");
                            m.setType("string");

                            String fieldType = fieldDeclaration.getType().toString().toLowerCase().replaceAll("\"", "");
                            if (fieldType.equals("boolean")) {
                                m.setType("boolean");
                            } else if (fieldType.equals("integer") || fieldType.equals("int") || fieldType.equals("bigdecimal") || fieldType.equals("double") || fieldType.equals("long")) {
                                m.setType("number");
                            }

                            if (fieldExp != null) {
                                for (AnnotationExpr expr : fieldExp) {
                                    if (expr.getName().toString().equals("Column")) {
                                        allList.add(variableDeclarator.getId().toString());
                                        List<Node> nodes = expr.getChildrenNodes();
                                        for (Node node : nodes) {
                                            if (node instanceof MemberValuePair) {
                                                MemberValuePair memberValuePair = (MemberValuePair) node;
                                                if (memberValuePair.getName().equals("nullable")) {
                                                    Boolean nullable = Boolean.valueOf(memberValuePair.getValue().toString());
                                                    m.setNullable(nullable);
                                                } else if ((memberValuePair.getName().equals("length"))) {
                                                    m.setLength(memberValuePair.getValue().toString());
                                                }
                                                if (memberValuePair.getName().equals("unique")) {
                                                    if (Boolean.valueOf(memberValuePair.getValue().toString())) {
                                                        uniqueList.add(variableDeclarator.getId().toString());
                                                    }
                                                }
                                            }
                                        }
                                        model.add(m);
                                    }
                                }
                                allColumns.put(n.getName(), allList);
                                uniqueColumns.put(n.getName(), uniqueList);
                                models.put(n.getName(), model);
                            }
                        }
                    }
                    classes.add(n.getName());
                }
            }

        }

    }
}