import java.util.*;
import javax.swing.table.*;

public class TimeRecordSet implements java.lang.Cloneable, java.io.Serializable {
    private Vector timeRecords;
    private boolean reverseSort = false;
    private int currColumnSorted = 0;
    
    public TimeRecordSet() {
            timeRecords = new Vector();
    }
    
    public void setTimeRecords(Vector records) { this.timeRecords = records; }
    public Vector getTimeRecords() { return this.timeRecords; }
    public void setReverseSort(boolean sort) { this.reverseSort = sort; }
    public boolean getReverseSort() { return this.reverseSort; }
    public void setCurrColumnSorted(int column) { this.currColumnSorted = column; }
    public int getCurrColumnSorted() { return this.currColumnSorted; }
    
    public void add(TimeRecord rec) {
        timeRecords.add(rec);
    }
    public void delete(int index) {
        timeRecords.remove(index);
    }
    public void resetTime(int index) {
        setSeconds(index, 0L);
    }
    public void resetTime() {
        for(int i=0; i<size(); i++) resetTime(i);
    }
    public void setSeconds(int index, long time) {
        TimeRecord record = elementAt(index);
        record.setSeconds(time);
    }
    public long getSeconds(int index) {
        TimeRecord record = elementAt(index);
        return record.getSeconds();
    }

    public long getBillableTime() {
        long total = 0;
        Enumeration records = timeRecords.elements();
        while (records.hasMoreElements()) {
            TimeRecord record = (TimeRecord)records.nextElement();
            if(record.isBillable()) total += record.getSeconds();
        }
        return total;
    }
    
    public long getTotalTime() {
        long total = 0;
        Enumeration records = timeRecords.elements();
        while (records.hasMoreElements()) {
            TimeRecord record = (TimeRecord)records.nextElement();
            total += record.getSeconds();
        }
        return total;
    }
    
    public String[] getAllProjects() {
        String[] names = new String[timeRecords.size()];
        Enumeration records = timeRecords.elements();
        for(int i=0; records.hasMoreElements(); i++) {
            TimeRecord record = (TimeRecord)records.nextElement();
            names[i] = record.toString();
        }
        return names;
    }
    
    public Vector getGroupRecords(String groupName) {
        Vector records = new Vector();
        for(int i=0; i<timeRecords.size(); i++) {
            TimeRecord record = (TimeRecord)timeRecords.elementAt(i);
            if(record.getGroupName().equals(groupName))
                records.addElement(record);
        }
        return records;
    }

    public Vector getGroups() {
        Vector records = new Vector();
        String lastGroupName = null;
        
        for(int i=0; i<timeRecords.size(); i++) {
            TimeRecord record = (TimeRecord)timeRecords.elementAt(i);
            if(lastGroupName == null || ! lastGroupName.equals(record.getGroupName())) {
                lastGroupName = record.getGroupName();
                records.addElement(lastGroupName);
            }
        }
        return records;
    }
    
    public TimeRecord elementAt(int index) throws java.lang.ArrayIndexOutOfBoundsException {
        return (TimeRecord)timeRecords.elementAt(index);
    }
    public int size() {
        return timeRecords.size();
    }
    public int indexOf(TimeRecord rec) {
        return timeRecords.indexOf(rec);
    }
    public int indexOfProject(TimeRecord project) {
        Enumeration records = timeRecords.elements();
        for(int i=0; records.hasMoreElements(); i++) {
            TimeRecord record = (TimeRecord)records.nextElement();
            if(project.getProjectName().equals(record.getProjectName()) && 
            project.getGroupName().equals(record.getGroupName()))
                return i;
        }
        return -1;
    }
    
    public void sort() {
        if(reverseSort)
            if(currColumnSorted == 0) //Project column
                Collections.sort(timeRecords, new ProjectReverseComparator());
            else
                Collections.sort(timeRecords, new TimeReverseComparator());
        else
            if(currColumnSorted == 0) //Project column
                Collections.sort(timeRecords, new ProjectComparator());
            else
                Collections.sort(timeRecords, new TimeComparator());
    }
    
    public void sort(int column) {
        try {
            if((currColumnSorted != column) || reverseSort) {
                if(currColumnSorted == 0) //Project column
                    Collections.sort(timeRecords, new ProjectComparator());
                else
                    Collections.sort(timeRecords, new TimeComparator());
                reverseSort = false;
            } else {
                if(currColumnSorted == 0) //Project column
                    Collections.sort(timeRecords, new ProjectReverseComparator());
                else
                    Collections.sort(timeRecords, new TimeReverseComparator());
                reverseSort = true;
            }
            currColumnSorted = column;
        } catch (Exception e) {
            System.err.println("Cannot sort by column "+column);
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public class TimeComparator implements Comparator {
        public int compare(Object obj, Object obj1) {
            TimeRecord comp = (TimeRecord)obj;
            TimeRecord compTo = (TimeRecord)obj1;
            int groupComp = comp.getGroupName().compareTo(compTo.getGroupName());
            return groupComp == 0 ? (int)(comp.getSeconds()-compTo.getSeconds()) : groupComp;
        }
    }
    
    public class ProjectComparator implements Comparator {
        public int compare(Object obj, Object obj1) {
            TimeRecord comp = (TimeRecord)obj;
            TimeRecord compTo = (TimeRecord)obj1;
            return comp.getFullName().compareToIgnoreCase(compTo.getFullName());
        }
    }

    public class TimeReverseComparator implements Comparator {
        public int compare(Object obj, Object obj1) {
            TimeRecord comp = (TimeRecord)obj;
            TimeRecord compTo = (TimeRecord)obj1;
            int groupComp = comp.getGroupName().compareTo(compTo.getGroupName());
            return groupComp == 0 ? (int)(compTo.getSeconds()-comp.getSeconds()) : groupComp;
        }
    }
    
    public class ProjectReverseComparator implements Comparator {
        public int compare(Object obj, Object obj1) {
            TimeRecord comp = (TimeRecord)obj;
            TimeRecord compTo = (TimeRecord)obj1;
            return compTo.getFullName().compareToIgnoreCase(comp.getFullName());
        }
    }
}
