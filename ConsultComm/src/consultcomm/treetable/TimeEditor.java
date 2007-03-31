package consultcomm.treetable;

import consultcomm.project.Time;
import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;

/**
 * Control the components & edited values when editing a table cell.
 *
 * @author jellis
 */
public class TimeEditor
    extends DefaultCellEditor
{
  private DateFormat numberFormat;
  
  /** Creates a new instance of TimeEditor */
  public TimeEditor()
  {
    super(new JFormattedTextField());
    
    this.numberFormat = new SimpleDateFormat("HH:mm:ss");
    this.numberFormat.setTimeZone(new SimpleTimeZone(0, "NONE"));
  }
  
  /**
   * Provides a value object that represents the given cell when it is being edited.
   * @param table The parent component that "owns" the cell
   * @param value The actual value in the cell
   * @param isSelected Is the cell currently selected?
   * @param row The row index of the cell
   * @param column The column index of the cell
   * @return The component representing the table's cell
   */
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
  {
    JFormattedTextField textField = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
    textField.setValue(value);
    return textField;
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
    
    assert textValue instanceof String || textValue instanceof Time;
    
    if(textValue instanceof String)
    {
      String stringValue = (String) textValue;
      
      if(stringValue.matches("[0-9]*"))
      { //Maybe the user just typed in a whole number. Let's assume they meant the number of elapsed minutes
        Long elapsed = Long.parseLong(stringValue);
        return new Time(elapsed * 60 * 1000);
      }
      
      else if(stringValue.matches("[0-9]*:[0-9]*"))
      { //The user may have just typed in hh:mm, so we'll make the string well-formed
        stringValue += ":00";
      }
      
      try
      { //Actually parse the date based on the format 24:60:60
        Date elapsed = this.numberFormat.parse(stringValue);
        return new Time(elapsed.getTime());
      }
      
      catch(ParseException e)
      {
        System.err.println("Couldn't parse "+textValue+": "+e);
        return textValue;
      }
    }
    
    else
    {
      return textValue;
    }
  }
}
