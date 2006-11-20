/*
 * AbstractTableTreeModel.java
 *
 * Created on November 19, 2006, 11:27 PM
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

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author jellis
 */
public abstract class AbstractTableTreeModel implements TableTreeModel {
  private Object rootNode;
  private EventListenerList listeners;
  
  /** Creates a new instance of AbstractTableTreeModel */
  public AbstractTableTreeModel(Object rootNode) {
    this.rootNode = rootNode;
    this.listeners = new EventListenerList();
  }

  public Object getRoot() {
    return rootNode;
  }

  public boolean isLeaf(Object node) {
    return getChildCount(node) == 0;
  }

  public void valueForPathChanged(TreePath treePath, Object node) {
    return;
  }

  public int getIndexOfChild(Object parentNode, Object childNode) {
    for(int i = 0, max = getChildCount(parentNode); i < max; i++) {
      if(getChild(parentNode, i).equals(childNode)) {
        return i;
      }
    }
    return -1; //Failsafe
  }

  public void addTreeModelListener(TreeModelListener listener) {
    listeners.add(TreeModelListener.class, listener);
  }

  public void removeTreeModelListener(TreeModelListener listener) {
    listeners.remove(TreeModelListener.class, listener);
  }  
}
