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
 * @version 2.0
 * @author John T. Ellis
 * Created on May 19, 2000, 7:37 PM
 */
public class ClntComm extends javax.swing.JPanel {
  private static long totalSeconds, billableSeconds;
  private static JFrame frame = new JFrame("Consultant Manager");
  private TimerThread timer;
  private TimeRecordSet times;
  private java.awt.Dimension windowSize;

  // Needed for prompt windows
  private JTextField projField;
  private JTextField timeField;
  private JCheckBox billable;
  private JFrame editFrame;
  private TimeRecord record;
  private int index;
  
  /** Creates new form TimeTrack */
  public ClntComm() {
    readPrefs();
    initComponents ();
    timer = new TimerThread(1000);
    timer.start();
  }
  
  
  /**
   * Update the total time elapsed
   */
  public void refreshTotalTime(){
    if(totalText.getText() == "Total:"){
      totalTime.setText(""+times.getTotalTimeString());
    } else {
      totalTime.setText(""+times.getBillableTimeString());
    }
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
      } catch (SAXParseException e) {
        System.out.println("Error parsing prefs file, line "+e.getLineNumber()+": "+e.getMessage());
      } catch (SAXException e) {
        System.out.println("Error reading prefs file: "+e);
        e.printStackTrace(System.out);
      } catch (Exception e) {
        System.out.println("Cannot read prefs file: "+e);
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
      for(int i=0; i<times.size(); i++){
        TimeRecord record = times.elementAt(i);
        Element newNode = doc.createElement("project");
        newNode.setAttribute("name", record.projectName);
        newNode.setAttribute("seconds", ""+record.seconds);
        newNode.setAttribute("billable", ""+record.billable);
        rootNode.appendChild(newNode);
      }
      
      Element newNode = null;

      //Save window dimensions      
      java.awt.Dimension size = getSize();
      newNode = doc.createElement("dimensions");
      newNode.setAttribute("width", ""+size.getWidth());
      newNode.setAttribute("height", ""+size.getHeight());
      rootNode.appendChild(newNode);
      
      //Write to file
      doc.getDocumentElement().normalize();
      TransformerFactory fac = TransformerFactory.newInstance();
      Transformer trans = fac.newTransformer();
      trans.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(prefs));
    } catch (ParserConfigurationException e) {
      System.out.println("Error writing prefs file: "+e);
      e.printStackTrace(System.out);
    } catch (Exception e) {
      System.out.println("Cannot write prefs file: "+e);
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
    toolBar = new javax.swing.JToolBar();
    start_button = new javax.swing.JButton();
    edit_button = new javax.swing.JButton();
    new_button = new javax.swing.JButton();
    delete_button = new javax.swing.JButton();
    zero_button = new javax.swing.JButton();
    totalPanel = new javax.swing.JPanel();
    totalText = new javax.swing.JLabel();
    totalTime = new javax.swing.JLabel();
    scrollPane = new javax.swing.JScrollPane();
    timeList = new javax.swing.JTable();
    setLayout(new java.awt.BorderLayout());
    setPreferredSize(windowSize);
    
    
    start_button.setToolTipText("Start Timer");
      start_button.setText("G");
      start_button.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          toggleTimer(evt);
        }
      }
      );
      toolBar.add(start_button);
      
      
    edit_button.setToolTipText("Edit Project");
      edit_button.setText("E");
      edit_button.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          editWindow(evt);
        }
      }
      );
      toolBar.add(edit_button);
      
      
    new_button.setToolTipText("New Project");
      new_button.setText("N");
      new_button.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          newProj(evt);
        }
      }
      );
      toolBar.add(new_button);
      
      
    delete_button.setToolTipText("Delete Project");
      delete_button.setText("D");
      delete_button.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          delProj(evt);
        }
      }
      );
      toolBar.add(delete_button);
      
      
    zero_button.setToolTipText("Zero-Out Project");
      zero_button.setText("Z");
      zero_button.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          zeroProj(evt);
        }
      }
      );
      toolBar.add(zero_button);
      
      
    add(toolBar, java.awt.BorderLayout.NORTH);
    
    
    totalPanel.setLayout(new java.awt.GridLayout(1, 2));
    
    totalText.setText("Total:");
      totalText.setForeground(java.awt.Color.black);
      totalText.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
          toggleTotals(evt);
        }
      }
      );
      totalPanel.add(totalText);
      
      
    totalTime.setText(times.getTotalTimeString());
      totalTime.setForeground(java.awt.Color.black);
      totalTime.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
          toggleTotals(evt);
        }
      }
      );
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
      scrollPane.setViewportView(timeList);
      
      
    add(scrollPane, java.awt.BorderLayout.CENTER);
    
  }//GEN-END:initComponents
  
