package consultcomm.project;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;

/**
 * A POJO that stores the elapsed time of a project
 * @author jellis
 */
public class Time
    implements Serializable
{
  private Long begin;
  private Long end;
  
  /** Creates a new instance of Time */
  public Time()
  {
    this.begin = this.end = Calendar.getInstance().getTime().getTime();
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
   * @return The point at which the time counter began
   */
  public Long getBegin()
  {
    return this.begin;
  }
  
  /**
   * @return The point at which the time counter stopped
   */
  public Long getEnd()
  {
    return this.end;
  }
  
  /**
   * @return The total elapsed time
   */
  public Long getElapsed()
  {
    return this.end - this.begin;
  }
  
  /**
   * @param end The point at which the time counter begain
   */
  public void setBegin(Long begin)
  {
    this.begin = begin;
  }
  
  /**
   * @param end The point at which the time counter stopped
   */
  public void setEnd(Long end)
  {
    this.end = end;
  }
  
  /**
   * @param elapsed Set the elapsed time
   */
  public void setElapsed(Long elapsed)
  {
    this.end = Calendar.getInstance().getTime().getTime();
    this.begin = this.end - elapsed;
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
}
