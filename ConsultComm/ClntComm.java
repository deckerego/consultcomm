import java.util.*;
import java.text.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/*
 * TimeTrack.java
 * @version 0.1
 * @author John T. Ellis
 * Created on May 19, 2000, 7:37 PM
 */
public class ClntComm extends javax.swing.JPanel {
public static String[] projects, times, notes; 
public static long[] secondsList;
static long totalSeconds, billableSeconds;
static BitSet billableFlags = new BitSet();
static JFrame frame = new JFrame("Consultant Manager");
int fontWidth;
int index;
String project, time;
TimerThread timer;
JTextField projField, timeField;
JCheckBox billable;
JFrame editFrame;

  /** Creates new form TimeTrack */
  public ClntComm() {
    int width=0;    
    //Try to get preferences file
    File prefs = new File("ClntComm.def");
    if (!prefs.exists()) createPrefs();
    readPrefs(width);

    initComponents ();

    //Start the proverbial stopwatch.
    timer = new TimerThread(1000, this);
    timer.start();
  }


  /**
   * Update the total time elapsed
   */
  public void refreshTotalTime(){
    int numItems = secondsList.length;
    billableSeconds = 0;
    totalSeconds = 0;
    for(int i=0; i < numItems; i++){
      totalSeconds += secondsList[i];
      if(billableFlags.get(i)){
        billableSeconds += secondsList[i];
      }
    }
    if(totalText.getText() == "Total:"){
      totalTime.setText(""+parseSeconds(totalSeconds));
    } else {
      totalTime.setText(""+parseSeconds(billableSeconds));
    }
    totalTime.repaint();
  }

  /**
  * Read through preferances file
  * @parm wide - Variable to store the width of the project/timer columns
  * @parm high - Variable to store the height of the project/timer columns 
  */
  public void readPrefs(int wide) {
    StringTokenizer parameters; 
    String str, parm;
    
    try {      
      BufferedReader in = new BufferedReader(new FileReader("ClntComm.def")); 
      while((str = in.readLine()) != null){
        parameters = new StringTokenizer(str);
        parm = parameters.nextToken();
        if (parm.equals("Projects")){
          String[] temp = new String[parameters.countTokens()];
          for(int i = 0; parameters.hasMoreTokens(); i++)
          //Replace all '$' characters with spaces
          temp[i] = parameters.nextToken().replace('$', ' ');
          projects = temp;
        }
        else if (parm.equals("Times")){
          String[] temp1 = new String[parameters.countTokens()];
          long[] temp2 = new long[parameters.countTokens()];
          for(int i = 0; parameters.hasMoreTokens(); i++){
            temp1[i] = parameters.nextToken();
            if(temp1[i].equals("0"))
            temp1[i] = "0:00";
            temp2[i] = parseTimeString(temp1[i]);
            totalSeconds += temp2[i];
          }
          times = temp1;
          secondsList = temp2;
        }					
        else if (parm.equals("Billable")){
          for(int i = 0; parameters.hasMoreTokens(); i++) {
            String bitIndex = parameters.nextToken();
            if(! bitIndex.equals("{}")){
              if(bitIndex.charAt(0) == '{'){
                int index = Integer.parseInt(bitIndex.substring(1, bitIndex.length() - 1));
                billableFlags.set(index);
              } else {
                int index = Integer.parseInt(bitIndex.substring(0, bitIndex.length() - 1));
                billableFlags.set(index);
              }
            }
          }
        }
      }
      in.close();
    } catch (IOException e) {
      System.out.println("Error reading prefs file.");
    }
  }

