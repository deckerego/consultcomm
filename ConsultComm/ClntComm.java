//Standard Components
import java.util.*;
import java.util.prefs.*;
import java.io.*;
import java.beans.*;
//Swing Components
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

/*
 * ClntComm.java
 * @author John T. Ellis
 * Created on May 19, 2000, 7:37 PM
 */
public class ClntComm extends javax.swing.JPanel {
    public static final int SECONDS = 0;
    public static final int MINUTES = 1;
    
    private static JFrame frame = new JFrame("Consultant Manager");
    
    /**
     * There are currently two types of property changes:
     * 1) 'times' which means the TimeRecordSet has been altered, or
     * 2) 'record' when a record is edited but the TimeRecordSet stays the same
     */
    private PropertyChangeSupport changes;
    
    private CsltComm csltComm;
    private TimerThread timerTask;
    private java.util.Timer timer;
    private int index;
    private int selectedIndex;
    private Vector plugins;
    private TimeRecordSet times;
    private TotalPanel totalPanel;
    
    int timeFormat;
    int showTotal;
    int saveInterval = 60;
    
    /**
     * Initialize a new ConsultComm project timer application
     */
    public ClntComm(CsltComm parent) {
        csltComm = parent;                                  //Set parent object for reloading CsltComm
        totalPanel = new TotalPanel();                      //Create a new total/elapsed counter
        timerTask = new TimerThread();                      //Create a new "clock" for the project timer
        readPrefs();                                        //Read in user preferences
        setTotals();                                        //Add our predefined counters
        initComponents();                                   //Init all our GUI components specified in the form
        loadPlugins();                                      //Load all the plugins
        readLayout();                                       //Restore the GUI layout after the components init
        menuPanel.add(menuBar, java.awt.BorderLayout.NORTH);//Add our menu items to the GUI
        timer = new java.util.Timer();                      //Wind up the clock and start it
        timer.schedule(timerTask, 0, 1000);                 
    }
    
    public void reload() {
        savePrefs();
        removeAll();
        csltComm.reload();
        totalPanel = new TotalPanel();
        readPrefs();
        setTotals();
        initComponents();
        loadPlugins();
        readLayout();
        menuPanel.add(menuBar, java.awt.BorderLayout.NORTH);
        try {
            if(times.size() != 0) timeList.setSelectedRecord(selectedIndex);
        } catch (IllegalArgumentException e) {
            System.err.println("Row index invalid, not setting selection.");
        }
        revalidate();
    }
    
    public void setTimes(TimeRecordSet times) { this.times = times; }
    public TimeRecordSet getTimes() { return this.times; }
    public void setSelectedIndex(int i) {
        this.selectedIndex = i;
        timeList.setSelectedRecord(selectedIndex);
    }
    public int getSelectedIndex() { return this.selectedIndex; }
    public boolean isRunning(){ return timerTask.clockRunning; }
    public TotalPanel getTotalPanel() { return this.totalPanel; }
    Vector getPlugins() { return this.plugins; }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        menuBar = new javax.swing.JMenuBar();
        projectMenu = new javax.swing.JMenu();
        addMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        editMenuItem = new javax.swing.JMenuItem();
        zeroMenuItem = new javax.swing.JMenuItem();
        toolMenu = new javax.swing.JMenu();
        pluginsMenuItem = new javax.swing.JMenuItem();
        prefsMenuItem = new javax.swing.JMenuItem();
        helpMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JPopupMenu();
        editPopupItem = new javax.swing.JMenuItem();
        deletePopupItem = new javax.swing.JMenuItem();
        addPopupItem = new javax.swing.JMenuItem();
        scrollPane = new javax.swing.JScrollPane();
        menuPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        totalGUIPanel = totalPanel;

