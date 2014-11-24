package io.robe.crud.gui;

import io.robe.crud.helper.ClassVisitor;
import io.robe.crud.helper.GenerateJS;
import io.robe.crud.helper.Model;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import org.apache.tools.ant.DirectoryScanner;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class RobeHtmlCrudGUI extends javax.swing.JFrame {

    private static final String JAVA_IO_TMP_DIR = "user.home";

    private static String OUTPUT_PATH;
    private static CompilationUnit compilationUnit;
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

    public RobeHtmlCrudGUI() {
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
            java.util.logging.Logger.getLogger(RobeHtmlCrudGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RobeHtmlCrudGUI().setVisible(true);
            }
        });
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

        GenerateJS generateJS = new GenerateJS();

        for (Object[] data : tableData) {
            String entity = (String) data[0];
            Boolean create = (Boolean) data[1];
            if (create) {
                try {

                    String folderName = entity + "Management";
                    String moduleName = OUTPUT_PATH + File.separator + folderName;
                    new File(moduleName).mkdir();

                    List<Model> fields = ClassVisitor.models.get(entity);

                    //create modelFile
                    Writer modelFile = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(moduleName + File.separator + entity + "Model.js", true), "UTF-8"));

                    String modelContent = generateJS.createModel(entity, fields);
                    modelFile.write(modelContent);

                    modelFile.flush();
                    modelFile.close();


                    //create dataSourceFile file
                    Writer dataSourceFile = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(moduleName + File.separator + entity + "DataSource.js", true), "UTF-8"));

                    String dataSourceContent = generateJS.createDataSource(entity);
                    dataSourceFile.write(dataSourceContent);

                    dataSourceFile.flush();
                    dataSourceFile.close();


                    //create viewFile file
                    Writer viewFile = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(moduleName + File.separator + entity + "Management.js", true), "UTF-8"));

                    String viewContent = generateJS.createView(entity, fields);
                    viewFile.write(viewContent);

                    viewFile.flush();
                    viewFile.close();


                    //create viewFile file
                    Writer htmlFile = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(moduleName + File.separator + entity + "Management.html", true), "UTF-8"));

                    String htmlContent = generateJS.createHtml(entity);
                    htmlFile.write(htmlContent);

                    htmlFile.flush();
                    htmlFile.close();


                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
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
            } catch (ParseException | IOException e) {

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
}
