import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;

public class TableTreeModelAdapter extends AbstractTableModel {
    private JTree tree;
    private TableTreeModel model;
    private Vector expandedPaths = new Vector();

    public TableTreeModelAdapter(TableTreeModel model, JTree tree) {
        this.tree = tree;
        this.model = model;

	tree.addTreeExpansionListener(new TreeExpansionListener() {
	    public void treeExpanded(TreeExpansionEvent evt) {
	      fireTableDataChanged();
              //Remeber which nodes were opened
              JTree tree = (JTree)evt.getSource();
              TreePath selectedPath = tree.getSelectionPath();
              if(selectedPath != null) expandedPaths.addElement(selectedPath);
	    }
            public void treeCollapsed(TreeExpansionEvent evt) {
	      fireTableDataChanged();
              //Forget closed nodes
              JTree tree = (JTree)evt.getSource();
              TreePath selectedPath = tree.getSelectionPath();
              if(selectedPath != null) expandedPaths.remove(selectedPath);
	    }
	});
    }

    public int getColumnCount() { return model.getColumnCount(); }
    public String getColumnName(int column) { return model.getColumnName(column); }
    public Class getColumnClass(int column) { return model.getColumnClass(column); }
    public int getRowCount() {	return tree.getRowCount(); }
    public Vector getExpandedPaths() { return expandedPaths; }

    public Vector getExpandedRows() {
        Vector rows = new Vector();
        for(int i=0, max=expandedPaths.size(); i<max; i++) {
            TreePath path = (TreePath)expandedPaths.elementAt(i);
            Integer rowIndex = new Integer(tree.getRowForPath(path));
            if(rowIndex.intValue() > -1) rows.addElement(rowIndex);
        }
        return rows;
    }
    
    public void setExpandedPaths(Vector expandedPaths) {
        this.expandedPaths = expandedPaths;
        //Reset expanded rows
        for(int i=0, max=expandedPaths.size(); i<max; i++)
            tree.expandPath((TreePath)expandedPaths.elementAt(i));
    }
    
    public void setExpandedRows(Vector expandedRows) {
        Collections.sort(expandedRows); //Ensure proper order for restoring
        this.expandedPaths = new Vector();
        for(int i=0, max=expandedRows.size(); i<max; i++) {
            Integer row = (Integer)expandedRows.elementAt(i);
            TreePath path = tree.getPathForRow(row.intValue());
            tree.expandPath(path);
            this.expandedPaths.addElement(path);
        }
    }
    
    protected Object nodeForRow(int row) {
	TreePath treePath = tree.getPathForRow(row);
	return treePath.getLastPathComponent();         
    }

    public Object getValueAt(int row, int column) {
	return model.getValueAt(nodeForRow(row), column);
    }

    public boolean isCellEditable(int row, int column) {
         return model.isCellEditable(nodeForRow(row), column); 
    }

    public void setValueAt(Object value, int row, int column) {
	model.setValueAt(value, nodeForRow(row), column);
    }
}

