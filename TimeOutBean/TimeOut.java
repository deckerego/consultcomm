/*
 * TimeOut.java
 *
 * Created on December 5, 2002, 10:12 AM
 */

//package TimeOutBean;

import java.beans.*;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author  jellis
 */
public class TimeOut extends Object implements java.io.Serializable, java.lang.Cloneable, PropertyChangeListener{
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
    
    private boolean use = true;
    private int seconds = 0;
    private boolean changeProject;
    private boolean pauseTimer;
    private String project;
    
    private native long getIdleTime();
    
    /** Creates new TimeOut */
    public TimeOut() {
        idleSeconds = -1;
        getNativeLibrary("X/libtimeout.so");
        getNativeLibrary("Win32/timeout.dll");
        try {
            System.loadLibrary("timeout");
            timeoutLibrary = true;
            System.out.println("Loaded Timeout");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Couldn't find timeout library in "+System.getProperty("java.library.path"));
            timeoutLibrary = false;
        }
    }
    
    public String[] getProjectNames(){ return clntComm.getTimes().getAllProjects(); }
    public boolean isUse() { return this.use; }
    public void setUse(boolean use) { this.use = use; }
    public int getSeconds() { return this.seconds; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
    public boolean isChangeProject() { return this.changeProject; }
    public void setChangeProject(boolean changeProject) { this.changeProject = changeProject; }
    public boolean isPauseTimer() { return this.pauseTimer; }
    public void setPauseTimer(boolean pauseTimer) { this.pauseTimer = pauseTimer; }
    public String getProject() { return this.project; }
    public void setProject(String project) { this.project = project; }
    
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
        if(clntComm == null) clntComm = (ClntComm)propertyChangeEvent.getSource();
        
        running = clntComm.isRunning();
        
        if(use && timeoutLibrary){
            idleSeconds = (int)(getIdleTime()/1000L);
            System.out.println("Idle: "+getIdleTime());
            if(isChangeProject()) checkInvalidProject();
        }
        
        
        if(use && idleSeconds > seconds && running && !paused) {
            if(isPauseTimer()) {
                clntComm.toggleTimer();
            } else {
                System.out.println("getIndex: " + clntComm.getSelectedIndex());
                savedProject = clntComm.getTimes().elementAt(clntComm.getSelectedIndex()).getProjectName();
                System.out.println("setIndex1: " + clntComm.getTimes().indexOfProject(this.getProject()));
                clntComm.setSelectedIndex(clntComm.getTimes().indexOfProject(this.getProject()));
            }
            paused = true;
        }
        
        if(use && idleSeconds < seconds && (! running || isChangeProject()) && paused){
            if(isPauseTimer()) {
                clntComm.toggleTimer();
            } else {
                System.out.println("setIndex: " + clntComm.getTimes().indexOfProject(this.getProject()));
                clntComm.setSelectedIndex(clntComm.getTimes().indexOfProject(this.savedProject));
            }
            paused = false;
        }
    }
    
    /**
     * Translate a file from a bytestream in the JAR file
     * @param path The relative path to the file stored in a Java Archive
     */
    private void getNativeLibrary(String path) {
        try{
            String fileName = path.substring(path.lastIndexOf('/'));
            File file = new File(PluginManager.libsdir, fileName);
            file.deleteOnExit();
            InputStream in = new BufferedInputStream(this.getClass().getResourceAsStream(path));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[4096];
            while(true) {
                int nBytes = in.read(buffer);
                if (nBytes <= 0) break;
                out.write(buffer, 0, nBytes);
            }
            out.flush();
            out.close();
            in.close();
        } catch(Exception e){
            System.out.println("Error loading file "+path+": "+e);
        }
    }
}
