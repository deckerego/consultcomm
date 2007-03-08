/*
 * ProjectTreeTableModel.java
 *
 * Created on March 6, 2007, 8:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package consultcomm.treetable;

import consultcomm.*;
import consultcomm.project.Project;
import consultcomm.project.ProjectGroup;
import java.util.ArrayList;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 * @author jellis
 */
public class ProjectTreeTableModel extends DefaultTreeTableModel
{
  //TODO Make the column names i18n-ized
  public static final String[] COLUMNS = {"Project", "Time"};
  
  ArrayList<ProjectGroup> groups;
  
  /**
   * Creates a new instance of ProjectTreeTableModel 
   */
  public ProjectTreeTableModel(ArrayList<ProjectGroup> groups)
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
    
    else
    { //Assume we should instead use the list of projects
      assert parent.getClass() == ProjectGroup.class;
      ProjectGroup group = (ProjectGroup) parent;
      return group.projects.get(index);
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
    
    else
    { //Assume we should instead use the list of projects
      assert parent.getClass() == ProjectGroup.class;
      ProjectGroup group = (ProjectGroup) parent;
      return group.projects.size();
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
    
    else
    { //Assume we should instead use the list of projects
      assert parent.getClass() == ProjectGroup.class;
      ProjectGroup group = (ProjectGroup) parent;
      return group.projects.indexOf(child);
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
    return COLUMNS.length;
  }
  
  /**
   * TableModel interface method for rendering JTables
   * @param index The index number of the column
   * @return The name of the column
   */
  public String getColumnName(int index)
  {
    assert index < getColumnCount();
    return COLUMNS[index];
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
    { // This is the main list of groups
      ProjectGroup group = (ProjectGroup) node;
      
      switch(column) {
        case 0:
          return group.name;
        default:
          return null;
      }
    }
    
    else
    { //Assume we should instead use the list of projects
      assert node.getClass() == Project.class;
      Project project = (Project) node;
      
      switch(column) {
        case 0:
          return project.name;
        default:
          return project.time;
      }
    }
  }
}
