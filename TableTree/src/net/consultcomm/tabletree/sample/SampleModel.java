/*
 * TestModel.java
 *
 * Created on November 19, 2006, 11:24 PM
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

package net.consultcomm.tabletree.sample;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import net.consultcomm.tabletree.*;

/**
 *
 * @author jellis
 */
public class SampleModel extends AbstractTableTreeModel implements TableTreeModel {
  static final private String[] columnNames = {"Column0", "Column1"};
  static final private String[] rowNames = {"Row0", "Row1", "Row2"};
  
  public SampleModel() {
    super(new Object());
  }
  
  public int getColumnCount() {
    return columnNames.length;
  }

  public Object getValueAt(Object cell, int column) {
    return cell.toString() + column;
  }

  public Object getChild(Object object, int i) {
    return rowNames[i];
  }

  public int getChildCount(Object object) {
    return rowNames.length;
  }
}
