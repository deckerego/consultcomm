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
//SkinLF support from L2FProd.com
import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.CompoundSkin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;


public class CsltComm extends javax.swing.JFrame {
  public static final String release = "ConsultComm CVS Release";
  static MediaTracker iconTracker;
  static AnimatePanel iconPanel;
  static Timer iconTimer;
  private ClntComm projectList;
  private String themePack, gtkTheme, kdeTheme;
  protected Image appIcon;
  protected boolean animateIcons = true;
  
  /** Creates new form CsltComm */
  public CsltComm() {
    appIcon = CsltComm.getImage(this, "graphics/icon.gif");
    
    // We need to attempt to find all weird XML errors before they happen...
    try { // This test will fail if the JVM < 1.4 or XML pack is not installed
      Class.forName("javax.xml.parsers.DocumentBuilder"); // jaxp.jar
      Class.forName("org.w3c.dom.Document"); // crimson.jar
      Class.forName("org.xml.sax.SAXException"); // xalan.jar
    } catch (ClassNotFoundException e) {
      String version = System.getProperty("java.vm.version");
      String errMsg;
      if(version.compareTo("1.4.0") < 0) {
        String extdir = System.getProperty("java.ext.dirs");
        errMsg = "Java API for XML Parsing was not found. "+
        "Make sure you have downloaded the Java API at "+
        "http://java.sun.com/xml/download.html and have installed "+
        "all .jar files in "+extdir;
      } else {
        errMsg = "You have Java 1.4 or higher installed, but "+
        "for some reason you're missing the Java XML libraries. Make "+
        "sure you've received your copy of the Java Runtime Environment "+
        "from Sun at http://java.sun.com. You can also report this "+
        "error to the ConsultComm web page at http://consultcomm."+
        "sourceforge.net";
      }
      CustomOptionPane.showMessageDialog(this, errMsg, "Error Loading XML", CustomOptionPane.ERROR_MESSAGE);
      System.exit(0);
    }
    
    readPrefs();
    loadSkin();
    initComponents();
    
    projectList = new ClntComm(this);
    getContentPane().add(projectList);

    if(animateIcons) {
      Image clockIcon = CsltComm.getImage(this, "graphics/BlueBar.gif");
      iconPanel = new AnimatePanel(clockIcon);      
      getContentPane().add(iconPanel);
      iconPanel.start();
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
    setIconImage(appIcon);
    setName("frame");
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
    if(iconPanel != null) remove(iconPanel);
    readPrefs();    
    loadSkin();
    if(animateIcons) {
      getContentPane().add(iconPanel);
      iconPanel.start();
    }
    pack();
  }

  private void loadSkin() {
    Skin skin = null;
    try {
      if(! kdeTheme.equals("") && ! gtkTheme.equals(""))
        skin = new CompoundSkin(SkinLookAndFeel.loadSkin(kdeTheme), SkinLookAndFeel.loadSkin(gtkTheme));
      else if(! kdeTheme.equals(""))
        skin = SkinLookAndFeel.loadSkin(kdeTheme);
      else if(! gtkTheme.equals(""))
        skin = SkinLookAndFeel.loadSkin(gtkTheme);
      else if(! themePack.equals(""))
        skin = SkinLookAndFeel.loadThemePack(themePack);
      
      if(skin != null) {
        SkinLookAndFeel.setSkin(skin);
        UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
      }
    } catch (ClassNotFoundException e) {
      System.err.println("Couldn't load theme engine!");
    } catch (Exception e) {
      System.err.println("Couldn't load theme! "+e);
    }
  }
  
  /**
   * Read through preferances file
   */
  private void readPrefs() {
    try {
      PrefsFile prefs = new PrefsFile("ClntComm.def");

      //Get animation flag
      Boolean iconAnimation = prefs.readFirstBoolean("animations", "display");
      if(iconAnimation == null) animateIcons = true;
      else animateIcons = iconAnimation.booleanValue();
      
      //Get skins
      themePack = prefs.readFirstString("skin", "theme");
      kdeTheme = prefs.readFirstString("skin", "kde");
      gtkTheme = prefs.readFirstString("skin", "gtk");
    } catch (Exception e) {
      System.err.println("Cannot read prefs file: "+e);
      e.printStackTrace(System.out);
    }
  }
  
  public static void main(String args[]) {
    new CsltComm().show();
  }
  
  /**
   * Translate a file from a bytestream in the JAR file
   * @param parent The parent object that is loading the file
   * @param path The relative path to the file stored in a Java Archive
   */
  static File getFile(Object parent, String path) {
    File file = null;    
    byte[] tn = null;
    InputStream in = parent.getClass().getResourceAsStream(path);
    try{
      file = File.createTempFile("csltcomm", null);
      file.deleteOnExit();
      FileOutputStream fout = new FileOutputStream(file);
      int length = in.available();
      tn = new byte[length];
      in.read(tn);
      fout.write(tn);
    } catch(Exception e){
      System.out.println("Error loading file "+path+": "+e);
    }
    return file;
  }
  
  /**
   * Translate an image from a bytestream in the JAR file
   * @param parent The parent object that is loading the file
   * @param path The relative path to the file stored in a Java Archive
   */
  static Image getImage(Object parent, String path) {
    Image image = null;
    byte[] tn = null;
    InputStream in = parent.getClass().getResourceAsStream(path);
    try{
      int length = in.available();
      tn = new byte[length];
      in.read(tn);
      image = Toolkit.getDefaultToolkit().createImage(tn);
    } catch(Exception e){
      System.out.println("Error loading image "+path+": "+e);
    }
    return image;
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
  class AnimatePanel extends JPanel {
    Image animationFrame;
    int frameNumber, frameDelay;
    int imageWidth, imageHeight;
    Timer iconTimer;
    
    public AnimatePanel(Image frame) {
      super();
      
      frameNumber = 0;
      frameDelay = 10;
      animationFrame = frame;
      iconTracker = new MediaTracker(this);
      iconTracker.addImage(animationFrame, 0);
      try{
        iconTracker.waitForAll();
      }catch(InterruptedException except){
        System.out.println("Error loading animated icons");
      }
      imageWidth = animationFrame.getWidth(this);
      imageHeight = animationFrame.getHeight(this);

      setPreferredSize(new Dimension(imageWidth, imageHeight));
      setMinimumSize(new Dimension(imageWidth, imageHeight));
      setMaximumSize(new Dimension(1024, imageHeight));
      
      iconTimer = new Timer(frameDelay,
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(projectList.isRunning()){
            frameNumber++;
            iconPanel.repaint();
          }
        }
      });
      
      iconTimer.setInitialDelay(0);
    }
    
    public void start() {
      iconTimer.start();
    }
    
    //Draw the current frame of animation.
    public void paintComponent(Graphics g) {
      super.paintComponent(g); //paint the background
      int width = getWidth();
      int height = getHeight();
      frameNumber = frameNumber%(width+imageWidth);
      
      //Paint the frame into the image.
      g.drawImage(animationFrame, (frameNumber%(width+imageWidth))-imageWidth, 0, this);
    }
  }
}
