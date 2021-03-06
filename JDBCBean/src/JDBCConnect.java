import java.util.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.beans.*;
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
//Clipboard Components
import java.awt.datatransfer.*;

public class JDBCConnect extends CsltCommPlugin {
    final static String ODBCDRIVERNAME = "sun.jdbc.odbc.JdbcOdbcDriver";
    final String TOTAL_TITLE = "To Export:";
    
    private Vector errorList;
    private JFrame parentFrame;
    ClntComm clntComm;
    transient String password="";
    transient boolean validated;
    private String qualifiedTable;
    private int widthExportList = 640;
    private int heightExportList = 480;
    
    private TableMap tableMap;
    private String jarFile;
    private String name = "";
    private String url = "";
    private String userName = "";
    private String database;
    private String table="";
    private String projectDatabase="";
    private String projectTable="";
    private String projectField="";
    private int hourFormat;
    private boolean projectValidate;
    private boolean projectCase;
    private boolean use;
    
    public JDBCConnect() {
        tableMap = new TableMap();
        tableMap.setConnection(this);
        parentFrame = new JFrame(); //Temporarily until window opens
    }
    
    public JDBCConnect(String name, String url, String database, String table) {
        tableMap = new TableMap();
        tableMap.setConnection(this);
        parentFrame = new JFrame(); //Temporarily until window opens
        this.name = name;
        this.url = url;
        this.database = database;
        this.table = table;
    }
    
