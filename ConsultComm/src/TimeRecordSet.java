import java.util.*;
import javax.swing.table.*;

public class TimeRecordSet implements java.lang.Cloneable, java.io.Serializable {
  private Vector<TimeRecord> timeRecords; //Using generics. Aaaahhh! CrystalSpace csPtr flashbacks!
  private boolean reverseSort = false;
  private int currColumnSorted = 0;
  
  public TimeRecordSet() {
    timeRecords = new Vector<TimeRecord>();
  }
  
  public void setTimeRecords(Vector<TimeRecord> records) { this.timeRecords = records; }
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
    for(int i=0, max=size(); i<max; i++) resetTime(i);
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
    for(TimeRecord record : timeRecords)
      if(record.isBillable()) total += record.getSeconds();
    return total;
  }
  
  public long getTotalTime() {
    long total = 0;
    Enumeration records = timeRecords.elements();
    for(TimeRecord record : timeRecords)
      total += record.getSeconds();
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
    Vector<TimeRecord> records = new Vector<TimeRecord>();
    for(TimeRecord record : timeRecords) {
      if(record.getGroupName().equals(groupName))
        records.addElement(record);
    }
    return records;
  }
  
  public Vector getGroups() {
    Vector<String> records = new Vector<String>();
    String lastGroupName = null;
    
    for(TimeRecord record : timeRecords) {
      if(lastGroupName == null || ! lastGroupName.equals(record.getGroupName())) {
        lastGroupName = record.getGroupName();
        records.addElement(lastGroupName);
      }
    }
    return records;
  }
  
  public TimeRecord elementAt(int index) throws java.lang.ArrayIndexOutOfBoundsException {
    if(index == -1) return null;
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
  
  public TimeRecordSet clone() throws CloneNotSupportedException {
    TimeRecordSet clone = (TimeRecordSet)super.clone();
    //You can't clone a Vector then cast the resulting Object as a Vector<Generic>
    //(see http://forum.java.sun.com/thread.jsp?thread=554995&forum=316&message=2736584)
    //so instead create a new Vector intialzed to the original Vector
    clone.timeRecords = new Vector<TimeRecord> (this.timeRecords);
    return clone;
  }
  
  public class TimeComparator implements Comparator<TimeRecord> {
    public int compare(TimeRecord comp, TimeRecord compTo) {
      int groupComp = comp.getGroupName().compareTo(compTo.getGroupName());
      return groupComp == 0 ? (int)(comp.getSeconds()-compTo.getSeconds()) : groupComp;
    }
  }
  
  public class ProjectComparator implements Comparator<TimeRecord> {
    public int compare(TimeRecord comp, TimeRecord compTo) {
      return comp.getFullName().compareToIgnoreCase(compTo.getFullName());
    }
  }
  
  public class TimeReverseComparator implements Comparator<TimeRecord> {
    public int compare(TimeRecord comp, TimeRecord compTo) {
      int groupComp = comp.getGroupName().compareTo(compTo.getGroupName());
      return groupComp == 0 ? (int)(compTo.getSeconds()-comp.getSeconds()) : groupComp;
    }
  }
  
  public class ProjectReverseComparator implements Comparator<TimeRecord> {
    public int compare(TimeRecord comp, TimeRecord compTo) {
      return compTo.getFullName().compareToIgnoreCase(comp.getFullName());
    }
  }
}
