/*
 * Empty.java
 *
 * Created on September 17, 2004, 1:57 PM
 */

/**
 * @author http://consultcomm.sourceforge.net
 */
public class Empty extends CsltCommPlugin {
  private ClntComm clntComm;
  
  public Empty() {
  }
  
  /**
   * Called whenever something happens in the main ConsultComm window
   * (i.e. the timer has incremented or a record has been changed
   * @param evt The property change event that notifies us of the change
   */
  public void propertyChange(java.beans.PropertyChangeEvent evt) {
    //If we haven't already retreived a reference to ConsultComm, do so now
    if(clntComm == null) clntComm = (ClntComm)evt.getSource();

    // This tests to see if the GUI window for ConsultComm has opened. 
    // The types of property changes that correspond to window events
    // are: activated, closed, closing, deactivated, deiconified, 
    // iconified and opened
    if(evt.getPropertyName().equals("opened")) {
      System.out.println("The ConsultComm window has opened.");
    }
    
    // User has edited a project (changed a name or time amount)
    if(evt.getPropertyName().equals("record")) {
      System.out.println("Edited a record");
    }

    // Timelist has been changed, find out how
    if(evt.getPropertyName().equals("times")) {
      //List of times after the change occured
      TimeRecordSet newTimes = (TimeRecordSet)evt.getNewValue();
      //List of times before the change occured
      TimeRecordSet oldTimes = (TimeRecordSet)evt.getOldValue();
      
       //User has added a project
      if(newTimes != null && oldTimes != null && newTimes.size() > oldTimes.size()) {
        System.out.println("Added a record");
      } else { //Otherwise, the clock has just incremented
        System.out.println("Time has changed");
      }
    }
  }
  
}
