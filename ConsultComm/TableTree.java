import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * A TableTree is a Swing layout that allows table rows to be used within a tree.
 */
public class TableTree extends JTable {
    protected TableTreeCellRenderer tree;

    /**
     * Create a new TableTree based on the given model
     * @param tableTreeModel A TableTreeModel for the given TableTree
     */
    public TableTree(TableTreeModel tableTreeModel) {
        super();

        //Tell the tree section how to render components
        tree = new TableTreeCellRenderer(tableTreeModel); //Make the hybrid row elements
        ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
        tree.setSelectionModel(selectionWrapper); //Set to override DefaultTreeSelectionModel actions
        
        //Tell the table section how to render components
        TableTreeModelAdapter adapter = new TableTreeModelAdapter(tableTreeModel, tree);
        super.setModel(adapter); //Feed the table a tree
        setSelectionModel(selectionWrapper.getListSelectionModel()); //Set to catch cell options and update JTree accordingly
        setDefaultRenderer(TableTreeModel.class, tree); //Print the tree in the table correctly
        setDefaultEditor(TableTreeModel.class, new TableTreeCellEditor()); //Collapse and expand trees
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Set the current model to something new (i.e. when the timelist changes).
     * Resets the cell renderer, model and selection listeners.
     * @param tableTreeModel A TableTreeModel for the TableTree to pattern itself after
     */
    public void setModel(TableTreeModel tableTreeModel) {

        //Save info from the old model to rebuild the new one
        TableTreeModelAdapter adapter = (TableTreeModelAdapter)super.getModel();
        Vector expanded = adapter.getExpandedPaths(); //Save the expanded rows for the model
        //Save the model's event listeners and formatting
        TableColumn projectColumn = getColumnModel().getColumn(0);
        int projColumnWidth = projectColumn.getPreferredWidth();
        ListToTreeSelectionModelWrapper selectionWrapper = (ListToTreeSelectionModelWrapper)tree.getSelectionModel();
        EventListener[] listeners = selectionWrapper.getListSelectionModelListeners(ListSelectionListener.class);

        //Tell the tree section how to render components
        tree = new TableTreeCellRenderer(tableTreeModel);  //Make the hybrid row elements
        selectionWrapper = new ListToTreeSelectionModelWrapper();
        selectionWrapper.setListSelectionModelListeners(listeners);
        tree.setSelectionModel(selectionWrapper); //Set to override DefaultTreeSelectionModel actions        
        adapter = new TableTreeModelAdapter(tableTreeModel, tree);
        adapter.setExpandedPaths(expanded); //Restore expanded rows
        
        //Tell the table section how to render components
        super.setModel(adapter); //Feed the table a tree        
        projectColumn = getColumnModel().getColumn(0);
        projectColumn.setPreferredWidth(projColumnWidth);
        setSelectionModel(selectionWrapper.getListSelectionModel()); //Set to catch cell options and update JTree accordingly
        setDefaultRenderer(TableTreeModel.class, tree); //Print the tree in the table correctly        
        setDefaultEditor(TableTreeModel.class, new TableTreeCellEditor()); //Collapse and expand trees
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Get the selected index relative to the TimeRecordSet used
     * @return An index into the TimeRecordSet
     */
    public int getSelectedRecordIndex() {
        try {
            TreeModel model = tree.getModel();
            TimeRecord selected = getSelectedRecord();
            TimeRecordSet times = (TimeRecordSet)model.getRoot();
            return times.indexOf(selected);
        } catch(Exception e) {
            return -1;
        }
    }
    
    /**
     * Get the selected project record
     * @return The TimeRecord object currently selected
     */
    public TimeRecord getSelectedRecord() {
        try {
            TreePath path = tree.getSelectionPath();
            return (TimeRecord)path.getLastPathComponent();
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Set the value for the record at the given index into a TimeRecordSet
     * @param value The TimeRecord that contains the needed values to set
     * @param index The index into the TimeRecordSet object to set
     * @param column The column of the TableTree to set (i.e. 0 = Project Name,
     *        1 = Time String)
     */
    public void setRecordAt(Object value, int index, int column) {
        TableTreeModel model = (TableTreeModel)tree.getModel();
        TimeRecordSet times = (TimeRecordSet)model.getRoot();
        TimeRecord selected = times.elementAt(index);
        if(selected == null) return; //Quit if we don't have a selected record
        model.setValueAt(value, selected, column);
    }
    
    /**
     * Renders the individual cells as nodes of a JTree
     */
    public class TableTreeCellRenderer extends JTree implements TableCellRenderer {
        private int visibleRow;
        private ListToTreeSelectionModelWrapper selectionModelWrapper;
       
        /**
         * Creates a new renderer
         * @param model The TreeModel to use, based on a TableTreeModel
         */
        public TableTreeCellRenderer(TreeModel model) {
            super(model);
            setRootVisible(false);
        }

        /**
         * Sets the row height, needed to synchronize both the JTree (this)
         * and the JTable (one higher) to have the same values. Otherwise
         * if the row height is set when less than or equal to 0 we get a 
         * nutty looking JTree.
         * @param rowHeight Sets the height of each cell, in pixels. 
         */
        public void setRowHeight(int rowHeight) {
            if (rowHeight > 0) {
                super.setRowHeight(rowHeight);
                if(TableTree.this != null) TableTree.this.setRowHeight(rowHeight);
            }
        }

        /**
         * Set the bounds for the renderer - we need to set the height
         * to be that of the JTable
         */
        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, TableTree.this.getHeight());
        }
        
        /**
         * Paint the cell to be where the visible row's origin is
         */
        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }
        
        /**
         * Initialize the background to be the table's background, sync the visible row
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(isSelected) setBackground(table.getSelectionBackground());
            else setBackground(table.getBackground());
            visibleRow = row;
            return this;
        }
        
        /**
         * If the node is a TimeRecord, just show the project name, not the <group>-<project name>
         */
        public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if(leaf && value.getClass().equals(TimeRecord.class)) {
                TimeRecord record = (TimeRecord)value;
                return record.getProjectName();
            } else {
                return value.toString();
            }
        }
    }
    