        menuBar.setBorder(null);
        projectMenu.setText("Project");
        addMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        addMenuItem.setText("Add Project");
        addMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProject(evt);
            }
        });

        projectMenu.add(addMenuItem);
        deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteMenuItem.setText("Delete Project");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProject(evt);
            }
        });

        projectMenu.add(deleteMenuItem);
        editMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        editMenuItem.setText("Edit Project");
        editMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProject(evt);
            }
        });

        projectMenu.add(editMenuItem);
        zeroMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        zeroMenuItem.setText("Reset Timers");
        zeroMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zeroProject(evt);
            }
        });

        projectMenu.add(zeroMenuItem);
        menuBar.add(projectMenu);
        toolMenu.setText("Tools");
        pluginsMenuItem.setText("Manage Plugins");
        pluginsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPlugins(evt);
            }
        });

        toolMenu.add(pluginsMenuItem);
        prefsMenuItem.setText("Preferences");
        prefsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPrefs(evt);
            }
        });

        toolMenu.add(prefsMenuItem);
        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setText("Help");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHelp(evt);
            }
        });

        toolMenu.add(helpMenuItem);
        menuBar.add(toolMenu);
        editPopupItem.setText("Edit Project");
        editPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProject(evt);
            }
        });

        editMenu.add(editPopupItem);
        deletePopupItem.setText("Delete Project");
        deletePopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProject(evt);
            }
        });

        editMenu.add(deletePopupItem);
        addPopupItem.setText("Add Project");
        addPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProject(evt);
            }
        });

        editMenu.add(addPopupItem);

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(94, 50));
        timeList = new TableTree(new TableTreeModel(times, timeFormat));
        scrollPane.setViewportView(timeList);
        add(scrollPane, java.awt.BorderLayout.CENTER);

        menuPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        initSelectionModel();

        startButton.setMnemonic(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK).getKeyCode());
        startButton.setText(timerTask.clockRunning ? "Pause" : "Start");
        startButton.setToolTipText(timerTask.clockRunning ? "Pause Timer" : "Start Timer");
        startButton.setBorder(null);
        startButton.setBorderPainted(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleTimer(evt);
            }
        });

        menuPanel.add(startButton);

        add(menuPanel, java.awt.BorderLayout.NORTH);

        add(totalGUIPanel, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents
    
    private void initSelectionModel() {
        ListSelectionModel rowSM = timeList.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                selectionChanged(evt);
            }
        });
        JTableHeader timeListHeader = timeList.getTableHeader();
        timeListHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sortColumn(evt);
            }
        });
        timeList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                editWindow(evt);
            }
        });
    }

  private void showPrefs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPrefs
      new PrefsPanel(this).show();
  }//GEN-LAST:event_showPrefs

  private void showHelp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHelp
      new HelpDisplay().show();
  }//GEN-LAST:event_showHelp

  private void editPlugins(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPlugins
      new PluginManager(this).show();
  }//GEN-LAST:event_editPlugins

  private void sortColumn(java.awt.event.MouseEvent evt) {
      TableColumnModel columnModel = timeList.getColumnModel();
      int viewColumn = columnModel.getColumnIndexAtX(evt.getX());
      int column = timeList.convertColumnIndexToModel(viewColumn);

      TimeRecord record = times.elementAt(selectedIndex); //find current selected record
      times.sort(column);
      timeList.setModel(new TableTreeModel(times, timeFormat));
      setSelectedIndex(times.indexOf(record)); //restore selected record
  }

  private void editWindow(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editWindow
      int selectedRow = timeList.getSelectedRecord();
      this.selectedIndex = selectedRow;
      if(selectedRow != -1) {
          if(evt.getModifiers() == java.awt.event.MouseEvent.BUTTON1_MASK)
              if(evt.getClickCount() == 2) editWindow(selectedRow);
          if(evt.getModifiers() == java.awt.event.MouseEvent.BUTTON3_MASK) {
              javax.swing.tree.TreePath selectedPath = timeList.tree.getPathForLocation(evt.getX(), evt.getY());
              timeList.tree.setSelectionPath(selectedPath);
              editMenu.show(timeList, evt.getX(), evt.getY());
          }
      }
  }//GEN-LAST:event_editWindow
  
  private void toggleTimer(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleTimer
      toggleTimer();
  }//GEN-LAST:event_toggleTimer
  
  public void toggleTimer() {
      if(timerTask.clockRunning){
          timerTask.clockRunning = false;
          startButton.setToolTipText("Start Timer");
          startButton.setText("Start");
      } else {
          setTimer();
          timerTask.clockRunning = true;
          startButton.setToolTipText("Pause Timer");
          startButton.setText("Pause");
      }
  }
  
  private void setTotals() {
      totalPanel.setEntry("Total:", times.getTotalTime());
      totalPanel.setEntry("Billable:", times.getBillableTime());
  }
  
  private void zeroProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroProject
    zeroProject();
  }//GEN-LAST:event_zeroProject
  
  public void zeroProject() {
      Object[] options = {"OK", "Cancel"};
      int dialog = CustomOptionPane.showOptionDialog(frame,
      "All projects will be marked as having no elapsed time. Continue?",
      "Zero-Out All Projects", CustomOptionPane.YES_NO_OPTION,
      CustomOptionPane.WARNING_MESSAGE, null, options, options[1]);
      if(dialog == 0){
          int index = timeList.getSelectedRecord();
          TimeRecordSet oldTimes; //Copy the old timeset for the property listener
          try { oldTimes = (TimeRecordSet)times.clone(); }
          catch (CloneNotSupportedException e) { oldTimes = null; }
          times.resetTime();
          changes.firePropertyChange("times", oldTimes, times);
          setTotals();
          timerTask.startTime = System.currentTimeMillis()/1000;
          timeList.setModel(new TableTreeModel(times, timeFormat));
          totalPanel.repaint();
      }
  }
  
  private void editProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProject
      editWindow(timeList.getSelectedRecord());
  }//GEN-LAST:event_editProject
  
  private void deleteProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProject
      int len = 0;
      int selectedIndex = timeList.getSelectedRecord();
      
      if(selectedIndex > -1){
          //Warn the user
          Object[] options = {"OK", "Cancel"};
          int dialog = CustomOptionPane.showOptionDialog(frame,
          "Project will be deleted. Continue?",
          "Delete Project", CustomOptionPane.YES_NO_OPTION,
          CustomOptionPane.WARNING_MESSAGE, null, options, options[1]);
          if(dialog == 0){
              TimeRecordSet oldTimes; //Copy the old timeset for the property listener
              try { oldTimes = (TimeRecordSet)times.clone(); }
              catch (CloneNotSupportedException e) { oldTimes = null; }
              times.delete(selectedIndex);
              timeList.setModel(new TableTreeModel(times, timeFormat));
              changes.firePropertyChange("times", oldTimes, times);
              setTotals();
          }
      }
  }//GEN-LAST:event_deleteProject
  
  private void newProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProject
      editWindow(times.size());
  }//GEN-LAST:event_newProject
  
