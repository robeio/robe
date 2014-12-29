package io.robe.crud.gui;

import io.robe.crud.DaoCrud;
import io.robe.crud.ResourceCrud;
import io.robe.crud.helper.ClassVisitor;
import io.robe.crud.helper.Constants;
import io.robe.crud.helper.CrudUtility;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.expr.NameExpr;
import org.apache.tools.ant.DirectoryScanner;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RobeCrudGUI extends javax.swing.JFrame {
    public static final String JAVA_IO_TMP_DIR = "user.home";
    public static String OUTPUT_PATH;
    public static CompilationUnit compilationUnit;
    private javax.swing.JButton btnProjectPath;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tfProjectPath;
    private javax.swing.JTextField tfProjectOutputPath;
    private javax.swing.JTextField txtPackageName;
    private javax.swing.JProgressBar progressBar;

    public RobeCrudGUI() {
        setResizable(false);
        initComponents();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RobeCrudGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RobeCrudGUI().setVisible(true);
            }
        });
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfProjectPath = new javax.swing.JTextField();
        tfProjectOutputPath = new javax.swing.JTextField();
        tfProjectOutputPath.setText("None");
        btnProjectPath = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnGenerate = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Select Project:");

        tfProjectPath.setText("None");

        btnProjectPath.setText("Select");
        btnProjectPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProjectPathActionPerformed();
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
                        "Entitiy", "Dao", "Resource", "Inject", "Auth"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed();
            }
        });

        JLabel lblSelectOutput = new JLabel();
        lblSelectOutput.setText("Select Output:");

        JButton btnOutputPath = new JButton();
        btnOutputPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                btnProjectOutputPathActionPerformed();

            }
        });
        btnOutputPath.setText("Select");


        txtPackageName = new javax.swing.JTextField();
        txtPackageName.setColumns(10);

        JLabel lblPackage = new JLabel();
        lblPackage.setText("Package:");
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
                                                .addComponent(txtPackageName, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE))
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
                                        .addComponent(txtPackageName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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

    private void btnProjectPathActionPerformed() {

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

    protected void btnProjectOutputPathActionPerformed() {

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

    protected void btnGenerateActionPerformed() {


        Object[][] tableData = getTableData(jTable1);
        progressBar.setIndeterminate(true);

        for (Object[] data : tableData) {
            String entity = (String) data[0];
            Boolean dao = (Boolean) data[1];
            Boolean resource = (Boolean) data[2];
            Boolean inject = (Boolean) data[3];
            Boolean auth = (Boolean) data[4];

            String fileDaoLocation = OUTPUT_PATH + File.separator + txtPackageName.getText().replace(".", File.separator) + File.separator + "dao";
            new File(fileDaoLocation).mkdirs();

            String fileResourceLocation = OUTPUT_PATH + File.separator + txtPackageName.getText().replace(".", File.separator) + File.separator + "resource";
            new File(fileResourceLocation).mkdirs();

            try {

                String daoName = entity + "Dao";
                String newDaoClassName = fileDaoLocation + File.separator + daoName + ".java";

                List<String> fieldGet = ClassVisitor.allColumns.get(entity);
                List<String> uniqueFields = ClassVisitor.uniqueColumns.get(entity);

                String findBy = "findById";
                if (uniqueFields != null) {
                    for (String string : uniqueFields) {
                        findBy += "Or" + CrudUtility.capitalizeToUpper(string);
                    }
                }
                String importEntity = ClassVisitor.imports.get(entity);
                String packageName = txtPackageName.getText();

                if (dao) {
                    List<ImportDeclaration> daoImports = new ArrayList<ImportDeclaration>();
                    daoImports.addAll(Constants.daoImports);
                    daoImports.add(new ImportDeclaration(new NameExpr(importEntity + "." + entity), false, false));
                    File fileDao = new File(newDaoClassName);
                    if (!fileDao.exists()) {
                        fileDao.createNewFile();
                    }

                    DaoCrud.setEntityName(entity);
                    DaoCrud.setUniqueFields(uniqueFields);
                    DaoCrud.setFindBy(findBy);

                    FileWriter fwDao = new FileWriter(fileDao.getAbsoluteFile());
                    BufferedWriter bwDao = new BufferedWriter(fwDao);
                    bwDao.write(DaoCrud.createDao(packageName + ".dao", daoImports));
                    bwDao.close();

                }
                if (resource) {

                    ResourceCrud.setUniqueFields(uniqueFields);
                    ResourceCrud.setFields(fieldGet);
                    ResourceCrud.setDaoName(daoName);
                    ResourceCrud.setAuth(auth);
                    ResourceCrud.setEntityName(entity);
                    ResourceCrud.setIdGetFunction(findBy);

                    String newResourceClassName = fileResourceLocation + File.separator + entity + "Resource.java";
                    File fileResource = new File(newResourceClassName);
                    if (!fileResource.exists()) {
                        fileResource.createNewFile();
                    }

                    FileWriter fwResource = new FileWriter(fileResource.getAbsoluteFile());
                    BufferedWriter bwResource = new BufferedWriter(fwResource);
                    List<ImportDeclaration> resourceImports = new ArrayList<ImportDeclaration>();
                    resourceImports.addAll(Constants.resourceImports);
                    resourceImports.add(new ImportDeclaration(new NameExpr(packageName + ".dao." + entity + "Dao"), false, false));
                    resourceImports.add(new ImportDeclaration(new NameExpr(importEntity + "." + entity), false, false));
                    bwResource.write(ResourceCrud.resourceGenerate(resourceImports, packageName + ".resource", inject));
                    bwResource.close();
                }

            } catch (IOException e) {
                javax.swing.JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
        progressBar.setIndeterminate(false);
        javax.swing.JOptionPane.showMessageDialog(this, "created successful!");

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
            } catch (ParseException | IOException e) {

                e.printStackTrace();
            }
            new ClassVisitor().visit(compilationUnit, null);

        }

        Object[][] list = new Object[ClassVisitor.classes.size()][5];


        int i = 0;
        for (String entityClass : ClassVisitor.classes) {
            list[i][0] = entityClass;
            list[i][1] = true;
            list[i][2] = true;
            list[i][3] = true;
            list[i++][4] = true;
        }

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                list,
                new String[]{
                        "Entitiy", "Dao", "Resource", "Inject", "Auth"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
    }
}
