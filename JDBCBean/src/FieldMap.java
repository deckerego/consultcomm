import java.util.*;
import java.sql.*;
import java.text.*;
import java.math.BigDecimal;
import javax.swing.*;

public class FieldMap implements java.io.Serializable {
    final static int DATE_SQLDATE = 0;
    final static int DATE_SQLTIMESTAMP = 1;
    final static int DATE_CCYYMMDD = 2;
    final static int HOUR_FULL = 0;
    final static int HOUR_QUARTER = 1;
    final static int HOUR_TENTH = 2;
    final static int SHOW_EXPORT = 2;

    private static PreparedStatement findProject;
    private static Connection conn;
    private static JDBCConnect jdbc;
    private static JFrame parentFrame = new JFrame();

    private int sqlType;
    private int dbFieldIndex;
    private boolean exportable;
    private String dbFieldName;
    private String valueExpression;
    private String globalPromptValue = null;
    
    public FieldMap() {
        this(null, -1, -1, null);
    }
    
    public FieldMap(String name, int type, int index, String value) {
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
    
    public void setParentFrame(JFrame parentFrame) { this.parentFrame = parentFrame; }
    
    public static void setConnection(JDBCConnect jdbcConnect) throws SQLException {
        jdbc = jdbcConnect;
        conn = jdbc.openConnection();
        String qualifiedTable = jdbc.getProjectTable();
        if(jdbc.getProjectDatabase() != null) 
            qualifiedTable = jdbc.getProjectDatabase()+"."+jdbc.getProjectTable();
        String queryString = "SELECT "+jdbc.getProjectField()+" FROM "+
        qualifiedTable+" WHERE "+jdbc.getProjectField()+"=?";
        findProject = conn.prepareStatement(queryString);
    }
    
    public static void closeConnection() throws SQLException {
        if(findProject != null) findProject.close();
        if(conn != null) conn.close();
    }
    
    private boolean validateProject(String project, String name) {
        ResultSet rs = null;
        boolean isValid = false;
        try {
            this.findProject.setString(1, project);
            rs = this.findProject.executeQuery();
            isValid = rs.next();
            rs.close();
            
            if(! isValid) {
                ProjectAddDialog addDialog = new ProjectAddDialog(parentFrame, project, name, jdbc);
                addDialog.setVisible(true);
                if(addDialog.getValue().equals("0"))
                    isValid = validateProject(project, name);
                else
                    isValid = false;
            }
        } catch (SQLException e) {
            System.err.println("Couldn't attempt project validation: "+e);
        } finally {
            try {
                if(rs != null) rs.close();
            } catch (Exception ex) {}
        }
        return isValid;
    }
        
    public Object valueOf(TimeRecord record) throws ClassCastException, ProjectInvalidException {
        StringTokenizer toker = new StringTokenizer(valueExpression, "$ ", true);
        Object realValue = null;
        while(toker.hasMoreTokens()) {
            String value = toker.nextToken();
            
            if(value.equals("$")) { //We have a variable to decipher
                value = toker.nextToken();
                
                if(value.equals("PROJECT")) {
                    //Get the project map, then find the associated project mapping, then get its alias
                    ProjectMap project = (ProjectMap)jdbc.getTableMap().getProjectMaps().get(record.toString());
                    String projectName = null;
                    if(project != null)
                        projectName = "".equals(project.getAlias()) ? record.getProjectName() : project.getAlias();
                    
                    //Put the project in all caps if it needs to be
                    realValue = jdbc.isProjectCase() ? projectName.toUpperCase() : projectName;
                    //Attempt to validate the project
                    if(jdbc.getProjectValidate() && ! validateProject((String)realValue, record.getProjectName()))
                        throw new ProjectInvalidException("Project "+realValue+" not in table "+jdbc.getProjectDatabase()+"."+jdbc.getProjectTable());
                    
                } else if(value.equals("USERNAME")) {
                    realValue = jdbc.getUserName();
                    
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
                            System.err.println("Unknown conversion, assuming DATE");
                            realValue = new java.sql.Date(System.currentTimeMillis());
                    }
                    
                }else if(value.equals("HOURS")) {
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
                        case java.sql.Types.VARCHAR:
                            realValue = record.isBillable() ? "Y" : "N";
                            break;
                        case java.sql.Types.BIT:
                            realValue = new Boolean(record.isBillable());
                            break;
                        case java.sql.Types.INTEGER:
                            realValue = record.isBillable() ? new Integer(-1) : new Integer(0);
                            break;
                        case java.sql.Types.TINYINT:
                            realValue = record.isBillable() ? new Short((short)-1) : new Short((short)0);
                            break;
                        default:
                            System.err.println("Unknown conversion: assuming INTEGER");
                            realValue = record.isBillable() ? new Integer(-1) : new Integer(0);
                    }
                } else if(value.equals("PROMPT")) {
                  String promptValue = (String)JOptionPane.showInputDialog(parentFrame, record.getProjectName()+"'s value for "+dbFieldName+":", dbFieldName, JOptionPane.PLAIN_MESSAGE);
                  try {
                    switch(sqlType) {
                      case java.sql.Types.CHAR:
                      case java.sql.Types.VARCHAR:
                      case java.sql.Types.CLOB:
                        realValue = promptValue;
                        break;
                      case java.sql.Types.INTEGER:
                        realValue = new Integer(promptValue);
                        break;
                      case java.sql.Types.DECIMAL:
                      case java.sql.Types.NUMERIC:
                        realValue = new BigDecimal(promptValue);
                        break;
                      case java.sql.Types.DATE:
                        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT); //Default for user's locale
                        realValue = dateFormat.parse(promptValue);
                        break;
                      default:
                        System.err.println("Unknown conversion: assuming CHAR");
                        realValue = promptValue;
                    }
                  } catch (NumberFormatException e) {
                    System.err.println("Couldn't reformat number: "+promptValue);
                  } catch (ParseException e) {
                    System.err.println("Couldn't reformat date: "+promptValue);
                  }
                } else if(value.equals("GLOBALPROMPT")) {
                  if(globalPromptValue == null)
                    globalPromptValue = (String)JOptionPane.showInputDialog(parentFrame, "Global value for "+dbFieldName+":", dbFieldName, JOptionPane.PLAIN_MESSAGE);
                  try {
                    switch(sqlType) {
                      case java.sql.Types.CHAR:
                      case java.sql.Types.VARCHAR:
                      case java.sql.Types.CLOB:
                        realValue = globalPromptValue;
                        break;
                      case java.sql.Types.INTEGER:
                        realValue = new Integer(globalPromptValue);
                        break;
                      case java.sql.Types.DECIMAL:
                      case java.sql.Types.NUMERIC:
                        realValue = new BigDecimal(globalPromptValue);
                        break;
                      case java.sql.Types.DATE:
                        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT); //Default for user's locale
                        realValue = dateFormat.parse(globalPromptValue);
                        break;
                      default:
                        System.err.println("Unknown conversion: assuming CHAR");
                        realValue = globalPromptValue;
                    }
                  } catch (NumberFormatException e) {
                    System.err.println("Couldn't reformat number: "+globalPromptValue);
                  } catch (ParseException e) {
                    System.err.println("Couldn't reformat date: "+globalPromptValue);
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