public void exitForm() {
    savePrefs();
}

private void selectionChanged(ListSelectionEvent e) {
    setTimer();
}

private void setTimer() {
    if(timeList.getSelectedRecord() >= 0){
        long currTime = System.currentTimeMillis()/1000;
        timerTask.startTime = currTime - times.getSeconds(timeList.getSelectedRecord());
    }
}

/**
 * Create edit project window.
 * @parm Index into the project list
 */
public void editWindow(int i){
    index = i;
    selectedIndex = timeList.getSelectedRecord();
    TimeRecord record;
    boolean newRecord;
    
    try {
        record = times.elementAt(index);
        newRecord = false;
    } catch (ArrayIndexOutOfBoundsException e) {
        record = new TimeRecord();
        int currSelected = timeList.getSelectedRecord();
        if(currSelected != -1) { //Suggest a group name based on the currently selected record
            TimeRecord currRecord = times.elementAt(currSelected);
            String groupName = currRecord.getGroupName();
            record.setGroupName(groupName);
        }
        newRecord = true;
    }
    
    TimeRecord oldRecord; //Copy the old record for the property listener
    try { oldRecord = (TimeRecord)record.clone(); }
    catch (CloneNotSupportedException e) { oldRecord = null; }
    
    ProjectEditDialog edit = new ProjectEditDialog((JFrame)this.getTopLevelAncestor(), record);
    edit.pack();
    edit.setLocationRelativeTo(this);
    edit.setVisible(true);
    
    if (edit.getValue().equals("0")) {
        long newTime = System.currentTimeMillis()/1000;
        if (index == selectedIndex) timerTask.startTime = newTime-record.getSeconds();
        
        if(newRecord) { //Send appropriate property change and add record
            TimeRecordSet oldTimes; //Copy the old timeset for the property listener
            try { oldTimes = (TimeRecordSet)times.clone(); }
            catch (CloneNotSupportedException e) { oldTimes = null; }
            times.add(record);
            changes.firePropertyChange("times", oldTimes, times);
        } else {
            changes.firePropertyChange("record", oldRecord, record);
        }
        
        times.sort();
        timeList.setModel(new TableTreeModel(times, timeFormat));
        setTotals();
        if(selectedIndex == -1) setSelectedIndex(index); //Nothing selected
        else timeList.setSelectedRecord(selectedIndex);
        totalPanel.repaint();
    }
}

