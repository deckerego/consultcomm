import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TableTree extends JTable {
    protected TableTreeCellRenderer tree;
    
    public TableTree(TableTreeModel tableTreeModel) {
        super();
        tree = new TableTreeCellRenderer(tableTreeModel);
        super.setModel(new TableTreeModelAdapter(tableTreeModel, tree));
        setDefaultRenderer(TableTreeModel.class, tree);
        setDefaultEditor(TableTreeModel.class, new TableTreeCellEditor());

        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void setModel(TableTreeModel tableTreeModel) {
        tree = new TableTreeCellRenderer(tableTreeModel);
        super.setModel(new TableTreeModelAdapter(tableTreeModel, tree));
        setDefaultRenderer(TableTreeModel.class, tree);
    }

    public JTree getTree() {
        return tree;
    }
    
    //Compatibility methods for JTable
    public int getSelectedRecord() {
        try {
            TreePath path = tree.getSelectionPath();
            TreeModel model = tree.getModel();
            TimeRecord selected = (TimeRecord)path.getLastPathComponent();
            TimeRecordSet times = (TimeRecordSet)model.getRoot();
            return times.indexOf(selected);
        } catch(Exception e) {
            return -1;
        }
    }

    public void setSelectedRecord(int index) {
        TreeModel model = tree.getModel();
        TimeRecordSet times = (TimeRecordSet)model.getRoot();
        TimeRecord selected = times.elementAt(index);
        TreePath path = new TreePath(selected);
        tree.setSelectionPath(path);
    }

    public void setRecordAt(Object value, int index, int column) {
        TableTreeModel model = (TableTreeModel)tree.getModel();
        TimeRecordSet times = (TimeRecordSet)model.getRoot();
        TimeRecord selected = times.elementAt(index);
        model.setValueAt(value, selected, column);
    }

    public class TableTreeCellRenderer extends JTree implements TableCellRenderer {
        protected int visibleRow;
        
        public TableTreeCellRenderer(TreeModel model) {
            super(model);
            setRootVisible(false);
        }

        public void setRowHeight(int rowHeight) {
            if (rowHeight > 0) {
                super.setRowHeight(rowHeight);
                if (TableTree.this != null && TableTree.this.getRowHeight() != rowHeight)
                    TableTree.this.setRowHeight(getRowHeight());
            }
        }

        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, TableTree.this.getHeight());
        }
        
        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(isSelected) setBackground(table.getSelectionBackground());
            else setBackground(table.getBackground());
            visibleRow = row;
            return this;
        }
    }

    public class TableTreeCellEditor implements TableCellEditor {
        protected EventListenerList listenerList = new EventListenerList();
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            return tree;
        }

        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                for (int counter = getColumnCount() - 1; counter >= 0; counter--) {
                    if (getColumnClass(counter) == TableTreeModel.class) {
                        MouseEvent me = (MouseEvent)e;
                        MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - getCellRect(0, counter, true).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
                        tree.dispatchEvent(newME);
                        break;
                    }
                }
            }
            return false;
        }

        public Object getCellEditorValue() { return null; }
        public boolean shouldSelectCell(EventObject anEvent) { return false; }
        public boolean stopCellEditing() { return true; }
        public void cancelCellEditing() {}

        public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
        }
        
        public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
        }

    }
}
