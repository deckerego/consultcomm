package consultcomm.project;

import consultcomm.PlainOldJavaObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
  
  private PropertyChangeSupport notifications;
  private String name;
  private Time time;
  
  public Project()
  {
    super();
    this.notifications = new PropertyChangeSupport(this);
    this.setName(MESSAGES.getString("Default Project"));
    this.setTime(new Time());
  }
  
  /** 
   * Creates a new instance of Project 
   * @param name The project name
   * @param time The elapsed time for the project
   */
  public Project(String name, Long time)
  {
    super();
    this.notifications = new PropertyChangeSupport(this);
    this.setName(name);
    this.setTime(new Time(time));
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
    firePropertyChange();
  }
  
  /**
   * @param time The elapsed time of the project
   */
  public void setTime(Time time)
  {
    this.time = time;
    this.time.addListener(new java.beans.PropertyChangeListener()
    {
      public void propertyChange(java.beans.PropertyChangeEvent evt)
      {
        timeChange(evt);
      }
    });;
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
   * @return The time elapsed for the project
   */
  public Time getTime()
  {
    return this.time;
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
  
  /**
   * Fire off an event
   * @param evt The event that has caused the change
   */
  public void timeChange(PropertyChangeEvent evt)
  {
    this.notifications.firePropertyChange(evt);
  }
}
