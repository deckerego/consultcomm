//Standard Components
import java.io.*;
import java.net.*;
import java.util.prefs.*;
//Swing/AWT Components
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
//SkinLF support from L2FProd.com
import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.CompoundSkin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;


public class CsltComm extends javax.swing.JFrame {
    public static final String release = "ConsultComm 3.0.4";
    static MediaTracker iconTracker;
    static AnimatePanel iconPanel;
    static javax.swing.Timer iconTimer;
    
    protected Image appIcon;
    protected boolean animateIcons = true;
    
    private ClntComm projectList;
    private String themePack, gtkTheme, kdeTheme;
    
    /** Creates new form CsltComm */
    public CsltComm() {
        if(System.getProperty("java.vm.version").compareTo("1.4.0") < 0) {
            String errMsg = "ConsultComm 3 requires Java 1.4 or higher. "+
            "Download the latest release of the Java Runtime Environment "+
            "at http://java.sun.com";
            CustomOptionPane.showMessageDialog(this, errMsg, "Error Loading ConsultComm", CustomOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        appIcon = JarImage.loadImage("graphics/icon.gif");

        readPrefs();
        loadSkin();
        initComponents();
        
        projectList = new ClntComm(this);
        getContentPane().add(projectList, java.awt.BorderLayout.CENTER);
        
        if(animateIcons) {
            Image clockIcon = JarImage.loadImage("graphics/BlueBar.gif");
            iconPanel = new AnimatePanel(clockIcon);
            getContentPane().add(iconPanel, java.awt.BorderLayout.SOUTH);
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
          getContentPane().add(iconPanel, java.awt.BorderLayout.SOUTH);
          iconPanel.start();
      }
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
          //Read in prefs file, close input stream
          File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
          File prefsFile = new File(prefsdir, "CsltComm.xml");
          InputStream inStream = new FileInputStream(prefsFile);
          Preferences prefs = Preferences.userRoot().node("CsltComm");
          prefs.importPreferences(inStream);
          inStream.close();
          
          //Read prefs
          animateIcons = prefs.getBoolean("animations", true); //Get animation flag
          themePack = prefs.get("theme", ""); //Get skins
          kdeTheme = prefs.get("kde", "");
          gtkTheme = prefs.get("gtk", "");
      } catch(Exception e) {
          System.err.println("Couldn't read prefs: "+e);
      }
  }
  
  public static void main(String args[]) {
      new CsltComm().show();
  }
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
  class AnimatePanel extends JPanel {
      Image animationFrame;
      int frameNumber, frameDelay;
      int imageWidth, imageHeight;
      javax.swing.Timer iconTimer;
      
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
          
          iconTimer = new javax.swing.Timer(frameDelay,
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
