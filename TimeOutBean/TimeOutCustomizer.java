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

        buttonGroup1 = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        optionPanel = new javax.swing.JPanel();
        optionInputPanel = new javax.swing.JPanel();
        useCheckBox = new javax.swing.JCheckBox();
        pauseLabel = new javax.swing.JLabel();
        secField = new javax.swing.JTextField();
        secLabel = new javax.swing.JLabel();
        pauseButton = new javax.swing.JRadioButton();
        switchButton = new javax.swing.JRadioButton();
        projectCombo = new javax.swing.JComboBox();
        optionButtonPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(238, 221));
        setPreferredSize(new java.awt.Dimension(387, 254));
        tabbedPane.setMinimumSize(new java.awt.Dimension(238, 221));
        tabbedPane.setPreferredSize(new java.awt.Dimension(387, 254));
        optionPanel.setLayout(new java.awt.BorderLayout());

        optionInputPanel.setLayout(new java.awt.GridBagLayout());

        useCheckBox.setText(" Use plugin?");
        useCheckBox.setToolTipText("<html>\n<body>\n<h3>\nCheck if you want to use the Time Out plugin\n</h3>\n</body>\n</html>");
        useCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(useCheckBox, gridBagConstraints);

        pauseLabel.setText("Perform action when idle for ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(pauseLabel, gridBagConstraints);

        secField.setColumns(10);
        secField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        optionInputPanel.add(secField, gridBagConstraints);

        secLabel.setText(" seconds");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(secLabel, gridBagConstraints);

        pauseButton.setSelected(true);
        pauseButton.setText("Pause after idle");
        pauseButton.setToolTipText("<html>\n<body>\n<h3>Pauses timer if the system has been idle for the entered number of seconds.</h3>\n</body>\n<html>");
        buttonGroup1.add(pauseButton);
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(pauseButton, gridBagConstraints);

        switchButton.setText("Switch to project     ");
        switchButton.setToolTipText("<html>\n<body>\n<h3>\nSwitches to selected project if the system has been idle for the given number of seconds\n</h3>\n</body>\n</html>\n");
        buttonGroup1.add(switchButton);
        switchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(switchButton, gridBagConstraints);

        projectCombo.setMaximumRowCount(50);
        projectCombo.setToolTipText("<html>\n<body>\n<h3>Select project to switch to</h3>\n</body>\n</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tabbedPane, gridBagConstraints);

    }//GEN-END:initComponents

    private void switchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_switchButtonActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_switchButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void secFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secFieldActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_secFieldActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveTimeOutSettings();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void useCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useCheckBoxActionPerformed
        enableAll(useCheckBox.isSelected());
    }//GEN-LAST:event_useCheckBoxActionPerformed

    private void enableAll(boolean enable){
        if(! enable){
            secField.setEnabled(false);
            secField.setText("");
            projectCombo.setEnabled(false);
            pauseButton.setEnabled(false);
            switchButton.setEnabled(false);
            pauseLabel.setEnabled(false);
            secLabel.setEnabled(false);
        }else{
            secField.setEnabled(true);
            projectCombo.setEnabled(true);
            pauseButton.setEnabled(true);
            switchButton.setEnabled(true);
            pauseLabel.setEnabled(true);
            secLabel.setEnabled(true);
        }
    }
    
    public void setObject(Object obj) {
        timeOut = (TimeOut)obj;
        displayBean();
    }    
    
    private void displayBean(){
        secField.setText("" + timeOut.getSeconds());
        useCheckBox.setSelected(timeOut.isUse());
        pauseButton.setSelected(timeOut.isPauseTimer());
        switchButton.setSelected(timeOut.isChangeProject());
        
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
        try{
            secs = Integer.parseInt(secField.getText());
        }catch(NumberFormatException e){
            if(selected){
                    secs = defaultTime;
            }else{
                secs = 0;
            }
        }
        timeOut.setUse(selected);
        timeOut.setSeconds(secs);
        timeOut.setPauseTimer(pauseButton.isSelected());
        timeOut.setChangeProject(switchButton.isSelected());
        timeOut.setProject((String)projectCombo.getSelectedItem());
        secField.setText("" + secs);
        
        try {
            File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
            File prefsFile = new File(prefsdir, "TimeOut.xml");
            Thread.currentThread().setContextClassLoader(timeOut.getClass().getClassLoader()); //Sun BugID 4676532
            FileOutputStream outStream = new FileOutputStream(prefsFile);
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(outStream));
            e.writeObject(timeOut);
            e.close();
        } catch (Exception e) {
            System.err.println("Couldn't save JDBC Prefs");
            e.printStackTrace(System.out);
        }
        
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel optionButtonPanel;
    private javax.swing.JComboBox projectCombo;
    private javax.swing.JPanel optionInputPanel;
    private javax.swing.JTextField secField;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel secLabel;
    private javax.swing.JLabel pauseLabel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JButton saveButton;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JCheckBox useCheckBox;
    private javax.swing.JRadioButton pauseButton;
    private javax.swing.JRadioButton switchButton;
    // End of variables declaration//GEN-END:variables
    
}
