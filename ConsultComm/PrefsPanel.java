//Standard Components
import java.util.*;
import java.io.*;
//XML Components
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class PrefsPanel extends javax.swing.JFrame {
  protected int timeFormat = ClntComm.MINUTES;
  protected boolean animateIcons = true;
  protected int saveInterval = 60;
  private ClntComm clntComm;
  
  /** Creates new form PrefsPanel */
  public PrefsPanel(ClntComm parent) {
    clntComm = parent;
    readPrefs();
    initComponents();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    timeFormatGroup = new javax.swing.ButtonGroup();
    tabbedPane = new javax.swing.JTabbedPane();
    prefsPanel = new javax.swing.JPanel();
    prefsInputPanel = new javax.swing.JPanel();
    generalLabel = new javax.swing.JLabel();
    timeFormatLabel = new javax.swing.JLabel();
    minuteButton = new javax.swing.JRadioButton();
    secondButton = new javax.swing.JRadioButton();
    showIconCheckBox = new javax.swing.JCheckBox();
    save1Label = new javax.swing.JLabel();
    saveField = new javax.swing.JTextField();
    save2Label = new javax.swing.JLabel();
    prefsButtonPanel = new javax.swing.JPanel();
    prefsOKButton = new javax.swing.JButton();
    prefsCancelButton = new javax.swing.JButton();
    flagsPanel = new javax.swing.JPanel();
    flagsInputPanel = new javax.swing.JPanel();
    flagLabel = new javax.swing.JLabel();
    billableCheckBox = new javax.swing.JCheckBox();
    exportCheckBox = new javax.swing.JCheckBox();
    flagsButtonPanel = new javax.swing.JPanel();
    flagsOKButton = new javax.swing.JButton();
    flagsCancelButton = new javax.swing.JButton();
    
    
    getContentPane().setLayout(new java.awt.GridLayout(1, 0));
    
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        exitForm(evt);
      }
    });
    
    prefsPanel.setLayout(new java.awt.BorderLayout());
    
    prefsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        showPrefs(evt);
      }
    });
    
    prefsInputPanel.setLayout(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints gridBagConstraints1;
    
    generalLabel.setText("General Properties");
    generalLabel.setForeground(java.awt.Color.black);
    generalLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(10, 0, 5, 0);
    prefsInputPanel.add(generalLabel, gridBagConstraints1);
    
    timeFormatLabel.setText("Show time in:  ");
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    prefsInputPanel.add(timeFormatLabel, gridBagConstraints1);
    
    minuteButton.setSelected(timeFormat == ClntComm.MINUTES);
    minuteButton.setForeground(new java.awt.Color(102, 102, 153));
    minuteButton.setText("Minutes");
    timeFormatGroup.add(minuteButton);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    prefsInputPanel.add(minuteButton, gridBagConstraints1);
    
    secondButton.setSelected(timeFormat == ClntComm.SECONDS);
    secondButton.setForeground(new java.awt.Color(102, 102, 153));
    secondButton.setText("Seconds");
    timeFormatGroup.add(secondButton);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    prefsInputPanel.add(secondButton, gridBagConstraints1);
    
    showIconCheckBox.setSelected(animateIcons);
    showIconCheckBox.setForeground(new java.awt.Color(102, 102, 153));
    showIconCheckBox.setText("Show Animated Icons");
    showIconCheckBox.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    showIconCheckBox.setMargin(new java.awt.Insets(6, 2, 2, 2));
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    prefsInputPanel.add(showIconCheckBox, gridBagConstraints1);
    
    save1Label.setText("Save info every ");
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    prefsInputPanel.add(save1Label, gridBagConstraints1);
    
    saveField.setColumns(3);
    saveField.setText(Integer.toString(saveInterval));
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    prefsInputPanel.add(saveField, gridBagConstraints1);
    
    save2Label.setText(" seconds");
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    prefsInputPanel.add(save2Label, gridBagConstraints1);
    
    prefsPanel.add(prefsInputPanel, java.awt.BorderLayout.CENTER);
    
    prefsOKButton.setText("OK");
    prefsOKButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        savePrefs(evt);
      }
    });
    
    prefsButtonPanel.add(prefsOKButton);
    
    prefsCancelButton.setText("Cancel");
    prefsCancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancel(evt);
      }
    });
    
    prefsButtonPanel.add(prefsCancelButton);
    
    prefsPanel.add(prefsButtonPanel, java.awt.BorderLayout.SOUTH);
    
    tabbedPane.addTab("General", prefsPanel);
    
    flagsPanel.setLayout(new java.awt.BorderLayout());
    
    flagsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        showFlags(evt);
      }
    });
    
    flagsInputPanel.setLayout(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints gridBagConstraints2;
    
    flagLabel.setText("Project Attribute Flags");
    flagLabel.setForeground(java.awt.Color.black);
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints2.insets = new java.awt.Insets(10, 0, 5, 0);
    flagsInputPanel.add(flagLabel, gridBagConstraints2);
    
    billableCheckBox.setSelected(clntComm.attributeSet(ClntComm.SHOW_BILLABLE));
    billableCheckBox.setForeground(new java.awt.Color(102, 102, 153));
    billableCheckBox.setText("Show Billable Flag");
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
    flagsInputPanel.add(billableCheckBox, gridBagConstraints2);
    
    exportCheckBox.setSelected(clntComm.attributeSet(ClntComm.SHOW_EXPORT));
    exportCheckBox.setForeground(new java.awt.Color(102, 102, 153));
    exportCheckBox.setText("Use Export to Database Flag");
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
    flagsInputPanel.add(exportCheckBox, gridBagConstraints2);
    
    flagsPanel.add(flagsInputPanel, java.awt.BorderLayout.CENTER);
    
    flagsOKButton.setText("OK");
    flagsOKButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        savePrefs(evt);
      }
    });
    
    flagsButtonPanel.add(flagsOKButton);
    
    flagsCancelButton.setText("Cancel");
    flagsCancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancel(evt);
      }
    });
    
    flagsButtonPanel.add(flagsCancelButton);
    
    flagsPanel.add(flagsButtonPanel, java.awt.BorderLayout.SOUTH);
    
    tabbedPane.addTab("Flags", flagsPanel);
    
    getContentPane().add(tabbedPane);
    
    pack();
  }//GEN-END:initComponents

  private void showFlags(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showFlags
    getRootPane().setDefaultButton(flagsOKButton);
  }//GEN-LAST:event_showFlags

  private void showPrefs(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showPrefs
    getRootPane().setDefaultButton(prefsOKButton);
  }//GEN-LAST:event_showPrefs
    
    private void savePrefs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePrefs
      savePrefs();
      clntComm.reload();
      exitForm();
    }//GEN-LAST:event_savePrefs
    
    private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
      exitForm();
    }//GEN-LAST:event_cancel
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
      exitForm();
    }//GEN-LAST:event_exitForm
    
    private void exitForm() {
      setVisible(false);
    }
    
    /**
     * Read through preferances file
     */
    private void readPrefs() {
      File prefs = new File(CsltComm.prefsDir, "ClntComm.def");
      
      if (prefs.exists()) {
        try {
          DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
          Document doc = docBuilder.parse(prefs);
          doc.getDocumentElement().normalize();
          
          NamedNodeMap attributes = null;

          //Get time format
          NodeList timeFormats = doc.getElementsByTagName("timeformat");
          if(timeFormats.getLength() > 0) {
            Node timeFormatting = timeFormats.item(0);
            attributes = timeFormatting.getAttributes();
            String timeFormatString = attributes.getNamedItem("type").getNodeValue();
            if(timeFormatString.equals("seconds")) timeFormat = ClntComm.SECONDS;
            if(timeFormatString.equals("minutes")) timeFormat = ClntComm.MINUTES;
          } else {
            timeFormat = ClntComm.MINUTES;
          }

          //Get animation flag
          NodeList iconAnimations = doc.getElementsByTagName("animations");
          if(iconAnimations.getLength() > 0) {
            Node iconAnimation = iconAnimations.item(0);
            attributes = iconAnimation.getAttributes();
            if(attributes.getNamedItem("display").getNodeValue().equals("true"))
              animateIcons = true;
            else
              animateIcons = false;
          } else {
            animateIcons = true;
          }

          //Get save interval
          NodeList saveInfos = doc.getElementsByTagName("saveinfo");
          if(saveInfos.getLength() > 0) {
            Node saveInfo = saveInfos.item(0);
            attributes = saveInfo.getAttributes();
            String saveIntervalString = attributes.getNamedItem("seconds").getNodeValue();
            saveInterval = Integer.parseInt(saveIntervalString);
          } else {
            saveInterval = 60;
          }
        } catch (SAXParseException e) {
          System.err.println("Error parsing prefs file, line "+e.getLineNumber()+": "+e.getMessage());
        } catch (SAXException e) {
          System.err.println("Error reading prefs file: "+e);
          e.printStackTrace(System.out);
        } catch (Exception e) {
          System.err.println("Cannot read prefs file: "+e);
          e.printStackTrace(System.out);
        }
      }
    }

    private void savePrefs() {
      File prefs = new File(CsltComm.prefsDir, "ClntComm.def");
      try {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc;
        Element rootNode, newNode;
        
        if (prefs.exists()) {
          doc = docBuilder.parse(prefs);
          rootNode = doc.getDocumentElement();
        } else {
          doc = docBuilder.newDocument();
          rootNode = doc.createElement("clntcomm");
          rootNode.setAttribute("version", "2.0");
          doc.appendChild(rootNode);
        }
        rootNode.normalize();
        

        //Save time format
        NodeList timeFormats = doc.getElementsByTagName("timeformat");
        newNode = doc.createElement("timeformat");
        if(secondButton.isSelected()) newNode.setAttribute("type", "seconds");
        if(minuteButton.isSelected()) newNode.setAttribute("type", "minutes");
        if(timeFormats.getLength() > 0) {
          Node timeFormatting = timeFormats.item(0);
          rootNode.replaceChild(newNode, timeFormatting);
        } else {
          rootNode.appendChild(newNode);
        }

        //Save animation flag
        NodeList iconAnimations = doc.getElementsByTagName("animations");
        newNode = doc.createElement("animations");
        newNode.setAttribute("display", ""+showIconCheckBox.isSelected());
        if(iconAnimations.getLength() > 0) {
          Node iconAnimation = iconAnimations.item(0);
          rootNode.replaceChild(newNode, iconAnimation);
        } else {
          rootNode.appendChild(newNode);
        }

        //Save attribute flag settings
        int attributes = 0;
        if(billableCheckBox.isSelected()) attributes = attributes | ClntComm.SHOW_BILLABLE;
        if(exportCheckBox.isSelected()) attributes = attributes | ClntComm.SHOW_EXPORT;

        NodeList attributeFlags = doc.getElementsByTagName("attributes");
        newNode = doc.createElement("attributes");
        newNode.setAttribute("value", Integer.toString(attributes));
        if(attributeFlags.getLength() > 0) {
          Node attributeFlag = attributeFlags.item(0);
          rootNode.replaceChild(newNode, attributeFlag);
        } else {
          rootNode.appendChild(newNode);
        }

        //Save save interval
        NodeList saveInfos = doc.getElementsByTagName("saveinfo");
        newNode = doc.createElement("saveinfo");
        newNode.setAttribute("seconds", saveField.getText());
        if(saveInfos.getLength() > 0) {
          Node saveInfo = saveInfos.item(0);
          rootNode.replaceChild(newNode, saveInfo);
        } else {
          rootNode.appendChild(newNode);
        }

        //Write to file
        doc.getDocumentElement().normalize();
        TransformerFactory fac = TransformerFactory.newInstance();
        Transformer trans = fac.newTransformer();
        trans.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(prefs));
      } catch (ParserConfigurationException e) {
        System.err.println("Error writing prefs file: "+e);
      } catch (Exception e) {
        System.err.println("Cannot write to prefs file: "+e);
        e.printStackTrace();
      }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup timeFormatGroup;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel prefsPanel;
    private javax.swing.JPanel prefsInputPanel;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JLabel timeFormatLabel;
    private javax.swing.JRadioButton minuteButton;
    private javax.swing.JRadioButton secondButton;
    private javax.swing.JCheckBox showIconCheckBox;
    private javax.swing.JLabel save1Label;
    private javax.swing.JTextField saveField;
    private javax.swing.JLabel save2Label;
    private javax.swing.JPanel prefsButtonPanel;
    private javax.swing.JButton prefsOKButton;
    private javax.swing.JButton prefsCancelButton;
    private javax.swing.JPanel flagsPanel;
    private javax.swing.JPanel flagsInputPanel;
    private javax.swing.JLabel flagLabel;
    private javax.swing.JCheckBox billableCheckBox;
    private javax.swing.JCheckBox exportCheckBox;
    private javax.swing.JPanel flagsButtonPanel;
    private javax.swing.JButton flagsOKButton;
    private javax.swing.JButton flagsCancelButton;
    // End of variables declaration//GEN-END:variables
    
}