void loadPlugins() {
    try{
        changes = new PropertyChangeSupport(this);
        plugins = PluginManager.getPlugins();
        for(int i=0; i<plugins.size(); i++) {
            Object plugin = plugins.elementAt(i);
            changes.addPropertyChangeListener((PropertyChangeListener)plugin);
            
            try {
                Expression getMenuItems = new Expression(plugin, "getMenuItems", null);
                JMenuItem[] menuItems = (JMenuItem[])getMenuItems.getValue();
                for(int j=0; j<menuItems.length; j++) toolMenu.add(menuItems[j]);
            } catch (Exception e) {}
        }
        changes.firePropertyChange("times", null, times); //Sync everyone on an initial clock tick
    } catch(Exception e) {
        System.err.println("Couldn't load plugins: "+e);
    }
}

private void readLayout() {
    File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
    if(! prefsdir.exists()) prefsdir.mkdir();
    
    try { //Remember expanded/collapsed rows
        File prefsFile = new File(prefsdir, "layout.xml");
        if(! prefsFile.exists()) prefsFile.createNewFile();
        FileInputStream inStream = new FileInputStream(prefsFile);
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(inStream));
        Vector expandedRows = (Vector)d.readObject();
        d.close();
        TableTreeModelAdapter adapter = (TableTreeModelAdapter)timeList.getModel();
        adapter.setExpandedRows(expandedRows);
    } catch (Exception e) {
        System.err.println("Cannot read layout file: "+e);
    }
    
    try { //Read in prefs file, close input stream
        File prefsFile = new File(prefsdir, "CsltComm.xml");
        if(! prefsFile.exists()) prefsFile.createNewFile();
        FileInputStream inStream = new FileInputStream(prefsFile);
        Preferences prefs = Preferences.userRoot().node("CsltComm");
        prefs.importPreferences(inStream);
        inStream.close();
        
        //Read dimensions
        double width = prefs.getDouble("windowWidth", (double)256); //Get window dimensions
        double height = prefs.getDouble("windowHeight", (double)256);
        this.setPreferredSize(new java.awt.Dimension((int)width, (int)height));
        int projColumnWidth = prefs.getInt("columnWidth", (int)width/2); //Get project column dimensions
        TableColumn column = timeList.getColumnModel().getColumn(0);
        column.setPreferredWidth(projColumnWidth);
        totalPanel.toggleTotal(prefs.getInt("totalIndex", 0)); //Get time panel's current selection
    } catch (Exception e) {
        System.err.println("Cannot read prefs file: "+e);
        times = new TimeRecordSet(); //Load default settings
    }
}

/**
 * Read through preferances file
 */
private void readPrefs() {
    File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
    if(! prefsdir.exists()) prefsdir.mkdir();
    
    try { //Get all projects
        File prefsFile = new File(prefsdir, "projects.xml");
        if(! prefsFile.exists()) prefsFile.createNewFile();
        FileInputStream inStream = new FileInputStream(prefsFile);
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(inStream));
        times = (TimeRecordSet)d.readObject();
        d.close();
    } catch (Exception e) {
        System.err.println("Cannot read projects file: "+e);
    }
    
    try { //Read in prefs file, close input stream
        File prefsFile = new File(prefsdir, "CsltComm.xml");
        if(! prefsFile.exists()) prefsFile.createNewFile();
        FileInputStream inStream = new FileInputStream(prefsFile);
        Preferences prefs = Preferences.userRoot().node("CsltComm");
        prefs.importPreferences(inStream);
        inStream.close();
        
        //Read prefs
        timeFormat = prefs.getInt("timeFormat", MINUTES); //Get time format
        saveInterval = prefs.getInt("saveInterval", 60); //Get save interval
    } catch (Exception e) {
        System.err.println("Cannot read prefs file: "+e);
        times = new TimeRecordSet(); //Load default settings
    }
}

