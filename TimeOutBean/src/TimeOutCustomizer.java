/*
 * TimeOutCustomizer.java
 *
 * Created on December 5, 2002, 10:13 AM
 */

//package TimeOutBean;

/**
 *
 * @author  jellis
 */

import java.beans.*;
import java.io.*;
import javax.swing.*;

/**
 * Customizes the TimeOut bean
 */
public class TimeOutCustomizer extends javax.swing.JPanel implements java.beans.Customizer{
    
    private TimeOut timeOut;
    private static final int defaultTime = 120;
    
    /** Creates new customizer TimeOutCustomizer */
    public TimeOutCustomizer() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
  private void initComponents() {//GEN-BEGIN:initComponents
    java.awt.GridBagConstraints gridBagConstraints;

    selectButtonGroup = new javax.swing.ButtonGroup();
    tabbedPane = new javax.swing.JTabbedPane();
    optionPanel = new javax.swing.JPanel();
    optionInputPanel = new javax.swing.JPanel();
    useCheckBox = new javax.swing.JCheckBox();
    pauseLabel = new javax.swing.JLabel();
    secField = new javax.swing.JTextField();
    secLabel = new javax.swing.JLabel();
    pauseRadioButton = new javax.swing.JRadioButton();
    switchRadioButton = new javax.swing.JRadioButton();
    projectCombo = new javax.swing.JComboBox();
    optionButtonPanel = new javax.swing.JPanel();
    saveButton = new javax.swing.JButton();

    setLayout(new java.awt.BorderLayout());

    setMinimumSize(new java.awt.Dimension(238, 221));
    setPreferredSize(new java.awt.Dimension(387, 254));
    tabbedPane.setMinimumSize(new java.awt.Dimension(238, 221));
    tabbedPane.setPreferredSize(new java.awt.Dimension(387, 254));
    optionPanel.setLayout(new java.awt.BorderLayout());

    optionInputPanel.setLayout(new java.awt.GridBagLayout());

    useCheckBox.setText(" Use plugin?");
    useCheckBox.setToolTipText("<html>\n<body>\n<h3>\nCheck if you want to use the Time Out plugin\n</h3>\n</body>\n</html>");
    useCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        usePluginChanged(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    optionInputPanel.add(useCheckBox, gridBagConstraints);

    pauseLabel.setText("Perform action when idle for ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    optionInputPanel.add(pauseLabel, gridBagConstraints);

    secField.setColumns(5);
    secField.setMaximumSize(null);
    secField.setMinimumSize(new java.awt.Dimension(59, 19));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    optionInputPanel.add(secField, gridBagConstraints);

    secLabel.setText(" seconds");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    optionInputPanel.add(secLabel, gridBagConstraints);

    pauseRadioButton.setSelected(true);
    pauseRadioButton.setText("Pause after idle");
    pauseRadioButton.setToolTipText("<html>\n<body>\n<h3>Pauses timer if the system has been idle for the entered number of seconds.</h3>\n</body>\n<html>");
    selectButtonGroup.add(pauseRadioButton);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    optionInputPanel.add(pauseRadioButton, gridBagConstraints);

    switchRadioButton.setText("Switch to project     ");
    switchRadioButton.setToolTipText("<html>\n<body>\n<h3>\nSwitches to selected project if the system has been idle for the given number of seconds\n</h3>\n</body>\n</html>\n");
    selectButtonGroup.add(switchRadioButton);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    optionInputPanel.add(switchRadioButton, gridBagConstraints);

    projectCombo.setMaximumRowCount(50);
    projectCombo.setToolTipText("<html>\n<body>\n<h3>Select project to switch to</h3>\n</body>\n</html>");
    projectCombo.setMaximumSize(null);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    optionInputPanel.add(projectCombo, gridBagConstraints);

    optionPanel.add(optionInputPanel, java.awt.BorderLayout.CENTER);

    saveButton.setText("Save");
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveButtonActionPerformed(evt);
      }
    });

    optionButtonPanel.add(saveButton);

    optionPanel.add(optionButtonPanel, java.awt.BorderLayout.SOUTH);

    tabbedPane.addTab("Options", optionPanel);

    add(tabbedPane, java.awt.BorderLayout.CENTER);

  }//GEN-END:initComponents

  private void usePluginChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_usePluginChanged
    if(useCheckBox.isSelected()) enableAll(true);
    else enableAll(false);
  }//GEN-LAST:event_usePluginChanged

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveTimeOutSettings();
        ((PluginManager)getTopLevelAncestor()).exitForm();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void enableAll(boolean enable){
        if(! enable){
            secField.setEnabled(false);
            projectCombo.setEnabled(false);
            pauseRadioButton.setEnabled(false);
            switchRadioButton.setEnabled(false);
            pauseLabel.setEnabled(false);
            secLabel.setEnabled(false);
        } else {
            secField.setEnabled(true);
            projectCombo.setEnabled(true);
            pauseRadioButton.setEnabled(true);
            switchRadioButton.setEnabled(true);
            pauseLabel.setEnabled(true);
            secLabel.setEnabled(true);
        }
    }
    
    public void setObject(Object obj) {
        timeOut = (TimeOut)obj;
        secField.setText("" + timeOut.getSeconds());
        useCheckBox.setSelected(timeOut.isUse());
        pauseRadioButton.setSelected(timeOut.getIdleAction() == TimeOut.IDLE_PAUSE);
        switchRadioButton.setSelected(timeOut.getIdleAction() == TimeOut.IDLE_PROJECT);
        
        String[] names = timeOut.getProjectNames();
        for(int i = 0; i < names.length; i++){
            projectCombo.addItem(names[i]);
        }
        projectCombo.setSelectedItem(timeOut.getProject());
        
        enableAll(timeOut.isUse());
    }    
    
    private void saveTimeOutSettings() {
        boolean selected = useCheckBox.isSelected();
        int secs = 0;
        try{ timeOut.setSeconds(Integer.parseInt(secField.getText())); }
        catch(NumberFormatException e){ timeOut.setSeconds(defaultTime); }
        timeOut.setUse(selected);
        timeOut.setIdleAction(switchRadioButton.isSelected() ? TimeOut.IDLE_PROJECT : TimeOut.IDLE_PAUSE);
        if(timeOut.getIdleAction() == TimeOut.IDLE_PROJECT) timeOut.setProject(projectCombo.getSelectedIndex());
        
        try {
          PluginManager.serializeObject(timeOut);
        } catch (java.io.FileNotFoundException e) {
          System.err.println("Error saving prefs for TimeOut plugin");
        }
    }
    
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel optionButtonPanel;
  private javax.swing.JPanel optionInputPanel;
  private javax.swing.JPanel optionPanel;
  private javax.swing.JLabel pauseLabel;
  private javax.swing.JRadioButton pauseRadioButton;
  private javax.swing.JComboBox projectCombo;
  private javax.swing.JButton saveButton;
  private javax.swing.JTextField secField;
  private javax.swing.JLabel secLabel;
  private javax.swing.ButtonGroup selectButtonGroup;
  private javax.swing.JRadioButton switchRadioButton;
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.JCheckBox useCheckBox;
  // End of variables declaration//GEN-END:variables
    
}