import java.beans.*;
import javax.swing.*;
import java.io.*;

public class TimeOut extends Object implements java.io.Serializable, java.lang.Cloneable, PropertyChangeListener{
    public static final int IDLE_PAUSE = 5;
    public static final int IDLE_PROJECT = 6;

    private static boolean timeoutLibrary;
    private boolean paused = false; //means paused the ClntComm is paused by Timeout
    private String savedProject;
    private ClntComm clntComm;
    
    private int idleAction = IDLE_PAUSE;
    private boolean use = true;
    private int seconds = 0;
    private String project;
    
    private native long getIdleTime();
    
    /** Creates new TimeOut */
    public TimeOut() {
        if(! timeoutLibrary) { //Load twice, cause big JVM CRASH!
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
    }
    
    public String[] getProjectNames(){ return clntComm.getTimes().getAllProjects(); }
    public boolean isUse() { return this.use; }
    public void setUse(boolean use) { this.use = use; }
    public int getIdleAction() { return this.idleAction; }
    public void setIdleAction(int action) { this.idleAction = action; }
    public int getSeconds() { return this.seconds; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
    public String getProject() { return this.project; }
    public void setProject(String project) { this.project = project; }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        clntComm = (ClntComm)propertyChangeEvent.getSource();
        boolean running = clntComm.isRunning();
        
        if(use && timeoutLibrary){
            int idleSeconds = (int)(getIdleTime()/1000L);
            
            if(idleSeconds > seconds && running && ! paused) {
                if(this.idleAction == IDLE_PROJECT) {
                    System.out.println("getIndex: " + clntComm.getSelectedIndex());
                    savedProject = clntComm.getTimes().elementAt(clntComm.getSelectedIndex()).getProjectName();
                    System.out.println("setIndex1: " + clntComm.getTimes().indexOfProject(this.project));
                    clntComm.setSelectedIndex(clntComm.getTimes().indexOfProject(this.project));
                } else {
                    clntComm.toggleTimer();
                }
                paused = true;
            }
            
            if(idleSeconds < seconds && (! running || this.idleAction == IDLE_PROJECT) && paused){
                if(this.idleAction == IDLE_PROJECT) {
                    System.out.println("setIndex: " + clntComm.getTimes().indexOfProject(this.getProject()));
                    clntComm.setSelectedIndex(clntComm.getTimes().indexOfProject(this.savedProject));
                } else {
                    clntComm.toggleTimer();
                }
                paused = false;
            }
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
