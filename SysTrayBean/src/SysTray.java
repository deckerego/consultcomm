import org.jdesktop.jdic.tray.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;


public class SysTray extends CsltCommPlugin implements ActionListener, ItemListener {
  private static final long CLICK_THRESHHOLD = 500;

  private static boolean systrayLibrary = false;
  private TrayIcon sysTrayIcon;
  private SystemTray tray;
  private ClntComm clntComm;
  private long lastClicked;
  private boolean visible;
  private JPopupMenu menu;
      
  static {
    //Even on non-*nix platforms we load this file. Doesn't hurt, and ensures that we have a
    //valid system library with sufficient room and permissions.
    systrayLibrary = JarLoader.loadNativeLibrary("libtray.so", SysTray.class) == JarLoader.FILE_OK;
    //Windows .dlls aren't automatically unloaded by Java, both because the JVM remains
    //resident and because .dlls (even when forced) remain locked and marked as in use.
    //Therefore it's not a huge surprise that an existing file is still sticking around.
    //Don't panic if it does; just panic if there was an I/O or other error thrown.
    systrayLibrary &= JarLoader.loadNativeLibrary("tray.dll", SysTray.class) != JarLoader.FILE_ERROR;
    //We don't call the native library here - the Tray API does that for us in the
    //prepackaged jar we get from JDIC.
  }
  
  public void setVisible(boolean visible) { this.visible = visible; }
  public boolean getVisible() { return this.visible; }
  public boolean isVisible() { return this.visible; }
  
  public SysTray() {
    if(systrayLibrary) {
      try {
        JMenuItem menuItem;
        
        tray = SystemTray.getDefaultSystemTray();
        ImageIcon icon = JarLoader.loadImageIcon("systray_22.png", SysTray.class);
        sysTrayIcon = new TrayIcon(icon, "Consultant Communicator 3", new JPopupMenu());
        sysTrayIcon.setIconAutoSize(true);
        tray.addTrayIcon(sysTrayIcon);
        
        //Load the plugin-specific menu options
        menu = new JPopupMenu("ConsultComm");
        menuItem = new JMenuItem("Cancel");
        menuItem.getAccessibleContext().setAccessibleDescription("Nevermind.");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Quit ConsultComm");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        sysTrayIcon.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            //The below line is a hack around the Win32 implementation always generating an 
            //event on every mousedown action, so several of them get submitted with each 
            //mouse click. Only take one event every CLICK_THRESHHOLD seconds
            if(evt.getWhen() - lastClicked < CLICK_THRESHHOLD) return;
            lastClicked = evt.getWhen();
            Component csltCommComponent = clntComm.getTopLevelAncestor();
            visible = csltCommComponent.isVisible();
            csltCommComponent.setVisible(! visible);
          }
        });
        
        sysTrayIcon.setPopupMenu(menu);
      } catch (java.lang.UnsatisfiedLinkError e) {
        systrayLibrary = false;
      }
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    JMenuItem source = (JMenuItem) (e.getSource());
    String s = source.getText();
    if (s.equalsIgnoreCase("Quit")) {
      clntComm.exitForm();
      System.exit(0);
    }
  }
  
  public void itemStateChanged(ItemEvent e) {
    JMenuItem source = (JMenuItem) (e.getSource());
  }
  
  public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
    if(systrayLibrary) {
      String eventName = propertyChangeEvent.getPropertyName();
      
      clntComm = (ClntComm)propertyChangeEvent.getSource();
      int selectedIndex = clntComm.getSelectedIndex();
      if(selectedIndex > 0) {
        TimeRecordSet recordSet = clntComm.getTimes();
        String[] projectNames = recordSet.getAllProjects();
        sysTrayIcon.setCaption(projectNames[selectedIndex]);
      }
      
      // ClntComm just started up, we now can access the GUI components            
      if("opened".equals(eventName)) {
        clntComm.getTopLevelAncestor().setVisible(visible);
      }
      
      if("unload".equals(eventName)) {
        try { //Save the app's state
          visible = clntComm.getTopLevelAncestor().isVisible();
          PluginManager.serializeObject(this);
        } catch (java.io.FileNotFoundException e) {
          System.err.println("Error saving prefs for SysTray plugin");
        }
      }
    }
  }
}
