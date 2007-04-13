package consultcomm.project;

import consultcomm.PlainOldJavaObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

/**
 * A POJO that stores the elapsed time of a project
 * @author jellis
 */
public class Time
    extends PlainOldJavaObject
{
  private PropertyChangeSupport notifications;
  private Long elapsed;
  
  /** Creates a new instance of Time */
  public Time()
  {
    super();
    this.notifications = new PropertyChangeSupport(this);
    this.elapsed = 0L;
  }
  
  /**
   * Creates a new instance of Time
   * @param elapsed The amount of time elapsed so far
   */
  public Time(Long elapsed)
  {
    super();
    this.notifications = new PropertyChangeSupport(this);
    this.setElapsed(elapsed);
  }
  
  /**
   * @return The total elapsed time
   */
  public Long getElapsed()
  {
    return this.elapsed;
  }
  
  /**
   * @param elapsed Set the elapsed time
   */
  public void setElapsed(Long elapsed)
  {
    this.elapsed = elapsed;
    firePropertyChange();
  }
  
  /**
   * @param elapsed Add the elapsed time to the current total
   */
  public void addElapsed(Long diff)
  {
    this.setElapsed(this.getElapsed() + diff);
  }
  
  /**
   * @return The object as HH:mm:ss
   */
  public String toString()
  {
    //Does the below look idiotic? It is. I attempted to create my own
    //custom TimeFormat class, but the format classes in the Java
    //packages are so locked down that this proved untenable. I still
    //may create a custom DecimalFormat or the like, but until then I'll
    //just create a Date object that's set to a 0-offset time zone.
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    timeFormat.setTimeZone(new SimpleTimeZone(0, "NONE"));
    return timeFormat.format(getElapsed());
  }

  /**
   * Adds a property change listener
   * @param listener The property change listener that will receive event notifications
   */
  public void addListener(PropertyChangeListener listener)
  {
    assert this.notifications != null;
    this.notifications.addPropertyChangeListener(listener);
  }

  /**
   * A POJO-specific implementation of the property change notifier. This one
   * stops infinite cascades of reflection when cloning objects.
   */
  protected void firePropertyChange()
  {
    this.notifications.firePropertyChange(this.getClass().getName(), null, this);
  }
}
