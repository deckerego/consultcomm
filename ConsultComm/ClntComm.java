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
 * @version 1.0
 * @author John T. Ellis
 * Created on May 19, 2000, 7:37 PM
 */
public class ClntComm extends javax.swing.JPanel {
  private static long totalSeconds, billableSeconds;
  private static JFrame frame = new JFrame("Consultant Manager");
  private TimerThread timer;
  private TimeRecordSet times;
  private java.awt.Dimension windowSize;
  private int index, selectedIndex;
  
  //Flags for initializing components
  private boolean showTotal = true;
  
  /** Creates new form TimeTrack */
  public ClntComm() {
    readPrefs();
    initComponents();
    menuPanel.add(menuBar, java.awt.BorderLayout.NORTH);
    timer = new TimerThread(1000);
    timer.start();
    try {
      timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
    } catch (IllegalArgumentException e) {
      System.err.println("Row index invalid, not setting selection.");
    }
  }
  
  /**
   * Update the total time elapsed
   */
  public void refreshTotalTime(){
    if(showTotal)
      totalTime.setText(""+times.getTotalTimeString());
    else
      totalTime.setText(""+times.getBillableTimeString());
    totalTime.repaint();
  }
  
  /**
   * Read through preferances file
   */
  public void readPrefs() {
    File prefs = new File("ClntComm.def");
    times = new TimeRecordSet();
    if (prefs.exists()) {
      try {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(prefs);
        doc.getDocumentElement().normalize();
        NodeList projects = doc.getElementsByTagName("project");
        
        //Get all projects
        for(int i=0; i<projects.getLength(); i++){
          Node project = projects.item(i);
          NamedNodeMap attributes = project.getAttributes();
          Node nameNode = attributes.getNamedItem("name");
          String name = nameNode.getNodeValue();
          Node secondsNode = attributes.getNamedItem("seconds");
          long seconds = Long.parseLong(secondsNode.getNodeValue());
          Node billableNode = attributes.getNamedItem("billable");
          boolean billable = true;
          if(billableNode.getNodeValue().equals("false")) billable = false;
          
          Node selectedNode = attributes.getNamedItem("selected");
          if(selectedNode != null && selectedNode.getNodeValue().equals("true"))
            selectedIndex = i;
          TimeRecord record = new TimeRecord(name, seconds, billable);
          times.add(record);
        }
        
        NamedNodeMap attributes = null;
        
        //Get window dimensions
        NodeList dimensions = doc.getElementsByTagName("dimensions");
        Node dimension = dimensions.item(0);
        attributes = dimension.getAttributes();
        double width = Double.parseDouble(attributes.getNamedItem("width").getNodeValue());
        double height = Double.parseDouble(attributes.getNamedItem("height").getNodeValue());
        windowSize = new java.awt.Dimension((int)width, (int)height);
        
        //Decide whether to show total time/billable time
        NodeList showTotalTimes = doc.getElementsByTagName("showtotaltime");
        Node showTotalTime = showTotalTimes.item(0);
        attributes = showTotalTime.getAttributes();
        if(attributes.getNamedItem("display").getNodeValue().equals("billable"))
          showTotal = false;
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
  }
  
  public void savePrefs() {
    File prefs = new File("ClntComm.def");
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element rootNode = doc.createElement("clntcomm");
      rootNode.setAttribute("version", "2.0");
      doc.appendChild(rootNode);
      
      //Save projects
      selectedIndex = timeList.getSelectedRow();
      for(int i=0; i<times.size(); i++){
        TimeRecord record = times.elementAt(i);
        Element newNode = doc.createElement("project");
        newNode.setAttribute("name", record.projectName);
        newNode.setAttribute("seconds", ""+record.seconds);
        newNode.setAttribute("billable", ""+record.billable);
        if(i == selectedIndex)
          newNode.setAttribute("selected", "true");
        rootNode.appendChild(newNode);
      }
      
      Element newNode = null;
      
      //Save window dimensions
      java.awt.Dimension size = getSize();
      newNode = doc.createElement("dimensions");
      newNode.setAttribute("width", ""+size.getWidth());
      newNode.setAttribute("height", ""+size.getHeight());
      rootNode.appendChild(newNode);
      
      //Save show billable/total time flag
      newNode = doc.createElement("showtotaltime");
      newNode.setAttribute("display", showTotal ? "total" : "billable");
      rootNode.appendChild(newNode);
      
      //Write to file
      doc.getDocumentElement().normalize();
      TransformerFactory fac = TransformerFactory.newInstance();
      Transformer trans = fac.newTransformer();
      trans.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(prefs));
    } catch (ParserConfigurationException e) {
      System.err.println("Error writing prefs file: "+e);
      e.printStackTrace(System.out);
    } catch (Exception e) {
      System.err.println("Cannot write prefs file: "+e);
      e.printStackTrace(System.out);
    }
  }
  
