import java.util.*;
import java.sql.*;
//GUI Componenets
import javax.swing.*;
import javax.swing.table.*;

public class ProjectAddDialog extends javax.swing.JDialog {
  private JDBCConnect dbConnection;
  private String value;
  private String project, projectField;
  
  public ProjectAddDialog(JFrame frame, String project, String projectField) {
    super(frame, true);
    this.project = project;
    this.projectField = projectField;
    dbConnection = new JDBCConnect();
    dbConnection.setParentFrame(frame);
    dbConnection.database = dbConnection.projectDatabase;
    dbConnection.table = dbConnection.projectTable;
    try {
      dbConnection.tableMap.init();
    } catch (java.sql.SQLException e) {
      System.err.println("Couldn't initialize table mapping");
    }
    initComponents();
    setLocationRelativeTo(frame);
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    titleLabel = new javax.swing.JLabel();
    fieldScrollPane = new javax.swing.JScrollPane();
    fieldTable = new javax.swing.JTable();
    fieldPanel = new javax.swing.JPanel();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog(evt);
      }
    });
    
    titleLabel.setText("Add new project "+project+":");
    titleLabel.setForeground(java.awt.Color.black);
    getContentPane().add(titleLabel, java.awt.BorderLayout.NORTH);
    
    fieldTable.setModel(dbConnection.tableMap.toTableModel());
    fieldScrollPane.setViewportView(fieldTable);
    
    getContentPane().add(fieldScrollPane, java.awt.BorderLayout.CENTER);
    
    okButton.setText("OK");
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addProject(evt);
      }
    });
    
    fieldPanel.add(okButton);
    
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancel(evt);
      }
    });
    
    fieldPanel.add(cancelButton);
    
    getContentPane().add(fieldPanel, java.awt.BorderLayout.SOUTH);
    
    pack();
  }//GEN-END:initComponents

  private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
    value = "1";
    closeDialog();
  }//GEN-LAST:event_cancel

  private void addProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProject
    Vector statements = new Vector(1); //We'll only need one row
    Object[] statement = new Object[dbConnection.tableMap.size()];

    try {
     for(int i=0; i < dbConnection.tableMap.size(); i++)
        statement[i] = (String)fieldTable.getValueAt(i, 2);    
      statements.addElement(statement);
      dbConnection.insertVector(statements);
    } catch (SQLException e) {
      System.err.println("Couldn't create new project: "+e);
    }
    
    value = "0";
    closeDialog();
  }//GEN-LAST:event_addProject
    
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
      closeDialog();
    }//GEN-LAST:event_closeDialog

    private void closeDialog() {
      setVisible(false);
      dispose();
    }

    public String getValue() {
      return value;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel titleLabel;
    private javax.swing.JScrollPane fieldScrollPane;
    private javax.swing.JTable fieldTable;
    private javax.swing.JPanel fieldPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton cancelButton;
    // End of variables declaration//GEN-END:variables
    
}
