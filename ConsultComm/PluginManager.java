import java.io.*;
import java.beans.*;
import java.net.*;
import java.util.*;

public class PluginManager extends javax.swing.JFrame {
    Vector pluginList;
    
    /** Creates new form PluginManager */
    public PluginManager() {
        pluginList = new Vector();
        
        try { loadPlugins(); } 
        catch (Exception e) { System.err.println("Couldn't load all the plugins: "+e); }
        
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        iconsPanel = new javax.swing.JPanel();
        iconScrollPane = new javax.swing.JScrollPane();
        iconListPanel = new javax.swing.JPanel();
        settingsPanel = new javax.swing.JPanel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Plugin Settings");
        setName("pluginFrame");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        iconListPanel.setLayout(new javax.swing.BoxLayout(iconListPanel, javax.swing.BoxLayout.Y_AXIS));

        loadIcons();
        iconScrollPane.setViewportView(iconListPanel);

        iconsPanel.add(iconScrollPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(iconsPanel, gridBagConstraints);

        loadSettingsPanel(0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(settingsPanel, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
  
  /** Exit the Application */
  private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
      setVisible(false);
      dispose();
  }//GEN-LAST:event_exitForm
  
  private void loadSettingsPanel(int index) {
      try{
          Object bean = pluginList.elementAt(index);
          BeanInfo pluginInfo = Introspector.getBeanInfo(bean.getClass());
          Class customizerClass = pluginInfo.getBeanDescriptor().getCustomizerClass();
          Customizer customizer = (Customizer)customizerClass.newInstance();
          customizer.setObject(bean);
          settingsPanel.add((javax.swing.JPanel)customizer);
      } catch(Exception e) {
          System.err.println("Couldn't load settings: "+e);
      }
  }
  
  private void loadIcons() {
      try{
          for(int i=0; i<pluginList.size(); i++) {
              BeanInfo pluginInfo = Introspector.getBeanInfo(pluginList.elementAt(i).getClass());
              javax.swing.JButton button = new javax.swing.JButton();
              java.awt.Image icon = pluginInfo.getIcon(SimpleBeanInfo.ICON_COLOR_32x32);
              button.setIcon(new javax.swing.ImageIcon(icon));
              iconListPanel.add(button);
          }
      } catch(IntrospectionException e) {
          System.err.println("Couldn't load icons: "+e);
      }
  }
  
  private void loadPlugins() throws MalformedURLException, ClassNotFoundException, IOException {
      File pluginsdir = new File(System.getProperty("user.dir")+"/plugins");
      System.out.println("Looking for plugins in "+pluginsdir);
      File[] pluginfiles = pluginsdir.listFiles(new FilenameFilter() {
          public boolean accept(File dir, String name) {
              return name.endsWith(".jar");
          }
      });
      URL[] pluginurls = new URL[pluginfiles.length];
      for(int i=0; i<pluginfiles.length; i++)
          pluginurls[i] = pluginfiles[i].toURL();
      ClassLoader loader = new URLClassLoader(pluginurls);
      
      for(int i=0; i<pluginurls.length; i++) {
          String currBean = pluginfiles[i].getName();
          currBean = currBean.substring(0, currBean.lastIndexOf(".jar"));
          System.out.println("Loading plugin "+currBean);
          Object plugin = Beans.instantiate(loader, currBean);
          pluginList.addElement(plugin);
      }
  }
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane iconScrollPane;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JPanel iconListPanel;
    private javax.swing.JPanel iconsPanel;
    // End of variables declaration//GEN-END:variables
  
}
