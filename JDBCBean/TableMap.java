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
    
    protected void clearFieldMaps() throws java.sql.SQLException {
        fieldMaps.clear();
        Connection conn = jdbc.openConnection();
        DatabaseMetaData dbmeta = conn.getMetaData();
        ResultSet cols = dbmeta.getColumns(null, jdbc.getDatabase(), jdbc.getTable(), null);
        while(cols.next())
            fieldMaps.addElement(new FieldMap(cols.getString(4), cols.getShort(5), cols.getInt(17), ""));
        cols.close();
        conn.close();
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
            model.addRow(new Object[] {record.getDbFieldName(), typeString(record.getSqlType()), record.getValueExpression()});
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
            String projectName = record.toString();
            ProjectMap projectMap = (ProjectMap)projectMaps.get(projectName);
            if(projectMap == null) model.addRow(new Object[] {new Boolean(false), projectName, ""});
            else model.addRow(new Object[] {new Boolean(projectMap.getExport()), projectName, projectMap.getAlias()});
        }
        return model;
    }
    
    public static String typeString(int sqlType) {
        switch(sqlType) {
            case java.sql.Types.ARRAY:
                return "ARRAY";
            case java.sql.Types.BIGINT:
                return "BIGINT";
            case java.sql.Types.BINARY:
                return "BINARY";
            case java.sql.Types.BIT:
                return "BIT";
            case java.sql.Types.BLOB:
                return "BLOB";
            case java.sql.Types.CHAR:
                return "CHAR";
            case java.sql.Types.CLOB:
                return "CLOB";
            case java.sql.Types.DATE:
                return "DATE";
            case java.sql.Types.DECIMAL:
                return "DECIMAL";
            case java.sql.Types.DOUBLE:
                return "DOUBLE";
            case java.sql.Types.FLOAT:
                return "FLOAT";
            case java.sql.Types.INTEGER:
                return "INTEGER";
            case java.sql.Types.LONGVARBINARY:
                return "LONGVARBINARY";
            case java.sql.Types.LONGVARCHAR:
                return "LONGVARCHAR";
            case java.sql.Types.NULL:
                return "NULL";
            case java.sql.Types.NUMERIC:
                return "NUMERIC";
            case java.sql.Types.SMALLINT:
                return "SMALLINT";
            case java.sql.Types.TIME:
                return "TIME";
            case java.sql.Types.TIMESTAMP:
                return "TIMESTAMP";
            case java.sql.Types.TINYINT:
                return "TINYINT";
            case java.sql.Types.VARBINARY:
                return "VARBINARY";
            case java.sql.Types.VARCHAR:
                return "VARCHAR";
            default:
                return "OTHER";
        }
    }
}
