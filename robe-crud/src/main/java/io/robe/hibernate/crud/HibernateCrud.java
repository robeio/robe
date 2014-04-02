package io.robe.hibernate.crud;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.expr.NameExpr;
import org.reflections.Reflections;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.reflections.ReflectionUtils.*;

public class HibernateCrud {

    public static void generateDaoAndResource(Properties properties) throws IOException{

        String packageScan = (String)properties.get("packageScan");
        String packageResource = (String)properties.get("packageResource");
        String packageDao=(String)properties.get("packageDao");
        Set<Class<?>> scanClasses = getEntityClasses(packageScan);

        if (scanClasses.isEmpty()) {
            System.out.println("Exiting: No entity found!");
            return;
        }

        String fileDaoLocation = properties.getProperty("projectPath", "./") + "/src/main/java/" + packageDao.replace('.', '/');
        System.out.println("DAO: " + fileDaoLocation);
        new File(fileDaoLocation).mkdirs();

        String fileResourceLocation = properties.getProperty("projectPath", ".") + "/src/main/java/" + packageResource.replace('.', '/');
        System.out.println("RESOURCES: " + fileResourceLocation);

        new File(fileResourceLocation).mkdirs();

        for (Class<?> classes : scanClasses) {

            String newDaoClassName=fileDaoLocation+"/"+classes.getSimpleName()+"Dao.java";

            File fileDao = new File(newDaoClassName);
            if (!fileDao.exists()) {
                fileDao.createNewFile();
            }
            FileWriter fwDao = new FileWriter(fileDao.getAbsoluteFile());
            BufferedWriter bwDao = new BufferedWriter(fwDao);


            String newResourceClassName=fileResourceLocation+"/"+classes.getSimpleName()+"Resource.java";

            File fileResource = new File(newResourceClassName);
            if (!fileResource.exists()) {
                fileResource.createNewFile();
            }
            FileWriter fwResource = new FileWriter(fileResource.getAbsoluteFile());
            BufferedWriter bwResource = new BufferedWriter(fwResource);

            List<String> fieldGet = new ArrayList<String>();
            List<String> uniqueFields= new ArrayList<String>();
            Set<Field> fields = getAllFields(classes, withAnnotation(Column.class));
            for (Field field : fields) {
                if (!field.getAnnotation(Column.class).unique()) {
                    Set<Method> getters = getAllMethods(classes,
                            withModifier(Modifier.PUBLIC), withPrefix("get"+CrudUtility.capitalizeToUpper(field.getName())), withParametersCount(0));
                    if(getters.iterator().hasNext()){
                        fieldGet.add(CrudUtility.capitalizeToUpper(field.getName()));
                    }
                }else if(field.getAnnotation(Column.class).unique()){
                    Set<Method> checks = getAllMethods(classes,
                            withModifier(Modifier.PUBLIC), withPrefix("get"+CrudUtility.capitalizeToUpper(field.getName())), withParametersCount(0));
                    if(checks.iterator().hasNext()){
                        uniqueFields.add(CrudUtility.capitalizeToUpper(field.getName()));
                        fieldGet.add(CrudUtility.capitalizeToUpper(field.getName()));
                    }
                }
            }

            String entityName=classes.getSimpleName();
            String daoName=classes.getSimpleName()+"Dao";

            String findBy="findById";
            for (String string : uniqueFields) {
                findBy+="Or"+CrudUtility.capitalizeToUpper(string);
            }

            List<ImportDeclaration> importDeclarations = new ArrayList<ImportDeclaration>();
            importDeclarations.add(new ImportDeclaration(new NameExpr(classes.getName().toString()), false, false));
            bwDao.write(DaoCrud.createDao(entityName, packageDao, importDeclarations, uniqueFields,findBy));
            bwDao.close();
            System.err.println("Dao Created!");


            List<BodyDeclaration> bodyDeclarations = new ArrayList<BodyDeclaration>();

            bodyDeclarations.add(ResourceCrud.getAll(entityName, daoName, "findAll"));
            bodyDeclarations.add(ResourceCrud.get(entityName, daoName,findBy, "get"+entityName+"s"));
            bodyDeclarations.add(ResourceCrud.create(entityName, daoName, uniqueFields, findBy, "create"));
            bodyDeclarations.add(ResourceCrud.update(entityName, daoName,fieldGet, "getOid",findBy, "update", "detach"));
            bodyDeclarations.add(ResourceCrud.delete(entityName, daoName, "getOid", findBy, "delete"));
            List<ImportDeclaration> importDeclarationsResource = new ArrayList<ImportDeclaration>();
            importDeclarationsResource.addAll(CrudUtility.getImports("com.google.inject.Inject","com.yammer.dropwizard.auth.Auth","com.yammer.dropwizard.hibernate.UnitOfWork"));
            bwResource.write(ResourceCrud.ResourceGenerate(entityName+"Resource",entityName,daoName,bodyDeclarations,importDeclarationsResource,packageResource));
            bwResource.close();
            System.err.println("Resource Created");

        }
    }

    public static Set<Class<?>> getEntityClasses(String packageScan) {
        Reflections reflections = new Reflections(packageScan);

        Set<Class<?>> scanClasses = new HashSet<Class<?>>();

        scanClasses.addAll(reflections.getTypesAnnotatedWith(Entity.class));
        return scanClasses;
    }

    public static void main(String[] args) throws IOException {



	}
}