private void toggleTotals (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toggleTotals
  if(totalText.getText() == "Total:"){
    totalText.setText("Billable:");
  } else {
    totalText.setText("Total:");
  }
  refreshTotalTime();
  }//GEN-LAST:event_toggleTotals
  
  private void selectionChanged(ListSelectionEvent e) {
    setTimer(timeList);
  }
  
  private void setTimer (JTable theList) {
    if(theList.getSelectedRow() >= 0){
      Calendar newTime = Calendar.getInstance();
      timer.startTime = (newTime.get(Calendar.HOUR_OF_DAY)*3600)
        + (newTime.get(Calendar.MINUTE)*60)
        + newTime.get(Calendar.SECOND)
        - times.getSeconds(theList.getSelectedRow());
    }
  }
  
private void zeroProj (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroProj
  Object[] options = {"OK", "Cancel"};
  int dialog = JOptionPane.showOptionDialog(frame,
  "Project will be marked as having \n no elapsed time. Continue?",
  "Zero-Out Project", JOptionPane.YES_NO_OPTION,
  JOptionPane.WARNING_MESSAGE, null, options, options[1]);
  if(dialog == 0){
    Calendar newTime = Calendar.getInstance();
    int index = timeList.getSelectedRow();
    times.resetTime(index);
    timer.startTime = (newTime.get(Calendar.HOUR_OF_DAY)*3600)
    + (newTime.get(Calendar.MINUTE)*60)
    + newTime.get(Calendar.SECOND);
    timeList.setModel(times.toTableModel());
    timeList.repaint();
    refreshTotalTime();
  }
  }//GEN-LAST:event_zeroProj
  
private void delProj (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delProj
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
  }//GEN-LAST:event_delProj
  
private void newProj (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProj
  editWindow(times.size());
  }//GEN-LAST:event_newProj
  
private void editWindow (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editWindow
  editWindow(timeList.getSelectedRow());
  }//GEN-LAST:event_editWindow
  
/**
 * Create edit project window.
 * @parm Index into the project list
 */
  public void editWindow(int i){
    index = i;
    try {
      record = times.elementAt(index);
    } catch (ArrayIndexOutOfBoundsException e) {
      record = new TimeRecord();
      times.add(record);
    }

    //Set up the edit project window.
    projField = new JTextField(record.projectName);
    timeField = new JTextField(record.toString());
    editFrame = new JFrame("Revise Client");
    
    JPanel editPanel = new JPanel();
    editPanel.setLayout(new java.awt.GridLayout(3, 1));
    editPanel.add(projField);
    editPanel.add(timeField);
    
    //Is project billable or not (otherwise, will it count in the total
    //minutes or not?)
    billable = new JCheckBox("Billable Project", record.billable);
    editPanel.add(billable);
    
    //Set up buttons
    JButton okay = new JButton("OK");
    okay.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        Calendar newTime = Calendar.getInstance();
        record.projectName = projField.getText();
        record.setSeconds(timeField.getText());
        if (index == timeList.getSelectedRow()){
          timer.startTime = (newTime.get(Calendar.HOUR_OF_DAY)*3600)
          + (newTime.get(Calendar.MINUTE)*60)
          + newTime.get(Calendar.SECOND)
          - record.seconds;
        }
        record.billable = billable.isSelected();
        timeList.setModel(times.toTableModel());
        timeList.repaint();
        refreshTotalTime();
        editFrame.dispose();
      }
    });
    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        editFrame.dispose();
      }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new java.awt.GridLayout(1, 2));
    buttonPanel.add(okay);
    buttonPanel.add(cancel);
    
    //Create edit window
    editFrame.getContentPane().add(editPanel, java.awt.BorderLayout.CENTER);
    editFrame.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
    editFrame.pack();
    editFrame.setVisible(true);
  }
  
