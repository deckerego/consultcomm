import java.util.*;

public class TimeRecord {
    private long seconds;
    private String projectName;
    private boolean billable;
    
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
    
    public String getProjectName() { return this.projectName; }
    public void setProjectName(String name) { this.projectName = name; }
    public boolean getBillable() { return this.billable; }
    public void setBillable(boolean billable) { this.billable = billable; }
    public boolean isBillable() { return this.billable; }
    public long getSeconds() { return this.seconds; }
    public void setSeconds(long seconds) { this.seconds = seconds; }
    public void setSeconds(String secs) {
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
    
    public boolean equals(Object to) {
        TimeRecord toRecord = (TimeRecord)to;
        boolean isEqual = true;
        isEqual = isEqual && (seconds == toRecord.seconds);
        isEqual = isEqual && ((projectName == toRecord.projectName) || (projectName.equals(toRecord.projectName)));
        isEqual = isEqual && (billable == toRecord.billable);
        return isEqual;
    }
}
