import org.jdesktop.jdic.tray.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;


public class SysTray extends CsltCommPlugin implements ActionListener, ItemListener {
    private static boolean systrayLibrary = false;
    private static TrayIcon sysTrayIcon;
    private static SystemTray tray;
    private ClntComm clntComm;
    
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
        ImageIcon icon = JarLoader.loadImageIcon("systray.png", SysTray.class);
        sysTrayIcon = new TrayIcon(icon, "Consultant Communicator 3", new JPopupMenu());
        sysTrayIcon.setIconAutoSize(true);
        tray.addTrayIcon(sysTrayIcon);
    }
    
    public SysTray() {
        JPopupMenu menu;
        JMenuItem menuItem;
        
        
        sysTrayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Component csltCommComponent = clntComm.getTopLevelAncestor();
                csltCommComponent.setVisible(! csltCommComponent.isVisible());
            }
        });
        
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
        clntComm = (ClntComm)propertyChangeEvent.getSource();
        int selectedIndex = clntComm.getSelectedIndex();
        TimeRecordSet recordSet = clntComm.getTimes();
        String[] projectNames = recordSet.getAllProjects();
        sysTrayIcon.setCaption(projectNames[selectedIndex]);
    }
    
    public void unregister() {
    }
    
}
