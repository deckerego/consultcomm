//Swing Components
import javax.swing.*;
import javax.swing.event.*;

/*
 * ProjectEditDialog.java
 * Created on January 8, 2002, 1:30 PM
 * @author John T. Ellis
 */
public class ProjectEditDialog extends javax.swing.JDialog {
  final TimeRecord record;
  private String value;
  
  public ProjectEditDialog(JFrame frame, TimeRecord timerec) {
    super(frame, true);
    record = timerec;
    initComponents();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        inputPanel = new javax.swing.JPanel();
        groupLabel = new javax.swing.JLabel();
        groupField = new javax.swing.JTextField();
        projLabel = new javax.swing.JLabel();
        projField = new javax.swing.JTextField();
        timeLabel = new javax.swing.JLabel();
        timeField = new javax.swing.JTextField();
        billable = new javax.swing.JCheckBox();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Edit Project");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        inputPanel.setLayout(new java.awt.GridBagLayout());

        groupLabel.setText("Group:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        inputPanel.add(groupLabel, gridBagConstraints);

        groupField.setText(record.getGroupName());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        inputPanel.add(groupField, gridBagConstraints);

        projLabel.setText("Project: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        inputPanel.add(projLabel, gridBagConstraints);

        projField.setColumns(15);
        projField.setText(record.getProjectName());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        inputPanel.add(projField, gridBagConstraints);

        timeLabel.setText("Time: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        inputPanel.add(timeLabel, gridBagConstraints);

        timeField.setColumns(10);
        timeField.setText(record.toMinuteString());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        inputPanel.add(timeField, gridBagConstraints);

        billable.setForeground(new java.awt.Color(102, 102, 153));
        billable.setSelected(record.isBillable());
        billable.setText("Billable Project");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        inputPanel.add(billable, gridBagConstraints);

        getContentPane().add(inputPanel, java.awt.BorderLayout.CENTER);

        getRootPane().setDefaultButton(okButton);
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save(evt);
            }
        });

        buttonPanel.add(okButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });

        buttonPanel.add(cancelButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents
  
  private void save(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save
    record.setGroupName(groupField.getText());
    record.setProjectName(projField.getText());
    record.setSeconds(timeField.getText());
    record.setBillable(billable.isSelected());
    value = "0";
    closeDialog();
  }//GEN-LAST:event_save
  
  private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
    value = "1";
    closeDialog();
  }//GEN-LAST:event_cancel
  
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
    private javax.swing.JTextField groupField;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField timeField;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JCheckBox billable;
    private javax.swing.JTextField projField;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel projLabel;
    private javax.swing.JButton cancelButton;
    // End of variables declaration//GEN-END:variables
    
}
