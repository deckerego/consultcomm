package consultcomm.treetable;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author jellis
 */
public class TimeRenderer
    extends DefaultTableCellRenderer
{
  public int getHorizontalAlignment()
  {
    return SwingConstants.RIGHT;
  }
}
