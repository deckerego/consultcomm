import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/**
 * Model that feeds the tree elements. This is referenced by the
 * tree renderers
 */
public class TableTreeModel implements TreeModel {
    static private String[]  cNames = {"Name", "Times"};
    static private Class[]  cTypes = { TableTreeModel.class, String.class };

    private EventListenerList listenerList = new EventListenerList();
    private Object root;
    private int timeFormat;
    private boolean groupTime;
    
    public TableTreeModel(TimeRecordSet times, int timeFormat) {
        this.root = times;
        this.timeFormat = timeFormat;
    }
    
    public Object getRoot() { return this.root; }
    public boolean isLeaf(Object node) { return node.getClass().equals(TimeRecord.class); }
    public int getColumnCount() { return this.cNames.length; }
    public String getColumnName(int column) { return this.cNames[column]; }
    public Class getColumnClass(int column) { return this.cTypes[column]; }
    public boolean getGroupTime() { return this.groupTime; }
    public void setGroupTime(boolean groupTime) { this.groupTime = groupTime; }
    
    public int getChildCount(Object node) {
        if(node.getClass().equals(TimeRecordSet.class))
            return ((TimeRecordSet)node).getGroups().size();
        else
            return ((TimeRecordSet)root).getGroupRecords((String)node).size();
    }
    
    public Object getChild(Object node, int i) {
        if(node.getClass().equals(TimeRecordSet.class))
            return ((TimeRecordSet)node).getGroups().elementAt(i);
        else
            return ((TimeRecordSet)root).getGroupRecords((String)node).elementAt(i);
    }
    
    public Object getValueAt(Object node, int column) {
        if(node.getClass().equals(TimeRecordSet.class)) {
            return null;

        } else if(node.getClass().equals(TimeRecord.class)) {
            TimeRecord record = (TimeRecord)node;
            switch(column) {
                case 0:
                    return record.getProjectName();
                case 1:
                    return timeFormat == ClntComm.SECONDS ? ClntComm.toSecondString(record.getSeconds()) : ClntComm.toMinuteString(record.getSeconds());
            }

        } else if(node.getClass().equals(String.class)) {
            switch(column) {
                case 1:
                    if(groupTime) {
                        String groupName = (String)node;
                        TimeRecordSet times = (TimeRecordSet)root;
                        long totalSeconds = times.getGroupTotalTime(groupName);
                        return timeFormat == ClntComm.SECONDS ? ClntComm.toSecondString(totalSeconds) : ClntComm.toMinuteString(totalSeconds);
                    } else {
                        return null;
                    }
                default:
                    return (String)node;
            }
        }            

        return node.getClass().getName();
    }

    public void setValueAt(Object aValue, Object node, int column) {
        if(node.getClass().equals(TimeRecord.class)) {
            TimeRecord record = (TimeRecord)node;
            TimeRecord value = (TimeRecord)aValue;
            switch(column) {
                case 0:
                    record.setProjectName(value.getProjectName());
                    break;
                case 1:
                    record.setSeconds(value.getSeconds());
                    break;
            }
        }
    }    

    public int getIndexOfChild(Object parent, Object child) {
        for (int i=0, max=getChildCount(parent); i < max; i++) {
            if (getChild(parent, i).equals(child)) {
                return i;
            }
        }
        return -1;
    }
    
    public void valueForPathChanged(TreePath path, Object newValue) {}
    public void addTreeModelListener(TreeModelListener l) { listenerList.add(TreeModelListener.class, l); }
    public void removeTreeModelListener(TreeModelListener l) { listenerList.remove(TreeModelListener.class, l); }
    public boolean isCellEditable(Object node, int column) { return getColumnClass(column) == TableTreeModel.class; }
}
