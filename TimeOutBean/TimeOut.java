import javax.swing.*;
import java.io.*;

public class TimeOut extends CsltCommPlugin {
    public static final int IDLE_PAUSE = 5;
    public static final int IDLE_PROJECT = 6;
    
    private static boolean timeoutLibrary;
    private boolean paused = false; //means paused the ClntComm is paused by Timeout
    private TimeRecord savedProject;
    private ClntComm clntComm;
    
    private int idleAction = IDLE_PAUSE;
    private boolean use = true;
    private int seconds = 0;
    private TimeRecord project;
    
    private native long getIdleTime();
    
    static {
        if(JarLoader.loadNativeLibrary("libtimeout.so", TimeOut.class) && JarLoader.loadNativeLibrary("timeout.dll", TimeOut.class)) {
            try {
                System.loadLibrary("timeout");
                timeoutLibrary = true;
            } catch (UnsatisfiedLinkError e) {
                System.err.println("Couldn't find timeout library in "+System.getProperty("java.library.path"));
                timeoutLibrary = false;
            }
        }
    }
    
    public TimeOut() {
    }
    
    public String[] getProjectNames(){ return clntComm.getTimes().getAllProjects(); }
    public boolean isUse() { return this.use; }
    public boolean getUse() { return this.use; }
    public void setUse(boolean use) { this.use = use; }
    public int getIdleAction() { return this.idleAction; }
    public void setIdleAction(int action) { this.idleAction = action; }
    public int getSeconds() { return this.seconds; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
    public TimeRecord getProject() { return this.project; }
    public void setProject(TimeRecord project) { this.project = project; }
    public void setProject(int project) { this.project = clntComm.getTimes().elementAt(project); }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        clntComm = (ClntComm)propertyChangeEvent.getSource();
        boolean running = clntComm.isRunning();
        
        if(use && timeoutLibrary){
            int idleSeconds = (int)(getIdleTime()/1000L);
            
            if(idleSeconds > seconds && running && ! paused) {
                if(this.idleAction == IDLE_PROJECT) {
                    int selectedIndex = clntComm.getSelectedIndex();
                    
                    if(selectedIndex >= 0) {
                      savedProject = clntComm.getTimes().elementAt(selectedIndex);
                      clntComm.setSelectedIndex(clntComm.getTimes().indexOfProject(this.project));
                    } else {
                      savedProject = null;
                    }
                } else {
                    clntComm.toggleTimer();
                }
                paused = true;
            }
            
            if(idleSeconds < seconds && (! running || this.idleAction == IDLE_PROJECT) && paused){
                if(savedProject != null && this.idleAction == IDLE_PROJECT) {
                    clntComm.setSelectedIndex(clntComm.getTimes().indexOfProject(this.savedProject));
                } else {
                    clntComm.toggleTimer();
                }
                paused = false;
            }
        }
    }
}
