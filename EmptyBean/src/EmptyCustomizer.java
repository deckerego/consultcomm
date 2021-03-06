/*
 * EmptyCustomizer.java
 *
 * Created on September 17, 2004, 1:59 PM
 */

/**
 * @author http://consultcomm.sourceforge.net
 */
public class EmptyCustomizer extends javax.swing.JPanel implements java.beans.Customizer {
  private Empty empty;
  
  /** Creates new customizer EmptyCustomizer */
  public EmptyCustomizer() {
    initComponents();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    tabbedPane = new javax.swing.JTabbedPane();
    optionPanel = new javax.swing.JPanel();
    optionInputPanel = new javax.swing.JPanel();
    optionButtonPanel = new javax.swing.JPanel();
    saveButton = new javax.swing.JButton();

    setLayout(new java.awt.BorderLayout());

    optionPanel.setLayout(new java.awt.BorderLayout());

    optionInputPanel.setLayout(new java.awt.GridBagLayout());

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

  private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
    saveEmptySettings();
    ((PluginManager)getTopLevelAncestor()).exitForm();
  }//GEN-LAST:event_saveButtonActionPerformed
  
  public void setObject(Object obj) {
    empty = (Empty)obj;
    //Copy fields from the given Object into the fields of this form
  }
  
  private void saveEmptySettings() {
    //Committ the values in this form into the Object
    
    //Now save the values for this object into an XML file
    try {
      PluginManager.serializeObject(empty);
    } catch (java.io.FileNotFoundException e) {
      System.err.println("Error saving prefs for Empty plugin");
    }
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel optionButtonPanel;
  private javax.swing.JPanel optionInputPanel;
  private javax.swing.JPanel optionPanel;
  private javax.swing.JButton saveButton;
  private javax.swing.JTabbedPane tabbedPane;
  // End of variables declaration//GEN-END:variables
  
}
