/*
 * CsltCommPlugin.java
 *
 * Created on July 31, 2004, 8:40 PM
 */

/**
 * Interface for building ConsultComm plugins. Plugins may or may not
 * offer methods to add to the tools menu or to the main project list
 * pop-up menu. They must look for property changes and be serializeable
 * (it's how preferences are usually stored and the plugin's state is retained)
 */
public abstract class CsltCommPlugin extends Object implements java.io.Serializable, java.lang.Cloneable, java.beans.PropertyChangeListener {
    public abstract void propertyChange(java.beans.PropertyChangeEvent evt);
    
    public javax.swing.JMenuItem[] getMenuItems() { return null; }
    public javax.swing.JMenuItem[] getPopupMenuItems() { return null; }
}
