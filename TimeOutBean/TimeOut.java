/*
 * TimeOut.java
 *
 * Created on December 5, 2002, 10:12 AM
 */

package TimeOutBean;

import java.beans.*;

/**
 *
 * @author  jellis
 */
public class TimeOut extends Object implements java.io.Serializable, CsltCommListener {
    public static final int IDLE_PAUSE = ClntComm.IDLE_PAUSE;
    public static final int IDLE_PROJECT = ClntComm.IDLE_PROJECT;
    
    private int idleAction = IDLE_PAUSE;
    private int allowedIdle;
    private int idleSeconds;
    private String idleProject;
    private boolean timeoutLibrary;
    private boolean asleep;
    
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
    
    /** Creates new TimeOut */
    public TimeOut() {
        idleSeconds = -1;
    }
    
    public void clockTick(CsltCommEvent evt) {
        ClntComm parent = (ClntComm)evt.getObject();
        
        if(allowedIdle > 0 && timeoutLibrary) //0 means don't worry about idle
            idleSeconds = (int)(getIdleTime()/1000L);
        
        //Check and see if we're supposed to wake up the clock
        //after the session has been idle
        if(allowedIdle > 0 && idleSeconds < allowedIdle && asleep) {
            parent.toggleIdle();
            asleep = false;
        }

        //Check and see if we're supposed to do something when the
        //user session is idle
        if(allowedIdle > 0 && idleSeconds >= allowedIdle && ! asleep) {
            parent.toggleIdle();
            asleep = true;
        }
    }
}
