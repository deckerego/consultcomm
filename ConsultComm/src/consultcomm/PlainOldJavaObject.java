package consultcomm;

import java.beans.BeanInfo;
import java.beans.Beans;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Quite literally a POJO. This is a base object used for notification and serialization.
 * @author jellis
 */
public abstract class PlainOldJavaObject
    implements Serializable, PropertyChangeListener, Cloneable
{
  protected PropertyChangeSupport notifications;
  
  private transient boolean cloning = false; //A quiet hack to stop infinite reflections when cloning
  
  /** Creates a new instance of PlainOldJavaObject */
  public PlainOldJavaObject()
  {
    this.notifications = new PropertyChangeSupport(this);
  }
  
  /**
   * Adds a property change listener
   * @param listener The property change listener that will receive event notifications
   */
  public void addListener(PropertyChangeListener listener)
  {
    assert notifications != null;
    notifications.addPropertyChangeListener(listener);
  }

  /**
   * A POJO-specific implementation of the property change notifier. This one
   * stops infinite cascades of reflection when cloning objects.
   * @param oldValue The old value of the object
   */
  protected void firePropertyChange(Object oldValue)
  {
    assert oldValue != null;

    if(! this.cloning) notifications.firePropertyChange(this.getClass().getName(), oldValue, this);
  }

  private 
  
  /**
   * Attempt to use object reflection to clone getters and setters
   * @return A cloned instance of this object
   */
  public Object clone() throws CloneNotSupportedException
  {
    assert this instanceof PlainOldJavaObject;
      
    try
    {
      Object clonedObject = Beans.instantiate(Thread.currentThread().getContextClassLoader(), this.getClass().getName());
      assert clonedObject instanceof PlainOldJavaObject;
      assert ((PlainOldJavaObject) clonedObject).cloning == false;
    
      BeanInfo info = Introspector.getBeanInfo(this.getClass());
      PropertyDescriptor[] descriptions = info.getPropertyDescriptors();
      ((PlainOldJavaObject) clonedObject).cloning = true; //Pause deep reflection... we're cloning
      
      for (PropertyDescriptor description : descriptions)
      {
        Method readMethod = description.getReadMethod();
        Method writeMethod = description.getWriteMethod();
        
        if(readMethod != null && writeMethod != null)
        {
          Object property = readMethod.invoke(this);
          writeMethod.invoke(clonedObject, property);
          System.out.println("Attempted to use "+readMethod+" and "+writeMethod+" to set "+property);
        }
      }
        
      ((PlainOldJavaObject) clonedObject).cloning = false;
      return clonedObject;
    } 
    
    catch (Exception ex)
    {
      ex.printStackTrace(System.err);
      throw new CloneNotSupportedException(ex.toString());
    }
  }
  
}
