import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class TotalPanel extends javax.swing.JPanel {
    Map entries = new LinkedHashMap();
    int timesClicked;

    public TotalPanel() {
        initComponents();
    }

    public void removeEntry(String title) { entries.remove(title); }
    public void setEntry(String title, long value) { entries.put(title, new Long(value)); }
    public void setEntry(String title, String value) { entries.put(title, value); }
    public int getIndex() { return timesClicked; }

    public void toggleTotal(int index) {
        Object[] keySet = entries.keySet().toArray();
        if(keySet.length > 0) {
          if(index > keySet.length || index < 0) timesClicked = 0;
          else timesClicked = index;
          String title = (String)keySet[timesClicked];
        
          Object valueObj = entries.get(title);
          String value;
          if(valueObj.getClass() == Long.class) value = parseSeconds(((Long)valueObj).longValue());
          else value = valueObj.toString();
        
          titleLabel.setText(title);
          valueLabel.setText(value);
        }
    }
    
    public String parseSeconds(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes -= hours * 60;
        if (minutes < 10) return ""+hours+":0"+minutes;
        else return ""+hours+":"+minutes;
    }
    
    public void repaint() {
        if(valueLabel != null) toggleTotal(timesClicked);
        super.repaint();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        titleLabel = new javax.swing.JLabel();
        valueLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridLayout(1, 2));

        titleLabel.setText("Total:");
        titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleTotal(evt);
            }
        });

        add(titleLabel);

        valueLabel.setText("0:00");
        valueLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleTotal(evt);
            }
        });

        add(valueLabel);

    }//GEN-END:initComponents

    private void toggleTotal(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toggleTotal
        if(evt.getModifiers() == java.awt.event.MouseEvent.BUTTON3_MASK) {
            if(--timesClicked < 0) timesClicked = entries.size()-1;
        } else {
            timesClicked = (timesClicked+1)%entries.size();
        }
        toggleTotal(timesClicked);
    }//GEN-LAST:event_toggleTotal

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel valueLabel;
    // End of variables declaration//GEN-END:variables

}
