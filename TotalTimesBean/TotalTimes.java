import javax.swing.*;
import java.io.*;

public class TotalTimes extends Object implements java.io.Serializable, java.lang.Cloneable, java.beans.PropertyChangeListener{
    private static java.text.NumberFormat dollarFormat = java.text.NumberFormat.getInstance();
    private TotalPanel totalPanel;
    private ClntComm clntComm;
    private long countdownTime;
    private int countdownIndex = -1;
    private double cashAmount;
    private int cashIndex = -1;
    
    public TotalTimes() {
        dollarFormat.setMaximumFractionDigits(2);
    }
    
    public long getCountdownTime() { return this.countdownTime; }
    public long getRemainingTime() {
        long remainingTime = this.countdownTime*60 - clntComm.getTimes().getTotalTime();
        if(remainingTime > 0) return remainingTime;
        return 0;
    }
    public void setCountdownTime(long countdownTime) {
        this.countdownTime = countdownTime;
        if(clntComm != null) {
            if(this.countdownTime != 0) { //We have seconds, add if necessary
                if(this.countdownIndex != -1) totalPanel.setValueAt(getRemainingTime(), this.countdownIndex);
                else this.countdownIndex = totalPanel.addElement("Remaining:", getRemainingTime());
            } else { //No seconds, remove it if we need to
                if(this.countdownIndex != -1) {
                    totalPanel.removeValueAt(this.countdownIndex);
                    this.countdownIndex = -1;
                }
            }
        }
    }
    public double getCashAmount() { return this.cashAmount; }
    public String getTotalCash() {
        double hours = ((double)clntComm.getTimes().getTotalTime()) / ((double)(60*60));
        double cash = this.cashAmount * hours;
        return "$"+dollarFormat.format(cash);
    }
    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
        if(clntComm != null) {
            if(this.cashAmount != 0) { //We have cash; add it if necessary
                if(this.cashIndex != -1) totalPanel.setValueAt(getTotalCash(), this.cashIndex);
                else this.cashIndex = totalPanel.addElement("Earned:", getTotalCash());
            } else { //No cash, remove it if we need to
                if(this.cashIndex != -1) {
                    totalPanel.removeValueAt(this.cashIndex);
                    this.cashIndex = -1;
                }
            }
        }
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        if(clntComm == null) {
            clntComm = (ClntComm)propertyChangeEvent.getSource();
            totalPanel = clntComm.getTotalPanel();
            //Refresh fields to use clntComm fields
            setCountdownTime(this.countdownTime);
            setCashAmount(this.cashAmount);
        }
        if(this.countdownIndex != -1) totalPanel.setValueAt(getRemainingTime(), this.countdownIndex);
        if(this.cashIndex != -1) totalPanel.setValueAt(getTotalCash(), this.cashIndex);
    }
}
