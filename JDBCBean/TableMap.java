import java.util.*;
import java.sql.*;
import javax.swing.table.*;

public class TableMap implements java.io.Serializable {
    private final String[] projectNameTitles = {"Export", "Project", "Alias"};
    private final String[] fieldValueTitles = {"Field Name", "Type", "Value"};
    
    private Vector fieldMaps;
    private Hashtable projectMaps;
    private JDBCConnect jdbc;
    
    public TableMap() {
        fieldMaps = new Vector();
        projectMaps = new Hashtable();
    }
    
    public void setProjectMaps(Hashtable projectMaps) { this.projectMaps = projectMaps; }
    public Hashtable getProjectMaps() { return this.projectMaps; }
    public void setFieldMaps(Vector fieldMaps) { this.fieldMaps = fieldMaps; }
    public Vector getFieldMaps() { return this.fieldMaps; }

    public void setConnection(JDBCConnect jdbc) { this.jdbc = jdbc; }
    
    protected void init() throws java.sql.SQLException {
        fieldMaps.clear();
        Connection conn = jdbc.openConnection();
        DatabaseMetaData dbmeta = conn.getMetaData();
        ResultSet cols = dbmeta.getColumns(null, jdbc.getDatabase(), jdbc.getTable(), null);
        while(cols.next())
            fieldMaps.addElement(new FieldMap(cols.getString(4), cols.getShort(5), cols.getInt(17), ""));
        cols.close();
        conn.close();
    }
    
    public int size() {
        return fieldMaps.size();
    }
    
    public FieldMap elementAt(int i) {
        return (FieldMap)fieldMaps.elementAt(i);
    }
    
    public DefaultTableModel toFieldValuesTableModel(){
        DefaultTableModel model = new javax.swing.table.DefaultTableModel(
        //Set to three empty columns
        new Object [][][] {
        },
        fieldValueTitles
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                boolean[] editable = {false, false, true};
                return editable[columnIndex];
            }
        };
        
        Enumeration records = fieldMaps.elements();
        while (records.hasMoreElements()) {
            FieldMap record = (FieldMap)records.nextElement();
            model.addRow(new Object[] {record.getDbFieldName(), JDBCConnect.typeString(record.getSqlType()), record.getValueExpression()});
        }
        return model;
    }
    
    public DefaultTableModel toProjectNamesTableModel(){
        DefaultTableModel model = new javax.swing.table.DefaultTableModel(
        //Set to three empty columns
        new Object [][][] {
        },
        projectNameTitles
        ) {
            public Class getColumnClass(int columnIndex) {
                Class[] types = new Class[] {Boolean.class, String.class, String.class};
                return types[columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                boolean[] editable = {true, false, true};
                return editable[columnIndex];
            }
        };
        
        TimeRecordSet records = jdbc.clntComm.getTimes();
        for (int i=0; i<records.size(); i++) {
            TimeRecord record = (TimeRecord)records.elementAt(i);
            String projectName = record.getProjectName();
            ProjectMap projectMap = (ProjectMap)projectMaps.get(projectName);
            if(projectMap == null) model.addRow(new Object[] {new Boolean(false), projectName, ""});
            else model.addRow(new Object[] {new Boolean(projectMap.getExport()), projectName, projectMap.getAlias()});
        }
        return model;
    }
}
