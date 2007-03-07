/*
 * ProjectGroup.java
 *
 * Created on March 6, 2007, 9:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package consultcomm;

import java.util.ArrayList;

/**
 *
 * @author jellis
 */
public class ProjectGroup
{
  public String name;
  public ArrayList<Project> projects;
  
  /** 
   * Creates a new instance of ProjectGroup 
   * @param The group's name
   */
  public ProjectGroup(String name)
  {
    this.name = name;
    this.projects = new ArrayList<Project>();
  }
  
  /**
   * Convert the current object into a String
   * @return The group's name
   */
  public String toString()
  {
    return this.name;
  }
}
