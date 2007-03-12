/*
 * ProjectTreeTableModel.java
 *
 * Created on March 6, 2007, 8:50 PM
 */

package consultcomm.treetable;

import consultcomm.*;
import consultcomm.project.Project;
import consultcomm.project.ProjectGroup;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
  
  public ProjectTreeTableModel()
  {
    ArrayList<Project> projects = new ArrayList<Project>();
    projects.add(new Project(MESSAGES.getString("Default Project"), "00:00"));
    
    this.groups = new ArrayList<ProjectGroup>();
    this.groups.add(new ProjectGroup(MESSAGES.getString("Default Group"), projects));
  }
  
  /**
   * Creates a new instance of ProjectTreeTableModel
   */
  public ProjectTreeTableModel(List<ProjectGroup> groups)
  {
    assert groups != null;
    
    this.groups = groups;
  }
  
  /**
   * @return The list of ProjectGroup objects
   */
  public List<ProjectGroup> getGroups()
  {
    return this.groups;
  }
  
  /**
   * @param groups The list of ProjectGroup objects to display
   */
  public void setGroups(List<ProjectGroup> groups)
  {
    this.groups = groups;
  }
  
  /**
   * TreeModel interface method used for rendering JTrees
   * @param parent The parent object to search within
   * @param index The index of the nested child object
   * @return The child object at the given index
   */
  public Object getChild(Object parent, int index)
  {
    if(parent.getClass() == ProjectTreeTableModel.class)
    { // This is the main list of groups
      ProjectTreeTableModel model = (ProjectTreeTableModel) parent;
      return model.groups.get(index);
    }
    
    if(parent.getClass() == ProjectGroup.class)
    { //We should instead use the list of projects
      ProjectGroup group = (ProjectGroup) parent;
      return group.getProjects().get(index);
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
    if(parent.getClass() == ProjectTreeTableModel.class)
    { // This is the main list of groups
      ProjectTreeTableModel model = (ProjectTreeTableModel) parent;
      return model.groups.size();
    }
    
    if(parent.getClass() == ProjectGroup.class)
    { //We should instead use the list of projects
      ProjectGroup group = (ProjectGroup) parent;
      return group.getProjects().size();
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
    if(parent.getClass() == ProjectTreeTableModel.class)
    { // This is the main list of groups
      ProjectTreeTableModel model = (ProjectTreeTableModel) parent;
      return model.groups.indexOf(child);
    }
    
    if(parent.getClass() == ProjectGroup.class)
    { //We should instead use the list of projects
      ProjectGroup group = (ProjectGroup) parent;
      return group.getProjects().indexOf(child);
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
    return node.getClass() == Project.class;
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
   * TreeTable interface method for rendering JXTreeTables
   * @param node The tree node that serves as the "record" of the table
   * @param column The index of the node's record
   * @return The value to be rendered in the specified column
   */
  public Object getValueAt(Object node, int column)
  {
    assert column < getColumnCount();
    
    if(node.getClass() == ProjectGroup.class)
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
    
    if(node.getClass() == Project.class)
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
    return node.getClass() != ProjectGroup.class || column == 0;
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
    assert value.getClass() == String.class;
    
    if(node.getClass() == ProjectGroup.class)
    { // This is a group node
      ProjectGroup group = (ProjectGroup) node;
      
      switch(column)
      {
        case 0:
          group.setName((String) value);
          break;
      }
    }
    
    if(node.getClass() == Project.class)
    { //We should instead use the list of projects
      Project project = (Project) node;
      
      switch(column)
      {
        case 0:
          project.setName((String) value);
          break;
        case 1:
          project.setTime((String) value);
          break;
      }
    }
    
    else
    {
      super.setValueAt(value, node, column);
    }
  }
}