  public boolean isRunning(){
    return timer.clockRunning;
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
    jdbcMenuItem = new javax.swing.JMenuItem();
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
    deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
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
    jdbcMenuItem.setText("JDBC Settings");
    jdbcMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editJDBC(evt);
      }
    });
    
    toolMenu.add(jdbcMenuItem);
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
    
    totalText.setText(showTotal ? "Total:" : "Billable:");
    totalText.setForeground(java.awt.Color.black);
    totalText.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    totalText.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        toggleTotals(evt);
      }
    });
    
    totalPanel.add(totalText);
    
    totalTime.setText(showTotal ? times.getTotalTimeString() : times.getBillableTimeString());
    totalTime.setForeground(java.awt.Color.black);
    totalTime.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    totalTime.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        toggleTotals(evt);
      }
    });
    
    totalPanel.add(totalTime);
    
    add(totalPanel, java.awt.BorderLayout.SOUTH);
    
    timeList.setModel(times.toTableModel());
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
  
  private void editJDBC(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editJDBC
    JDBCControlPanel panel = new JDBCControlPanel();
    panel.initGUI();
    panel.show();
  }//GEN-LAST:event_editJDBC
  
  private void sortColumn(java.awt.event.MouseEvent evt) {
    TableColumnModel columnModel = timeList.getColumnModel();
    int viewColumn = columnModel.getColumnIndexAtX(evt.getX());
    int column = timeList.convertColumnIndexToModel(viewColumn);
    times.sort(column);
    timeList.setModel(times.toTableModel());
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
  }//GEN-LAST:event_toggleTimer
  
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
      timeList.setModel(times.toTableModel());
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
        timeList.setModel(times.toTableModel());
      }
    }
  }//GEN-LAST:event_deleteProject
  
  private void newProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProject
    editWindow(times.size());
  }//GEN-LAST:event_newProject
  
private void toggleTotals (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toggleTotals
  showTotal = showTotal == false;
  if(showTotal)
    totalText.setText("Total:");
  else
    totalText.setText("Billable:");
  refreshTotalTime();
  }//GEN-LAST:event_toggleTotals
  
  private void selectionChanged(ListSelectionEvent e) {
    setTimer();
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
    try {
      record = times.elementAt(index);
    } catch (ArrayIndexOutOfBoundsException e) {
      record = new TimeRecord();
      times.add(record);
    }
    EditDialog edit = new EditDialog((JFrame)this.getTopLevelAncestor(), record);
    edit.pack();
    edit.setLocationRelativeTo(this);
    edit.setVisible(true);
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenu projectMenu;
  private javax.swing.JMenuItem addMenuItem;
  private javax.swing.JMenuItem deleteMenuItem;
  private javax.swing.JMenuItem editMenuItem;
  private javax.swing.JMenuItem zeroMenuItem;
  private javax.swing.JMenu toolMenu;
  private javax.swing.JMenuItem jdbcMenuItem;
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
    public boolean runThread, clockRunning;
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
      startTime = 0;
    }
    
    public void run(){
      long currTime;
      int index;
      long currSeconds;
      
      while(runThread){
        try {
          sleep(updateSeconds);
          if(clockRunning){
            //Get the current seconds past midnight.
            currTime = System.currentTimeMillis()/1000;
            if((index = timeList.getSelectedRow()) >= 0){
              currSeconds = currTime - startTime;
              times.setSeconds(index, currSeconds);
              //Only repaint if the minutes have changed.
              if (currSeconds % 60 == 0){
                refreshTotalTime();
                timeList.setValueAt(times.getTime(index), index, 1);
                timeList.repaint();
                savePrefs();
              }
            }
          }
        } catch (InterruptedException e) {
          System.err.println("Sleep failed.");
        }
      }
    }
  }
  
  private class EditDialog extends JDialog {
    private JOptionPane optionPane;
    
    EditDialog(JFrame frame, TimeRecord timerec) {
      super(frame, true);
      setTitle("Edit Project");
      
      java.awt.GridBagConstraints first = new java.awt.GridBagConstraints();;
      java.awt.GridBagConstraints last = new java.awt.GridBagConstraints();
      last.gridwidth = java.awt.GridBagConstraints.REMAINDER;
      java.awt.GridBagConstraints bottom = new java.awt.GridBagConstraints();
      bottom.gridwidth = 2;
      
      final TimeRecord record = timerec;
      final JTextField projField = new JTextField(record.projectName);
      projField.setColumns(10);
      final JTextField timeField = new JTextField(record.toString());
      timeField.setColumns(10);
      final JCheckBox billable = new JCheckBox("Billable Project", record.billable);
      JPanel editPanel = new JPanel();
      editPanel.setLayout(new java.awt.GridBagLayout());
      editPanel.add(new JLabel("Project: "), first);
      editPanel.add(projField, last);
      editPanel.add(new JLabel("Time: "), first);
      editPanel.add(timeField, last);
      editPanel.add(billable, bottom);
      
      optionPane = new JOptionPane(editPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      setContentPane(optionPane);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      addWindowListener(new java.awt.event.WindowAdapter() {
        public void windowClosing(java.awt.event.WindowEvent we) {
          optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
        }
      });
      
      optionPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent e) {
          String prop = e.getPropertyName();
          
          if (isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
            String value = optionPane.getValue().toString();
            if (value == JOptionPane.UNINITIALIZED_VALUE) return;
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
            
            if (value.equals("0")) {
              long newTime = System.currentTimeMillis()/1000;
              record.projectName = projField.getText();
              record.setSeconds(timeField.getText());
              if (index == selectedIndex) timer.startTime = newTime-record.seconds;
              record.billable = billable.isSelected();
              timeList.setModel(times.toTableModel());
              timeList.repaint();
              if(selectedIndex == -1) //Nothing selected
                timeList.setRowSelectionInterval(index, index);
              else
                timeList.setRowSelectionInterval(selectedIndex, selectedIndex);
              refreshTotalTime();
              setVisible(false);
            } else { // user closed dialog or clicked cancel
              setVisible(false);
            }
          }
        }
      });
    }
  }
}
