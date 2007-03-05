/*
 * TableTreeCellRenderer.java
 *
 * Created on November 14, 2006, 11:02 PM
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

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeModel;

/**
 *
 * @author jellis
 */
public class TableTreeCellRenderer 
extends JTree
implements TableCellRenderer {
  protected int visibleRow;
  
  public TableTreeCellRenderer(TreeModel model) {
    super(model);
    
    //We don't want the model adapter invoking getValueAt for the root (parent) node
    setRootVisible(false);
  }
  
  public Component getTableCellRendererComponent(JTable jTable, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
    visibleRow = row;
    return this;
  }
  
  public void setBounds(int x, int y, int w, int h) {
    super.setBounds(x, 0, w, this.getHeight());
  }
  
  public void paint(Graphics g) {
    g.translate(0, -visibleRow * getRowHeight());
    super.paint(g);
  }
}
