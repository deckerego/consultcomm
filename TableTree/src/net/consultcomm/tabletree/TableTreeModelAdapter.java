/*
 * TableTreeModelAdapter.java
 *
 * Created on November 19, 2006, 4:53 PM
 *
 * Copyright (C) 2006 by John T. Ellis
 * jellis
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.consultcomm.tabletree;

import javax.swing.JTree;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author jellis
 */
public class TableTreeModelAdapter extends AbstractTableModel {
  JTree tree;
  TableTreeModel model;
  
  /** Creates a new instance of TableTreeModelAdapter */
  public TableTreeModelAdapter(TableTreeModel model, JTree tree) {
    this.tree = tree;
    this.model = model;
  }

  public int getRowCount() {
    return tree.getRowCount();
  }

  public int getColumnCount() {
    return model.getColumnCount();
  }

  //Traverse the JTree and get its component instead
  public Object getValueAt(int row, int column) {
    //The root node for the tree will have no parent and is the first element
    TreePath selectedPath = tree.getPathForRow(row);
    
    //Have the model interpret the value for us based on the component & column #
    return model.getValueAt(selectedPath.getLastPathComponent(), column);
  }
}