 /**
  * Create a new preferances file with default values
  */
  public void createPrefs() {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter("ClntComm.def"));
      out.close();
    } catch (IOException e) {
      System.out.println("Error writing to prefs file.");
    }
  }

  public static void savePrefs() {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter("ClntComm.def"));
      if((projects != null) && (times != null)){
        out.write("Projects");
        for(int i = 0; i < projects.length; i++)
        //Replace spaces with some weird character so the tokenizer
        //doesn't get messed up.
        out.write(" " + projects[i].replace(' ', '$'));
        out.write("\n");
        out.write("Times");
        for(int i = 0; i < times.length; i++)
        out.write(" " + times[i]);
        out.write("\n");
        out.write("Billable ");
        out.write(billableFlags.toString());
        out.write("\n");
      }
      out.close();
    } catch (IOException e) {
      System.out.println("Error writing to prefs file.");
    }
  }

  public long parseTimeString(String time){
    StringTokenizer timeWords = new StringTokenizer(time, ":");
    long seconds;
    //If time format is 1:30
    if (timeWords.countTokens() == 2){
      seconds = Long.parseLong(timeWords.nextToken()) * 3600;
      return seconds += Long.parseLong(timeWords.nextToken()) * 60;
    }
    //If time format is simply 90
    else if (timeWords.countTokens() == 1)
    return seconds = Long.parseLong(timeWords.nextToken()) * 60;
    //Else we have the wrong format
    else
    return 0;
  }

  public String parseSeconds(long seconds){
    long minutes = seconds / 60;
    long hours = minutes / 60;
    minutes -= hours * 60;
    if (minutes < 10)
      return "" + hours + ":0" + minutes;
    else
      return "" + hours + ":" + minutes;
  }

  public boolean isRunning(){
    return timer.clockRunning;
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents () {//GEN-BEGIN:initComponents
    toolBar = new javax.swing.JToolBar ();
    start_button = new javax.swing.JButton ();
    edit_button = new javax.swing.JButton ();
    new_button = new javax.swing.JButton ();
    delete_button = new javax.swing.JButton ();
    zero_button = new javax.swing.JButton ();
    trackerPane = new javax.swing.JScrollPane ();
    timerPanel = new javax.swing.JPanel ();
    if(projects != null){
      projectList = new JList(projects);
    } else {
      projectList = new JList();
    }
    if(times != null){
      timeList = new JList(times);
    } else {
      timeList = new JList();
    }
    totalPanel = new javax.swing.JPanel ();
    totalText = new javax.swing.JLabel ();
    totalTime = new javax.swing.JLabel ();
    setLayout (new java.awt.BorderLayout ());


      start_button.setToolTipText ("Start Timer");
      start_button.setText ("G");
      start_button.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          toggleTimer (evt);
        }
      }
      );
  
      toolBar.add (start_button);
  
      edit_button.setToolTipText ("Edit Project");
      edit_button.setText ("E");
      edit_button.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          editWindow (evt);
        }
      }
      );
  
      toolBar.add (edit_button);
  
      new_button.setToolTipText ("New Project");
      new_button.setText ("N");
      new_button.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          newProj (evt);
        }
      }
      );
  
      toolBar.add (new_button);
  
      delete_button.setToolTipText ("Delete Project");
      delete_button.setText ("D");
      delete_button.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          delProj (evt);
        }
      }
      );
  
      toolBar.add (delete_button);
  
      zero_button.setToolTipText ("Zero-Out Project");
      zero_button.setText ("Z");
      zero_button.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          zeroProj (evt);
        }
      }
      );
  
      toolBar.add (zero_button);
  

    add (toolBar, java.awt.BorderLayout.NORTH);


      timerPanel.setLayout (new java.awt.GridLayout (1, 2));
      timerPanel.setMinimumSize (new java.awt.Dimension(0, 0));
  
        projectList.setSelectionBackground (new java.awt.Color (170, 170, 255));
        projectList.setSelectionMode (javax.swing.ListSelectionModel.SINGLE_SELECTION);
        projectList.setFixedCellWidth (0);
        projectList.addListSelectionListener (new javax.swing.event.ListSelectionListener () {
          public void valueChanged (javax.swing.event.ListSelectionEvent evt) {
            setTimer (evt);
          }
        }
        );
    
        timerPanel.add (projectList);
    
        timeList.setSelectionBackground (new java.awt.Color (170, 170, 255));
        timeList.setSelectionMode (javax.swing.ListSelectionModel.SINGLE_SELECTION);
        timeList.addListSelectionListener (new javax.swing.event.ListSelectionListener () {
          public void valueChanged (javax.swing.event.ListSelectionEvent evt) {
            setTimer (evt);
          }
        }
        );
    
        timerPanel.add (timeList);
    
      trackerPane.setViewportView (timerPanel);
  

    add (trackerPane, java.awt.BorderLayout.CENTER);

    totalPanel.setLayout (new java.awt.GridLayout (1, 2));

      totalText.setText ("Total:");
      totalText.setForeground (java.awt.Color.black);
      totalText.addMouseListener (new java.awt.event.MouseAdapter () {
        public void mouseClicked (java.awt.event.MouseEvent evt) {
          toggleTotals (evt);
        }
      }
      );
  
      totalPanel.add (totalText);
  
      totalTime.setText (""+parseSeconds(totalSeconds));
      totalTime.setForeground (java.awt.Color.black);
      totalTime.addMouseListener (new java.awt.event.MouseAdapter () {
        public void mouseClicked (java.awt.event.MouseEvent evt) {
          toggleTotals (evt);
        }
      }
      );
  
      totalPanel.add (totalTime);
  

    add (totalPanel, java.awt.BorderLayout.SOUTH);

  }//GEN-END:initComponents

