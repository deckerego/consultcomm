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
    public static final int SHOW_TOTAL = 0;
    public static final int SHOW_BILLABLE = 1;
    public static final int SECONDS = 0;
    public static final int MINUTES = 1;
    public static final int IDLE_PAUSE = 5;
    public static final int IDLE_PROJECT = 6;
    
    private static long totalSeconds, billableSeconds, countdownMinutes;
    private static JFrame frame = new JFrame("Consultant Manager");
    
    private CsltComm csltComm;
    private TimerThread timerTask;
    private java.util.Timer timer;
    private java.awt.Dimension windowSize;
    private int projColumnWidth;
    private int index;
    private int selectedIndex;
    private Vector plugins;
    private PropertyChangeSupport changes;
    private TimeRecordSet times;
    
    int timeFormat;
    int showTotal;
    int saveInterval = 60;
    
    /** Creates new form TimeTrack */
    public ClntComm(CsltComm parent) {
        csltComm = parent;
        changes = new PropertyChangeSupport(this);
        
        readPrefs();
        timerTask = new TimerThread();
        initComponents();
        initSelectionModel();
        initColumns();
        loadPlugins();
        menuPanel.add(menuBar, java.awt.BorderLayout.NORTH);
        timer = new java.util.Timer();
        timer.schedule(timerTask, 0, 1000);
    }
    
    public void setTimes(TimeRecordSet times) { this.times = times; }
    public TimeRecordSet getTimes() { return this.times; }
    public void setSelectedIndex(int i) {
        this.selectedIndex = i;
        timeList.setSelectedRecord(selectedIndex);
    }
    public int getSelectedIndex() { return this.selectedIndex; }
    
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
        totalPanel = new javax.swing.JPanel();
        totalText = new javax.swing.JLabel();
        totalTime = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        menuPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();

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

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(windowSize);
        totalPanel.setLayout(new java.awt.GridLayout(1, 2));

        totalText.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        totalText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleTotals(evt);
            }
        });

        totalPanel.add(totalText);

        totalTime.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        refreshTotalTime();
        totalTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleTotals(evt);
            }
        });

        totalPanel.add(totalTime);

        add(totalPanel, java.awt.BorderLayout.SOUTH);

        timeList = new TableTree(new TableTreeModel(times, timeFormat));
        scrollPane.setViewportView(timeList);
        add(scrollPane, java.awt.BorderLayout.CENTER);

        menuPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

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

    }//GEN-END:initComponents
    
    private void initColumns() {
        TableColumn projectColumn = timeList.getColumnModel().getColumn(0);
        projectColumn.setPreferredWidth(projColumnWidth);
    }
    
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
      if(selectedRow != -1) {
          if(evt.getModifiers() == java.awt.event.MouseEvent.BUTTON1_MASK)
              if(evt.getClickCount() == 2) editWindow(selectedRow);
          if(evt.getModifiers() == java.awt.event.MouseEvent.BUTTON3_MASK) {
              selectedIndex = timeList.rowAtPoint(new java.awt.Point(evt.getX(), evt.getY()));
              timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
              editMenu.show(timeList, evt.getX(), evt.getY());
          }
      }
  }//GEN-LAST:event_editWindow
  
  private void toggleTimer(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleTimer
      toggleTimer();
  }//GEN-LAST:event_toggleTimer
  
  void toggleTimer() {
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
  
  private void zeroProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroProject
      Object[] options = {"OK", "Cancel"};
      int dialog = CustomOptionPane.showOptionDialog(frame,
      "All projects will be marked as having no elapsed time. Continue?",
      "Zero-Out All Projects", CustomOptionPane.YES_NO_OPTION,
      CustomOptionPane.WARNING_MESSAGE, null, options, options[1]);
      if(dialog == 0){
          int index = timeList.getSelectedRecord();
          times.resetTime();
          timerTask.startTime = System.currentTimeMillis()/1000;
          timeList.setModel(new TableTreeModel(times, timeFormat));
          //timeList.repaint();
          refreshTotalTime();
      }
  }//GEN-LAST:event_zeroProject
  
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
          }
      }
  }//GEN-LAST:event_deleteProject
  
  private void newProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProject
      editWindow(times.size());
      if(selectedIndex < 0) selectedIndex = 0;
      TimeRecord record = times.elementAt(selectedIndex); //find current selected record
      times.sort();
      timeList.setModel(new TableTreeModel(times, timeFormat));
      setSelectedIndex(times.indexOf(record)); //restore selected record
  }//GEN-LAST:event_newProject
  
