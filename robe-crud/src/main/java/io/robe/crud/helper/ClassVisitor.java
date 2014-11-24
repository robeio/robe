package io.robe.crud.helper;

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

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {

        List<AnnotationExpr> list = n.getAnnotations();

        if (list != null) {
            for (AnnotationExpr annotationExpr : list) {
                if (annotationExpr.toString().equals("@Entity")) {

                    List<BodyDeclaration> body = n.getMembers();

                    List<Model> model = new ArrayList<Model>();
                    for (BodyDeclaration bodyDeclaration : body) {

                        if (bodyDeclaration instanceof FieldDeclaration) {
                            FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
                            List<AnnotationExpr> fieldExp = fieldDeclaration.getAnnotations();

                            if (fieldExp != null) {

                                VariableDeclarator variableDeclarator = fieldDeclaration.getVariables().get(0);

                                for (AnnotationExpr expr : fieldExp) {
                                    if (expr.getName().toString().equals("Column")) {

                                        List<Node> nodes = expr.getChildrenNodes();
                                        Model m = new Model();
                                        m.setName(variableDeclarator.getId().toString());
                                        m.setNullable(true);
                                        m.setLength("255");
                                        m.setType("string");
                                        String fieldType = fieldDeclaration.getType().toString().toLowerCase().replaceAll("\"", "");

                                        if (fieldType.equals("boolean")) {
                                            m.setType("boolean");
                                        } else if (fieldType.equals("integer") || fieldType.equals("int") || fieldType.equals("bigdecimal") || fieldType.equals("double") || fieldType.equals("long")) {
                                            m.setType("number");
                                        }
                                        for (Node node : nodes) {
                                            if (node instanceof MemberValuePair) {
                                                MemberValuePair memberValuePair = (MemberValuePair) node;
                                                if (memberValuePair.getName().equals("nullable")) {
                                                    Boolean nullable = Boolean.valueOf(memberValuePair.getValue().toString());
                                                    m.setNullable(nullable);
                                                } else if ((memberValuePair.getName().equals("length"))) {
                                                    m.setLength(memberValuePair.getValue().toString());
                                                }
                                            }
                                        }
                                        model.add(m);
                                    }
                                }
                                models.put(n.getName(), model);
                            }
                        }
                    }
                    classes.add(n.getName());
                    return;
                }
            }
        }
    }
}
