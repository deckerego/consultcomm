package consultcomm;

import consultcomm.project.Project;
import consultcomm.project.ProjectGroup;
import consultcomm.project.Time;
import consultcomm.treetable.ProjectTreeTableModel;
import consultcomm.treetable.TimeRenderer;
import consultcomm.treetable.TimeEditor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.prefs.Preferences;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 * The main instance that invokes ConsultComm
 * @author  jellis
 */
public class ConsultComm extends javax.swing.JFrame
{
  private static final String WHOAMI = "ConsultComm 4";
  private static final File prefsdir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+".consultcomm"); //TODO: Make Windows-specific directories
  private static enum ClientProperties { CLICKED_TREEPATH };
  
  /** Creates new form ConsultComm */
  public ConsultComm()
  {
    initComponents();
    loadPrefs();
  }
  
  private void loadPrefs()
  {
    if(! prefsdir.exists()) prefsdir.mkdir();
    
    try
    { //Get all projects
      File prefsFile = new File(prefsdir, "projects.xml");
      
      if(! prefsFile.exists()) 
      { //Create new preferences file
        prefsFile.createNewFile();
        projectTreeTable.setTreeTableModel(new ProjectTreeTableModel());
      }
      else
      { //Read in existing preferences file
        FileInputStream inStream = new FileInputStream(prefsFile);
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(inStream));
        projectTreeTable.setTreeTableModel((ProjectTreeTableModel) d.readObject());
        d.close();
      }
    }
    catch (Exception e)
    {
      System.err.println("Cannot read projects file: "+e);
    }
  }
  
  private void savePrefs()
  {
    if(! prefsdir.exists()) prefsdir.mkdir();
    
    try
    { //Save projects
      File prefsFile = new File(prefsdir, "projects.xml");
      FileOutputStream outStream = new FileOutputStream(prefsFile);
      XMLEncoder e = new XMLEncoder(new BufferedOutputStream(outStream));
      e.writeObject(projectTreeTable.getTreeTableModel());
      e.close();
    }
    catch (Exception e)
    {
      System.err.println("Cannot read projects file: "+e);
    }
  }
 
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents()
  {
    projectMenu = new javax.swing.JPopupMenu();
    renameProject = new javax.swing.JMenuItem();
    deleteProject = new javax.swing.JMenuItem();
    groupMenu = new javax.swing.JPopupMenu();
    addProject = new javax.swing.JMenuItem();
    renameGroup = new javax.swing.JMenuItem();
    deleteGroup = new javax.swing.JMenuItem();
    projectScrollPane = new javax.swing.JScrollPane();
    projectTreeTable = new org.jdesktop.swingx.JXTreeTable();
    menuBar = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    exitMenuItem = new javax.swing.JMenuItem();
    helpMenu = new javax.swing.JMenu();
    contentsMenuItem = new javax.swing.JMenuItem();
    aboutMenuItem = new javax.swing.JMenuItem();

    renameProject.setText("Rename Project");
    renameProject.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        renameProject(evt);
      }
    });

    projectMenu.add(renameProject);

    deleteProject.setText("Delete Project");
    deleteProject.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        deleteProject(evt);
      }
    });

    projectMenu.add(deleteProject);

    addProject.setText("Add Project");
    addProject.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        addProject(evt);
      }
    });

    groupMenu.add(addProject);

    renameGroup.setText("Rename Group");
    renameGroup.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        renameGroup(evt);
      }
    });

    groupMenu.add(renameGroup);

    deleteGroup.setText("Delete Group");
    deleteGroup.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        deleteGroup(evt);
      }
    });

    groupMenu.add(deleteGroup);

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle(WHOAMI);
    addWindowListener(new java.awt.event.WindowAdapter()
    {
      public void windowClosing(java.awt.event.WindowEvent evt)
      {
        formWindowClosing(evt);
      }
    });

    projectTreeTable.setPreferredScrollableViewportSize(new java.awt.Dimension(400, 200));
    projectTreeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    projectTreeTable.sizeColumnsToFit(0);
    projectTreeTable.setDefaultRenderer(Time.class, new TimeRenderer());
    projectTreeTable.setDefaultEditor(Time.class, new TimeEditor());
    projectTreeTable.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        projectTreeTableMouseClicked(evt);
      }
    });

    projectScrollPane.setViewportView(projectTreeTable);

    getContentPane().add(projectScrollPane, java.awt.BorderLayout.CENTER);

    fileMenu.setText("File");
    exitMenuItem.setText("Exit");
    exitMenuItem.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        exitMenuItemActionPerformed(evt);
      }
    });

    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);

    helpMenu.setText("Help");
    contentsMenuItem.setText("Contents");
    helpMenu.add(contentsMenuItem);

    aboutMenuItem.setText("About");
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    setJMenuBar(menuBar);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void deleteGroup(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteGroup
  {//GEN-HEADEREND:event_deleteGroup
    assert groupMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH) != null;
    ProjectTreeTableModel projectList = (ProjectTreeTableModel) projectTreeTable.getTreeTableModel();
    
    if(projectList.getGroups().size() == 1)
    { //If this is the last group, don't get rid of it
      //TODO send the user a warning
    }
    
    else
    {
      TreePath clickedPath = (TreePath) groupMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH);
      ProjectGroup projectGroup = (ProjectGroup) clickedPath.getLastPathComponent();
    
      projectList.getGroups().remove(projectGroup);
    
      projectTreeTable.updateUI(); //TODO We may be able to omit this line after property change listeners are fixed in 0.9      
    }
  }//GEN-LAST:event_deleteGroup

  private void renameGroup(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renameGroup
  {//GEN-HEADEREND:event_renameGroup
    assert groupMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH) != null;
    TreePath clickedPath = (TreePath) groupMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH);
    int row = projectTreeTable.getRowForPath(clickedPath);
    projectTreeTable.editCellAt(row, 0);
  }//GEN-LAST:event_renameGroup

  private void renameProject(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renameProject
  {//GEN-HEADEREND:event_renameProject
    assert projectMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH) != null;
    TreePath clickedPath = (TreePath) projectMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH);
    int row = projectTreeTable.getRowForPath(clickedPath);
    projectTreeTable.editCellAt(row, 0);
  }//GEN-LAST:event_renameProject

  private void deleteProject(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteProject
  {//GEN-HEADEREND:event_deleteProject
    assert projectMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH) != null;
    TreePath clickedPath = (TreePath) projectMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH);
    Project project = (Project) clickedPath.getLastPathComponent();
    
    TreePath parentPath = clickedPath.getParentPath();
    ProjectGroup projectGroup = (ProjectGroup) parentPath.getLastPathComponent();
    
    projectGroup.getProjects().remove(project);
    
    projectTreeTable.updateUI(); //TODO We may be able to omit this line after property change listeners are fixed in 0.9
  }//GEN-LAST:event_deleteProject

  private void addProject(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addProject
  {//GEN-HEADEREND:event_addProject
    assert groupMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH) != null;
    TreePath clickedPath = (TreePath) groupMenu.getClientProperty(ClientProperties.CLICKED_TREEPATH);
    ProjectGroup projectGroup = (ProjectGroup) clickedPath.getLastPathComponent();
    
    Project project = new Project();
    projectGroup.getProjects().add(project);
    
    projectTreeTable.updateUI(); //TODO We may be able to omit this line after property change listeners are fixed in 0.9
    
    int row = projectTreeTable.getRowForPath(clickedPath.pathByAddingChild(project));
    projectTreeTable.editCellAt(row, 0);
  }//GEN-LAST:event_addProject

  private void projectTreeTableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_projectTreeTableMouseClicked
  {//GEN-HEADEREND:event_projectTreeTableMouseClicked
    switch(evt.getButton())
    {
      case MouseEvent.BUTTON1: //Left mouse click
        break;
      case MouseEvent.BUTTON3: //Right mouse click
        TreePath clickedPath = projectTreeTable.getPathForLocation(evt.getX(), evt.getY());
        
        if(clickedPath.getLastPathComponent() instanceof Project)
        { //Show the project pop-up menu
          projectMenu.putClientProperty(ClientProperties.CLICKED_TREEPATH, clickedPath);
          projectMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
        
        if(clickedPath.getLastPathComponent() instanceof ProjectGroup)
        { //Show the group pop-up menu
          groupMenu.putClientProperty(ClientProperties.CLICKED_TREEPATH, clickedPath);
          groupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
        
        break;
      case MouseEvent.BUTTON2: //Middle mouse click
        break;
    }
  }//GEN-LAST:event_projectTreeTableMouseClicked

  private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
  {//GEN-HEADEREND:event_formWindowClosing
    savePrefs();
  }//GEN-LAST:event_formWindowClosing
  
  private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitMenuItemActionPerformed
  {
    System.exit(0);
  }//GEN-LAST:event_exitMenuItemActionPerformed
  
  /**
   * @param args the command line arguments
   */
  public static void main(String args[])
  {
    java.awt.EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        new ConsultComm().setVisible(true);
      }
    });
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem aboutMenuItem;
  private javax.swing.JMenuItem addProject;
  private javax.swing.JMenuItem contentsMenuItem;
  private javax.swing.JMenuItem deleteGroup;
  private javax.swing.JMenuItem deleteProject;
  private javax.swing.JMenuItem exitMenuItem;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JPopupMenu groupMenu;
  private javax.swing.JMenu helpMenu;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JPopupMenu projectMenu;
  private javax.swing.JScrollPane projectScrollPane;
  private org.jdesktop.swingx.JXTreeTable projectTreeTable;
  private javax.swing.JMenuItem renameGroup;
  private javax.swing.JMenuItem renameProject;
  // End of variables declaration//GEN-END:variables
  
}
