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

  /** Creates new form PrefsPanel */
    public PrefsPanel() {
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
      prefsButtonPanel = new javax.swing.JPanel();
      okButton = new javax.swing.JButton();
      cancelButton = new javax.swing.JButton();
      
      
      getContentPane().setLayout(new java.awt.GridLayout(1, 0));
      
      addWindowListener(new java.awt.event.WindowAdapter() {
        public void windowClosing(java.awt.event.WindowEvent evt) {
          exitForm(evt);
        }
      });
      
      prefsPanel.setLayout(new java.awt.BorderLayout());
      
      prefsInputPanel.setLayout(new java.awt.GridBagLayout());
      java.awt.GridBagConstraints gridBagConstraints1;
      
      generalLabel.setText("General Properties (requires restart)");
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
      
      prefsPanel.add(prefsInputPanel, java.awt.BorderLayout.CENTER);
      
      okButton.setText("OK");
      okButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          savePrefs(evt);
        }
      });
      
      prefsButtonPanel.add(okButton);
      
      cancelButton.setText("Cancel");
      cancelButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          cancel(evt);
        }
      });
      
      prefsButtonPanel.add(cancelButton);
      
      prefsPanel.add(prefsButtonPanel, java.awt.BorderLayout.SOUTH);
      
      tabbedPane.addTab("General", prefsPanel);
      
      getContentPane().add(tabbedPane);
      
      pack();
    }//GEN-END:initComponents

    private void savePrefs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePrefs
      savePrefs();
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
  private javax.swing.JPanel prefsButtonPanel;
  private javax.swing.JButton okButton;
  private javax.swing.JButton cancelButton;
  // End of variables declaration//GEN-END:variables

}
