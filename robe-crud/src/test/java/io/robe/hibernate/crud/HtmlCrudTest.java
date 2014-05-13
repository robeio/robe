package io.robe.hibernate.crud;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class HtmlCrudTest {


    public static void main5(String[] args) {
		
		Configuration cfg = new Configuration();
        try {


            Template template = cfg.getTemplate("src/main/java/resource/html.ftl");
            
            String name="Role";
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("view", name);
            
             
            Writer out = new OutputStreamWriter(System.out,"UTF8");
            template.process(data, out);
            out.flush();
 
            
            Writer file1 = new FileWriter (new File("src/test/resource/"+name+".html"),true);
            template.process(data, file1);
            file1.flush();
            file1.close();
             
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
		
	}

    public static void main3(String[] args) {

		 Configuration cfg = new Configuration();
	        try {
	        	
	        	String name="Role";
	            Template template = cfg.getTemplate("src/test/resource/view.ftl");

	            List<Model> fields = new ArrayList<Model>();
	            
	            Model model = new Model();
	            
	            model.setName("oid");
	            model.setEditable(true);
	            model.setNullable(true);
	            model.setValidation(true);
	            
	            fields.add(model);
	            
	            Model model1 = new Model();
	            
	            model1.setName("name");
	            model1.setEditable(true);
	            model1.setNullable(true);
	            model1.setValidation(true);
	            
	            fields.add(model1);
	           
	            
	            Map<String, Object> data = new HashMap<String, Object>();
	            data.put("view", name+"Management");
	            data.put("fields", fields);
	            data.put("dataSource", name+"DataSource");
	            
	             
	            Writer out = new OutputStreamWriter(System.out,"UTF8");
	            template.process(data, out);
	            out.flush();
	 
	            
	            Writer file1 = new FileWriter (new File("src/test/resource/"+name+"Management.js"),true);
	            template.process(data, file1);
	            file1.flush();
	            file1.close();
	             
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (TemplateException e) {
	            e.printStackTrace();
	        }
	    
		
	}

    public static void main2(String[] args) {
		

		 Configuration cfg = new Configuration();
	        try {
	        	
	        	String name="Role";
	            Template template = cfg.getTemplate("src/test/resource/datasource.ftl");
	            
	            Map<String, Object> data = new HashMap<String, Object>();
	            data.put("dataSourceName", name+"DataSource");
	            data.put("entity", name);
	            data.put("modelName", name+"Model");
	            
	             
	            Writer out = new OutputStreamWriter(System.out);
	            template.process(data, out);
	            out.flush();
	 
	            
	            Writer file1 = new FileWriter (new File("src/test/resource/DataSource.js"),true);
	            template.process(data, file1);
	            file1.flush();
	            file1.close();
	             
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (TemplateException e) {
	            e.printStackTrace();
	        }
	    
		
	}
	
	public static void main1(String[] args) {
		 Configuration cfg = new Configuration();
	        try {
	        	String name="Role";
	            Template template = cfg.getTemplate("src/test/resource/model.ftl");
	             
	            List<Model> fields = new ArrayList<Model>();
	            
	            Model model = new Model();
	            
	            model.setName("oid");
	            model.setEditable(true);
	            model.setNullable(true);
	            model.setValidation(true);
	            
	            fields.add(model);
	            
	           
	            
	            Map<String, Object> data = new HashMap<String, Object>();
	            data.put("modelName", name+"Model");
	            data.put("fields", fields);
	            
	             
	            Writer out = new OutputStreamWriter(System.out,"UTF8");
	            template.process(data, out);
	            out.flush();
	 
	            
	            Writer file1 = new FileWriter (new File("src/test/resource/Models.js"),true);
	            template.process(data, file1);
	            file1.flush();
	            file1.close();
	             
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (TemplateException e) {
	            e.printStackTrace();
	        }
	    }

}