private void toggleTotals (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toggleTotals
    if(evt.getModifiers() == java.awt.event.MouseEvent.BUTTON3_MASK) {
        if(showTotal == 0) showTotal = SHOW_BILLABLE; //Reset to last item
        else showTotal >>>= 1;
    } else {
        if(showTotal > SHOW_BILLABLE) showTotal = 0; //Reset to first item
        else if(showTotal == 0) showTotal = 1; //Add a bit (since left shift won't do anything)
        else showTotal <<= 1;
    }
    refreshTotalTime();
  }//GEN-LAST:event_toggleTotals

public void exitForm() {
    savePrefs();
}

/**
 * Update the total time elapsed
 */
public void refreshTotalTime(){
    switch(showTotal) {
        case SHOW_TOTAL:
            totalText.setText("Total:");
            totalTime.setText(times.getTotalTimeString());
            break;
        case SHOW_BILLABLE:
            totalText.setText("Billable:");
            totalTime.setText(times.getBillableTimeString());
            break;
        default: //showTotal is undefined, choose default
            showTotal = SHOW_TOTAL;
            refreshTotalTime();
    }
    //totalTime.repaint();
}

private void selectionChanged(ListSelectionEvent e) {
    setTimer();
}

public void reload() {
    savePrefs();
    removeAll();
    
    csltComm.reload();
    
    readPrefs();
    initComponents();
    initColumns();
    
    menuPanel.add(menuBar, java.awt.BorderLayout.NORTH);
    try {
        timeList.setSelectedRecord(selectedIndex);
    } catch (IllegalArgumentException e) {
        System.err.println("Row index invalid, not setting selection.");
    }
    
    revalidate();
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
    boolean newRecord = false;
    
    try {
        record = times.elementAt(index);
        newRecord = false;
    } catch (ArrayIndexOutOfBoundsException e) {
        record = new TimeRecord();
        newRecord = true;
    }
    
    ProjectEditDialog edit = new ProjectEditDialog((JFrame)this.getTopLevelAncestor(), record);
    edit.pack();
    edit.setLocationRelativeTo(this);
    edit.setVisible(true);
    
    if (edit.getValue().equals("0")) {
        TimeRecordSet oldTimes; //Copy the old timeset for the property listener
        try { oldTimes = (TimeRecordSet)times.clone(); }
        catch (CloneNotSupportedException e) { oldTimes = null; }
        
        long newTime = System.currentTimeMillis()/1000;
        if (index == selectedIndex) timerTask.startTime = newTime-record.getSeconds();
        if(newRecord) times.add(record);
        
        timeList.setModel(new TableTreeModel(times, timeFormat));
        //timeList.repaint();
        
        changes.firePropertyChange("times", oldTimes, times);
        if(selectedIndex == -1) //Nothing selected
            timeList.setSelectedRecord(index);
        else
            timeList.setSelectedRecord(selectedIndex);
        refreshTotalTime();
    }
}

private void loadPlugins() {
    try{
        plugins = new Vector();
        plugins = PluginManager.getPlugins();
        for(int i=0; i<plugins.size(); i++) {
            Object plugin = plugins.elementAt(i);
            System.out.println("Reading plugin "+plugin.getClass().getName());
            changes.addPropertyChangeListener((PropertyChangeListener)plugin);

            try {
                Expression getMenuItems = new Expression(plugin, "getMenuItems", null);
                System.out.println("Loading menu items...");
                JMenuItem[] menuItems = (JMenuItem[])getMenuItems.getValue();
                for(int j=0; j<menuItems.length; j++) toolMenu.add(menuItems[j]);
                System.out.println("Menu items loaded");
            } catch (Exception e) {
                System.out.println("No menu items for "+plugin.getClass().getName());
            }
        }
        changes.firePropertyChange("times", null, times); //Sync everyone on an initial clock tick
    } catch(Exception e) {
        System.err.println("Couldn't load plugins: "+e);
    }
}

