//Standard Components
import java.util.*;
import java.io.*;
//XML Components
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
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
  protected static final int SECONDS = 0;
  protected static final int MINUTES = 1;
  protected static final int SHOW_TOTAL = 2;
  protected static final int SHOW_BILLABLE = 3;
  protected static final int SHOW_EXPORT = 4;
  protected static final int IDLE_PAUSE = 5;
  protected static final int IDLE_PROJECT = 6;
  
  private static long totalSeconds, billableSeconds;
  private static JFrame frame = new JFrame("Consultant Manager");
  private static File stylesheet;
  private static File prefs;
  
  private CsltComm csltComm;
  private TimerThread timer;
  TimeRecordSet times;
  private java.awt.Dimension windowSize;
  private int projColumnWidth;
  private int index, selectedIndex;
  private int timeFormat;
  private int attributes;
  private int showTotal;
  private int saveInterval = 60;
  private int idleAction = IDLE_PAUSE, allowedIdle = 0;
  private String idleProject = null;
  private static boolean timeoutLibrary = false;
  
  private native long getIdleTime();
  
  static {
    try {
      System.loadLibrary("timeout");
      timeoutLibrary = true;
    } catch (UnsatisfiedLinkError e) {
      System.err.println("Couldn't find timeout library in "+System.getProperty("java.library.path"));
      timeoutLibrary = false;
    }
  }
  
  /** Creates new form TimeTrack */
  public ClntComm(CsltComm parent) {
    prefs = new File(CsltComm.prefsDir, "ClntComm.def");
    stylesheet = CsltComm.getFile(this, "stylesheet.xsl");
    csltComm = parent;
    
    readPrefs();
    initComponents();
    initColumns();
    
    timer = new TimerThread(1000);
    timer.start();
    
    menuPanel.add(menuBar, java.awt.BorderLayout.NORTH);
    try {
      timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
    } catch (IllegalArgumentException e) {
      System.err.println("Row index invalid, not setting selection.");
    }
  }
  
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
    dbexportMenuItem = new javax.swing.JMenuItem();
    jdbcMenuItem = new javax.swing.JMenuItem();
    prefsMenuItem = new javax.swing.JMenuItem();
    helpMenuItem = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JPopupMenu();
    editPopupItem = new javax.swing.JMenuItem();
    deletePopupItem = new javax.swing.JMenuItem();
    totalPanel = new javax.swing.JPanel();
    totalText = new javax.swing.JLabel();
    totalTime = new javax.swing.JLabel();
    scrollPane = new javax.swing.JScrollPane();
    timeList = new javax.swing.JTable();
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
    dbexportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
    dbexportMenuItem.setText("Export to Table");
    dbexportMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportToTable(evt);
      }
    });
    
    toolMenu.add(dbexportMenuItem);
    jdbcMenuItem.setText("JDBC Settings");
    jdbcMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editJDBC(evt);
      }
    });
    
    toolMenu.add(jdbcMenuItem);
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
    
    timeList.setModel(times.toTableModel(timeFormat));
    timeList.setAutoCreateColumnsFromModel(false);
    timeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    ListSelectionModel rowSM = timeList.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        selectionChanged(e);
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
    
    scrollPane.setViewportView(timeList);
    
    add(scrollPane, java.awt.BorderLayout.CENTER);
    
    menuPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
    
    startButton.setMnemonic(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK).getKeyCode());
    startButton.setText("Start");
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
  
  private void showPrefs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPrefs
    new PrefsPanel(this).show();
  }//GEN-LAST:event_showPrefs
  
  private void showHelp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHelp
    new HelpDisplay().show();
  }//GEN-LAST:event_showHelp
  
  private void exportToTable(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToTable
    JDBCConnect dbConnection = new JDBCConnect();
    if(dbConnection.exportTimeRecordSet(times)) zeroProject(evt);
  }//GEN-LAST:event_exportToTable
  
  private void editJDBC(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editJDBC
    JDBCControlPanel panel = new JDBCControlPanel();
    panel.show();
  }//GEN-LAST:event_editJDBC
  
  private void sortColumn(java.awt.event.MouseEvent evt) {
    TableColumnModel columnModel = timeList.getColumnModel();
    int viewColumn = columnModel.getColumnIndexAtX(evt.getX());
    int column = timeList.convertColumnIndexToModel(viewColumn);
    
    TimeRecord record = times.elementAt(selectedIndex); //find current selected record
    times.sort(column);
    selectedIndex = times.indexOf(record); //restore selected record
    timeList.setModel(times.toTableModel(timeFormat));
    timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
  }
  
  private void editWindow(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editWindow
    if(evt.getModifiers() == java.awt.event.MouseEvent.BUTTON1_MASK) {
      if(evt.getClickCount() == 2) editWindow(timeList.getSelectedRow());
    }
    if(evt.getModifiers() == java.awt.event.MouseEvent.BUTTON3_MASK) {
      selectedIndex = timeList.rowAtPoint(new java.awt.Point(evt.getX(), evt.getY()));
      timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
      editMenu.show(timeList, evt.getX(), evt.getY());
    }
  }//GEN-LAST:event_editWindow
  
  private void toggleTimer(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleTimer
    toggleTimer();
  }//GEN-LAST:event_toggleTimer
  
  void toggleTimer() {
    if(timer.clockRunning){
      timer.clockRunning = false;
      startButton.setToolTipText("Start Timer");
      startButton.setText("Start");
    } else {
      setTimer();
      timer.clockRunning = true;
      startButton.setToolTipText("Pause Timer");
      startButton.setText("Pause");
    }
  }
  
  private void zeroProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroProject
    Object[] options = {"OK", "Cancel"};
    int dialog = JOptionPane.showOptionDialog(frame,
    "All projects will be marked as having \n no elapsed time. Continue?",
    "Zero-Out All Projects", JOptionPane.YES_NO_OPTION,
    JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    if(dialog == 0){
      int index = timeList.getSelectedRow();
      times.resetTime();
      timer.startTime = System.currentTimeMillis()/1000;
      timeList.setModel(times.toTableModel(timeFormat));
      timeList.repaint();
      refreshTotalTime();
    }
  }//GEN-LAST:event_zeroProject
  
  private void editProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProject
    editWindow(timeList.getSelectedRow());
  }//GEN-LAST:event_editProject
  
  private void deleteProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProject
    int len = 0;
    int selectedIndex = timeList.getSelectedRow();
    
    if(selectedIndex > -1){
      //Warn the user
      Object[] options = {"OK", "Cancel"};
      int dialog = JOptionPane.showOptionDialog(frame,
      "Project will be deleted. Continue?",
      "Delete Project", JOptionPane.YES_NO_OPTION,
      JOptionPane.WARNING_MESSAGE, null, options, options[1]);
      if(dialog == 0){
        times.delete(selectedIndex);
        timeList.setModel(times.toTableModel(timeFormat));
      }
    }
  }//GEN-LAST:event_deleteProject
  
  private void newProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProject
    editWindow(times.size());
    if(selectedIndex < 0) selectedIndex = 0;
    TimeRecord record = times.elementAt(selectedIndex); //find current selected record
    times.sort();
    selectedIndex = times.indexOf(record); //restore selected record
    timeList.setModel(times.toTableModel(timeFormat));
    timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
  }//GEN-LAST:event_newProject
  
