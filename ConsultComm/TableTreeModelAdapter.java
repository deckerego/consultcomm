import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;

public class TableTreeModelAdapter extends AbstractTableModel {
    private JTree tree;
    private TableTreeModel model;
    private Vector expandedRows = new Vector();

    public TableTreeModelAdapter(TableTreeModel model, JTree tree) {
        this.tree = tree;
        this.model = model;

	tree.addTreeExpansionListener(new TreeExpansionListener() {
	    public void treeExpanded(TreeExpansionEvent evt) {
	      fireTableDataChanged();
              //Remeber which nodes were opened
              JTree tree = (JTree)evt.getSource();
              TreePath selectedPath = tree.getSelectionPath();
              if(selectedPath != null) expandedRows.addElement(selectedPath);
	    }
            public void treeCollapsed(TreeExpansionEvent evt) {
	      fireTableDataChanged();
              //Forget closed nodes
              JTree tree = (JTree)evt.getSource();
              TreePath selectedPath = tree.getSelectionPath();
              if(selectedPath != null) expandedRows.remove(selectedPath);
	    }
	});
    }

    public int getColumnCount() {
	return model.getColumnCount();
    }

    public String getColumnName(int column) {
	return model.getColumnName(column);
    }

    public Class getColumnClass(int column) {
	return model.getColumnClass(column);
    }

    public int getRowCount() {
	return tree.getRowCount();
    }
    
    public Vector getExpandedRows() {
        return expandedRows;
    }
    
    public void setExpandedRows(Vector expandedRows) {
        this.expandedRows = expandedRows;
        //Reset expanded rows
        for(int i=0; i<expandedRows.size(); i++)
            tree.expandPath((TreePath)expandedRows.elementAt(i));
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

