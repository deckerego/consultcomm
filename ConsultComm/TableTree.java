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
        
        ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
        tree.setSelectionModel(selectionWrapper);
        setSelectionModel(selectionWrapper.getListSelectionModel());

        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void setModel(TableTreeModel tableTreeModel) {
        tree = new TableTreeCellRenderer(tableTreeModel);
        super.setModel(new TableTreeModelAdapter(tableTreeModel, tree));
        setDefaultRenderer(TableTreeModel.class, tree);
    }
    
    public void updateUI() {
        super.updateUI();
        if(tree != null) tree.updateUI();
        LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
    }
    
    public int getEditingRow() {
        return getColumnClass(editingColumn) == TableTreeModel.class ? -1 : editingRow;
    }
    
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        if (tree != null && tree.getRowHeight() != rowHeight)
            tree.setRowHeight(getRowHeight());
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
        tree.expandPath(path.getParentPath());
    }

    public class TableTreeCellRenderer extends JTree implements TableCellRenderer {
        protected int visibleRow;
        
        public TableTreeCellRenderer(TreeModel model) {
            super(model);
            setRootVisible(false);
        }
        
        public void updateUI() {
            super.updateUI();
            TreeCellRenderer tcr = getCellRenderer();
            if (tcr instanceof DefaultTreeCellRenderer) {
                DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer)tcr);
                dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
                dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
            }
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
        
        protected void fireEditingStopped() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length-2; i>=0; i-=2)
                if (listeners[i]==CellEditorListener.class)
                    ((CellEditorListener)listeners[i+1]).editingStopped(new ChangeEvent(this));
        }
        
        protected void fireEditingCanceled() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length-2; i>=0; i-=2)
                if (listeners[i]==CellEditorListener.class)
                    ((CellEditorListener)listeners[i+1]).editingCanceled(new ChangeEvent(this));
        }
        
    }
    
    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
        protected boolean updatingListSelectionModel;
        
        public ListToTreeSelectionModelWrapper() {
            super();
            getListSelectionModel().addListSelectionListener(createListSelectionListener());
        }
        
        ListSelectionModel getListSelectionModel() {
            return listSelectionModel;
        }
        
        public void resetRowSelection() {
            if(!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try { super.resetRowSelection(); } 
                finally { updatingListSelectionModel = false; }
            }
        }
        
        protected ListSelectionListener createListSelectionListener() {
            return new ListSelectionHandler();
        }
        
        protected void updateSelectedPathsFromSelectedRows() {
            if(!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    int min = listSelectionModel.getMinSelectionIndex();
                    int max = listSelectionModel.getMaxSelectionIndex();
                    
                    clearSelection();
                    if(min != -1 && max != -1) {
                        for(int counter = min; counter <= max; counter++) {
                            if(listSelectionModel.isSelectedIndex(counter)) {
                                TreePath selPath = tree.getPathForRow(counter);
                                if(selPath != null) addSelectionPath(selPath);
                            }
                        }
                    }
                } finally {
                    updatingListSelectionModel = false;
                }
            }
        }
        
        class ListSelectionHandler implements ListSelectionListener {
            public void valueChanged(ListSelectionEvent e) {
                updateSelectedPathsFromSelectedRows();
            }
        }
    }
}
