import java.util.*;
import java.sql.*;
//GUI Componenets
import javax.swing.*;
import javax.swing.table.*;

public class ProjectAddDialog extends javax.swing.JDialog {
    private JDBCConnect dbConnection;
    private String value;
    private String project, projectField;
    
    public ProjectAddDialog(JFrame frame, String project, String projectField, JDBCConnect connection) {
        super(frame, true);
        this.project = project;
        this.projectField = projectField;
        try {
            dbConnection = (JDBCConnect)connection.clone();
            dbConnection.setDatabase(dbConnection.getProjectDatabase());
            dbConnection.setTable(dbConnection.getProjectTable());
            dbConnection.getTableMap().setConnection(dbConnection);
            dbConnection.getTableMap().clearFieldMaps();
        } catch (java.sql.SQLException e) {
            System.err.println("Couldn't initialize table mapping");
        } catch (CloneNotSupportedException e) {
            System.err.println("Couldn't make a copy of the connection: "+e);
        }
        initComponents();
        setLocationRelativeTo(frame);
        
        //Set the value for the project name field
        for(int i=0; i<fieldTable.getRowCount(); i++) {
            if(fieldTable.getValueAt(i, 0).equals(projectField)) {
                fieldTable.setValueAt(project, i, 2);
            }
        }
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

        titleLabel.setForeground(java.awt.Color.black);
        titleLabel.setText("Add new project "+project+":");
        getContentPane().add(titleLabel, java.awt.BorderLayout.NORTH);

        fieldScrollPane.setPreferredSize(new java.awt.Dimension(387, 254));
        fieldTable.setModel(dbConnection.getTableMap().toFieldValuesTableModel());
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
      Object[] statement = new Object[dbConnection.getTableMap().getFieldMaps().size()];
      
      try {
          for(int i=0; i < dbConnection.getTableMap().getFieldMaps().size(); i++)
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
    private javax.swing.JPanel fieldPanel;
    private javax.swing.JTable fieldTable;
    private javax.swing.JButton okButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane fieldScrollPane;
    // End of variables declaration//GEN-END:variables
    
}
