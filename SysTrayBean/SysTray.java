import org.jdesktop.jdic.tray.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;


public class SysTray extends Object implements java.io.Serializable, java.lang.Cloneable, java.beans.PropertyChangeListener, ActionListener, ItemListener {
    private static boolean systrayLibrary = false;
    private static TrayIcon sysTrayIcon;
    private static SystemTray tray;
    
    static {
        if(JarLoader.loadNativeLibrary("libtray.so", SysTray.class) && JarLoader.loadNativeLibrary("tray.dll", SysTray.class)) {
            try {
                systrayLibrary = true;
            } catch (UnsatisfiedLinkError e) {
                System.err.println("Couldn't find systray library in "+System.getProperty("java.library.path"));
                systrayLibrary = false;
            }
        }
        
        tray = SystemTray.getDefaultSystemTray();
        ImageIcon icon = new ImageIcon(JarLoader.loadImage("systray.gif", SysTray.class));
        sysTrayIcon = new TrayIcon(icon, "Consultant Communicator 3", new JPopupMenu());
        sysTrayIcon.setIconAutoSize(true);
        tray.addTrayIcon(sysTrayIcon);
        
        sysTrayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("An action got performed. w00t.");
            }
        });
    }
    
    public SysTray() {
        JPopupMenu menu;
        JMenuItem menuItem;
        
        menu = new JPopupMenu("A Menu");
        menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Quit ConsultComm");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        sysTrayIcon.setPopupMenu(menu);
    }
    
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String s = source.getText();
        if (s.equalsIgnoreCase("Quit")) {
            System.exit(0);
        }
    }
    
    public void itemStateChanged(ItemEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
    }
}
