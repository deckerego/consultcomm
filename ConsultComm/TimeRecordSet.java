import java.util.*;
import javax.swing.table.*;

public class TimeRecordSet implements java.lang.Cloneable {
    private Vector timeRecords;
    private static final String[] titles = {"Project", "Time"};
    private boolean reverseSort = false;
    private int currColumnSorted = 0;
    
    public TimeRecordSet() {
            timeRecords = new Vector();
    }
    
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
    public String getSecondsString(int index) {
        TimeRecord record = elementAt(index);
        return record.toSecondString();
    }
    public String getMinutesString(int index) {
        TimeRecord record = elementAt(index);
        return record.toMinuteString();
    }
    public String getProjectName(int index) {
        TimeRecord record = elementAt(index);
        return record.getProjectName();
    }
    public String getBillableTimeString() {
        long total = 0;
        Enumeration records = timeRecords.elements();
        while (records.hasMoreElements()) {
            TimeRecord record = (TimeRecord)records.nextElement();
            if(record.isBillable()) total += record.getSeconds();
        }
        return parseSeconds(total);
    }
    public String getTotalTimeString() {
        long total = 0;
        Enumeration records = timeRecords.elements();
        while (records.hasMoreElements()) {
            TimeRecord record = (TimeRecord)records.nextElement();
            total += record.getSeconds();
        }
        return parseSeconds(total);
    }
    public String getCountdownTimeString(long minutes, int criteria) {
        long total = minutes*60;
        Enumeration records = timeRecords.elements();
        while (records.hasMoreElements()) {
            TimeRecord record = (TimeRecord)records.nextElement();
            if(record.isBillable() && criteria == ClntComm.SHOW_BILLABLE) total -= record.getSeconds();
            else if(criteria == ClntComm.SHOW_TOTAL) total -= record.getSeconds();
        }
        if(total < 0) total = 0;
        return parseSeconds(total);
    }
    public String getPayAmount(float perHour, int criteria) {
        float total = 0;
        java.text.NumberFormat dollarFormat = java.text.NumberFormat.getInstance();
        dollarFormat.setMinimumFractionDigits(2);
        dollarFormat.setMaximumFractionDigits(2);
        Enumeration records = timeRecords.elements();
        while (records.hasMoreElements()) {
            TimeRecord record = (TimeRecord)records.nextElement();
            if(record.isBillable() && criteria == ClntComm.SHOW_BILLABLE) total += record.getSeconds();
            else if(criteria == ClntComm.SHOW_TOTAL) total += record.getSeconds();
        }
        
        float hours = total / (float)(60*60);
        return "$"+dollarFormat.format(hours*perHour);
    }
    public String[] getAllProjects() {
        String[] names = new String[timeRecords.size()];
        Enumeration records = timeRecords.elements();
        for(int i=0; records.hasMoreElements(); i++) {
            TimeRecord record = (TimeRecord)records.nextElement();
            names[i] = record.getProjectName();
        }
        return names;
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
    public int indexOfProject(String projectName) {
        Enumeration records = timeRecords.elements();
        for(int i=0; records.hasMoreElements(); i++) {
            TimeRecord record = (TimeRecord)records.nextElement();
            if(projectName.equals(record.getProjectName()))
                return i;
        }
        return -1;
    }
    
    public DefaultTableModel toTableModel(int timeFormat){
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
            String timeString = null;
            if(timeFormat == ClntComm.SECONDS) timeString = record.toSecondString();
            else timeString = record.toMinuteString();
            model.addRow(new Object[] {record.getProjectName(), timeString});
        }
        return model;
    }
    
    public void sort() {
        if(reverseSort)
            if(titles[currColumnSorted].equals("Project"))
                Collections.sort(timeRecords, new ProjectComparator());
            else
                Collections.sort(timeRecords, new TimeComparator());
        else
            if(titles[currColumnSorted].equals("Project"))
                Collections.sort(timeRecords, new ProjectReverseComparator());
            else
                Collections.sort(timeRecords, new TimeReverseComparator());
    }
    
    public void sort(int column) {
        try {
            if((currColumnSorted != column) || reverseSort) {
                if(titles[currColumnSorted].equals("Project"))
                    Collections.sort(timeRecords, new ProjectComparator());
                else
                    Collections.sort(timeRecords, new TimeComparator());
                reverseSort = false;
            } else {
                if(titles[currColumnSorted].equals("Project"))
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
    
    public String parseSeconds(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes -= hours * 60;
        if (minutes < 10) return ""+hours+":0"+minutes;
        else return ""+hours+":"+minutes;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public class TimeComparator implements Comparator {
        public int compare(Object obj, Object obj1) {
            TimeRecord comp = (TimeRecord)obj;
            TimeRecord compTo = (TimeRecord)obj1;
            return (int)(comp.getSeconds()-compTo.getSeconds());
        }
    }
    
    public class ProjectComparator implements Comparator {
        public int compare(Object obj, Object obj1) {
            TimeRecord comp = (TimeRecord)obj;
            TimeRecord compTo = (TimeRecord)obj1;
            return comp.getProjectName().compareToIgnoreCase(compTo.getProjectName());
        }
    }

    public class TimeReverseComparator implements Comparator {
        public int compare(Object obj, Object obj1) {
            TimeRecord comp = (TimeRecord)obj;
            TimeRecord compTo = (TimeRecord)obj1;
            return (int)(compTo.getSeconds()-comp.getSeconds());
        }
    }
    
    public class ProjectReverseComparator implements Comparator {
        public int compare(Object obj, Object obj1) {
            TimeRecord comp = (TimeRecord)obj;
            TimeRecord compTo = (TimeRecord)obj1;
            return compTo.getProjectName().compareToIgnoreCase(comp.getProjectName());
        }
    }
}