private void toggleTotals (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toggleTotals
  if(totalText.getText() == "Total:"){
    totalText.setText("Billable:");
  } else {
    totalText.setText("Total:");
  }
  refreshTotalTime();
  }//GEN-LAST:event_toggleTotals

private void setTimer (javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_setTimer
  setTimer((JList)evt.getSource());
  }//GEN-LAST:event_setTimer

  private void setTimer (JList theList) {
  if(theList.getSelectedIndex() >= 0){
      Calendar newTime = Calendar.getInstance();
      timeList.setSelectedIndex(theList.getSelectedIndex());
      projectList.setSelectedIndex(theList.getSelectedIndex());
      timer.startTime = (newTime.get(Calendar.HOUR_OF_DAY)*3600)
      + (newTime.get(Calendar.MINUTE)*60)
      + newTime.get(Calendar.SECOND)
      - secondsList[theList.getSelectedIndex()];
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
    index = projectList.getSelectedIndex();
    times[index] = "0:00";
    secondsList[index] = 0;
    if (index == projectList.getSelectedIndex()){
      timer.startTime = (newTime.get(Calendar.HOUR_OF_DAY)*3600)
      + (newTime.get(Calendar.MINUTE)*60)
      + newTime.get(Calendar.SECOND);
    }
    timeList.repaint();
    refreshTotalTime();
  }
  }//GEN-LAST:event_zeroProj

