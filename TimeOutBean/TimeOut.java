/*
 * TimeOut.java
 *
 * Created on December 5, 2002, 10:12 AM
 */

//package TimeOutBean;

import java.beans.*;
import javax.swing.*;

/**
 *
 * @author  jellis
 */
public class TimeOut extends Object implements java.io.Serializable,
                                               java.lang.Cloneable,PropertyChangeListener{
    public static final int IDLE_PAUSE = ClntComm.IDLE_PAUSE;
    public static final int IDLE_PROJECT = ClntComm.IDLE_PROJECT;
    public JFrame parentFrame = new JFrame();
    
    private int idleAction = IDLE_PAUSE;
    private int allowedIdle;
    private int idleSeconds;
    private String idleProject;
    private static boolean timeoutLibrary;
    private boolean running = false;
    private boolean paused = false;//means paused the ClntComm is paused by Timeout
    private ClntComm clntComm;
    private String savedProject;
    /** Holds value of property use. */
    private boolean use = true;
    
    /** Holds value of property seconds. */
    private int seconds = 0;
    
    /** Holds value of property changeProject. */
    private boolean changeProject;
    
    /** Holds value of property pauseTimer. */
    private boolean pauseTimer;
    
    /** Holds value of property project. */
    private String project;
    
    private native long getIdleTime();
    
    static {
        try {
            System.loadLibrary("timeout");
            timeoutLibrary = true;
            System.out.println("Loaded Timeout");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Couldn't find timeout library in "+System.getProperty("java.library.path"));
            timeoutLibrary = false;
        }
    }
    
    /** Creates new TimeOut */
    public TimeOut() {
        idleSeconds = -1;
    }
    
    
    public javax.swing.JMenuItem[] getMenuItems() {
        javax.swing.JMenuItem menuitems[] = new javax.swing.JMenuItem[1];
        
        menuitems[0] = new javax.swing.JMenuItem();
        menuitems[0].setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        menuitems[0].setText("Show Time Out Settings...");
        menuitems[0].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displaySettings();
            }
        });
        
        return menuitems;
    }
    
    public String[] getProjectNames(){
        return clntComm.getTimes().getAllProjects();
    }
    
    private void displaySettings(){ 
        String infoString = "";
        if(! isUse()){
            infoString = "You are currently not using the Time Out plugin.";
        }else{
            infoString = "You are currently using the Time Out plugin.\n\n" +
                         "   Idle time:   " + this.getSeconds() + " seconds.\n" +
                         "Idle Action:   ";
            if(this.isPauseTimer()){
                infoString = infoString + "Pause Timer\n";
            }else{
                infoString = infoString + "Switch to project " + this.getProject();
            }
        }
       
            JOptionPane.showMessageDialog(parentFrame, infoString, "Time Out Plugin Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /** Getter for property use.
     * @return Value of property use.
     */
    public boolean isUse() {
        return this.use;
    }
    
    /** Setter for property use.
     * @param use New value of property use.
     */
    public void setUse(boolean use) {
        this.use = use;
    }
    
    /** Getter for property seconds.
     * @return Value of property seconds.
     */
    public int getSeconds() {
        return this.seconds;
    }
    
    /** Setter for property seconds.
     * @param seconds New value of property seconds.
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    
    private void checkInvalidProject(){
        boolean containsProject = false;
        String[] names = getProjectNames();
        for(int i = 0; i < names.length; i++){
            if(names[i].trim().equals(getProject().trim())){
                containsProject = true;
            }
        }
        
        if(! containsProject && isChangeProject()){
            this.setPauseTimer(true);
            this.setChangeProject(false);
        }
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        if(clntComm == null){
            clntComm = (ClntComm)propertyChangeEvent.getSource();
        }
     
        running = clntComm.isRunning();
        
        if(use && timeoutLibrary){
            idleSeconds = (int)(getIdleTime()/1000L);
            if(isChangeProject()){
                checkInvalidProject();
            }
        }
       
        
        if(use && idleSeconds > seconds && running && !paused) {
            if(isPauseTimer()){
                clntComm.toggleTimer();
            }else{
                System.out.println("getIndex: " + clntComm.getSelectedIndex());
                savedProject = 
                  clntComm.getTimes().elementAt(clntComm.getSelectedIndex()).getProjectName();
                System.out.println("setIndex1: " + clntComm.getTimes().indexOfProject(this.getProject()));
                clntComm.setSelectedIndex(clntComm.getTimes().indexOfProject(this.getProject()));
            }
            paused = true;
        }
        
        if(use && idleSeconds < seconds && (! running || isChangeProject()) && paused){
            if(isPauseTimer()){
                clntComm.toggleTimer();
            }else{
                System.out.println("setIndex: " + clntComm.getTimes().indexOfProject(this.getProject()));
                clntComm.setSelectedIndex(clntComm.getTimes().indexOfProject(this.savedProject));
            }
            paused = false;
        }
    }
    
    /** Getter for property changeProject.
     * @return Value of property changeProject.
     */
    public boolean isChangeProject() {
        return this.changeProject;
    }
    
    /** Setter for property changeProject.
     * @param changeProject New value of property changeProject.
     */
    public void setChangeProject(boolean changeProject) {
        this.changeProject = changeProject;
    }
    
    /** Getter for property pauseTimer.
     * @return Value of property pauseTimer.
     */
    public boolean isPauseTimer() {
        return this.pauseTimer;
    }
    
    /** Setter for property pauseTimer.
     * @param pauseTimer New value of property pauseTimer.
     */
    public void setPauseTimer(boolean pauseTimer) {
        this.pauseTimer = pauseTimer;
    }
    
    /** Getter for property project.
     * @return Value of property project.
     */
    public String getProject() {
        return this.project;
    }
    
    /** Setter for property project.
     * @param project New value of property project.
     */
    public void setProject(String project) {
        this.project = project;
    }
    
}
