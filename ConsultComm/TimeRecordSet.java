import java.util.*;
import javax.swing.table.*;

class TimeRecordSet {
  private Vector timeRecords;
  private static final String[] titles = {"Project", "Time"};
  private boolean reverseSort = false;
  private int currColumnSorted = -1;
  
  public TimeRecordSet() {
    timeRecords = new Vector();
  }
  
  public void add(TimeRecord rec) {
    timeRecords.add(rec);
  }
  public void setSeconds(int index, long time) {
    TimeRecord record = elementAt(index);
    record.seconds = time;
  }
  public TimeRecord elementAt(int index) throws java.lang.ArrayIndexOutOfBoundsException {
    return (TimeRecord)timeRecords.elementAt(index);
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
  
  public int size() {
    return timeRecords.size();
  }
  public long getSeconds(int index) {
    TimeRecord record = elementAt(index);
    return record.seconds;
  }
  public String getTime(int index) {
    return parseSeconds(getSeconds(index));
  }
  public String getProjectName(int index) {
    TimeRecord record = elementAt(index);
    return record.projectName;
  }
  public String getBillableTimeString() {
    long total = 0;
    Enumeration records = timeRecords.elements();
    while (records.hasMoreElements()) {
      TimeRecord record = (TimeRecord)records.nextElement();
      if(record.billable) total += record.seconds;
    }
    return parseSeconds(total);
  }
  public String getTotalTimeString() {
    long total = 0;
    Enumeration records = timeRecords.elements();
    while (records.hasMoreElements()) {
      TimeRecord record = (TimeRecord)records.nextElement();
      total += record.seconds;
    }
    return parseSeconds(total);
  }
  
  public DefaultTableModel toTableModel(){
    DefaultTableModel model = new javax.swing.table.DefaultTableModel(
    //Set to two empty columns
    new Object [][] {
    },
    titles
    ) {
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
      }
    };
    
    Enumeration records = timeRecords.elements();
    while (records.hasMoreElements()) {
      TimeRecord record = (TimeRecord)records.nextElement();
      model.addRow(new Object[] {record.projectName, record.toString()});
    }
    return model;
  }
  
  public void sort(int column) {
    try {
      if((currColumnSorted != column) || reverseSort) {
        QuickSort.sort(timeRecords, titles[column]);
        reverseSort = false;
      } else {
        QuickSort.revSort(timeRecords, titles[column]);
        reverseSort = true;
      }
      currColumnSorted = column;
    } catch (Exception e) {
      System.err.println("Cannot sort by column "+column);
    }
  }
  
  public String parseSeconds(long seconds) {
    long minutes = seconds / 60;
    long hours = minutes / 60;
    minutes -= hours * 60;
    if (minutes < 10) return ""+hours+":0"+minutes;
    else return ""+hours+":"+minutes;
  }
}