/*
 * Project.java
 *
 * Created on March 6, 2007, 8:53 PM
 */

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
  private Long time;
  
  public Project()
  {
    this.name=MESSAGES.getString("Default Project");
    this.time = 0L;
  }
  
  /** 
   * Creates a new instance of Project 
   * @param name The project name
   * @param time The elapsed time for the project
   */
  public Project(String name, Long time)
  {
    this.name = name;
    this.time = time;
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
   * @param name The name of the project
   */
  public void setName(String name)
  {
    this.name = name;
  }
  
  /**
   * @param time The elapsed time of the project (in seconds)
   */
  public void setTime(Long time)
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
  public Long getTime()
  {
    return this.time;
  }
}
