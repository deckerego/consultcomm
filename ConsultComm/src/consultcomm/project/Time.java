package consultcomm.project;

import consultcomm.PlainOldJavaObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
  private Long elapsed;
  
  /** Creates a new instance of Time */
  public Time()
  {
    this.elapsed = 0L;
  }
  
  /**
   * Creates a new instance of Time
   * @param elapsed The amount of time elapsed so far
   */
  public Time(Long elapsed)
  {
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
    Object thisClone = null;
    
    try
    {
      thisClone = this.clone();
    }

    catch(CloneNotSupportedException e)
    {
      e.printStackTrace(System.err);
      thisClone = null;
    }
    
    this.elapsed = elapsed;
    
    firePropertyChange(thisClone);
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

  public void propertyChange(PropertyChangeEvent evt)
  {
  }
}
