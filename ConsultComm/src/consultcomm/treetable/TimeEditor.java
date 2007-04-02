package consultcomm.treetable;

import consultcomm.project.Time;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 * Control the components & edited values when editing a table cell.
 *
 * @author jellis
 */
public class TimeEditor
    extends DefaultCellEditor
{
  
  /** Creates a new instance of TimeEditor */
  public TimeEditor()
  {
    super(new JFormattedTextField());
    
    DateFormat numberFormat = new SimpleDateFormat("HH:mm:ss");
    numberFormat.setTimeZone(new SimpleTimeZone(0, "NONE"));
    
    DateFormatter numberFormatter = new DateFormatter(numberFormat);
    
    ((JFormattedTextField) getComponent()).setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
  }
  
  /**
   * Parse the value given to us after the cell is edited and pass it along to the
   * setValueAt method of the table's model.
   * @see ProjectTreeTableModel.setValueAt(Object, Object, int)
   * @return The value we want setValueAt to parse
   */
  public Object getCellEditorValue()
  {
    JFormattedTextField textField = (JFormattedTextField) getComponent();
    Object textValue = textField.getValue();
    
    assert textValue instanceof Date;
    
    return new Time(((Date) textValue).getTime());
  }
}
