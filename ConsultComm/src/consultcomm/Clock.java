package consultcomm;

import consultcomm.project.Time;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A time clock that fires events on a regular basis.
 * @author jellis
 */
public class Clock
    implements Runnable
{
  public static final String NOTIFICATION_NAME = "CLOCK";
  private Long lastTick;
  private PropertyChangeSupport notifications;
  
  /** Creates a new instance of Clock */
  public Clock()
  {
    this.lastTick = System.currentTimeMillis();
    this.notifications = new PropertyChangeSupport(this);
  }

  public void addClockListener(PropertyChangeListener listener)
  {
    assert notifications != null;
    notifications.addPropertyChangeListener(NOTIFICATION_NAME, listener);
  }
      
  public void run()
  {
    Long oldTick = this.lastTick;
    this.lastTick = System.currentTimeMillis();
    this.notifications.firePropertyChange(NOTIFICATION_NAME, oldTick, this.lastTick);
  }
  
}
