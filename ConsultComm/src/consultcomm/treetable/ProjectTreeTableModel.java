package consultcomm.treetable;

import consultcomm.*;
import consultcomm.project.Project;
import consultcomm.project.ProjectGroup;
import consultcomm.project.Time;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.Statement;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import javax.swing.tree.TreeNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 * This is a model implementing the JXTreeTable default model. Used to
 * group projects together & display as hiearchy.
 *
 * @author jellis
 */
public class ProjectTreeTableModel
    extends DefaultTreeTableModel
    implements Serializable
{
  private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("MessagesBundle");
  private static String[] columns = {MESSAGES.getString("Project"), MESSAGES.getString("Time")};
  
  private List<ProjectGroup> groups;
  private PropertyChangeSupport notifications;
  
  /**
   * Creates a new instance of ProjectTreeTableModel with a starter group
   */
  public ProjectTreeTableModel()
  {
    this.notifications = new PropertyChangeSupport(this);
    this.groups = new ArrayList<ProjectGroup>();
  }
  
  /**
   * Creates a new instance of ProjectTreeTableModel with a starter group
   * @param projectGroup The initial project group to use
   */
  public ProjectTreeTableModel(ProjectGroup projectGroup)
  {
    this.notifications = new PropertyChangeSupport(this);
    this.groups = new ArrayList<ProjectGroup>();
    this.add(projectGroup);
  }
  
  /**
   * @return The list of ProjectGroup objects
   */
  public List<ProjectGroup> getGroups()
  {
    return this.groups;
  }
  
  /**
   * @param projectGroup The group to add to the current list
   */
  public void add(ProjectGroup projectGroup)
  {
    projectGroup.addListener(new java.beans.PropertyChangeListener()
    {
      public void propertyChange(java.beans.PropertyChangeEvent evt)
      {
        projectGroupChange(evt);
      }
    });
    groups.add(projectGroup);
    
    this.notifications.fireIndexedPropertyChange(this.getClass().getName(), groups.size(), null, groups);
  }
  
  /**
   * @param projectGroup The group to remove from the current list
   */
  public void remove(ProjectGroup projectGroup)
  {
    int index = groups.indexOf(projectGroup);
    groups.remove(index);
    
    this.notifications.fireIndexedPropertyChange(this.getClass().getName(), index, null, groups);
  }
  
  /**
   * @param index The index into the list of groups
   * @return The listed ProjectGroup at the given index
   */
  public ProjectGroup get(int index)
  {
    return this.groups.get(index);
  }
  
  /**
   * @return The size of the list of groups within this group
   */
  public int size()
  {
    return this.groups.size();
  }
  
  /**
   * TreeModel interface method used for rendering JTrees
   * @param parent The parent object to search within
   * @param index The index of the nested child object
   * @return The child object at the given index
   */
  public Object getChild(Object parent, int index)
  {
    if(parent instanceof ProjectTreeTableModel)
    { // This is the main list of groups
      ProjectTreeTableModel model = (ProjectTreeTableModel) parent;
      return model.groups.get(index);
    }
    
    if(parent instanceof ProjectGroup)
    { //We should instead use the list of projects
      ProjectGroup group = (ProjectGroup) parent;
      return group.get(index);
    }
    
    else
    { //Nevermind
      return super.getChild(parent, index);
    }
  }
  
  /**
   * TreeModel interface method used for rendering JTrees
   * @param parent The parent object to search within
   * @return The number of children objects
   */
  public int getChildCount(Object parent)
  {
    if(parent instanceof ProjectTreeTableModel)
    { // This is the main list of groups
      ProjectTreeTableModel model = (ProjectTreeTableModel) parent;
      return model.groups.size();
    }
    
    if(parent instanceof ProjectGroup)
    { //We should instead use the list of projects
      ProjectGroup group = (ProjectGroup) parent;
      return group.size();
    }
    
    else
    { //Nevermind
      return super.getChildCount(parent);
    }
  }
  
  /**
   * TreeModel interface method used for rendering JTrees
   * @param parent The parent object to search within
   * @param child The child object to search for
   * @return The index of the child object within the parent's list of children
   */
  public int getIndexOfChild(Object parent, Object child)
  {
    if(parent instanceof ProjectTreeTableModel)
    { // This is the main list of groups
      ProjectTreeTableModel model = (ProjectTreeTableModel) parent;
      return model.groups.indexOf(child);
    }
    
    if(parent instanceof ProjectGroup)
    { //We should instead use the list of projects
      assert child instanceof Project;
      ProjectGroup group = (ProjectGroup) parent;
      return group.indexOf((Project) child);
    }
    
    else
    { //Nevermind
      return super.getIndexOfChild(parent, child);
    }
  }
  
  /**
   * TreeModel interface method used for rendering JTrees
   * @param node The object to test for leafiness
   * @return If this object should be considered the last descendent of a tree
   */
  public boolean isLeaf(Object node)
  {
    return node instanceof Project;
  }
  
  /**
   * TreeModel interface method used for rendering JTrees
   * @return The reference to this object
   */
  public Object getRoot()
  {
    return this;
  }
  
  /**
   * TableModel interface method for rendering JTables
   * @return The number of columns to display
   */
  public int getColumnCount()
  {
    return columns.length;
  }
  
  /**
   * TableModel interface method for rendering JTables
   * @param index The index number of the column
   * @return The name of the column
   */
  public String getColumnName(int index)
  {
    assert index < getColumnCount();
    return columns[index];
  }

  /**
   * Return the column class for a Project record that is based
   * on what type of value will actually be returned (not just its
   * String representation)
   * @param column The column number
   * @return The Class of the returned values in this column
   */
  public Class getColumnClass(int column) 
  {
    if(column == 0)
    { //This is the crazy JTree column - leave it alone
      return super.getColumnClass(column);
    }
    
    else
    { //Return what a Project record's value is, not it's toString() return value 
      return getValueAt(new Project(), column).getClass();
    }
  }
  
  /**
   * TreeTable interface method for rendering JXTreeTables
   * @param node The tree node that serves as the "record" of the table
   * @param column The index of the node's record
   * @return The value to be rendered in the specified column
   */
  public Object getValueAt(Object node, int column)
  {
    assert column < getColumnCount();
    
    if(node instanceof ProjectGroup)
    { // This is a group node
      ProjectGroup group = (ProjectGroup) node;
      
      switch(column)
      {
        case 0:
          return group.getName();
        default:
          return null;
      }
    }
    
    if(node instanceof Project)
    { //We should instead use the list of projects
      Project project = (Project) node;
      
      switch(column)
      {
        case 0:
          return project.getName();
        default:
          return project.getTime();
      }
    }
    
    else
    { //Nevermind
      return super.getValueAt(node, column);
    }
  }
  
  /**
   * TreeTable interface method for rendering JXTreeTables
   * @param node The tree node that serves as the "record" of the table
   * @param column The index of the node's record
   * @return Return false if this is the "time" column of a ProjectGroup, true otherwise
   */
  public boolean isCellEditable(Object node, int column)
  {
    //It doesn't make sense to edit the right-most column of a ProjectGroup record,
    //since there's nothing there to begin with.
    return ! (node instanceof ProjectGroup && column != 0);
  }
  
  /**
   * TreeTable interface method for rendering JXTreeTables
   * @param value The new value that is submitted from an editable cell
   * @param node The tree node that serves as the "record" of the table
   * @param column The index of the node's record
   */
  public void setValueAt(Object value, Object node, int column)
  {
    assert column < getColumnCount();
    
    if(node instanceof ProjectGroup)
    { // This is a group node
      ProjectGroup group = (ProjectGroup) node;
      
      switch(column)
      {
        case 0:
          group.setName((String) value);
          break;
      }
    }
    
    if(node instanceof Project)
    { //We should instead use the list of projects
      Project project = (Project) node;
      
      switch(column)
      {
        case 0:
          project.setName((String) value);
          break;
        case 1:
          project.setTime((Time) value);
          break;
      }
    }
    
    else
    {
      super.setValueAt(value, node, column);
    }
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
   * Process a change in the project group
   * @param evt The property change event transmitted by the project group
   */
  public void projectGroupChange(PropertyChangeEvent evt)
  {
    this.notifications.firePropertyChange(evt);
  }
  
  /**
   * A persistence delegate that more accurately writes list objects instead of
   * using the getter and setter pattern
   */
  public static class ProjectTreeTableModelPersistenceDelegate extends DefaultPersistenceDelegate
  {
    protected void initialize(Class objectType, Object oldObject, Object newObject, Encoder encoder)
    {
      super.initialize(objectType, oldObject, newObject, encoder);
      
      assert oldObject instanceof ProjectTreeTableModel;
      ProjectTreeTableModel model = (ProjectTreeTableModel) oldObject;
      
      for (int i = 0, max = model.size(); i < max; i++)
      {
        encoder.writeStatement(new Statement(oldObject, "add", new Object[] { model.get(i) }));
      }
    }
  }
}