/**
 * Read through preferances file
 */
private void readPrefs() {
    try {
        //Get all projects
        File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
        File prefsFile = new File(prefsdir, "projects.xml");
        FileInputStream inStream = new FileInputStream(prefsFile);
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(inStream));
        times = (TimeRecordSet)d.readObject();
        d.close();
        
        //Read in prefs file, close input stream
        prefsFile = new File(prefsdir, "CsltComm.xml");
        inStream = new FileInputStream(prefsFile);
        Preferences prefs = Preferences.userRoot().node("CsltComm");
        prefs.importPreferences(inStream);
        inStream.close();
        
        //Read prefs
        double width = prefs.getDouble("windowWidth", (double)256); //Get window dimensions
        double height = prefs.getDouble("windowHeight", (double)256);
        windowSize = new java.awt.Dimension((int)width, (int)height);
        projColumnWidth = prefs.getInt("columnWidth", (int)width/2); //Get project column dimensions
        showTotal = prefs.getInt("showTotal", SHOW_TOTAL); //Decide whether to show total time/billable time
        timeFormat = prefs.getInt("timeFormat", MINUTES); //Get time format
        saveInterval = prefs.getInt("saveInterval", 60); //Get save interval
    } catch (Exception e) {
        System.err.println("Cannot read prefs file: "+e);
        //Load default settings
        times = new TimeRecordSet();
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
        
        //save prefs
        Preferences prefs = Preferences.userRoot().node("CsltComm");
        java.awt.Dimension size = getSize(); //Save window dimensions
        prefs.putDouble("windowWidth", size.getWidth());
        prefs.putDouble("windowHeight", size.getHeight());
        TableColumn projectColumn = timeList.getColumnModel().getColumn(0); //Save project column dimensions
        prefs.putInt("columnWidth", projectColumn.getPreferredWidth());
        prefs.putInt("showTotal", showTotal); //Save show billable/total time flag

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

public boolean isRunning(){
    return timerTask.clockRunning;
}

private TableTree timeList;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem editMenuItem;
    private javax.swing.JMenuItem editPopupItem;
    private javax.swing.JButton startButton;
    private javax.swing.JMenuItem deletePopupItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JLabel totalTime;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JMenuItem prefsMenuItem;
    private javax.swing.JPanel totalPanel;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenuItem zeroMenuItem;
    private javax.swing.JMenu projectMenu;
    private javax.swing.JMenu toolMenu;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JMenuItem addMenuItem;
    private javax.swing.JMenuItem pluginsMenuItem;
    private javax.swing.JLabel totalText;
    private javax.swing.JPopupMenu editMenu;
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
            
            TimeRecordSet oldTimes; //Copy the old timeset for the property listener
            try { oldTimes = (TimeRecordSet)times.clone(); }
            catch (CloneNotSupportedException e) { oldTimes = null; }
                    
            if(clockRunning){
                selectedIndex = timeList.getSelectedRecord();
                //Get the current seconds past midnight.
                currTime = System.currentTimeMillis()/1000;
                if(selectedIndex >= 0){
                    
                    currSeconds = currTime - startTime;
                    times.setSeconds(selectedIndex, currSeconds);
                    
                    //Only repaint if the minutes (or seconds, depending on the
                    //time format) have changed.
                    if ((timeFormat == SECONDS) || (currSeconds % 60 == 0)){
                        refreshTotalTime();
                        timeList.setRecordAt(times.elementAt(selectedIndex), selectedIndex, 1);
                        timeList.repaint();
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
