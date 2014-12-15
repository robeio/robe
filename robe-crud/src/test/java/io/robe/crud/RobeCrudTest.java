package io.robe.crud;

import freemarker.template.TemplateException;
import io.robe.crud.helper.CrudUtility;
import io.robe.crud.helper.GenerateJS;
import io.robe.crud.helper.Model;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RobeCrudTest {
    @Test
    public void generateHtml() throws IOException, TemplateException {

        GenerateJS generateJS = new GenerateJS();
        String out = generateJS.createHtml("Address");
        System.out.println(out);
        assert out != null;
    }


    @Test
    public void generateView() throws IOException, TemplateException {
        GenerateJS generateJS = new GenerateJS();
        List<Model> models = new ArrayList<>();

        Model model1 = new Model();
        model1.setLength("255");
        model1.setName("item1");
        model1.setNullable(false);
        model1.setType("number");


        Model model2 = new Model();
        model2.setLength("255");
        model2.setName("item2");
        model2.setNullable(false);
        model2.setType("string");

        models.add(model1);
        models.add(model2);
        String out = generateJS.createView("Address", models);
        System.out.println(out);

        assert out != null;

    }


    @Test
    public void generateDataSource() throws IOException, TemplateException {
        GenerateJS generateJS = new GenerateJS();
        String out = generateJS.createDataSource("Address");
        System.out.println(out);
        assert out != null;
    }

    @Test
    public void generateModel() throws IOException, TemplateException {
        GenerateJS generateJS = new GenerateJS();
        List<Model> models = new ArrayList<>();

        Model model1 = new Model();
        model1.setLength("255");
        model1.setName("item1");
        model1.setNullable(false);
        model1.setType("number");


        Model model2 = new Model();
        model2.setLength("255");
        model2.setName("item2");
        model2.setNullable(false);
        model2.setType("string");

        models.add(model1);
        models.add(model2);
        String out = generateJS.createModel("Address", models);
        System.out.println(out);

        assert out != null;
    }


    @Test
    public void generateDao() {

        String entityName = "Address";
        String packageName = "io.robe.dao";

        List<ImportDeclaration> importDeclarations = new ArrayList<ImportDeclaration>();

        String[] imports = {
                "com.google.inject.Inject",
                "org.hibernate.SessionFactory",
                "io.robe.hibernate.dao.BaseDao",
                packageName + "." + entityName
        };

        importDeclarations.addAll(CrudUtility.getImports(imports));

        String clazz = DaoCrud.createDao(entityName, packageName, importDeclarations, new ArrayList<String>(), "findById");
        System.out.println(clazz);
        assert clazz != null;
    }

    @Test
    public void generateResource() {
        String entity = "Address";
        String daoName = "AddressDao";
        String findBy = "findById";
        String packageName = "io.robe.entity";
        boolean auth = true;
        boolean inject = true;

        List<String> uniqueFields = new ArrayList<>();
        List<String> fieldGet = new ArrayList<>();
        fieldGet.add("nick");
        fieldGet.add("city");
        fieldGet.add("county");

        List<BodyDeclaration> bodyDeclarations = new ArrayList<BodyDeclaration>();

        bodyDeclarations.add(ResourceCrud.getAll(entity, daoName, "findAll", auth));
        bodyDeclarations.add(ResourceCrud.get(entity, daoName, findBy, auth));
        bodyDeclarations.add(ResourceCrud.create(entity, daoName, uniqueFields, "create", auth, findBy));
        bodyDeclarations.add(ResourceCrud.update(entity, daoName, fieldGet, "getOid", findBy, "update", "detach", auth));
        bodyDeclarations.add(ResourceCrud.delete(entity, daoName, "getOid", findBy, "delete", auth));


        List<ImportDeclaration> importDeclarationsResource = new ArrayList<ImportDeclaration>();
        String[] imports = {
                "com.google.inject.Inject",
                "io.dropwizard.auth.Auth",
                "io.dropwizard.hibernate.UnitOfWork",
                "io.robe.auth.Credentials",
                "javax.validation.Valid",
                "javax.ws.rs.Consumes",
                "javax.ws.rs.DELETE",
                "javax.ws.rs.GET",
                "javax.ws.rs.POST",
                "javax.ws.rs.PUT",
                "javax.ws.rs.Path",
                "javax.ws.rs.PathParam",
                "javax.ws.rs.Produces",
                "javax.ws.rs.core.MediaType",
                "java.util.List",
                "io.robe.dao." + entity + "Dao",
                packageName + "." + entity,

        };
        importDeclarationsResource.addAll(CrudUtility.getImports(imports));

        String clazz = ResourceCrud.resourceGenerate(entity, daoName, bodyDeclarations, importDeclarationsResource, packageName + ".resource", inject);

        System.out.println(clazz);

        assert clazz != null;
    }

}
