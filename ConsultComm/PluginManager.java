import java.io.*;
import java.beans.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

public class PluginManager extends javax.swing.JFrame implements ActionListener {
    private static final String PLUGINMFST="CsltComm-Plugin";
    private static final String LOADONLYONCE="LoadOnlyOnce";
    
    public static File pluginsdir = new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"plugins");
    public static File libsdir = new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"syslibs");
    private static Hashtable pluginList = new Hashtable();
    
    private Vector buttonList;
    private ClntComm clntcomm;

    public PluginManager(ClntComm parent) {
        parent.loadPlugins();
        this.clntcomm = parent;
        this.buttonList = new Vector();
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  private void initComponents() {//GEN-BEGIN:initComponents
    iconsPanel = new javax.swing.JPanel();
    iconScrollPane = new javax.swing.JScrollPane();
    iconListPanel = new javax.swing.JPanel();
    settingsPanel = new javax.swing.JPanel();
    introPanel = new javax.swing.JPanel();
    introTextArea = new javax.swing.JTextArea();

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

    getContentPane().add(iconsPanel, java.awt.BorderLayout.WEST);

    settingsPanel.setLayout(new java.awt.BorderLayout());

    settingsPanel.setPreferredSize(new java.awt.Dimension(450, 275));
    introPanel.setLayout(new java.awt.BorderLayout());

    introPanel.setPreferredSize(new java.awt.Dimension(387, 254));
    introTextArea.setEditable(false);
    introTextArea.setFont(new java.awt.Font("Dialog", 0, 14));
    introTextArea.setLineWrap(true);
    introTextArea.setText("Select a plugin icon from the bar on the left side of the window.\n\nIf you're missing icons for plugin settings, make sure you have plugins installed in ConsultComm's plugins/ directory.");
    introTextArea.setWrapStyleWord(true);
    introTextArea.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
    introPanel.add(introTextArea, java.awt.BorderLayout.CENTER);

    settingsPanel.add(introPanel, java.awt.BorderLayout.CENTER);

    getContentPane().add(settingsPanel, java.awt.BorderLayout.CENTER);

    pack();
  }//GEN-END:initComponents
  
  /** Exit the Application */
  private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
      exitForm();
  }//GEN-LAST:event_exitForm

  public void exitForm() {
      setVisible(false);
      dispose();
      clntcomm.reload();
  }
  
  private void loadSettingsPanel(int index) {
      try{
          Vector pluginList = new Vector(PluginManager.getPluginList());
          Object bean = pluginList.elementAt(index);
          BeanInfo pluginInfo = Introspector.getBeanInfo(bean.getClass());
          Class customizerClass = pluginInfo.getBeanDescriptor().getCustomizerClass();
          Customizer customizer = (Customizer)customizerClass.newInstance();
          customizer.setObject(bean);
          settingsPanel.removeAll();
          settingsPanel.add((javax.swing.JPanel)customizer);
          settingsPanel.validate();
      } catch(Exception e) {
          System.err.println("Couldn't load settings: "+e);
          e.printStackTrace();
      }
  }

  private void loadIcons() {
      try{
          Iterator pluginIterator = getPluginList().iterator();
          while(pluginIterator.hasNext()) {
              BeanInfo pluginInfo = Introspector.getBeanInfo(pluginIterator.next().getClass());
              javax.swing.JButton button = new javax.swing.JButton();
              java.awt.Image icon = pluginInfo.getIcon(SimpleBeanInfo.ICON_COLOR_32x32);
              button.setIcon(new javax.swing.ImageIcon(icon));
              iconListPanel.add(button);
              buttonList.add(button);
              button.addActionListener(this);
          }
      } catch(IntrospectionException e) {
          System.err.println("Couldn't load icons: "+e);
      } catch (NullPointerException e) {
          System.err.println("No plugins found! "+e);
      }
  }
  
  public static Collection getPluginList() {
      return pluginList.values();
  }

  public static void getPlugins() throws MalformedURLException, ClassNotFoundException, IOException {
      File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
      File syslibdir = new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"syslibs");

      //The syslibs directory must exist - all native libraries are
      //statically linked to this directory.
      if(! syslibdir.exists()) syslibdir.mkdir();

      //Get a list of all Java Archive files in our plugins directory
      File[] pluginfiles = pluginsdir.listFiles(new FilenameFilter() {
          public boolean accept(File dir, String name) {
              return name.endsWith(".jar");
          }
      });

      //Build URLs for each file
      URL[] pluginurls = new URL[pluginfiles.length];
      for(int i=0; i<pluginfiles.length; i++)
          pluginurls[i] = pluginfiles[i].toURL();

      //Create the custom class loader with all our JavaBeans
      ClassLoader loader = new URLClassLoader(pluginurls);
      Thread.currentThread().setContextClassLoader(loader);  //Sun BugID 4676532

      for(int i=0; i<pluginfiles.length; i++) {
          String currBean = pluginfiles[i].getName();
          currBean = currBean.substring(0, currBean.lastIndexOf(".jar"));

          File serializedFile = new File(prefsdir, currBean+".xml");
          File jarFile = pluginfiles[i];
          CsltCommPlugin plugin;
          
          String pluginProperties = JarLoader.getManifestAttribute(new java.util.jar.JarFile(jarFile), PLUGINMFST);
          boolean loadOnlyOnce = pluginProperties != null && pluginProperties.indexOf(LOADONLYONCE) != -1;
          
          if(! loadOnlyOnce || ! pluginList.containsKey(currBean)) {
              try { //Attempt to retrieve plugin settings from serialization file
                  FileInputStream inStream = new FileInputStream(serializedFile);
                  XMLDecoder d = new XMLDecoder(new BufferedInputStream(inStream));
                  System.out.println("Deserializing plugin "+currBean+" from "+serializedFile.getName());
                  plugin = (CsltCommPlugin)d.readObject();
                  d.close();
              } catch (Exception e) { //No luck restoring bean, invoke a default bean
                  System.out.println("Instantiating plugin "+currBean+" from "+pluginfiles[i].getName());
                  plugin = (CsltCommPlugin)Beans.instantiate(loader, currBean);
              }
              
              pluginList.put(currBean, plugin);
          }
      }
  }
  
  public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
      Object o = actionEvent.getSource();
      loadSettingsPanel(buttonList.indexOf(o));
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel iconListPanel;
  private javax.swing.JScrollPane iconScrollPane;
  private javax.swing.JPanel iconsPanel;
  private javax.swing.JPanel introPanel;
  private javax.swing.JTextArea introTextArea;
  private javax.swing.JPanel settingsPanel;
  // End of variables declaration//GEN-END:variables
  
}
