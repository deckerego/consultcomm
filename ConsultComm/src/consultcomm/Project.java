/*
 * Project.java
 *
 * Created on March 6, 2007, 8:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package consultcomm;

/**
 *
 * @author jellis
 */
public class Project
{
  public String name;
  public String time;
  
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
}