private void delProj (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delProj
  int len = 0;
  int selectedIndex = projectList.getSelectedIndex();

  if(selectedIndex > -1){
    //Warn the user
    Object[] options = {"OK", "Cancel"};
    int dialog = JOptionPane.showOptionDialog(frame, 
    "Project will be deleted. Continue?", 
    "Delete Project", JOptionPane.YES_NO_OPTION, 
    JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    if(dialog == 0){
      if(projects != null) len = projects.length;
      String[] newProjects = new String[len - 1];
      System.arraycopy(projects, 0, newProjects, 0, selectedIndex);
      if(len > selectedIndex + 1)
        System.arraycopy(projects, selectedIndex + 1, newProjects, selectedIndex, len - (selectedIndex + 1));
      projects = newProjects;
      projectList.setListData(projects);

      if(times != null) len = times.length;
      String[] newTimes = new String[len - 1];
      System.arraycopy(times, 0, newTimes, 0, selectedIndex);
      if(len > selectedIndex + 1)
        System.arraycopy(times, selectedIndex + 1, newTimes, selectedIndex, len - (selectedIndex + 1));
      times = newTimes;
      timeList.setListData(times);

      if(secondsList != null) len = secondsList.length;
      long[] newSecondsList = new long[len - 1];
      System.arraycopy(secondsList, 0, newSecondsList, 0, selectedIndex);
      if(len > selectedIndex + 1)
        System.arraycopy(secondsList, selectedIndex + 1, newSecondsList, selectedIndex, len - (selectedIndex + 1));
      secondsList = newSecondsList;

      projectList.repaint();
      timeList.repaint();
      refreshTotalTime();

      //Now the painful process of changing the bitset... ugh...
      for(int i=selectedIndex; i < len; i++){
        if(billableFlags.get(i+1)){
          billableFlags.set(i);
        } else {
          billableFlags.clear(i);
        }
      }
    }
  }
  }//GEN-LAST:event_delProj

private void newProj (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProj
  int len = 0;
  //Save the current selection index, since we lose it once
  //we do a setListData.
  int selectedIndex = projectList.getSelectedIndex();

  if(projects != null) len = projects.length;
  String[] newProjects = new String[len + 1];
  if(projects != null) System.arraycopy(projects, 0, newProjects, 0, len);
  projects = newProjects;
  projects[len] = "Project";
  projectList.setListData(projects);
  projectList.setSelectedIndex(selectedIndex);

  if(times != null) len = times.length;
  String[] newTimes = new String[len + 1];
  if(times != null) System.arraycopy(times, 0, newTimes, 0, len);
  times = newTimes;
  times[len] = "0"; 
  timeList.setListData(times);
  timeList.setSelectedIndex(selectedIndex);

  if(secondsList != null) len = secondsList.length;
  long[] newSecondsList = new long[len + 1];
  if(secondsList != null) 
    System.arraycopy(secondsList, 0, newSecondsList, 0, len);
  secondsList = newSecondsList;
  secondsList[len] = 0;

  billableFlags.set(len);

  editWindow(len);
  }//GEN-LAST:event_newProj

private void editWindow (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editWindow
  editWindow(projectList.getSelectedIndex());
  }//GEN-LAST:event_editWindow

/**
 * Create edit project window.
 * @parm Index into the project list
 */
public void editWindow(int i){
  index = i;
  //Set up the edit project window.
  projField = new JTextField(projects[index]);
  timeField = new JTextField(times[index]);
  editFrame = new JFrame("Revise Client");

  JPanel editPanel = new JPanel();
  editPanel.setLayout(new GridLayout(3, 1));
  editPanel.add(projField);
  editPanel.add(timeField);
  
  //Is project billable or not (otherwise, will it count in the total
  //minutes or not?)
  if(billableFlags.get(index)){
    billable = new JCheckBox("Billable Project", true);
  } else {
    billable = new JCheckBox("Billable Project", false);
  }
  editPanel.add(billable);

  //Set up buttons
  JButton okay = new JButton("OK");
  okay.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent e) {
      Calendar newTime = Calendar.getInstance();
      projects[index] = projField.getText();
      times[index] = timeField.getText(); 
      secondsList[index] = parseTimeString(timeField.getText()); 
      if (index == projectList.getSelectedIndex()){
        timer.startTime = (newTime.get(Calendar.HOUR_OF_DAY)*3600)
        + (newTime.get(Calendar.MINUTE)*60)
        + newTime.get(Calendar.SECOND)
        - secondsList[index];
      }
      if (billable.isSelected()){
        billableFlags.set(index);
      } else {
        billableFlags.clear(index);
      }
      projectList.repaint();
      timeList.repaint();
      refreshTotalTime();
      editFrame.dispose();
    } 
  }); 
  JButton cancel = new JButton("Cancel");
  cancel.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent e) { 
      editFrame.dispose();
    } 
  }); 
  JPanel buttonPanel = new JPanel();
  buttonPanel.setLayout(new GridLayout(1, 2));
  buttonPanel.add(okay);
  buttonPanel.add(cancel);

  //Create edit window
  editFrame.getContentPane().add(editPanel, BorderLayout.CENTER);
  editFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
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

  public static void main(String args[]){
    frame.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        savePrefs();
        System.exit (0);
      }
    });

    frame.getContentPane().add(new ClntComm());
    frame.pack();
    frame.setVisible(true);
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JToolBar toolBar;
  private javax.swing.JButton start_button;
  private javax.swing.JButton edit_button;
  private javax.swing.JButton new_button;
  private javax.swing.JButton delete_button;
  private javax.swing.JButton zero_button;
  private javax.swing.JScrollPane trackerPane;
  private javax.swing.JPanel timerPanel;
  private javax.swing.JList projectList;
  private javax.swing.JList timeList;
  private javax.swing.JPanel totalPanel;
  private javax.swing.JLabel totalText;
  private javax.swing.JLabel totalTime;
  // End of variables declaration//GEN-END:variables

/**
 * TimerThread updates system variables upon each tick of the clock.
 */
class TimerThread extends Thread{
public boolean runThread, clockRunning;
public int updateSeconds;
public long startTime;
ClntComm mainObj;

/**
 * Create a new timer.
 * @parm How often to update (in milliseconds)
 * @parm The Consultant Manager screen to update
 */
public TimerThread(int update, ClntComm main) {
	updateSeconds = update;
	mainObj = main;
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

			if((index = mainObj.timeList.getSelectedIndex()) >= 0){
				currSeconds = currTime - startTime;
				mainObj.secondsList[index] = currSeconds;
				//Only repaint if the minutes have changed.
				if (currSeconds % 60 == 0){
					mainObj.times[index] = parseTimerSeconds(currSeconds);
					mainObj.refreshTotalTime();
					mainObj.timeList.repaint();
					mainObj.savePrefs();
				}
			}
		}
		} catch (InterruptedException e) {
		System.out.println("Sleep failed.");
		}
	}
}
public String parseTimerSeconds(long seconds){
	long minutes = seconds / 60;
	long hours = minutes / 60;
	minutes -= hours * 60;
	if (minutes < 10)
		return "" + hours + ":0" + minutes;
	else
		return "" + hours + ":" + minutes;
}
}
}