package consultcomm.project;

import consultcomm.PlainOldJavaObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A POJO that groups together lists of projects
 * @author jellis
 */
public class ProjectGroup
    extends PlainOldJavaObject
{
  private String name;
  private List<Project> projects;
  
  /** 
   * Creates a new instance of ProjectGroup 
   * @param The group's name
   */
  public ProjectGroup(String name)
  {
    super();
    this.name = name;
    this.projects = new ArrayList<Project>();
  }
  
  /** 
   * Creates a new instance of ProjectGroup 
   * @param The group's name
   * @param The initial project list
   */
  public ProjectGroup(String name, List<Project> projects)
  {
    this.name = name;
    this.projects = projects;
  }
  
  /**
   * Convert the current object into a String
   * @return The group's name
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
   * @return The name of the project
   */
  public String getName()
  {
    return this.name;
  }
  
  /**
   * @return The list of projects within this group
   */
  public List<Project> getProjects()
  {
    return this.projects;
  }
  
  /**
   * @param projects The list of projects within this group
   */
  public void setProjects(List<Project> projects)
  {
    this.projects = projects;
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
  }
}
