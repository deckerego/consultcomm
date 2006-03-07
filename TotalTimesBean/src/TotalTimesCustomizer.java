import java.beans.*;
import java.io.*;
import javax.swing.*;
import java.text.*;
import java.util.*;

public class TotalTimesCustomizer extends javax.swing.JPanel implements java.beans.Customizer{
    private TotalTimes totalTimes;
    private static NumberFormat dollarFormat = NumberFormat.getInstance();
    private static String dollarSymbol = Currency.getInstance(Locale.getDefault()).getSymbol();
    
    /** Creates new customizer TimeOutCustomizer */
    public TotalTimesCustomizer() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        selectButtonGroup = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        optionPanel = new javax.swing.JPanel();
        optionInputPanel = new javax.swing.JPanel();
        countdownCheckBox = new javax.swing.JCheckBox();
        countdownTextField = new javax.swing.JTextField();
        countdownLabel = new javax.swing.JLabel();
        cashCheckBox = new javax.swing.JCheckBox();
        cashTextField = new javax.swing.JTextField();
        cashLabel = new javax.swing.JLabel();
        optionButtonPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setMinimumSize(new java.awt.Dimension(238, 221));
        setPreferredSize(new java.awt.Dimension(387, 254));
        tabbedPane.setMinimumSize(new java.awt.Dimension(238, 221));
        tabbedPane.setPreferredSize(new java.awt.Dimension(387, 254));
        optionPanel.setLayout(new java.awt.BorderLayout());

        optionInputPanel.setLayout(new java.awt.GridBagLayout());

        countdownCheckBox.setText("Countdown to ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(countdownCheckBox, gridBagConstraints);

        countdownTextField.setColumns(4);
        countdownTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        countdownTextField.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(countdownTextField, gridBagConstraints);

        countdownLabel.setText(" minutes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(countdownLabel, gridBagConstraints);

        cashCheckBox.setText("Count "+dollarSymbol);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(cashCheckBox, gridBagConstraints);

        cashTextField.setColumns(4);
        cashTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashTextField.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(cashTextField, gridBagConstraints);

        cashLabel.setText(" per hour");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        optionInputPanel.add(cashLabel, gridBagConstraints);

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

    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveTimeOutSettings();
        ((PluginManager)getTopLevelAncestor()).exitForm();
    }//GEN-LAST:event_saveButtonActionPerformed

    public void setObject(Object obj) {
        totalTimes = (TotalTimes)obj;
        if(totalTimes.getCountdownTime() != 0) {
            countdownTextField.setText(Long.toString(totalTimes.getCountdownTime()));
            countdownCheckBox.setSelected(true);
        }
        if(totalTimes.getCashAmount() != 0.0) {
            cashTextField.setText(dollarFormat.format(totalTimes.getCashAmount()));
            cashCheckBox.setSelected(true);
        }
    }    
    
    private void saveTimeOutSettings() {
        if(! countdownCheckBox.isSelected()) totalTimes.setCountdownTime(0L);
        else totalTimes.setCountdownTime(Long.parseLong(countdownTextField.getText()));
        if(! cashCheckBox.isSelected()) totalTimes.setCashAmount(0.0);
        else try{ totalTimes.setCashAmount(dollarFormat.parse(cashTextField.getText()).doubleValue()); }
        catch (java.text.ParseException e) { totalTimes.setCashAmount(0.0); }
        
        try {
          PluginManager.serializeObject(totalTimes);
        } catch (java.io.FileNotFoundException e) {
          System.err.println("Error saving prefs for TotalTimes plugin");
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cashCheckBox;
    private javax.swing.JLabel cashLabel;
    private javax.swing.JTextField cashTextField;
    private javax.swing.JCheckBox countdownCheckBox;
    private javax.swing.JLabel countdownLabel;
    private javax.swing.JTextField countdownTextField;
    private javax.swing.JPanel optionButtonPanel;
    private javax.swing.JPanel optionInputPanel;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.ButtonGroup selectButtonGroup;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
    
}
