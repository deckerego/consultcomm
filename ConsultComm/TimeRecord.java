import java.util.*;

class TimeRecord implements Comparable {
  protected long seconds;
  protected String projectName;
  protected String alias;
  protected boolean billable;
  protected boolean export;
  
  TimeRecord() {
    projectName = "";
    seconds = 0L;
    billable = true;
    export = true;
  }
  TimeRecord(String name, String aka, long time, boolean isBillable, boolean isExportable) {
    projectName = name;
    alias = aka;
    seconds = time;
    billable = isBillable;
    export = isExportable;
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

  public java.math.BigDecimal getHours(int minutes, int scale) {
    double percent = (double)minutes/(double)(60*60);
    long partialHours = seconds / minutes;
    long remainder = seconds % minutes;
    if(remainder >= minutes/2) partialHours++;
    java.math.BigDecimal result = new java.math.BigDecimal(partialHours * percent);
    return result.setScale(scale, java.math.BigDecimal.ROUND_HALF_EVEN);
  }
  
  public String toString() {
    return toSecondString();
  }
  
  public String toSecondString() {
    long minutes = seconds / 60;
    long hours = minutes / 60;
    minutes -= hours * 60;
    long thisSeconds = seconds-(hours*60*60)-(minutes*60);

    String hourString = Long.toString(hours);
    String minuteString = minutes < 10 ? "0"+minutes : Long.toString(minutes);
    String secondString = thisSeconds < 10 ? "0"+thisSeconds : Long.toString(thisSeconds);
    return ""+hourString+":"+minuteString+"."+secondString;
  }
  
  public String toMinuteString() {
    long minutes = seconds / 60;
    long hours = minutes / 60;
    minutes -= hours * 60;

    String hourString = Long.toString(hours);
    String minuteString = minutes < 10 ? "0"+minutes : Long.toString(minutes);
    return ""+hourString+":"+minuteString;
  }
  
  public int compareTo(Comparable comp, String type) throws Exception {
    TimeRecord compTo = (TimeRecord)comp;
    if(type.equals("Time")) {
      return (int)(seconds-compTo.seconds);
    } else {
      boolean useAlias = ! ((alias == null) || alias.equals(""));
      boolean useAliasTo = ! ((compTo.alias == null) || compTo.alias.equals(""));
      boolean useProject = ! ((projectName == null) || projectName.equals(""));
      boolean useProjectTo = ! ((compTo.projectName == null) || compTo.projectName.equals(""));
      if(useAlias) {
        if(useAliasTo) return alias.compareToIgnoreCase(compTo.alias);
        else return alias.compareToIgnoreCase(compTo.projectName);
      } else {
        if(useAliasTo) return projectName.compareToIgnoreCase(compTo.alias);
        else return projectName.compareToIgnoreCase(compTo.projectName);
      }
    }
  }
}
