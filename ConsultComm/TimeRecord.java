import java.util.*;

class TimeRecord implements Comparable {
  protected long seconds;
  protected String projectName;
  protected boolean billable;
  
  TimeRecord() {
    projectName = "";
    seconds = 0L;
    billable = true;
  }
  TimeRecord(String name, long time, boolean isBillable) {
    projectName = name;
    seconds = time;
    billable = isBillable;
  }
  
  protected void setSeconds(String secs) {
    StringTokenizer timeWords = new StringTokenizer(secs, ":");
    // If time format is 1:30
    if(timeWords.countTokens() == 2) {
      seconds = Long.parseLong(timeWords.nextToken()) * 3600;
      seconds += Long.parseLong(timeWords.nextToken()) * 60;
    }
    // If time format is 90
    else if(timeWords.countTokens() == 1) {
      seconds =  Long.parseLong(timeWords.nextToken()) * 60;
      // Else we have the wrong format
    } else seconds = 0L;
  }

  public double getHours(int minutes) {
    long quarterHours = seconds / minutes;
    long remainder = seconds % minutes;
    if(remainder >= minutes/2) quarterHours++;
    return quarterHours * 0.25;
  }
  
  public String toString() {
    long minutes = seconds / 60;
    long hours = minutes / 60;
    minutes -= hours * 60;
    if (minutes < 10) return ""+hours+":0"+minutes;
    else return ""+hours+":"+minutes;
  }
  
  public int compareTo(Comparable comp, String type) throws Exception {
    TimeRecord compTo = (TimeRecord)comp;
    if(type.equals("Time")) return (int)(seconds-compTo.seconds);
    else return projectName.compareToIgnoreCase(compTo.projectName);
  }
}