private void savePrefs() {
    try {
        //Save projects
        File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
        File prefsFile = new File(prefsdir, "projects.xml");
        FileOutputStream outStream = new FileOutputStream(prefsFile);
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(outStream));
        e.writeObject(times);
        e.close();
        
        //Save list layout (closed/expanded rows)
        prefsFile = new File(prefsdir, "layout.xml");
        outStream = new FileOutputStream(prefsFile);
        e = new XMLEncoder(new BufferedOutputStream(outStream));
        TableTreeModelAdapter adapter = (TableTreeModelAdapter)timeList.getModel();
        e.writeObject(adapter.getExpandedRows());
        e.close();
        
        //save prefs
        Preferences prefs = Preferences.userRoot().node("CsltComm");
        java.awt.Dimension size = getSize(); //Save window dimensions
        prefs.putDouble("windowWidth", size.getWidth());
        prefs.putDouble("windowHeight", size.getHeight());
        TableColumn projectColumn = timeList.getColumnModel().getColumn(0); //Save project column dimensions
        prefs.putInt("columnWidth", projectColumn.getPreferredWidth());
        prefs.putInt("totalIndex", totalPanel.getIndex()); //Save total panel's current selection
        
        //Write to file, close output stream
        prefs.flush();
        prefsFile = new File(prefsdir, "CsltComm.xml");
        outStream = new FileOutputStream(prefsFile);
        prefs.exportNode(outStream);
        outStream.flush();
        outStream.close();
    } catch (Exception e) {
        System.err.println("Cannot write to prefs file(s): "+e);
    }
}

private TableTree timeList;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem pluginsMenuItem;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JMenuItem editMenuItem;
    private javax.swing.JMenuItem addMenuItem;
    private javax.swing.JMenuItem prefsMenuItem;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JPanel totalGUIPanel;
    private javax.swing.JMenu projectMenu;
    private javax.swing.JMenu toolMenu;
    private javax.swing.JButton startButton;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem addPopupItem;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPopupMenu editMenu;
    private javax.swing.JMenuItem deletePopupItem;
    private javax.swing.JMenuItem zeroMenuItem;
    private javax.swing.JMenuItem editPopupItem;
    private javax.swing.JMenuItem deleteMenuItem;
    // End of variables declaration//GEN-END:variables
    
    /**
     * TimerThread updates system variables upon each tick of the clock.
     */
    private class TimerThread extends TimerTask {
        public boolean clockRunning;
        public long startTime;
        
        public TimerThread() {
            clockRunning = false;
            startTime = 0;
        }
        
        public void run(){
            long currTime, currSeconds;
            
            //Copy the old timeset for the property listener
            TimeRecordSet oldTimes; 
            try { oldTimes = (TimeRecordSet)times.clone(); }
            catch (CloneNotSupportedException e) { oldTimes = null; }
                    
            if(clockRunning){
                selectedIndex = timeList.getSelectedRecord();
                currTime = System.currentTimeMillis()/1000;
                if(selectedIndex >= 0){
                    currSeconds = currTime - startTime;
                    times.setSeconds(selectedIndex, currSeconds);
                    //Only repaint if the minutes (or seconds, depending on the
                    //time format) have changed.
                    if ((timeFormat == SECONDS) || (currSeconds % 60 == 0)){
                        timeList.setRecordAt(times.elementAt(selectedIndex), selectedIndex, 1);
                        timeList.repaint();
                        totalPanel.repaint();
                        setTotals();
                    }
                    //If the user requested a save now, do it
                    if (currSeconds % saveInterval == 0) savePrefs();
                }
            }
            //Send out events to our plugins
            changes.firePropertyChange("times", oldTimes, times);
        }
    }
}