private void toggleTimer (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleTimer
  if(timer.clockRunning){
    timer.clockRunning = false;
    start_button.setToolTipText ("Start Timer");
    start_button.setText("G");
  } else {
    setTimer(timeList);
    timer.clockRunning = true;
    start_button.setToolTipText ("Pause Timer");
    start_button.setText("S");
  }
  }//GEN-LAST:event_toggleTimer
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JToolBar toolBar;
  private javax.swing.JButton start_button;
  private javax.swing.JButton edit_button;
  private javax.swing.JButton new_button;
  private javax.swing.JButton delete_button;
  private javax.swing.JButton zero_button;
  private javax.swing.JPanel totalPanel;
  private javax.swing.JLabel totalText;
  private javax.swing.JLabel totalTime;
  private javax.swing.JScrollPane scrollPane;
  private javax.swing.JTable timeList;
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
      Calendar currCalendar;
      long currTime;
      int index;
      long currSeconds;
      
      while(runThread){
        try {
          sleep(updateSeconds);
          if(clockRunning){
            //Get the current seconds past midnight.
            currCalendar = Calendar.getInstance();
            currTime = (currCalendar.get(Calendar.HOUR_OF_DAY)*3600)
              + (currCalendar.get(Calendar.MINUTE)*60)
              + currCalendar.get(Calendar.SECOND);
            
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
          System.out.println("Sleep failed.");
        }
      }
    }
  }
  
  private class TimeRecordSet {
    private Vector timeRecords;
    
    public TimeRecordSet() {
      timeRecords = new Vector();
    }
    
    public void add(TimeRecord rec) {
      timeRecords.add(rec);
    }
    public void setSeconds(int index, long time) {
      TimeRecord record = (TimeRecord)elementAt(index);
      record.seconds = time;
    }
    public TimeRecord elementAt(int index) throws java.lang.ArrayIndexOutOfBoundsException {
      return (TimeRecord)timeRecords.elementAt(index);
    }
    public void delete(int index) {
      timeRecords.remove(index);
    }
    public void resetTime(int index) {
      TimeRecord record = (TimeRecord)elementAt(index);
      record.seconds = 0L;
    }
    
    public int size() {
      return timeRecords.size();
    }
    public long getSeconds(int index) {
      TimeRecord record = (TimeRecord)elementAt(index);
      return record.seconds;
    }
    public String getTime(int index) {
      return parseSeconds(getSeconds(index));
    }
    public String getProjectName(int index) {
      TimeRecord record = (TimeRecord)elementAt(index);
      return record.projectName;
    }
    public String getBillableTimeString() {
      long total = 0;
      Enumeration records = timeRecords.elements();
      while (records.hasMoreElements()) {
        TimeRecord record = (TimeRecord)records.nextElement();
        if(record.billable) total += record.seconds;
      }
      return parseSeconds(total);
    }
    public String getTotalTimeString() {
      long total = 0;
      Enumeration records = timeRecords.elements();
      while (records.hasMoreElements()) {
        TimeRecord record = (TimeRecord)records.nextElement();
        total += record.seconds;
      }
      return parseSeconds(total);
    }
    
    public DefaultTableModel toTableModel(){
      DefaultTableModel model = new javax.swing.table.DefaultTableModel (
      new Object [][] {
      },
      new String [] {
        "Project", "Time"
      }
      ) {
        boolean[] canEdit = new boolean [] {
          false, false
        };
        public boolean isCellEditable (int rowIndex, int columnIndex) {
          return canEdit [columnIndex];
        }
      };
      Enumeration records = timeRecords.elements();
      while (records.hasMoreElements()) {
        TimeRecord record = (TimeRecord)records.nextElement();
        model.addRow(new Object[] {record.projectName, record.toString()});
      }
      return model;
    }

    public String parseSeconds(long seconds) {
      long minutes = seconds / 60;
      long hours = minutes / 60;
      minutes -= hours * 60;
      if (minutes < 10) return ""+hours+":0"+minutes;
      else return ""+hours+":"+minutes;
    }
  }
  
  private class TimeRecord {
    long seconds;
    String projectName;
    boolean billable;
    
    public TimeRecord() {
      projectName = "";
      seconds = 0L;
      billable = true;
    }
    public TimeRecord(String name, long time, boolean isBillable) {
      projectName = name;
      seconds = time;
      billable = isBillable;
    }

    public void setSeconds(String secs) {
      StringTokenizer timeWords = new StringTokenizer(secs, ":");
      // If time format is 1:30
      if(timeWords.countTokens() == 2) {
        seconds = Long.parseLong(timeWords.nextToken()) * 3600;
        seconds += Long.parseLong(timeWords.nextToken()) * 60;
      }
      // If time format is 90
      else if(timeWords.countTokens() == 1) {
        seconds =  Long.parseLong(timeWords.nextToken()) * 60;
      // Else we have the wrong format
      } else seconds = 0L;
    }
    
    public String toString() {
      long minutes = seconds / 60;
      long hours = minutes / 60;
      minutes -= hours * 60;
      if (minutes < 10) return ""+hours+":0"+minutes;
      else return ""+hours+":"+minutes;
    }
    public long getSeconds() {
      return seconds;
    }
  }
}
