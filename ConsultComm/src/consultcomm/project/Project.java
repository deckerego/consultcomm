/*
 * Project.java
 *
 * Created on March 6, 2007, 8:53 PM
 */

package consultcomm.project;

/**
 * A POJO for projects
 * @author jellis
 */
public class Project
{
  private String name;
  private String time;
  
  /** 
   * Creates a new instance of Project 
   * @param name The project name
   * @param time The elapsed time for the project
   */
  public Project(String name, String time)
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
   * @param time The elapsed time of the project
   */
  public void setTime(String time)
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
  public String getTime()
  {
    return this.time;
  }
}
