import java.util.*;
import java.sql.*;
import javax.swing.*;

public class FieldMap implements java.io.Serializable {
    final static int DATE_SQLDATE = 0;
    final static int DATE_SQLTIMESTAMP = 1;
    final static int DATE_CCYYMMDD = 2;
    final static int HOUR_FULL = 0;
    final static int HOUR_QUARTER = 1;
    final static int HOUR_TENTH = 2;
    final static int SHOW_EXPORT = 2;
    
    private JFrame parentFrame;
    private JDBCConnect jdbc;

    private int sqlType;
    private int dbFieldIndex;
    private boolean exportable;
    private String dbFieldName;
    private String valueExpression;
    
    public FieldMap() {
        parentFrame = new JFrame();
        sqlType = -1;
        dbFieldIndex = -1;
        exportable = false;
        dbFieldName = null;
        valueExpression = null;
    }
    
    public FieldMap(String name, int type, int index, String value) {
        parentFrame = new JFrame();
        this.dbFieldName = name;
        this.sqlType = type;
        this.dbFieldIndex = index;
        this.valueExpression = value;
    }
    
    public void setSqlType(int sqlType) { this.sqlType = sqlType; }
    public int getSqlType() { return this.sqlType; }
    public void setDbFieldIndex(int dbFieldIndex) { this.dbFieldIndex = dbFieldIndex; }
    public int getDbFieldIndex() { return this.dbFieldIndex; }
    public void setExportable(boolean exportable) { this.exportable = exportable; }
    public boolean getExportable() { return this.exportable; }
    public boolean isExportable() { return this.exportable; }
    public void setDbFieldName(String dbFieldName) { this.dbFieldName = dbFieldName; }
    public String getDbFieldName() { return this.dbFieldName; }
    public void setValueExpression(String valueExpression) { this.valueExpression = valueExpression; }
    public String getValueExpression() { return this.valueExpression; }
    
    public void setConnection(JDBCConnect jdbc) { this.jdbc = jdbc; }
    
    public String toString() {
        return dbFieldName+"("+dbFieldIndex+"): "+valueExpression+" type "+JDBCConnect.typeString(sqlType);
    }
    
    private boolean validateProject(String project) {
        Connection conn = jdbc.openConnection();
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        boolean isValid = false;
        try {
            String queryString = "SELECT "+jdbc.getProjectField()+" FROM "+jdbc.getProjectDatabase()+"."+jdbc.getProjectTable()+
            " WHERE "+jdbc.getProjectField()+"='"+project+"'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryString);
            isValid = rs.next();
            rs.close();
            stmt.close();
            conn.close();
            
            if(! isValid) {
                ProjectAddDialog addDialog = new ProjectAddDialog(parentFrame, project, jdbc.getProjectField(), jdbc);
                addDialog.show();
                if(addDialog.getValue().equals("0"))
                    isValid = validateProject(project);
                else
                    isValid = false;
            }
        } catch (SQLException e) {
            System.err.println("Couldn't attempt project validation: "+e);
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
                if(conn != null) conn.close();
            } catch (Exception ex) {}
        }
        return isValid;
    }
        
    public Object valueOf(TimeRecord record) throws ClassCastException, ProjectInvalidException {
        StringTokenizer toker = new StringTokenizer(valueExpression, "$ ", true);
        Object realValue = null;
        while(toker.hasMoreTokens()) {
            String value = toker.nextToken();
            if(value.equals("$")) {
                value = toker.nextToken();
                if(value.equals("PROJECT")) {
                    switch(sqlType) {
                        case java.sql.Types.CHAR:
                        case java.sql.Types.VARCHAR:
                            if(jdbc.getProjectCase()) realValue = record.getProjectName().toUpperCase();
                            else realValue = record.getProjectName();
                            if(jdbc.getProjectValidate() && ! validateProject((String)realValue))
                                throw new ProjectInvalidException("Project "+realValue+" not in table "+jdbc.getProjectDatabase()+"."+jdbc.getProjectTable());
                            break;
                        default:
                            throw new ClassCastException("Must be CHAR SQL type for project name");
                    }
                } else if(value.equals("USERNAME")) {
                    switch(sqlType) {
                        case java.sql.Types.CHAR:
                        case java.sql.Types.VARCHAR:
                            realValue = jdbc.getUserName();
                            break;
                        default:
                            throw new ClassCastException("Must be CHAR SQL type for username");
                    }
                } else if(value.equals("DATE")) {
                    switch(sqlType) {
                        case java.sql.Types.DATE:
                            realValue = new java.sql.Date(System.currentTimeMillis());
                            break;
                        case java.sql.Types.TIMESTAMP:
                            realValue = new java.sql.Timestamp(System.currentTimeMillis());
                            break;
                        case java.sql.Types.TIME:
                            realValue = new java.sql.Time(System.currentTimeMillis());
                            break;
                        case java.sql.Types.INTEGER:
                        case java.sql.Types.DECIMAL:
                        case java.sql.Types.NUMERIC:
                            Calendar today = Calendar.getInstance();
                            int year = today.get(Calendar.YEAR);
                            int month = today.get(Calendar.MONTH)+1;
                            int date = today.get(Calendar.DATE);
                            double cymd = (year*10000)+(month*100)+date;
                            realValue = new java.math.BigDecimal(cymd);
                            break;
                        default:
                            throw new ClassCastException("Unknown conversion for date");
                    }
                }else if(value.equals("HOURS")) {
                    if(sqlType != java.sql.Types.DECIMAL && sqlType != java.sql.Types.NUMERIC && sqlType != java.sql.Types.INTEGER)
                        throw new ClassCastException("Must be DECIMAL SQL type for hours");
                    switch(jdbc.getHourFormat()) {
                        case HOUR_FULL:
                            realValue = record.getHours(60, 2);
                            break;
                        case HOUR_QUARTER:
                            realValue = record.getHours(60*15, 2);
                            break;
                        case HOUR_TENTH:
                            realValue = record.getHours(60*6, 1);
                            break;
                        default:
                            realValue = record.getHours(60, 2);
                    }
                } else if(value.equals("BILLABLE")) {
                    switch(sqlType) {
                        case java.sql.Types.CHAR:
                            realValue = record.isBillable() ? "Y" : "N";
                            break;
                        case java.sql.Types.BIT:
                            realValue = new Boolean(record.isBillable());
                            break;
                        case java.sql.Types.INTEGER:
                            realValue = record.isBillable() ? new Integer(-1) : new Integer(0);
                            break;
                        default:
                            throw new ClassCastException("Unknown conversion for billable flag");
                    }
                } else {
                    System.err.println("Unknown expression variable: "+value);
                }
            } else {
                try {
                    realValue = new Integer(value);
                } catch (NumberFormatException e) {
                    try {
                        realValue = new java.math.BigDecimal(value);
                    } catch (NumberFormatException e2) {
                        realValue = value;
                    }
                }
            }
        }
        return realValue;
    }
}
