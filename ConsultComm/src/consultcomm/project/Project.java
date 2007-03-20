package consultcomm.project;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * A POJO for projects
 * @author jellis
 */
public class Project
    implements Serializable
{
  private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("MessagesBundle");
  
  private String name;
  private Time time;
  
  public Project()
  {
    this.name=MESSAGES.getString("Default Project");
    this.time = new Time();
  }
  
  /** 
   * Creates a new instance of Project 
   * @param name The project name
   * @param time The elapsed time for the project
   */
  public Project(String name, Long time)
  {
    this.name = name;
    this.time = new Time(time);
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
   * @param time The elapsed time of the project (in seconds)
   */
  public void setElapsedTime(Long time)
  {
    this.time = new Time(time * 1000);
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
   * @return The time elapsed for the project (in seconds)
   */
  public Long getElapsedTime()
  {
    return this.time.getElapsed() / 1000;
  }
  
  /**
   * @return The time elapsed for the project
   */
  public Time getTime()
  {
    return this.time;
  }
}