private void toggleTotals (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toggleTotals
  switch(showTotal) {
    case SHOW_TOTAL:
      if(attributeSet(SHOW_EXPORT)) showTotal = SHOW_EXPORT;
      if(attributeSet(SHOW_BILLABLE)) showTotal = SHOW_BILLABLE;
      break;
    case SHOW_BILLABLE:
      if(attributeSet(SHOW_TOTAL)) showTotal = SHOW_TOTAL;
      if(attributeSet(SHOW_EXPORT)) showTotal = SHOW_EXPORT;
      break;
    case SHOW_EXPORT:
      if(attributeSet(SHOW_BILLABLE)) showTotal = SHOW_BILLABLE;
      if(attributeSet(SHOW_TOTAL)) showTotal = SHOW_TOTAL;
      break;
    default:
      showTotal = SHOW_TOTAL;
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
      case SHOW_EXPORT:
        totalText.setText("To Export:");
        totalTime.setText(times.getExportTimeString());
        break;
    }
    totalTime.repaint();
  }
  
  public boolean attributeSet(int flag) {
    return flag==0 ? true : (flag ^ attributes) != (flag | attributes);
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
      timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
    } catch (IllegalArgumentException e) {
      System.err.println("Row index invalid, not setting selection.");
    }
    
    revalidate();
  }
  
  private void setTimer() {
    if(timeList.getSelectedRow() >= 0){
      long currTime = System.currentTimeMillis()/1000;
      timer.startTime = currTime - times.getSeconds(timeList.getSelectedRow());
    }
  }
  
  /**
   * Create edit project window.
   * @parm Index into the project list
   */
  public void editWindow(int i){
    index = i;
    selectedIndex = timeList.getSelectedRow();
    TimeRecord record;
    boolean newRecord = false;
    
    try {
      record = times.elementAt(index);
      newRecord = false;
    } catch (ArrayIndexOutOfBoundsException e) {
      record = new TimeRecord();
      newRecord = true;
    }
    
    ProjectEditDialog edit = new ProjectEditDialog((JFrame)this.getTopLevelAncestor(), record, attributes);
    edit.pack();
    edit.setLocationRelativeTo(this);
    edit.setVisible(true);
    
    if (edit.getValue().equals("0")) {
      long newTime = System.currentTimeMillis()/1000;
      if (index == selectedIndex) timer.startTime = newTime-record.seconds;
      if(newRecord) times.add(record); 
      timeList.setModel(times.toTableModel(timeFormat));
      timeList.repaint();
      if(selectedIndex == -1) //Nothing selected
        timeList.setRowSelectionInterval(index, index);
      else
        timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
      refreshTotalTime();
    }
  }
  
  /**
   * Read through preferances file
   */
  private void readPrefs() {
    try {
      File prefs = new File(CsltComm.prefsDir, "ClntComm.def");
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc;
      
      if(prefs.exists()) {
        doc = docBuilder.parse(prefs);
        doc.getDocumentElement().normalize();
      } else {
        doc = docBuilder.newDocument();
        Element rootNode = doc.createElement("clntcomm");
        rootNode.setAttribute("version", "2.2");
        doc.appendChild(rootNode);
      }
      
      //Get all projects
      NodeList projects = doc.getElementsByTagName("project");
      times = new TimeRecordSet();
      for(int i=0; i<projects.getLength(); i++){
        Node project = projects.item(i);
        NamedNodeMap attributes = project.getAttributes();
        Node nameNode = attributes.getNamedItem("name");
        String name = nameNode.getNodeValue();
        Node aliasNode = attributes.getNamedItem("alias");
        String alias = null;
        if(aliasNode != null) alias = aliasNode.getNodeValue();
        Node secondsNode = attributes.getNamedItem("seconds");
        long seconds = Long.parseLong(secondsNode.getNodeValue());
        Node billableNode = attributes.getNamedItem("billable");
        boolean billable = ! billableNode.getNodeValue().equals("false");
        Node exportNode = attributes.getNamedItem("export");
        boolean export = exportNode != null ? ! exportNode.getNodeValue().equals("false") : billable;
        
        Node selectedNode = attributes.getNamedItem("selected");
        if(selectedNode != null && selectedNode.getNodeValue().equals("true"))
          selectedIndex = i;
        TimeRecord record = new TimeRecord(name, alias, seconds, billable, export);
        times.add(record);
      }
      
      NamedNodeMap attribute = null;
      
      //Get window dimensions
      NodeList dimensions = doc.getElementsByTagName("dimensions");
      Node dimension = dimensions.item(0);
      if(dimension != null) {
        attribute = dimension.getAttributes();
        double width = Double.parseDouble(attribute.getNamedItem("width").getNodeValue());
        double height = Double.parseDouble(attribute.getNamedItem("height").getNodeValue());
        windowSize = new java.awt.Dimension((int)width, (int)height);
      }
      
      //Get project column dimensions
      NodeList projColumns = doc.getElementsByTagName("projcolumn");
      Node projColumn = projColumns.item(0);
      if(projColumn != null) {
        attribute = projColumn.getAttributes();
        projColumnWidth = Integer.parseInt(attribute.getNamedItem("width").getNodeValue());
      }
      
      //Decide whether to show total time/billable time
      NodeList showTotalTimes = doc.getElementsByTagName("showtotaltime");
      Node showTotalTime = showTotalTimes.item(0);
      if(showTotalTime != null) {
        attribute = showTotalTime.getAttributes();
        String showTotalString = attribute.getNamedItem("display").getNodeValue();
        try {
          showTotal = Integer.parseInt(showTotalString);
        } catch (NumberFormatException e) {
          //Older versions used a string or a boolean to save the pref instead
          //of an int, so just give it a default value
          showTotal = SHOW_TOTAL;
        }
      } else {
        showTotal = SHOW_TOTAL;
      }
      
      
      //Get time format
      NodeList timeFormats = doc.getElementsByTagName("timeformat");
      Node timeFormatting = timeFormats.item(0);
      if(timeFormatting != null) {
        attribute = timeFormatting.getAttributes();
        String timeFormatString = attribute.getNamedItem("type").getNodeValue();
        if(timeFormatString.equals("seconds")) timeFormat = SECONDS;
        if(timeFormatString.equals("minutes")) timeFormat = MINUTES;
      } else {
        timeFormat = MINUTES;
      }
      
      //Get attribute flags
      NodeList attributeFlags = doc.getElementsByTagName("attributes");
      Node attributeFlag = attributeFlags.item(0);
      if(attributeFlag != null) {
        attribute = attributeFlag.getAttributes();
        String attributesString = attribute.getNamedItem("value").getNodeValue();
        attributes = Integer.parseInt(attributesString);
      } else {
        attributes = SHOW_TOTAL | SHOW_BILLABLE | SHOW_EXPORT;
      }
      
      //Get save interval
      NodeList saveInfos = doc.getElementsByTagName("saveinfo");
      Node saveInfo = saveInfos.item(0);
      if(saveInfo != null) {
        attribute = saveInfo.getAttributes();
        String saveIntervalString = attribute.getNamedItem("seconds").getNodeValue();
        saveInterval = Integer.parseInt(saveIntervalString);
      } else {
        saveInterval = 60;
      }
      
      //Get idle time settings
      NodeList idleTimes = doc.getElementsByTagName("idle");
      Node idleTime = idleTimes.item(0);
      if(idleTime != null) {
        attribute = idleTime.getAttributes();
        String allowedIdleString = attribute.getNamedItem("seconds").getNodeValue();
        allowedIdle = Integer.parseInt(allowedIdleString);
        
        Node idleActionItem = attribute.getNamedItem("action");
        if(idleActionItem != null) {
          String idleActionString = idleActionItem.getNodeValue();
          if(idleActionString.equals("project"))
            idleAction = ClntComm.IDLE_PROJECT;
          else
            idleAction = ClntComm.IDLE_PAUSE;
        } else {
          idleAction = ClntComm.IDLE_PAUSE;
        }
        
        Node idleProjectItem = attribute.getNamedItem("project");
        if(idleProjectItem != null)
          idleProject = idleProjectItem.getNodeValue();
        else
          idleProject = "";
      } else {
        allowedIdle = 0;
        idleAction = ClntComm.IDLE_PAUSE;
        idleProject = "";
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
  
  private void savePrefs() {
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc;
      Element rootNode;
      
      if (prefs.exists()) {
        doc = docBuilder.parse(prefs);
        rootNode = doc.getDocumentElement();
      } else {
        doc = docBuilder.newDocument();
        rootNode = doc.createElement("clntcomm");
        rootNode.setAttribute("version", "2.2");
        doc.appendChild(rootNode);
      }
      rootNode.normalize();
      
      //Delete projects
      NodeList projects = rootNode.getElementsByTagName("project");
      for(int i=projects.getLength()-1; i>=0; i--)
        rootNode.removeChild(projects.item(i));
      
      //Save projects
      selectedIndex = timeList.getSelectedRow();
      for(int i=0; i<times.size(); i++){
        TimeRecord record = times.elementAt(i);
        Element newNode = doc.createElement("project");
        newNode.setAttribute("name", record.projectName);
        if(record.alias != null)
          newNode.setAttribute("alias", record.alias);
        newNode.setAttribute("seconds", Long.toString(record.seconds));
        newNode.setAttribute("billable", record.billable ? "true" : "false");
        newNode.setAttribute("export", record.export ? "true" : "false");
        if(i == selectedIndex)
          newNode.setAttribute("selected", "true");
        rootNode.appendChild(newNode);
      }
      
      Element newNode = null;
      
      //Save window dimensions
      NodeList dimensions = doc.getElementsByTagName("dimensions");
      java.awt.Dimension size = getSize();
      newNode = doc.createElement("dimensions");
      newNode.setAttribute("width", Double.toString(size.getWidth()));
      newNode.setAttribute("height", Double.toString(size.getHeight()));
      Node dimension = dimensions.item(0);
      if(dimension != null) rootNode.replaceChild(newNode, dimension);
      else rootNode.appendChild(newNode);
      
      //Save project column dimensions
      NodeList projColumns = doc.getElementsByTagName("projcolumn");
      TableColumn projectColumn = timeList.getColumnModel().getColumn(0);
      newNode = doc.createElement("projcolumn");
      newNode.setAttribute("width", Integer.toString(projectColumn.getPreferredWidth()));
      Node projColumn = projColumns.item(0);
      if(projColumn != null) rootNode.replaceChild(newNode, projColumn);
      else rootNode.appendChild(newNode);
      
      //Save show billable/total time flag
      NodeList showTotalTimes = doc.getElementsByTagName("showtotaltime");
      newNode = doc.createElement("showtotaltime");
      newNode.setAttribute("display", Integer.toString(showTotal));
      Node showTotalTime = showTotalTimes.item(0);
      if(showTotalTime != null) rootNode.replaceChild(newNode, showTotalTime);
      else rootNode.appendChild(newNode);
      
      //Write to file
      doc.getDocumentElement().normalize();
      StreamSource stylesource = new StreamSource(stylesheet);
      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer(stylesource);
      transformer.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(prefs));
    } catch (ParserConfigurationException e) {
      System.err.println("Error writing prefs file: "+e);
    } catch (Exception e) {
      System.err.println("Cannot write to prefs file: "+e);
      e.printStackTrace(System.out);
    }
  }
  
  public boolean isRunning(){
    return timer.clockRunning;
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenu projectMenu;
  private javax.swing.JMenuItem addMenuItem;
  private javax.swing.JMenuItem deleteMenuItem;
  private javax.swing.JMenuItem editMenuItem;
  private javax.swing.JMenuItem zeroMenuItem;
  private javax.swing.JMenu toolMenu;
  private javax.swing.JMenuItem dbexportMenuItem;
  private javax.swing.JMenuItem jdbcMenuItem;
  private javax.swing.JMenuItem prefsMenuItem;
  private javax.swing.JMenuItem helpMenuItem;
  private javax.swing.JPopupMenu editMenu;
  private javax.swing.JMenuItem editPopupItem;
  private javax.swing.JMenuItem deletePopupItem;
  private javax.swing.JPanel totalPanel;
  private javax.swing.JLabel totalText;
  private javax.swing.JLabel totalTime;
  private javax.swing.JScrollPane scrollPane;
  private javax.swing.JTable timeList;
  private javax.swing.JPanel menuPanel;
  private javax.swing.JButton startButton;
  // End of variables declaration//GEN-END:variables
  
  /**
   * TimerThread updates system variables upon each tick of the clock.
   */
  private class TimerThread extends Thread{
    public boolean runThread, clockRunning, asleep;
    public int updateSeconds;
    public long startTime;
    
    /**
     * Create a new timer.
     * @parm How often to update (in milliseconds)
     * @parm The Consultant Manager screen to update
     */
    public TimerThread(int update) {
      updateSeconds = update;
      clockRunning = false;
      runThread = true;
      asleep = false;
      startTime = 0;
    }
    
    private void toggleIdle() {
      if(idleAction == ClntComm.IDLE_PROJECT) {
        int index = times.indexOfProject(idleProject);
        if(index < 0) { //Not a valid project
          toggleTimer();
        } else { 
          //Set the current project to be the 'idle' project, 
          //set the idle project to be the current
          int oldIndex = timeList.getSelectedRow();
          idleProject = times.elementAt(oldIndex).projectName;
          timeList.setRowSelectionInterval(index, index);
        }
      } else {
        toggleTimer();
      }
    }
    
    public void run(){
      long currTime, currSeconds;
      int index;
      int idleSeconds = -1;
      
      while(runThread){
        try {
          sleep(updateSeconds);
          
          if(allowedIdle > 0 && timeoutLibrary) //0 means don't worry about idle
            idleSeconds = (int)(getIdleTime()/1000L);
          
          //Check and see if we're supposed to wake up the clock
          //after the session has been idle
          if(allowedIdle > 0 && idleSeconds < allowedIdle && asleep) {
            toggleIdle();
            asleep = false;
            System.out.println("Toggling Out...");
          }
          
          if(clockRunning){
            //Get the current seconds past midnight.
            currTime = System.currentTimeMillis()/1000;
            if((index = timeList.getSelectedRow()) >= 0){
              currSeconds = currTime - startTime;
              times.setSeconds(index, currSeconds);
              
              //Only repaint if the minutes (or seconds, depending on the
              //time format) have changed.
              if ((timeFormat == SECONDS) || (currSeconds % 60 == 0)){
                refreshTotalTime();
                if(timeFormat == SECONDS)
                  timeList.setValueAt(times.getSecondsString(index), index, 1);
                else
                  timeList.setValueAt(times.getMinutesString(index), index, 1);
                timeList.repaint();
              }
              
              //If the user requested a save now, do it
              if (currSeconds % saveInterval == 0) savePrefs();
              
              //Check and see if we're supposed to do something when the
              //user session is idle
              if(allowedIdle > 0 && idleSeconds >= allowedIdle && ! asleep) {
                toggleIdle();
                asleep = true;
              }
            }
          }
        } catch (InterruptedException e) {
          System.err.println("Sleep failed.");
        }
      }
    }
  }
}