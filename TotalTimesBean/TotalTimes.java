import javax.swing.*;
import java.io.*;

public class TotalTimes extends Object implements java.io.Serializable, java.lang.Cloneable, java.beans.PropertyChangeListener{
    final String REMAINING_TITLE = "Remaining:";
    final String EARNED_TITLE = "Earned:";
    
    private static java.text.NumberFormat dollarFormat = java.text.NumberFormat.getInstance();
    
    private ClntComm clntComm;
    private long countdownTime;
    private int countdownIndex = -1;
    private double cashAmount;
    private int cashIndex = -1;
    
    public TotalTimes() {
        dollarFormat.setMaximumFractionDigits(2);
    }
    
    public long getCountdownTime() { return this.countdownTime; }
    public void setCountdownTime(long countdownTime) { this.countdownTime = countdownTime; }
    public long getRemainingTime() {
        long remainingTime = this.countdownTime*60 - clntComm.getTimes().getTotalTime();
        if(remainingTime > 0) return remainingTime;
        return 0;
    }
    public double getCashAmount() { return this.cashAmount; }
    public void setCashAmount(double cashAmount) { this.cashAmount = cashAmount; }
    public String getTotalCash() {
        double hours = ((double)clntComm.getTimes().getTotalTime()) / ((double)(60*60));
        double cash = this.cashAmount * hours;
        return "$"+dollarFormat.format(cash);
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        if(clntComm == null) clntComm = (ClntComm)propertyChangeEvent.getSource();
        TotalPanel totalPanel = clntComm.getTotalPanel();
        if(this.countdownTime != 0) totalPanel.setEntry(REMAINING_TITLE, getRemainingTime());
        if(this.cashAmount != 0) totalPanel.setEntry(EARNED_TITLE, getTotalCash());
    }
}
