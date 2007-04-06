package consultcomm.project;

import consultcomm.PlainOldJavaObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * A POJO for projects
 * @author jellis
 */
public class Project
    extends PlainOldJavaObject
{
  private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("MessagesBundle");
  
  private String name;
  private Time time;
  
  public Project()
  {
    super();
    this.name=MESSAGES.getString("Default Project");
    this.time = new Time();
    
    this.time.addListener(this);
  }
  
  /** 
   * Creates a new instance of Project 
   * @param name The project name
   * @param time The elapsed time for the project
   */
  public Project(String name, Long time)
  {
    super();
    this.name = name;
    this.time = new Time(time);
        
    this.time.addListener(this);
  }
  
  /**
   * Convert the current object into a String
   * @return The project's name
   */
  public String toString()
  {
    return this.name;
  }
  
  /**
   * Sets the project's name. If an empty string is provided,
   * the default project name is used instead.
   * @param name The name of the project
   */
  public void setName(String name)
  {
    this.name = name;
  }
  
  /**
   * @param time The elapsed time of the project
   */
  public void setTime(Time time)
  {
    this.time = time;
  }
  
  /**
   * @return The name of the project
   */
  public String getName()
  {
    return this.name;
  }
  
  /**
   * @return The time elapsed for the project
   */
  public Time getTime()
  {
    return this.time;
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    Time newTime = (Time) evt.getNewValue();
  }
}