    /**
     * Used so we have the convenience of a JTable cell's keystrokes. Mouse events are forwarded
     * to the JTree, but all other stuff is sent to the JTable's event handler.
     */
    public class TableTreeCellEditor implements TableCellEditor {
        protected EventListenerList listenerList = new EventListenerList();
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            return tree;
        }
        
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                for (int counter = getColumnCount() - 1; counter >= 0; counter--) {
                    if (getColumnClass(counter) == TableTreeModel.class) { //Find the JTree node in the table's row
                        MouseEvent me = (MouseEvent)e;
                        MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - getCellRect(0, counter, true).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
                        tree.dispatchEvent(newME); //Send event to JTree
                        break;
                    }
                }
            }
            return false;
        }

        //Default stuff for the interface
        public Object getCellEditorValue() { return null; }
        public boolean shouldSelectCell(EventObject anEvent) { return false; }
        public boolean stopCellEditing() { return true; }
        public void cancelCellEditing() {}
        public void addCellEditorListener(CellEditorListener l) { listenerList.add(CellEditorListener.class, l); }
        public void removeCellEditorListener(CellEditorListener l) { listenerList.remove(CellEditorListener.class, l); }
    }
    
    /**
     * Used to sync every selection from the Table's rows - so if you click on a 
     * column that is not a node in the JTree then the tree still updates its
     * selected index
     */
    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
        protected boolean updatingListSelectionModel; //True when paths are the only thing that need updated
        
        public ListToTreeSelectionModelWrapper() {
            super();
            
            //Select the appropriate tree node
            getListSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if(! updatingListSelectionModel) { //Stop updater from calling itself recursively
                        updatingListSelectionModel = true; //Don't call ourselves!
                        int index = listSelectionModel.getMinSelectionIndex(); //Only single selections allowed
                        if(index != -1 && listSelectionModel.isSelectedIndex(index)) {
                            clearSelection();
                            TreePath selPath = tree.getPathForRow(index);
                            if(selPath != null) addSelectionPath(selPath);
                        }
                        updatingListSelectionModel = false; //Done
                    }
                }
            });
        }
        
        ListSelectionModel getListSelectionModel() { return listSelectionModel; }
        EventListener[] getListSelectionModelListeners(Class classType) { return listSelectionModel.getListeners(classType); }
        void setListSelectionModelListeners(EventListener[] listeners) { 
            for(int i=0; i<listeners.length; i++)
                listSelectionModel.addListSelectionListener((ListSelectionListener)listeners[i]);
        }
    }
}