    //Setters/getters
    public void setJarFile(String jarFile) { this.jarFile = jarFile; }
    public String getJarFile() { return this.jarFile; }
    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }
    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return this.url; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return this.userName; }
    public void setDatabase(String database) {
        this.database = database;
        this.qualifiedTable = database == null ? table : database+"."+table;
    }
    public String getDatabase() { return this.database; }
    public void setTable(String table) {
        this.table = table;
        this.qualifiedTable = database == null ? table : database+"."+table;
    }
    public String getTable() { return this.table; }
    public void setProjectDatabase(String projectDatabase) { this.projectDatabase = projectDatabase; }
    public String getProjectDatabase() { return this.projectDatabase; }
    public void setProjectTable(String projectTable) { this.projectTable = projectTable; }
    public String getProjectTable() { return this.projectTable; }
    public void setProjectField(String projectField) { this.projectField = projectField; }
    public String getProjectField() { return this.projectField; }
    public void setHourFormat(int hourFormat) { this.hourFormat = hourFormat; }
    public int getHourFormat() { return this.hourFormat; }
    public void setProjectValidate(boolean projectValidate) { this.projectValidate = projectValidate; }
    public boolean getProjectValidate() { return this.projectValidate; }
    public boolean isProjectValidate() { return this.projectValidate; }
    public void setProjectCase(boolean projectCase) { this.projectCase = projectCase; }
    public boolean getProjectCase() { return this.projectCase; }
    public boolean isProjectCase() { return this.projectCase; }
    public void setTableMap(TableMap tableMap) { this.tableMap = tableMap; }
    public TableMap getTableMap() { return this.tableMap; }
    public void setUse(boolean use) { this.use = use; }
    public boolean getUse() { return this.use; }
    public boolean isUse() { return this.use; }
    
    public javax.swing.JMenuItem[] getMenuItems() {
        if(! use) return null; // Nevermind.
        
        javax.swing.JMenuItem menuitems[] = new javax.swing.JMenuItem[1];
        
        menuitems[0] = new javax.swing.JMenuItem();
        menuitems[0].setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        menuitems[0].setText("Export to Database");
        menuitems[0].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boolean worked = exportTimeRecordSet(clntComm.getTimes());
                if(worked) clntComm.zeroProject();
            }
        });
        
        return menuitems;
    }
    
    public javax.swing.JMenuItem[] getPopupMenuItems() {
        if(! use) return null; // Nevermind.
        
        javax.swing.JMenuItem menuitems[] = new javax.swing.JMenuItem[1];
        
        menuitems[0] = new javax.swing.JMenuItem();
        menuitems[0].setText("Copy Alias to Clipboard");
        menuitems[0].addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
            TimeRecord selected = clntComm.getSelectedRecord();
            Hashtable projectMaps = tableMap.getProjectMaps();
            ProjectMap projectMap = (ProjectMap)projectMaps.get(selected.toString());
            Clipboard clipboard = clntComm.getToolkit().getSystemClipboard();
            StringSelection contents = new StringSelection(projectMap.getAlias());
            clipboard.setContents(contents, clntComm);
          }
        });
        
        return menuitems;
    }
    
    public Connection openConnection() {
        Connection conn = null;
        errorList = new Vector();
        ClassLoader loader = this.getClass().getClassLoader();
        
        try{
            if(jarFile != null && jarFile.length() > 0) {
                File jar = new File(jarFile);
                if(jar.exists()) {
                    try { loader = new URLClassLoader(new URL[] {jar.toURL()}); }
                    catch (MalformedURLException e) { System.err.println("Error loading Jar file: "+e); }
                } else {
                    System.err.println("Jar file doesn't exist. Using default classpath.");
                }
            }
            
            Class driverClass = loader.loadClass(this.name);
            Driver driver = (Driver)driverClass.newInstance();
            if(! validated) { //Send login dialog box
                LoginDialog prompt = new LoginDialog(userName);
                prompt.pack();
                prompt.setLocationRelativeTo(parentFrame);
                prompt.setVisible(true);
            }
            Properties properties = new Properties();
            properties.put("password", this.password);
            properties.put("user", this.userName);
            properties.put("prompt", "false");
            conn = driver.connect(this.url, properties);
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
            String msgString = "Could not find JDBC driver "+name+". "+
            "Make sure you have the correct driver file installed in "
            +extdir+" or specified in the JDBC preferences screen, "+
            "then restart ConsultComm.";
            errorList.addElement(msgString);
        } catch (SQLException e) {
            String msgString = e.toString();
            errorList.addElement("Could not build JDBC connection: "+msgString);
        } catch (NullPointerException e) {
            errorList.addElement("One or more arguments are null");
        } catch (Exception e) {
            errorList.addElement(e.toString());
        }
        
        if(errorList.size() > 0) {
            String msgString = (String)errorList.elementAt(0);
            if(msgString.length() > 512) msgString = msgString.substring(0, 512);
            JDBCOptionPane.showMessageDialog(parentFrame, msgString, "Database Connection Error", JDBCOptionPane.ERROR_MESSAGE);
        }
        
        return conn;
    }
    
    void insertVector(Vector statements) throws SQLException {
        Connection conn = openConnection();
        PreparedStatement insert = null;
        SQLException exception = null;
        
        if(tableMap.getFieldMaps().size() == 0) throw new SQLException("Table map is empty.");
        
        try {
            StringBuffer queryString = new StringBuffer("INSERT INTO "+qualifiedTable+" (");
            Vector fieldMaps = tableMap.getFieldMaps();
            
            //Field names to insert
            for(int i=0, max=fieldMaps.size()-1; i<max; i++) queryString.append(((FieldMap)fieldMaps.elementAt(i)).getDbFieldName()+", ");
            queryString.append(((FieldMap)fieldMaps.lastElement()).getDbFieldName()+")");
            
            //Values to insert
            queryString.append(" VALUES (?");
            for(int i=1, max=fieldMaps.size(); i<max; i++) queryString.append(" ,?");
            queryString.append(")");
            
            insert = conn.prepareStatement(queryString.toString());
            
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
            FieldMap.setConnection(this);
            for(int j=0; j < times.size(); j++) {
                TimeRecord record = times.elementAt(j);
                
                //Find out how many hours exist - if no hours exist, skip this record
                FieldMap hourTest = new FieldMap("TEST", java.sql.Types.DECIMAL, 0, "$HOURS");
                java.math.BigDecimal hours = (java.math.BigDecimal)hourTest.valueOf(record);
                if(hours.compareTo(new java.math.BigDecimal(0.0)) <= 0) continue;
                
                //We also get to set the parent frame so relative positioning of popups works
                hourTest.setParentFrame(this.parentFrame);
                
                //Find out if this project is exportable or not
                ProjectMap project = (ProjectMap)this.tableMap.getProjectMaps().get(record.toString());
                if(project == null || ! project.isExport()) continue;
                
                //Start translating the TimeRecord into a SQL statement
                Object[] statement = new Object[tableMap.getFieldMaps().size()];
                Vector fieldMaps = tableMap.getFieldMaps();
                for(int i=0; i < statement.length; i++) {
                    FieldMap fieldMap = (FieldMap)fieldMaps.elementAt(i);
                    statement[i] = fieldMap.valueOf(record);
                }
                statements.addElement(statement);
            }
            
            FieldMap.closeConnection();
            
            //Send the vector of statements
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
    
    public void testDriverSettings() {
        try {
            if(name.equals(ODBCDRIVERNAME)) url = "jdbc:odbc:"+url;
            Connection conn = openConnection();
            if(conn != null) {
                DatabaseMetaData dbmeta = conn.getMetaData();
                ResultSet cols = dbmeta.getColumns(null, database, table, null);
                if((cols == null) || ! cols.next())
                    JDBCOptionPane.showMessageDialog(parentFrame, "Table "+qualifiedTable+" cannot be found.", "Table Not Found", JDBCOptionPane.ERROR_MESSAGE);
                else
                    JDBCOptionPane.showMessageDialog(parentFrame, "Driver connection verified.", "Driver verified", JDBCOptionPane.INFORMATION_MESSAGE);
                if(cols != null) cols.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Uncaught SQL error during test: "+e);
        }
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        if(clntComm == null) clntComm = (ClntComm)propertyChangeEvent.getSource();
        TotalPanel totalPanel = clntComm.getTotalPanel();
        totalPanel.setEntry("To Export:", getTotalTime());
        String eventName = propertyChangeEvent.getPropertyName();
        
        if("opened".equals(eventName)) {
            //We should now have a frame to position things relatively
            this.parentFrame = (JFrame)clntComm.getTopLevelAncestor();
        }
        
        if("record".equals(eventName) && use) { //Edited a project
            TimeRecord newRecord = (TimeRecord)propertyChangeEvent.getNewValue();
            TimeRecord oldRecord = (TimeRecord)propertyChangeEvent.getOldValue();
            
            if(newRecord != null && oldRecord != null && ! newRecord.toString().equals(oldRecord.toString())) { //The project name changed
                Hashtable projectMaps = tableMap.getProjectMaps();
                ProjectMap projectMap = (ProjectMap)projectMaps.get(oldRecord.toString());
                projectMaps.put(newRecord.toString(), projectMap); //Add new project/alias
                projectMaps.remove(oldRecord.toString()); //Remove old project/alias

                try {
                  PluginManager.serializeObject(this);
                } catch (java.io.FileNotFoundException e) {
                  System.err.println("Error saving prefs for Empty plugin");
                }
            }
        }

        if(propertyChangeEvent.getPropertyName().equals("times") && use) { //Timelist has changed
            TimeRecordSet newTimes = (TimeRecordSet)propertyChangeEvent.getNewValue();
            TimeRecordSet oldTimes = (TimeRecordSet)propertyChangeEvent.getOldValue();
            
            if(newTimes != null && oldTimes != null && newTimes.size() > oldTimes.size()) { //Added a project, prompt
                Object[] options = {"OK", "Cancel"};
                int dialog = javax.swing.JOptionPane.showOptionDialog(parentFrame,
                "Add Project to Database Export List?",
                "Add to Export", javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                
                if(dialog == 0){
                    JFrame frame = new JFrame();
                    frame.setLocationRelativeTo(parentFrame);
                    frame.getContentPane().setLayout(new java.awt.BorderLayout());
                    Customizer customizer = new JDBCConnectCustomizer();
                    customizer.setObject(this);
                    frame.getContentPane().add((javax.swing.JPanel)customizer, java.awt.BorderLayout.CENTER);
                    frame.pack();
                    frame.setSize(widthExportList, heightExportList);
                    frame.setVisible(true);
                    ((JDBCConnectCustomizer)customizer).tabbedPane.setSelectedIndex(2);
                }
            }
        }
    }
    
    private long getTotalTime() {
        long totalTime = 0;
        TimeRecordSet times = clntComm.getTimes();
        for(int j=0; j < times.size(); j++) {
            TimeRecord record = times.elementAt(j);
            //Find out if this project is exportable or not
            ProjectMap project = (ProjectMap)this.tableMap.getProjectMaps().get(record.toString());
            if(project == null || ! project.isExport()) continue;
            totalTime += record.getSeconds();
        }
        return totalTime;
    }

    public Object clone() throws CloneNotSupportedException {
        JDBCConnect clone = (JDBCConnect)super.clone();
        clone.tableMap = new TableMap();
        return clone;
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
                            setUserName(userField.getText());
                            setVisible(false);
                        } else { // user closed dialog or clicked cancel
                            setVisible(false);
                        }
                    }
                }
            });
        }
    }
}
