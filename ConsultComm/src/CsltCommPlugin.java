/*
 * CsltCommPlugin.java
 *
 * Created on July 31, 2004, 8:40 PM
 */

/**
 *
 * @author  jellis
 */
public abstract class CsltCommPlugin extends Object implements java.io.Serializable, java.lang.Cloneable, java.beans.PropertyChangeListener {
    public abstract void propertyChange(java.beans.PropertyChangeEvent evt);
    
    public javax.swing.JMenuItem[] getMenuItems() { return null; }
    public void unregister() { }
}