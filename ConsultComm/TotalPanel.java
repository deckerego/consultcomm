import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class TotalPanel extends javax.swing.JPanel {
    Vector titles = new Vector();
    Vector values = new Vector();
    int timesClicked;

    /** Creates new form TotalPanel */
    public TotalPanel() {
        addElement("Total:", 0);
        addElement("Billable:", 0);
        initComponents();
    }

    public int addElement(String title, long seconds) {
        int index = titles.size();
        titles.addElement(title);
        values.addElement(parseSeconds(seconds));
        return index;
    }
    public int addElement(String title, String value) {
        int index = titles.size();
        titles.addElement(title);
        values.addElement(value);
        return index;
    }
    public void removeValueAt(int index) {
        values.removeElementAt(index);
        titles.removeElementAt(index);
    }
    public void setValueAt(long seconds, int index) { values.setElementAt(parseSeconds(seconds), index); }
    public void setValueAt(String value, int index) { values.setElementAt(value, index); }
    public int getIndex() { return timesClicked; }

    public void toggleTotal(int index) {
        timesClicked = index;
        titleLabel.setText((String)titles.elementAt(index));
        valueLabel.setText((String)values.elementAt(index));
    }

    public String parseSeconds(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes -= hours * 60;
        if (minutes < 10) return ""+hours+":0"+minutes;
        else return ""+hours+":"+minutes;
    }
    
    public void repaint() {
        if(valueLabel != null) valueLabel.setText((String)values.elementAt(timesClicked));
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
            if(--timesClicked < 0) timesClicked = titles.size()-1;
        } else {
            timesClicked = (timesClicked+1)%titles.size();
        }
        toggleTotal(timesClicked);
    }//GEN-LAST:event_toggleTotal

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel valueLabel;
    // End of variables declaration//GEN-END:variables

}
