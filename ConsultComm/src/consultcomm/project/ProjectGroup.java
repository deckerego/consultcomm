package consultcomm.project;

import consultcomm.PlainOldJavaObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
  private PropertyChangeSupport notifications;
  private String name;
  private List<Project> projects;
  
  /** 
   * Creates a new instance of ProjectGroup 
   * @param The group's name
   */
  public ProjectGroup(String name)
  {
    super();
    this.notifications = new PropertyChangeSupport(this);
    this.setName(name);
    this.setProjects(new ArrayList<Project>());
  }
  
  /** 
   * Creates a new instance of ProjectGroup 
   * @param The group's name
   * @param The initial project list
   */
  public ProjectGroup(String name, List<Project> projects)
  {
    super();
    this.notifications = new PropertyChangeSupport(this);
    this.setName(name);
    this.setProjects(projects);
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
    firePropertyChange();
  }
  
  /**
   * @return The name of the project
   */
  public String getName()
  {
    return this.name;
  }
  
  /**
   * This is used for object serialization - but bear in mind this list
   * <b>should be considered immutable</b>. Modifications to this list
   * will royally bork up the event notification chain. I wish Java had
   * a C-like const modifier on returned objects, I'd use it. I marked
   * the method as final just to underline my point.
   * @return The list of projects within this group
   */
  public final List<Project> getProjects()
  {
    return this.projects;
  }
  
  /**
   * @param projects The list of projects within this group
   */
  public void setProjects(List<Project> projects)
  {
    this.projects = projects;
    
    //Attach each project to event listeners
    for(Project project : this.projects)
      project.addListener(this);
    
    firePropertyChange();
  }

  /**
   * @param index The index into the list of projects
   * @return The listed Project at the given index
   */
  public Project get(int index)
  {
    return this.projects.get(index);
  }
  
  /**
   * @param project The Project object to find the index of
   * @return The index of the given project within this group's list of projects
   */
  public int indexOf(Project project)
  {
    return this.projects.indexOf(project);
  }
  
  /**
   * @return The size of the list of projects within this group
   */
  public int size()
  {
    return this.projects.size();
  }
  
  /**
   * @param project The Project to remove from this group
   */
  public void remove(Project project)
  {
    this.projects.remove(project);
    firePropertyChange();
  }
  
  /**
   * @param project The Project to add to this group
   */
  public void add(Project project)
  {
    project.addListener(this);
    this.projects.add(project);
    firePropertyChange();
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

  public void propertyChange(PropertyChangeEvent evt)
  {
    firePropertyChange();
    System.out.println("ProjectGroup");
  }
}
