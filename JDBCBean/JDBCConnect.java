import java.util.*;
import java.sql.*;
import java.io.*;
//GUI Componenets
import javax.swing.*;
import javax.swing.table.*;
//XML Components
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class JDBCConnect implements java.io.Serializable, CsltCommListener {
    final static int DATE_SQLDATE = 0;
    final static int DATE_SQLTIMESTAMP = 1;
    final static int DATE_CCYYMMDD = 2;
    final static int HOUR_FULL = 0;
    final static int HOUR_QUARTER = 1;
    final static int HOUR_TENTH = 2;
    static final int SHOW_EXPORT = 2;
    
    final static String odbcDriverName = "sun.jdbc.odbc.JdbcOdbcDriver";
    
    TableMap tableMap;
    
    private Vector errorList;
    private JFrame parentFrame;
    private ClntComm clntComm;
    
    private String name = "";
    private String url = "";
    private String userName = "";
    private String password="";
    private String database="";
    private String table="";
    private String projectDatabase="";
    private String projectTable="";
    private String projectField="";
    private int hourFormat;
    private boolean projectValidate;
    private boolean projectCase;
    private boolean useExport;
    private boolean validated;
    
    public JDBCConnect() {
        tableMap = new TableMap();
        parentFrame = new JFrame();
        readPrefs();
    }
    
    public JDBCConnect(String name, String url, String database, String table) {
        tableMap = new TableMap();
        parentFrame = new JFrame();
        this.name = name;
        this.url = url;
        this.database = database;
        this.table = table;
    }
    
    //Setters/getters
    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }
    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return this.url; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return this.userName; }
    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return this.password; }
    public void setDatabase(String database) { this.database = database; }
    public String getDatabase() { return this.database; }
    public void setTable(String table) { this.table = table; }
    public String getTable() { return this.table; }
    public void setProjectDatabase(String projectDatabase) { this.projectDatabase = projectDatabase; }
    public String getProjectDatabase() { return this.projectDatabase; }
    public void setProjectTable(String projectTable) { this.projectTable = projectTable; }
    public String getProjectTable() { return this.projectTable; }
    public void setProjectField(String projectField) { this.projectField = projectField; }
    public String getProjectField() { return this.projectField; }
    public void setHourFormat(int hourFormat) { this.hourFormat = hourFormat; }
    public int getHourFormat() { return this.hourFormat; }
    public void setProjectValidate(boolean porjectValidate) { this.projectValidate = projectValidate; }
    public boolean getProjectValidate() { return this.projectValidate; }
    public boolean isProjectValidate() { return this.projectValidate; }
    public void setProjectCase(boolean projectCase) { this.projectCase = projectCase; }
    public boolean getProjectCase() { return this.projectCase; }
    public boolean isProjectCase() { return this.projectCase; }
    public void setUseExport(boolean useExport) { this.useExport = useExport; }
    public boolean getUseExport() { return this.useExport; }
    public boolean isUseExport() { return this.useExport; }
    public void setValidated(boolean validated) { this.validated = validated; }
    public boolean getValidated() { return this.validated; }
    public boolean isValidated() { return this.validated; }
    
    public javax.swing.JMenuItem[] getMenuItems() {
        javax.swing.JMenuItem menuitems[] = new javax.swing.JMenuItem[1];
        
        menuitems[0] = new javax.swing.JMenuItem();
        menuitems[0].setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        menuitems[0].setText("Export to Database");
        menuitems[0].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportTimeRecordSet(clntComm.getTimes());
            }
        });
        
        return menuitems;
    }
    
    private Connection openConnection() {
        Connection conn = null;
        errorList = new Vector();
        
        try{
            Class.forName(name);
            if(! validated) { //Send login dialog box
                LoginDialog prompt = new LoginDialog(userName);
                prompt.pack();
                prompt.setLocationRelativeTo(parentFrame);
                prompt.setVisible(true);
            }
            Properties properties = new Properties();
            properties.put("password", password);
            properties.put("user", userName);
            properties.put("prompt", "false");
            conn = DriverManager.getConnection(url, properties);
            if(conn.isClosed()) {
                errorList.addElement("Cannot open connection");
                conn = null;
            } else if(conn.isReadOnly()) {
                errorList.addElement("Connection is read only");
                conn = null;
            }
            if(conn != null) validated = true;
        } catch (ClassNotFoundException e) {
            String extdir = System.getProperty("java.ext.dirs");
            String msgString = "Could not find JDBC driver "+name+"."+
            "Make sure you have the correct driver files and that they"+
            "are installed in "+extdir+", then restart ConsultComm.";
            errorList.addElement(msgString);
        } catch (SQLException e) {
            errorList.addElement("Could not build JDBC connection: "+e);
        } catch (NullPointerException e) {
            errorList.addElement("One or more arguments are null");
        } catch (Exception e) {
            errorList.addElement(e.toString());
        }
        if(errorList.size() > 0)
            JDBCOptionPane.showMessageDialog(parentFrame, errorList.elementAt(0), "Database Connection Error", JDBCOptionPane.ERROR_MESSAGE);
        return conn;
    }
    
    void insertVector(Vector statements) throws SQLException {
        Connection conn = openConnection();
        PreparedStatement insert = null;
        SQLException exception = null;
        
        if(tableMap.size() == 0) throw new SQLException("Table map is empty.");
        
        try {
            String queryString = "?";
            for(int i=1; i<tableMap.size(); i++) queryString += " ,?";
            insert = conn.prepareStatement("INSERT INTO "+database+"."+table+" VALUES ("+queryString+")");
            
            for(int j=0; j < statements.size(); j++) {
                Object[] statement = (Object[])statements.elementAt(j);
                for(int i=0; i < statement.length; i++)
                    insert.setObject(i+1, statement[i]);
                insert.execute();
            }
            
            if(! conn.getAutoCommit()) conn.commit();
        } catch (SQLException e) {
            exception = e;
        } finally {
            try {if(insert != null) insert.close();} catch (SQLException e) {}
            try {if(conn != null) conn.close();} catch (SQLException e) {}
        }
        
        if(exception != null) throw exception;
    }
    
    public boolean exportTimeRecordSet(TimeRecordSet times) {
        boolean worked = false;
        
        try {
            // Okay... we've got a problem. We want to test for errors
            // before committing changes to the database but we also
            // can't depend on the rollback() method working (not all
            // db's we want to use support transaction management).
            // So we first load everything into a two dimensional array
            // then we insert the records into the database.
            Vector statements = new Vector();
            for(int j=0; j < times.size(); j++) {
                TimeRecord record = times.elementAt(j);
                FieldMap hourTest = new FieldMap("TEST", java.sql.Types.DECIMAL, 0, "$HOURS"); //Find out how many hours exist
                java.math.BigDecimal hours = (java.math.BigDecimal)hourTest.getValue(record);
                if(hours.compareTo(new java.math.BigDecimal(0.0)) <= 0) continue;
                Object[] statement = new Object[tableMap.size()];
                for(int i=0; i < statement.length; i++) {
                    FieldMap fieldMap = tableMap.elementAt(i);
                    statement[i] = fieldMap.getValue(record);
                }
                statements.addElement(statement);
            }
            
            insertVector(statements);
            worked = true;
        } catch (ProjectInvalidException e) {
            JDBCOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Export Error", JDBCOptionPane.ERROR_MESSAGE);
            worked = false;
        } catch (DataTruncation e) {
            String errorString = "Data truncation error: make sure the fields in the \"Field Mappings\" tab of your JDBC settings is correct. "+
            "It appears that one of the values is set incorrectly.";
            JDBCOptionPane.showMessageDialog(parentFrame, errorString, "Export Error", JDBCOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JDBCOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Export Error", JDBCOptionPane.ERROR_MESSAGE);
            System.err.println("Could not export timelist to database: "+e);
            worked = false;
        }
        return worked;
    }
    
    public String[] getProjectFieldNames() {
        Connection conn = openConnection();
        String[] names = null;
        ResultSet cols = null;
        
        try {
            Vector records = new Vector();
            DatabaseMetaData dbmeta = conn.getMetaData();
            cols = dbmeta.getColumns(null, projectDatabase, projectTable, null);
            while(cols.next())
                records.addElement(cols.getString(4));
            
            names = new String[records.size()];
            for(int i=0; i<names.length; i++)
                names[i] = (String)records.elementAt(i);
        } catch (SQLException e) {
            JDBCOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Project Error", JDBCOptionPane.ERROR_MESSAGE);
            System.err.println("Could not get project list from database: "+e);
        } finally {
            try {
                if(cols != null) cols.close();
                if(conn != null) conn.close();
            } catch (Exception ex) {}
        }
        return names;
    }
    
    private boolean validateProject(String project) {
        Connection conn = openConnection();
        Statement stmt = null;
        ResultSet rs = null;
        boolean isValid = false;
        try {
            String queryString = "SELECT "+projectField+" FROM "+projectDatabase+"."+projectTable+
            " WHERE "+projectField+"='"+project+"'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryString);
            isValid = rs.next();
            rs.close();
            stmt.close();
            conn.close();
            
            if(! isValid) {
                ProjectAddDialog addDialog = new ProjectAddDialog(parentFrame, project, projectField, this);
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
    
    public void testDriverSettings() {
        try {
            if(name.equals(odbcDriverName)) url = "jdbc:odbc:"+url;
            Connection conn = openConnection();
            if(conn != null) {
                DatabaseMetaData dbmeta = conn.getMetaData();
                ResultSet cols = dbmeta.getColumns(null, database, table, null);
                if((cols == null) || ! cols.next())
                    JDBCOptionPane.showMessageDialog(parentFrame, "Table "+database+"."+table+" cannot be found.", "Table Not Found", JDBCOptionPane.ERROR_MESSAGE);
                else
                    JDBCOptionPane.showMessageDialog(parentFrame, "Driver connection verified.", "Driver verified", JDBCOptionPane.INFORMATION_MESSAGE);
                if(cols != null) cols.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Uncaught SQL error during test: "+e);
        }
    }
    
    void savePrefs() {
        try {
            PrefsFile prefs = new PrefsFile("JDBCConnection.def");
            
            String[] driverAttributeList = {"name", "url", "username", "database", "table"};
            String[] driverValueList = {name, url, userName, database, table};
            prefs.saveFirst("driver", driverAttributeList, driverValueList);
            
            String[] optAttributeList = {"hourFormat", "projectCase", "projectValidate", "projectDatabase", "projectTable", "projectField"};
            String[] optValueList = {Integer.toString(hourFormat), Boolean.toString(projectCase), Boolean.toString(projectValidate), projectDatabase, projectTable, projectField};
            prefs.saveFirst("options", optAttributeList, optValueList);
            
            //Delete old field mappings
            prefs.removeAllChildren("fieldmap");
            
            //Save field mappings
            for(int i=0; i<tableMap.size(); i++){
                FieldMap record = tableMap.elementAt(i);
                Element newNode = prefs.createElement("fieldmap");
                newNode.setAttribute("name", record.dbFieldName);
                newNode.setAttribute("type", ""+record.sqlType);
                newNode.setAttribute("index", ""+record.dbFieldIndex);
                newNode.setAttribute("value", record.valueExpression);
                prefs.appendChild(newNode);
            }
            
            prefs.write();
        } catch (ParserConfigurationException e) {
            System.err.println("Error writing prefs file: "+e);
            e.printStackTrace(System.out);
        } catch (Exception e) {
            System.err.println("Cannot write prefs file: "+e);
            e.printStackTrace(System.out);
        }
    }
    
    void readPrefs() {
        try {
            PrefsFile prefs = new PrefsFile("JDBCConnection.def");
            
            name = prefs.readFirstString("driver", "name");
            url = prefs.readFirstString("driver", "url");
            userName = prefs.readFirstString("driver", "username");
            database = prefs.readFirstString("driver", "database");
            table = prefs.readFirstString("driver", "table");
            
            hourFormat = prefs.readFirstInt("options", "hourFormat");
            projectCase = prefs.readFirstBoolean("options", "projectCase").booleanValue();
            projectValidate = prefs.readFirstBoolean("options", "projectValidate").booleanValue();
            projectDatabase = prefs.readFirstString("options", "projectDatabase");
            projectTable = prefs.readFirstString("options", "projectTable");
            projectField = prefs.readFirstString("options", "projectField");
            
            NodeList fieldMaps = prefs.getElementsByTagName("fieldmap");
            tableMap.fieldMaps.clear();
            for(int i=0; i<fieldMaps.getLength(); i++){
                Node fieldMap = fieldMaps.item(i);
                NamedNodeMap attributes = fieldMap.getAttributes();
                Node nameNode = attributes.getNamedItem("name");
                String fieldName = nameNode.getNodeValue();
                Node typeNode = attributes.getNamedItem("type");
                short sqlType = Short.parseShort(typeNode.getNodeValue());
                Node indexNode = attributes.getNamedItem("index");
                int fieldIndex = Integer.parseInt(indexNode.getNodeValue());
                Node valueNode = attributes.getNamedItem("value");
                String valueExpression = valueNode.getNodeValue();
                FieldMap record = new FieldMap(fieldName, sqlType, fieldIndex, valueExpression);
                tableMap.fieldMaps.addElement(record);
            }
        } catch (Exception e) {
            System.err.println("Cannot read prefs file: "+e);
            e.printStackTrace(System.out);
        }
        
        try {
            PrefsFile prefs = new PrefsFile("ClntComm.def");
            
            //Get attribute flags
            int attributes = prefs.readFirstInt("attributes", "value");
            //REPLACE-->      useExport = (ClntComm.SHOW_EXPORT ^ attributes) != (ClntComm.SHOW_EXPORT | attributes);
        } catch (Exception e) {
            System.err.println("Cannot read prefs file: "+e);
            e.printStackTrace(System.out);
        }
    }
    
    public void clockTick(CsltCommEvent actionEvent) {
        //Set the referring object
        clntComm = (ClntComm)actionEvent.getSource();
    }
    
    private class LoginDialog extends JDialog {
        private JOptionPane optionPane;
        
        LoginDialog(String user) {
            super(parentFrame, true);
            setTitle("Login to Server");
            
            final JTextField userField = new JTextField(user);
            userField.setColumns(10);
            final JPasswordField passField = new JPasswordField();
            passField.setColumns(10);
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new java.awt.GridLayout(2, 2));
            loginPanel.add(new JLabel("Username: "));
            loginPanel.add(userField);
            loginPanel.add(new JLabel("Password: "));
            loginPanel.add(passField);
            
            optionPane = new JOptionPane(loginPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            setContentPane(optionPane);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent we) {
                    optionPane.setValue(new Integer(JDBCOptionPane.CLOSED_OPTION));
                }
            });
            
            optionPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    String prop = e.getPropertyName();
                    
                    if (isVisible() && (e.getSource() == optionPane) && (prop.equals(JDBCOptionPane.VALUE_PROPERTY) || prop.equals(JDBCOptionPane.INPUT_VALUE_PROPERTY))) {
                        String value = optionPane.getValue().toString();
                        if (value == JDBCOptionPane.UNINITIALIZED_VALUE) return;
                        optionPane.setValue(JDBCOptionPane.UNINITIALIZED_VALUE);
                        
                        if (value.equals("0")) {
                            password = new String(passField.getPassword());
                            userName = userField.getText();
                            setVisible(false);
                        } else { // user closed dialog or clicked cancel
                            setVisible(false);
                        }
                    }
                }
            });
        }
    }
    
    class TableMap {
        Vector fieldMaps;
        private final String[] titles = {"Field Name", "Type", "Value"};
        
        TableMap() {
            fieldMaps = new Vector();
        }
        
        protected void init() throws java.sql.SQLException {
            fieldMaps.clear();
            Connection conn = openConnection();
            DatabaseMetaData dbmeta = conn.getMetaData();
            ResultSet cols = dbmeta.getColumns(null, database, table, null);
            while(cols.next())
                fieldMaps.addElement(new FieldMap(cols));
            cols.close();
            conn.close();
        }
        
        public int size() {
            return fieldMaps.size();
        }
        
        public FieldMap elementAt(int i) {
            return (FieldMap)fieldMaps.elementAt(i);
        }
        
        public DefaultTableModel toTableModel(){
            DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            //Set to three empty columns
            new Object [][][] {
            },
            titles
            ) {
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    boolean[] editable = {false, false, true};
                    return editable[columnIndex];
                }
            };
            
            Enumeration records = fieldMaps.elements();
            while (records.hasMoreElements()) {
                FieldMap record = (FieldMap)records.nextElement();
                model.addRow(new Object[] {record.dbFieldName, typeString(record.sqlType), record.valueExpression});
            }
            return model;
        }
    }
    
    class FieldMap {
        int sqlType;
        int dbFieldIndex;
        String dbFieldName;
        String valueExpression = "";
        
        FieldMap(ResultSet rs) throws java.sql.SQLException {
            dbFieldName = rs.getString(4);
            sqlType = rs.getShort(5);
            dbFieldIndex = rs.getInt(17);
        }
        
        FieldMap(String name, int type, int index, String value) {
            dbFieldName = name;
            sqlType = type;
            dbFieldIndex = index;
            valueExpression = value;
        }
        
        public String toString() {
            return dbFieldName+"("+dbFieldIndex+"): "+valueExpression+" type "+typeString(sqlType);
        }
        
        protected Object getValue(TimeRecord record) throws ClassCastException, ProjectInvalidException {
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
                                if(projectCase) realValue = record.getProjectName().toUpperCase();
                                else realValue = record.getProjectName();
                                if(projectValidate && ! validateProject((String)realValue))
                                    throw new ProjectInvalidException("Project "+realValue+" not in table "+projectDatabase+"."+projectTable);
                                break;
                            default:
                                throw new ClassCastException("Must be CHAR SQL type for project name");
                        }
                    } else if(value.equals("USERNAME")) {
                        switch(sqlType) {
                            case java.sql.Types.CHAR:
                            case java.sql.Types.VARCHAR:
                                realValue = userName;
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
                        switch(hourFormat) {
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
}