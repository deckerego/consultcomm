//Standard Components
import java.io.*;
import java.net.*;
//XML Components
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
//Swing/AWT Components
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CsltComm extends javax.swing.JFrame {
  public static final String release = "ConsultComm CVS Release";
  final static File prefsDir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
  static MediaTracker iconTracker;
  static MTPanel iconPanel;
  public static int frameNumber;
  public static int imageWidth = 16;
  public static int imageHeight = 4;
  public static int frameDelay = 10;
  private ClntComm projectList;
  protected Image appIcon;
  protected boolean animateIcons = true;
  
  /** Creates new form CsltComm */
  public CsltComm() {
    frameNumber = 0;
    Image clockIcon = getImage("graphics/BlueBar.gif");
    appIcon = getImage("graphics/icon.gif");
    
    // Will this work for Java 1.4 ?
    try {
      Class.forName("javax.xml.parsers.DocumentBuilder"); // jaxp.jar
      Class.forName("org.w3c.dom.Document"); // crimson.jar
      Class.forName("org.xml.sax.Parser"); // xalan.jar
    } catch (ClassNotFoundException e) {
      String extdir = System.getProperty("java.ext.dirs");
      String errMsg = "Java API for XML Parsing was not found.\n"+
      "Make sure you have downloaded the Java API at\n"+
      "http://java.sun.com/xml/download.html and have installed\n"+
      "all .jar files in "+extdir;
      JOptionPane.showMessageDialog(this, errMsg, "JAXP Not Found", JOptionPane.ERROR_MESSAGE);
      System.exit(0);
    }
    initComponents();
    prefsDir.mkdir();
    readPrefs();
    
    projectList = new ClntComm(this);
    getContentPane().add(projectList);
    
    if(animateIcons) {
      iconTracker = new MediaTracker(this);
      iconTracker.addImage(clockIcon, 0);
      
      Timer iconTimer = new Timer(frameDelay,
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(projectList.isRunning()){
            try{
              iconTracker.waitForAll();
            }catch(InterruptedException except){
              System.out.println("Error printing frames");
            }
            frameNumber++;
            iconPanel.repaint();
          }
        }
      });
      
      iconTimer.setInitialDelay(0);
      iconPanel = new MTPanel(clockIcon);
      iconPanel.setPreferredSize(new Dimension(imageWidth, imageHeight));
      iconPanel.setMinimumSize(new Dimension(imageWidth, imageHeight));
      iconPanel.setMaximumSize(new Dimension(1024, imageHeight));

      getContentPane().add(iconPanel);
      iconTimer.start();
    }
    
    pack();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    
    getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));
    
    setTitle(release);
    setName("frame");
    setIconImage(appIcon);
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        exitForm(evt);
      }
    });
    
  }//GEN-END:initComponents
  
  /** Exit the Application */
  private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
    projectList.exitForm();
    System.exit(0);
  }//GEN-LAST:event_exitForm
  
  public void reload() {
    frameNumber = 0;
    Image clockIcon = getImage("graphics/BlueBar.gif");
    appIcon = getImage("graphics/icon.gif");
    
    if(iconPanel != null) remove(iconPanel);
    
    readPrefs();

    if(animateIcons) {
      iconTracker = new MediaTracker(this);
      iconTracker.addImage(clockIcon, 0);
      
      Timer iconTimer = new Timer(frameDelay,
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(projectList.isRunning()){
            try{
              iconTracker.waitForAll();
            }catch(InterruptedException except){
              System.out.println("Error printing frames");
            }
            frameNumber++;
            iconPanel.repaint();
          }
        }
      });
      
      iconTimer.setInitialDelay(0);
      iconPanel = new MTPanel(clockIcon);
      iconPanel.setPreferredSize(new Dimension(imageWidth, imageHeight));
      iconPanel.setMinimumSize(new Dimension(imageWidth, imageHeight));
      iconPanel.setMaximumSize(new Dimension(1024, imageHeight));

      getContentPane().add(iconPanel);
      iconTimer.start();
    }
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
  
  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    new CsltComm().show();
  }
  
  private Image getImage(String path) {
    //Load the animated icon from the JAR file... note that we can't just to a getImage()
    //anymore - we have to input the image data as a byte stream.
    Image image = null;
    byte[] tn = null;
    InputStream in = getClass().getResourceAsStream(path);
    try{
      int length = in.available();
      tn = new byte[length];
      in.read(tn);
      image = Toolkit.getDefaultToolkit().createImage(tn);
    } catch(Exception e){
      System.out.println("Error loading graphic "+path+": "+e);
    }
    return image;
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
  class MTPanel extends JPanel {
    Image animationFrame;
    
    public MTPanel(Image frame) {
      animationFrame = frame;
    }
    
    //Draw the current frame of animation.
    public void paintComponent(Graphics g) {
      super.paintComponent(g); //paint the background
      int width = getWidth();
      int height = getHeight();
      
      //If not all the images are loaded,
      //just display a status string.
      if (!iconTracker.checkAll()) {
        return;
      }
      
      //Paint the frame into the image.
      g.drawImage(animationFrame, (CsltComm.frameNumber%(width+imageWidth))-imageWidth, 0, this);
    }
  }
}
