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
  
  /** Creates a new instance of PlainOldJavaObject */
  public PlainOldJavaObject()
  {
  }
  
  /**
   * Adds a property change listener
   * @param listener The property change listener that will receive event notifications
   */
  public abstract void addListener(PropertyChangeListener listener);

  /**
   * A POJO-specific implementation of the property change notifier. This one
   * stops infinite cascades of reflection when cloning objects.
   */
  protected abstract void firePropertyChange();
  
}
