package io.robe.hibernate.gui;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.robe.hibernate.helper.Model;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import org.apache.tools.ant.DirectoryScanner;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobeHtmlCrudGUI extends javax.swing.JFrame {

    private static String OUTPUT_PATH;
    private static final String JAVA_IO_TMP_DIR = "user.home";
    private static final String JS = "js";
    private static final String HTML = "html";
    private static final String HTML_FTL = "html.ftl";
    private static final String MODEL = "Model";
    private static final String DATA = "data";
    private static final String VIEW = "view";
    private static final String VIEW_FTL = "view.ftl";
    private static final String VAR = "var ";
    private static final String DATASOURCE = "DataSource";
    private static final String DATASOURCE_FTL = "datasource.ftl";
    private static final String DATASOURCE_JS = "DataSources.js";
    private static final String MODEL_JS = "Model.js";
    private static final String MODEL_FTL = "model.ftl";
    private static final String MANAGEMENT = "Management";
    private static final String MANAGEMENT_JS = "Management.js";
    private static final String MANAGEMENT_HTML = "Management.html";
    private static String TEMPLATE_PATH = "/robe-crud/src/main/resource/";
    private static CompilationUnit compilationUnit;

    public RobeHtmlCrudGUI() {
        setResizable(false);
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        tfProjectPath = new javax.swing.JTextField();
        tfProjectOutputPath = new javax.swing.JTextField();
        tfProjectOutputPath.setText("None");
        btnProjectPath = new JButton();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new JTable();
        btnGenerate = new JButton();
        progressBar = new javax.swing.JProgressBar();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Select Project:");

        tfProjectPath.setText("None");

        btnProjectPath.setText("Select");
        btnProjectPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnProjectPathActionPerformed(evt);
            }
        });

        jLabel2.setText("Entities:");

        jLabel3.setForeground(new java.awt.Color(255, 0, 51));
        jLabel3.setText("(Please configure)");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        //{"Empty", false, false, false, false},
                },
                new String[]{
                        "Entitiy", "Create"
                }
        ) {
            Class[] types = new Class[]{
                    String.class, Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        JLabel lblSelectOutput = new JLabel();
        lblSelectOutput.setText("Select Output:");

        JButton btnOutputPath = new JButton();
        btnOutputPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                btnProjectOutputPathActionPerformed(evt);

            }
        });
        btnOutputPath.setText("Select");


        txtProjectName = new javax.swing.JTextField();
        txtProjectName.setColumns(10);

        JLabel lblPackage = new JLabel();
        lblPackage.setText("Project Name:");
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(11)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(19)
                                                .addComponent(tfProjectPath, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18)
                                                .addComponent(btnProjectPath, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(lblSelectOutput, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                                .addGap(11)
                                                .addComponent(tfProjectOutputPath, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18)
                                                .addComponent(btnOutputPath, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(lblPackage, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                                .addGap(11)
                                                .addComponent(txtProjectName, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(50)
                                                .addComponent(jLabel3))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 629, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 525, GroupLayout.PREFERRED_SIZE))
                                                .addComponent(btnGenerate))))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(11)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(4)
                                                .addComponent(jLabel1))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(1)
                                                .addComponent(tfProjectPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(btnProjectPath))
                                .addGap(6)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(4)
                                                .addComponent(lblSelectOutput))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(1)
                                                .addComponent(tfProjectOutputPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(btnOutputPath))
                                .addGap(6)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(3)
                                                .addComponent(lblPackage))
                                        .addComponent(txtProjectName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(23)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3))
                                .addGap(14)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(240)
                                                .addComponent(btnGenerate))))
        );
        getContentPane().setLayout(groupLayout);

        pack();
    }// </editor-fold>

    private void btnProjectPathActionPerformed(ActionEvent evt) {

        JFileChooser dialog = new JFileChooser(JAVA_IO_TMP_DIR);

        dialog.setMultiSelectionEnabled(false);
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = dialog.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = dialog.getSelectedFile();
            tfProjectPath.setText(file.getAbsolutePath());

            fillEntityList(file.getAbsolutePath());
        }
    }

    private void btnProjectOutputPathActionPerformed(ActionEvent evt) {

        JFileChooser dialog = new JFileChooser(JAVA_IO_TMP_DIR);

        dialog.setMultiSelectionEnabled(false);
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = dialog.showSaveDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = dialog.getSelectedFile();
            tfProjectOutputPath.setText(file.getAbsolutePath());
            OUTPUT_PATH = file.getAbsolutePath();
        }
    }

    private void btnGenerateActionPerformed(ActionEvent evt) {


        Object[][] tableData = getTableData(jTable1);
        progressBar.setIndeterminate(true);
        String js = OUTPUT_PATH + File.separator + JS + File.separator + txtProjectName.getText() + File.separator;
        new File(OUTPUT_PATH + File.separator + JS).mkdir();
        String html = OUTPUT_PATH + File.separator + HTML + File.separator;
        new File(OUTPUT_PATH + File.separator + JS + File.separator + txtProjectName.getText()).mkdir();
        new File(js + MODEL).mkdir();
        new File(js + DATA).mkdir();
        new File(js + VIEW).mkdir();
        new File(html).mkdir();

        String dataSources = VAR;
        String model = VAR;

        int l = 0;
        for (Object[] data : tableData) {
            String entity = (String) data[0];
            Boolean create = (Boolean) data[1];
            if (create) {
                dataSources += entity + DATASOURCE + ",";
                model += entity + MODEL + ",";
                l++;
            }

        }
        if (dataSources.endsWith(",")) {
            dataSources = dataSources.substring(0, dataSources.length() - 1);
        }
        dataSources += ";";


        if (model.endsWith(",")) {
            model = model.substring(0, model.length() - 1);
        }
        model += ";";
        int i = 0;
        for (Object[] data : tableData) {
            String entity = (String) data[0];
            Boolean create = (Boolean) data[1];
            Configuration cfg = new Configuration();
            if (create) {
                try {

                    List<Model> models = ClassVisitor.models.get(entity);
                    //Models
                    Template modelTemplate = cfg.getTemplate(TEMPLATE_PATH + MODEL_FTL);

                    Map<String, Object> datamodel = new HashMap<String, Object>();
                    datamodel.put("modelName", entity + MODEL);
                    datamodel.put("fields", models);


                    String modelLocation = js + MODEL + File.separator + MODEL_JS;
                    Writer modelFile = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(modelLocation, true), "UTF-8"));

                    if (i == 0) {
                        modelFile.write(model);
                        modelFile.write("\r\n");
                        modelFile.write("\r\n");
                        modelFile.write("define([\n" +
                                "    'kendo/kendo.data.min','robe/Validations'], function(){\n" +
                                "    console.log(\"Loading : Models\");");

                        modelFile.write("\r\n");
                        modelFile.write("\r\n");
                    }

                    modelTemplate.process(datamodel, modelFile);

                    if (i == l - 1) {
                        modelFile.write(" console.log(\"Finished : Models\");\n" +
                                "});");
                    }
                    modelFile.flush();
                    modelFile.close();


                    //DataSources created

                    Template templateDataSource = cfg.getTemplate(TEMPLATE_PATH + DATASOURCE_FTL);

                    Map<String, Object> dataDataSource = new HashMap<String, Object>();
                    dataDataSource.put("dataSourceName", entity + DATASOURCE);
                    dataDataSource.put("entity", entity);
                    dataDataSource.put("modelName", entity + MODEL);

                    String dataSourceLocation = js + DATA + File.separator + DATASOURCE_JS;
                    Writer dataSourceFile = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(dataSourceLocation, true), "UTF-8"));
                    if (i == 0) {
                        dataSourceFile.write(dataSources);
                        dataSourceFile.write("\r\n");
                        dataSourceFile.write("\r\n");
                        dataSourceFile.write("define([\n" +
                                "    'admin/data/SingletonDataSource', 'admin/Models'], function (S, HDS) {\n" +
                                "    console.log(\"Loading : Datasources\");");
                        dataSourceFile.write("\r\n");
                        dataSourceFile.write("\r\n");
                    }
                    templateDataSource.process(dataDataSource, dataSourceFile);
                    if (i == l - 1) {
                        dataSourceFile.write(" console.log(\"Finished : Datasources\");\n" +
                                "});");
                    }
                    dataSourceFile.flush();
                    dataSourceFile.close();


                    //View created

                    Template templateView = cfg.getTemplate(TEMPLATE_PATH + VIEW_FTL);
                    Map<String, Object> dataView = new HashMap<String, Object>();
                    dataView.put("view", entity + MANAGEMENT);
                    dataView.put("fields", models);
                    dataView.put("projectName", txtProjectName.getText());
                    dataView.put("dataSource", entity + DATASOURCE);


                    String viewLocation = js + VIEW + File.separator + entity + MANAGEMENT_JS;
                    Writer fileView = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(viewLocation, false), "UTF-8"));
                    templateView.process(dataView, fileView);
                    fileView.flush();
                    fileView.close();


                    //html created

                    Template templateHtml = cfg.getTemplate(TEMPLATE_PATH + HTML_FTL);

                    Map<String, Object> dataHtml = new HashMap<String, Object>();
                    dataHtml.put("view", entity);

                    String htmlLocation = html + entity + MANAGEMENT_HTML;
                    Writer fileHtml = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(htmlLocation, false), "UTF-8"));
                    templateHtml.process(dataHtml, fileHtml);
                    fileHtml.flush();
                    fileHtml.close();
                    i++;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            }
        }
        progressBar.setIndeterminate(false);
        javax.swing.JOptionPane.showMessageDialog(this, "created successfull!");

    }

    public Object[][] getTableData(JTable table) {
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) table.getModel();
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
        Object[][] tableData = new Object[nRow][nCol];
        for (int i = 0; i < nRow; i++)
            for (int j = 0; j < nCol; j++)
                tableData[i][j] = dtm.getValueAt(i, j);
        return tableData;
    }

    private void fillEntityList(String absolutePath) {

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*.java"});
        scanner.setBasedir(absolutePath);
        scanner.setCaseSensitive(false);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();

        ClassVisitor.classes.clear();
        for (String string : files) {
            try {
                compilationUnit = JavaParser.parse(new File(absolutePath + File.separator + string));
            } catch (ParseException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            new ClassVisitor().visit(compilationUnit, null);

        }

        Object[][] list = null;
        list = new Object[ClassVisitor.classes.size()][5];


        int i = 0;
        for (String entityClass : ClassVisitor.classes) {
            list[i][0] = entityClass;
            list[i++][1] = true;
        }

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                list,
                new String[]{
                        "Entitiy", "Create"
                }
        ) {
            Class[] types = new Class[]{
                    String.class, Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
    }

    private static class ClassVisitor extends VoidVisitorAdapter {

        public static List<String> classes = new ArrayList<String>();
        public static Map<String, List<Model>> models = new HashMap<String, List<Model>>();
        private static final String ENTITY = "@Entity";
        private static String COLUMN = "Column";

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {

            List<AnnotationExpr> list = n.getAnnotations();

            if (list != null) {
                for (AnnotationExpr annotationExpr : list) {
                    if (annotationExpr.toString().equals(ENTITY)) {

                        List<BodyDeclaration> body = n.getMembers();

                        List<Model> model = new ArrayList<Model>();
                        for (BodyDeclaration bodyDeclaration : body) {

                            if (bodyDeclaration instanceof FieldDeclaration) {
                                FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
                                List<AnnotationExpr> fieldExp = fieldDeclaration.getAnnotations();

                                if (fieldExp != null) {

                                    VariableDeclarator variableDeclarator = fieldDeclaration.getVariables().get(0);

                                    for (AnnotationExpr expr : fieldExp) {
                                        if (expr.getName().toString().equals(COLUMN)) {

                                            List<Node> nodes = expr.getChildrenNodes();
                                            Model m = new Model();
                                            m.setName(variableDeclarator.getId().toString());
                                            m.setDefinition(variableDeclarator.getId().toString());
                                            m.setNullable(true);
                                            m.setLength("255");
                                            m.setType("string");
                                            String fieldType = fieldDeclaration.getType().toString().toLowerCase();

                                            if (fieldType.equals("boolean")) {
                                                m.setType("boolean");
                                            } else if (fieldType.equals("integer") && fieldType.equals("int") && fieldType.equals("bigdecimal") && fieldType.equals("double") && fieldType.equals("long")) {
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
                                                    } else if (memberValuePair.getName().equals("columnDefinition")) {
                                                        if (!memberValuePair.getValue().toString().equals("")) {
                                                            m.setDefinition(memberValuePair.getValue().toString().replaceAll("\"", ""));
                                                        }
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

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RobeHtmlCrudGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RobeHtmlCrudGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RobeHtmlCrudGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RobeHtmlCrudGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RobeHtmlCrudGUI().setVisible(true);
            }
        });
    }


    private JButton btnProjectPath;
    private JButton btnGenerate;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private JTable jTable1;
    private javax.swing.JTextField tfProjectPath;
    private javax.swing.JTextField tfProjectOutputPath;
    private javax.swing.JTextField txtProjectName;
    private javax.swing.JProgressBar progressBar;
}
