import java.util.*;
import java.io.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.table.*;

public class JDBCConnectCustomizer extends javax.swing.JPanel implements java.beans.Customizer {
    private JDBCConnect dbConnection;
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tabbedPane = new javax.swing.JTabbedPane();
        driverPanel = new javax.swing.JPanel();
        driverInputPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        urlLabel = new javax.swing.JLabel();
        urlField = new javax.swing.JTextField();
        dbLabel = new javax.swing.JLabel();
        dbField = new javax.swing.JTextField();
        tableLabel = new javax.swing.JLabel();
        tableField = new javax.swing.JTextField();
        odbcCheckBox = new javax.swing.JCheckBox();
        driverButtonPanel = new javax.swing.JPanel();
        driverOK = new javax.swing.JButton();
        driverTest = new javax.swing.JButton();
        fieldPanel = new javax.swing.JPanel();
        fieldScrollPane = new javax.swing.JScrollPane();
        fieldMapping = new javax.swing.JTable();
        fieldButtonPanel = new javax.swing.JPanel();
        fieldOK = new javax.swing.JButton();
        fieldRefresh = new javax.swing.JButton();
        mapPanel = new javax.swing.JPanel();
        mapScrollPane = new javax.swing.JScrollPane();
        projectMapping = new javax.swing.JTable();
        mapButtonPanel = new javax.swing.JPanel();
        mapOK = new javax.swing.JButton();
        optionPanel = new javax.swing.JPanel();
        optionInputPanel = new javax.swing.JPanel();
        hourLabel = new javax.swing.JLabel();
        hourComboBox = new javax.swing.JComboBox();
        projectCaseCheckBox = new javax.swing.JCheckBox();
        projValidateCheckBox = new javax.swing.JCheckBox();
        projDBLabel = new javax.swing.JLabel();
        projDBField = new javax.swing.JTextField();
        projTableLabel = new javax.swing.JLabel();
        projTableField = new javax.swing.JTextField();
        projFieldLabel = new javax.swing.JLabel();
        projFieldComboBox = new javax.swing.JComboBox();
        optionButtonPanel = new javax.swing.JPanel();
        optionOK = new javax.swing.JButton();
        optionApply = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        tabbedPane.setPreferredSize(new java.awt.Dimension(387, 254));
        driverPanel.setLayout(new java.awt.BorderLayout());

        driverPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showDriverPanel(evt);
            }
        });

        driverInputPanel.setLayout(new java.awt.GridBagLayout());

        nameLabel.setText("Driver Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(nameLabel, gridBagConstraints);

        nameField.setColumns(20);
        nameField.setText(dbConnection.getName());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(nameField, gridBagConstraints);

        urlLabel.setText("URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(urlLabel, gridBagConstraints);

        urlField.setText(dbConnection.getUrl());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(urlField, gridBagConstraints);

        dbLabel.setText("Database");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(dbLabel, gridBagConstraints);

        dbField.setText(dbConnection.getDatabase());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(dbField, gridBagConstraints);

        tableLabel.setText("Table");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(tableLabel, gridBagConstraints);

        tableField.setText(dbConnection.getTable());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(tableField, gridBagConstraints);

        odbcCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        odbcCheckBox.setSelected(dbConnection.getName().equals(JDBCConnect.ODBCDRIVERNAME));
        odbcCheckBox.setText("Use ODBC Bridge");
        odbcCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleODBC(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        driverInputPanel.add(odbcCheckBox, gridBagConstraints);

        driverPanel.add(driverInputPanel, java.awt.BorderLayout.CENTER);

        driverOK.setText("Save");
        driverOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDriverSettings(evt);
            }
        });

        driverButtonPanel.add(driverOK);

        driverTest.setText("Test");
        driverTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testDriverSettings(evt);
            }
        });

        driverButtonPanel.add(driverTest);

        driverPanel.add(driverButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("JDBC Driver", null, driverPanel, "Change JDBC Driver Settings");

        fieldPanel.setLayout(new java.awt.BorderLayout());

        fieldPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showFieldMap(evt);
            }
        });

        fieldMapping.setModel(dbConnection.getTableMap().toFieldValuesTableModel());
        fieldScrollPane.setViewportView(fieldMapping);

        fieldPanel.add(fieldScrollPane, java.awt.BorderLayout.CENTER);

        fieldOK.setText("Save");
        fieldOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDriverSettings(evt);
            }
        });

        fieldButtonPanel.add(fieldOK);

        fieldRefresh.setText("Refresh");
        fieldRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshFieldMap(evt);
            }
        });

        fieldButtonPanel.add(fieldRefresh);

        fieldPanel.add(fieldButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("Field Values", null, fieldPanel, "Define values for table fields");

        mapPanel.setLayout(new java.awt.BorderLayout());

        mapPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showProjectNames(evt);
            }
        });

        projectMapping.setModel(dbConnection.getTableMap().toProjectNamesTableModel());
        mapScrollPane.setViewportView(projectMapping);

        mapPanel.add(mapScrollPane, java.awt.BorderLayout.CENTER);

        mapOK.setText("Save");
        mapOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDriverSettings(evt);
            }
        });

        mapButtonPanel.add(mapOK);

        mapPanel.add(mapButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("Project Names", mapPanel);

        optionPanel.setLayout(new java.awt.BorderLayout());

        optionPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showOptionPanel(evt);
            }
        });

        optionInputPanel.setLayout(new java.awt.GridBagLayout());

        hourLabel.setText("Export hours by:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(hourLabel, gridBagConstraints);

        hourComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Full Precision", "Quarter of an hour", "Tenth of an hour" }));
        hourComboBox.setSelectedIndex(dbConnection.getHourFormat());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(hourComboBox, gridBagConstraints);

        projectCaseCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        projectCaseCheckBox.setSelected(dbConnection.getProjectCase());
        projectCaseCheckBox.setText("Upper-Case Project");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(projectCaseCheckBox, gridBagConstraints);

        projValidateCheckBox.setForeground(new java.awt.Color(102, 102, 153));
        projValidateCheckBox.setSelected(dbConnection.isProjectValidate());
        projValidateCheckBox.setText("Validate Project");
        projValidateCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleValidateProject(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(projValidateCheckBox, gridBagConstraints);

        projDBLabel.setText("Project Database:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(projDBLabel, gridBagConstraints);

        projDBField.setText(dbConnection.getProjectDatabase());
        projDBField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(projDBField, gridBagConstraints);

        projTableLabel.setText("Project Table:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(projTableLabel, gridBagConstraints);

        projTableField.setText(dbConnection.getProjectTable());
        projTableField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(projTableField, gridBagConstraints);

        projFieldLabel.setText("Project Field");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(projFieldLabel, gridBagConstraints);

        projFieldComboBox.setModel(new DefaultComboBoxModel(new String[] {dbConnection.getProjectField()}));
        projFieldComboBox.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(projFieldComboBox, gridBagConstraints);

        optionPanel.add(optionInputPanel, java.awt.BorderLayout.CENTER);

        optionOK.setText("Save");
        optionOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDriverSettings(evt);
            }
        });

        optionButtonPanel.add(optionOK);

        optionApply.setText("Refresh");
        optionApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyOptions(evt);
            }
        });

        optionButtonPanel.add(optionApply);

        optionPanel.add(optionButtonPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("Options", null, optionPanel, "Set variable options");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tabbedPane, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void showProjectNames(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showProjectNames
        getRootPane().setDefaultButton(mapOK);
        projectMapping.setModel(dbConnection.getTableMap().toProjectNamesTableModel());
        projectMapping.repaint();
    }//GEN-LAST:event_showProjectNames
    
  private void applyOptions(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyOptions
      dbConnection.setProjectDatabase(projDBField.getText());
      dbConnection.setProjectTable(projTableField.getText());
      int projFieldIndex = projFieldComboBox.getSelectedIndex();
      dbConnection.setProjectField((String)projFieldComboBox.getItemAt(projFieldIndex));
      String[] fieldNames = dbConnection.getProjectFieldNames();
      DefaultComboBoxModel boxModel;
      if(fieldNames == null) {
          boxModel = new DefaultComboBoxModel(new String[] {"No Fields Found"});
          projFieldComboBox.setEnabled(false);
      } else {
          boxModel = new DefaultComboBoxModel(fieldNames);
          projFieldComboBox.setEnabled(true);
      }
      projFieldComboBox.setModel(boxModel);
      optionInputPanel.repaint();
  }//GEN-LAST:event_applyOptions
  
  private void toggleValidateProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleValidateProject
      toggleValidateProject();
  }//GEN-LAST:event_toggleValidateProject
  
  private void toggleValidateProject() {
      if(projValidateCheckBox.isSelected()) {
          projDBField.setEnabled(true);
          projTableField.setEnabled(true);
      } else {
          projDBField.setEnabled(false);
          projTableField.setEnabled(false);
      }
      optionInputPanel.repaint();
  }
  
  private void showDriverPanel(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showDriverPanel
      getRootPane().setDefaultButton(driverOK);
  }//GEN-LAST:event_showDriverPanel
  
  private void showOptionPanel(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showOptionPanel
      getRootPane().setDefaultButton(optionOK);
  }//GEN-LAST:event_showOptionPanel
  
  private void toggleODBC(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleODBC
      toggleODBC();
  }//GEN-LAST:event_toggleODBC
  private void toggleODBC() {
      if(odbcCheckBox.isSelected()) {
          nameField.setText(JDBCConnect.ODBCDRIVERNAME);
          nameField.setEnabled(false);
          urlLabel.setText("Data Source");
          int lastColon = dbConnection.getUrl().lastIndexOf(':')+1;
          urlField.setText(dbConnection.getUrl().substring(lastColon));
      } else {
          nameField.setText(dbConnection.getName());
          nameField.setEnabled(true);
          urlLabel.setText("URL");
          urlField.setText(dbConnection.getUrl());
      }
      driverInputPanel.repaint();
  }
  private void refreshFieldMap(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshFieldMap
      try {
          dbConnection.getTableMap().clearFieldMaps();
          fieldMapping.setModel(dbConnection.getTableMap().toFieldValuesTableModel());
          fieldMapping.repaint();
      } catch (java.sql.SQLException e) {
          System.err.println("Couldn't initialize table mapping");
      }
  }//GEN-LAST:event_refreshFieldMap
  
  private void showFieldMap(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showFieldMap
      getRootPane().setDefaultButton(fieldOK);
      if(dbConnection.getTableMap().getFieldMaps().size() == 0) {
          try {
              dbConnection.getTableMap().clearFieldMaps();
              fieldMapping.setModel(dbConnection.getTableMap().toFieldValuesTableModel());
              fieldMapping.repaint();
          } catch (java.sql.SQLException e) {
              System.err.println("Couldn't initialize table mapping");
          }
      }
  }//GEN-LAST:event_showFieldMap
  
  private void testDriverSettings(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testDriverSettings
      String name = nameField.getText();
      String url = urlField.getText();
      String database = dbField.getText();
      String table = tableField.getText();
      JDBCConnect testConnection = new JDBCConnect(name, url, database, table);
      testConnection.testDriverSettings();
  }//GEN-LAST:event_testDriverSettings
  
  private void saveDriverSettings(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDriverSettings
      //Give the bean all its setter values
      dbConnection.setName(nameField.getText());
      dbConnection.setUrl(urlField.getText());
      if(dbConnection.getName().equals(JDBCConnect.ODBCDRIVERNAME))
          dbConnection.setUrl("jdbc:odbc:"+dbConnection.getUrl());
      dbConnection.setDatabase(dbField.getText());
      dbConnection.setTable(tableField.getText());
      dbConnection.setProjectDatabase(projDBField.getText());
      dbConnection.setProjectTable(projTableField.getText());
      if(projFieldComboBox.getItemCount() != 0) {
          int projFieldIndex = projFieldComboBox.getSelectedIndex();
          dbConnection.setProjectField((String)projFieldComboBox.getItemAt(projFieldIndex));
      }
      dbConnection.setProjectValidate(projValidateCheckBox.isSelected());
      dbConnection.setHourFormat(hourComboBox.getSelectedIndex());
      dbConnection.setProjectCase(projectCaseCheckBox.isSelected());
      
      //Now populate collections
      TableMap tableMap = dbConnection.getTableMap();
      Vector fieldMaps = tableMap.getFieldMaps();
      Hashtable projectMaps = new Hashtable();
      
      for(int i=0; i < fieldMaps.size(); i++) {
          String value = (String)fieldMapping.getValueAt(i, 2);
          FieldMap record = (FieldMap)fieldMaps.elementAt(i);
          record.setValueExpression(value);
      } //Associate all existing fields with values from the JTable
      tableMap.setFieldMaps(fieldMaps);
      
      for(int i=0; i < projectMapping.getRowCount(); i++) {
          String projectName = (String)projectMapping.getValueAt(i, 1);
          boolean export = ((Boolean)projectMapping.getValueAt(i, 0)).booleanValue();
          String alias = (String)projectMapping.getValueAt(i, 2);
          projectMaps.put(projectName, new ProjectMap(alias, export));
      } //Add all the project maps listed in the JTable
      tableMap.setProjectMaps(projectMaps);
      
      dbConnection.setTableMap(tableMap);
      
      try { //Serialize the bean out to an XML file in the user's prefs directory
          File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
          File prefsFile = new File(prefsdir, "JDBCConnect.xml");
          Thread.currentThread().setContextClassLoader(dbConnection.getClass().getClassLoader()); //Sun BugID 4676532
          FileOutputStream outStream = new FileOutputStream(prefsFile);
          XMLEncoder e = new XMLEncoder(new BufferedOutputStream(outStream));
          e.writeObject(dbConnection);
          e.close();
      } catch (Exception e) {
          System.err.println("Couldn't save JDBC Prefs");
          e.printStackTrace(System.out);
      }
      
      exitForm();
  }//GEN-LAST:event_saveDriverSettings
            private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                exitForm();
    }//GEN-LAST:event_exitForm
            
            private void exitForm() {
                //Don't do anything - this is an embedded component
            }
            
            public void setObject(Object obj) {
                dbConnection = (JDBCConnect)obj;
                initComponents();
                toggleODBC();
                toggleValidateProject();
            }
            
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel optionButtonPanel;
    private javax.swing.JPanel driverPanel;
    private javax.swing.JButton optionApply;
    private javax.swing.JButton mapOK;
    private javax.swing.JPanel mapButtonPanel;
    private javax.swing.JTable projectMapping;
    private javax.swing.JTable fieldMapping;
    private javax.swing.JLabel hourLabel;
    private javax.swing.JPanel optionInputPanel;
    private javax.swing.JScrollPane fieldScrollPane;
    private javax.swing.JTextField nameField;
    private javax.swing.JCheckBox projectCaseCheckBox;
    private javax.swing.JPanel fieldPanel;
    private javax.swing.JPanel driverButtonPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel driverInputPanel;
    private javax.swing.JComboBox hourComboBox;
    private javax.swing.JTextField projDBField;
    private javax.swing.JTextField dbField;
    private javax.swing.JComboBox projFieldComboBox;
    private javax.swing.JButton driverTest;
    private javax.swing.JLabel projDBLabel;
    private javax.swing.JCheckBox odbcCheckBox;
    private javax.swing.JPanel fieldButtonPanel;
    private javax.swing.JLabel projFieldLabel;
    private javax.swing.JCheckBox projValidateCheckBox;
    private javax.swing.JButton fieldOK;
    private javax.swing.JLabel dbLabel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JButton optionOK;
    private javax.swing.JScrollPane mapScrollPane;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JTextField tableField;
    private javax.swing.JTextField urlField;
    private javax.swing.JTextField projTableField;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JLabel tableLabel;
    private javax.swing.JLabel urlLabel;
    private javax.swing.JButton driverOK;
    private javax.swing.JLabel projTableLabel;
    private javax.swing.JButton fieldRefresh;
    // End of variables declaration//GEN-END:variables
}
