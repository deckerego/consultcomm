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

/**
 *
 * @author  jellis
 */
public class JDBCControlPanel extends javax.swing.JFrame {
  private String name;
  private String url;
  private static String userName;
  private static String password;
  private static String database;
  private static String table;
  private Vector errorList;
  private TableMap tableMap;
  private static boolean validated;
  
  /** Creates new form JDBCControlPanel */
  public JDBCControlPanel() {
    tableMap = new TableMap();
    readPrefs();
  }
  
  public void initGUI() {
    initComponents();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    tabbedPane = new javax.swing.JTabbedPane();
    driverPanel = new javax.swing.JPanel();
    driverInputPanel = new javax.swing.JPanel();
    nameLabel = new javax.swing.JLabel();
    nameField = new javax.swing.JTextField();
    urlLabel = new javax.swing.JLabel();
    urlField = new javax.swing.JTextField();
    dbLabel = new javax.swing.JLabel();
    dbField = new javax.swing.JTextField();
    tableLabel = new javax.swing.JLabel();
    tableField = new javax.swing.JTextField();
    driverButtonPanel = new javax.swing.JPanel();
    driverOK = new javax.swing.JButton();
    driverCancel = new javax.swing.JButton();
    driverTest = new javax.swing.JButton();
    fieldPanel = new javax.swing.JPanel();
    fieldScrollPane = new javax.swing.JScrollPane();
    fieldMapping = new javax.swing.JTable();
    fieldButtonPanel = new javax.swing.JPanel();
    fieldOK = new javax.swing.JButton();
    fieldCancel = new javax.swing.JButton();
    fieldRefresh = new javax.swing.JButton();
    
    getContentPane().setLayout(new java.awt.GridLayout(1, 1));
    
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        exitForm(evt);
      }
    });
    
    tabbedPane.setPreferredSize(new java.awt.Dimension(387, 244));
    driverPanel.setLayout(new java.awt.BorderLayout());
    
    driverInputPanel.setLayout(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints gridBagConstraints2;
    
    nameLabel.setText("Driver Name");
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
    driverInputPanel.add(nameLabel, gridBagConstraints2);
    
    nameField.setColumns(20);
    nameField.setText(name);
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    driverInputPanel.add(nameField, gridBagConstraints2);
    
    urlLabel.setText("URL");
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
    driverInputPanel.add(urlLabel, gridBagConstraints2);
    
    urlField.setColumns(20);
    urlField.setText(url);
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    driverInputPanel.add(urlField, gridBagConstraints2);
    
    dbLabel.setText("Database");
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
    driverInputPanel.add(dbLabel, gridBagConstraints2);
    
    dbField.setColumns(20);
    dbField.setText(database);
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    driverInputPanel.add(dbField, gridBagConstraints2);
    
    tableLabel.setText("Table");
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
    driverInputPanel.add(tableLabel, gridBagConstraints2);
    
    tableField.setColumns(20);
    tableField.setText(table);
    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    driverInputPanel.add(tableField, gridBagConstraints2);
    
    driverPanel.add(driverInputPanel, java.awt.BorderLayout.CENTER);
    
    driverOK.setText("OK");
    driverOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveDriverSettings(evt);
      }
    });
    
    driverButtonPanel.add(driverOK);
    
    driverCancel.setText("Cancel");
    driverCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancel(evt);
      }
    });
    
    driverButtonPanel.add(driverCancel);
    
    driverTest.setText("Test");
    driverTest.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        testDriverSettings(evt);
      }
    });
    
    driverButtonPanel.add(driverTest);
    
    driverPanel.add(driverButtonPanel, java.awt.BorderLayout.SOUTH);
    
    tabbedPane.addTab("Driver Settings", driverPanel);
    
    fieldPanel.setLayout(new java.awt.BorderLayout());
    
    fieldPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        showFieldMap(evt);
      }
    });
    
    fieldMapping.setModel(tableMap.toTableModel());
    fieldScrollPane.setViewportView(fieldMapping);
    
    fieldPanel.add(fieldScrollPane, java.awt.BorderLayout.CENTER);
    
    fieldButtonPanel.setLayout(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints gridBagConstraints3;
    
    fieldOK.setText("OK");
    fieldOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveDriverSettings(evt);
      }
    });
    
    gridBagConstraints3 = new java.awt.GridBagConstraints();
    fieldButtonPanel.add(fieldOK, gridBagConstraints3);
    
    fieldCancel.setText("Cancel");
    fieldCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancel(evt);
      }
    });
    
    gridBagConstraints3 = new java.awt.GridBagConstraints();
    fieldButtonPanel.add(fieldCancel, gridBagConstraints3);
    
    fieldRefresh.setText("Refresh");
    fieldRefresh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        refreshFieldMap(evt);
      }
    });
    
    gridBagConstraints3 = new java.awt.GridBagConstraints();
    fieldButtonPanel.add(fieldRefresh, gridBagConstraints3);
    
    fieldPanel.add(fieldButtonPanel, java.awt.BorderLayout.SOUTH);
    
    tabbedPane.addTab("Field Mappings", fieldPanel);
    
    getContentPane().add(tabbedPane);
    
    pack();
  }//GEN-END:initComponents

  private void refreshFieldMap(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshFieldMap
      try {
        tableMap.init();
        fieldMapping.setModel(tableMap.toTableModel());
        fieldMapping.repaint();
      } catch (SQLException e) {
        System.err.println("Couldn't initialize table mapping");
      }
  }//GEN-LAST:event_refreshFieldMap
  
  private void showFieldMap(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showFieldMap
    if(tableMap.size() == 0) {
      try {
        tableMap.init();
        fieldMapping.setModel(tableMap.toTableModel());
        fieldMapping.repaint();
      } catch (SQLException e) {
        System.err.println("Couldn't initialize table mapping");
      }
    }
  }//GEN-LAST:event_showFieldMap
  
  private void testDriverSettings(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testDriverSettings
    name = nameField.getText();
    url = urlField.getText();
    database = dbField.getText();
    table = tableField.getText();
    Connection conn = openConnection();
    if(conn != null) {
      JOptionPane.showMessageDialog(this, "Driver connection verified.", "Driver verified", JOptionPane.INFORMATION_MESSAGE);
      try {
        conn.close();
      } catch (SQLException e) {
        System.err.println("Couldn't close database driver during test.");
      }
    }
  }//GEN-LAST:event_testDriverSettings
  
  private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
    exitForm();
  }//GEN-LAST:event_cancel
  
  private void saveDriverSettings(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDriverSettings
    name = nameField.getText();
    url = urlField.getText();
    database = dbField.getText();
    table = tableField.getText();
    for(int i=0; i < tableMap.size(); i++) {
      String value = (String)fieldMapping.getValueAt(i, 2);
      FieldMap record = (FieldMap)tableMap.elementAt(i);
      record.valueExpression = value;
    }
    savePrefs();
    exitForm();
  }//GEN-LAST:event_saveDriverSettings
      private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        exitForm();
    }//GEN-LAST:event_exitForm
    
    private void exitForm() {
      setVisible(false);
    }
    
    private void savePrefs() {
      File prefs = new File("JDBCConnection.def");
      try {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootNode = doc.createElement("jdbccontrolpanel");
        rootNode.setAttribute("version", "0.1");
        doc.appendChild(rootNode);
        
        Element newNode = doc.createElement("driver");
        newNode.setAttribute("name", name);
        newNode.setAttribute("url", url);
        newNode.setAttribute("username", userName);
        newNode.setAttribute("database", database);
        newNode.setAttribute("table", table);
        rootNode.appendChild(newNode);
        
        //Save field mappings
        for(int i=0; i<tableMap.size(); i++){
          FieldMap record = tableMap.elementAt(i);
          newNode = doc.createElement("fieldmap");
          newNode.setAttribute("name", record.dbFieldName);
          newNode.setAttribute("type", ""+record.sqlType);
          newNode.setAttribute("index", ""+record.dbFieldIndex);
          newNode.setAttribute("value", record.valueExpression);
          rootNode.appendChild(newNode);
        }
        
        doc.getDocumentElement().normalize();
        TransformerFactory fac = TransformerFactory.newInstance();
        Transformer trans = fac.newTransformer();
        trans.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(prefs));
      } catch (ParserConfigurationException e) {
        System.err.println("Error writing prefs file: "+e);
        e.printStackTrace(System.out);
      } catch (Exception e) {
        System.err.println("Cannot write prefs file: "+e);
        e.printStackTrace(System.out);
      }
    }
    
    private void readPrefs() {
      File prefs = new File("JDBCConnection.def");
      if (prefs.exists()) {
        try {
          DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
          Document doc = docBuilder.parse(prefs);
          doc.getDocumentElement().normalize();
          
          NamedNodeMap attributes = null;
          
          NodeList drivers = doc.getElementsByTagName("driver");
          Node driver = drivers.item(0);
          attributes = driver.getAttributes();
          name = attributes.getNamedItem("name").getNodeValue();
          url = attributes.getNamedItem("url").getNodeValue();
          userName = attributes.getNamedItem("username").getNodeValue();
          database = attributes.getNamedItem("database").getNodeValue();
          table = attributes.getNamedItem("table").getNodeValue();

          NodeList fieldMaps = doc.getElementsByTagName("fieldmap");
          tableMap.fieldMaps.clear();
          for(int i=0; i<fieldMaps.getLength(); i++){
            Node fieldMap = fieldMaps.item(i);
            attributes = fieldMap.getAttributes();
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
        } catch (SAXParseException e) {
          System.err.println("Error parsing prefs file, line "+e.getLineNumber()+": "+e.getMessage());
        } catch (SAXException e) {
          System.err.println("Error reading prefs file: "+e);
          e.printStackTrace(System.out);
        } catch (Exception e) {
          System.err.println("Cannot read prefs file: "+e);
          e.printStackTrace(System.out);
        }
      }
    }
    
    public Connection openConnection() {
      Connection conn = null;
      errorList = new Vector();
      if(! validated) {
        LoginDialog prompt = new LoginDialog(this, userName);
        prompt.pack();
        prompt.setLocationRelativeTo(this);
        prompt.setVisible(true);
      }
      
      try{
/* This code won't work... though I wish it would. It was supposed to
 * dynamically load a JAR file with a JDBC driver from an arbitrary path.
 * Unfortunately this can't be done because of how the methods
 * getCallerClassLoader() and getCallerClass(ClassLoader, String) in
 * the DriverManager class are written. They expect to be able to do a
 * straight Class.forName(String, true, ClassLoader) which only works if the
 * JAR was present in the classpath when the JVM started. I leave the code
 * here in case this starts working in 1.4. Until then the solution is to
 * use the extension mechanism in JRE 1.2+ and place the necessary JAR files
 * in the JRE's ext/ directory.
        char[] fileSeperator = System.getProperty("file.separator").toCharArray();
        String jarPath = jar.replace(fileSeperator[0], '/');
        URL[] jarURL = {new URL("file://"+jarPath)};
        URLClassLoader jarLoader = new URLClassLoader(jarURL, this.getClass().getClassLoader());
        Driver driver = (Driver)jarLoader.loadClass(name).newInstance();
        DriverManager.registerDriver(driver);
 */
        Class.forName(name);
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
        errorList.addElement("Could not find driver "+name);
      } catch (SQLException e) {
        errorList.addElement("Could not build JDBC connection: "+e);
      } catch (NullPointerException e) {
        errorList.addElement("One or more arguments are null");
      } catch (Exception e) {
        errorList.addElement(e.toString());
      }
      if(errorList.size() > 0)
        JOptionPane.showMessageDialog(this, errorList.elementAt(0), "Database Connection Error", JOptionPane.ERROR_MESSAGE);
      return conn;
    }
    
    public boolean exportTimeRecordSet(TimeRecordSet times) {
      Connection conn = openConnection();
      try {
        if(tableMap.size() == 0) return false;
        String queryString = "?";
        for(int i=1; i<tableMap.size(); i++) queryString += " ,?";
        PreparedStatement insert = conn.prepareStatement("INSERT INTO "+database+"."+table+" VALUES ("+queryString+")");
        for(int j=0; j < times.size(); j++) {
          TimeRecord record = times.elementAt(j);
          if(record.seconds <= 0 || ! record.billable) continue;
          for(int i=0; i < tableMap.size(); i++) {
            FieldMap fieldMap = tableMap.elementAt(i);
            Object fieldValue = fieldMap.getValue(record);
            insert.setObject(i+1, fieldValue);
          }
          insert.execute();
        }
        conn.commit();
        insert.close();
        conn.close();
        return true;
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Could not export timelist to database: "+e);
        return false;
      }
    }
    
    public static String typeString(short sqlType) {
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel driverPanel;
    private javax.swing.JPanel driverInputPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel urlLabel;
    private javax.swing.JTextField urlField;
    private javax.swing.JLabel dbLabel;
    private javax.swing.JTextField dbField;
    private javax.swing.JLabel tableLabel;
    private javax.swing.JTextField tableField;
    private javax.swing.JPanel driverButtonPanel;
    private javax.swing.JButton driverOK;
    private javax.swing.JButton driverCancel;
    private javax.swing.JButton driverTest;
    private javax.swing.JPanel fieldPanel;
    private javax.swing.JScrollPane fieldScrollPane;
    private javax.swing.JTable fieldMapping;
    private javax.swing.JPanel fieldButtonPanel;
    private javax.swing.JButton fieldOK;
    private javax.swing.JButton fieldCancel;
    private javax.swing.JButton fieldRefresh;
    // End of variables declaration//GEN-END:variables
    
    private class LoginDialog extends JDialog {
      private JOptionPane optionPane;
      
      LoginDialog(JFrame frame, String user) {
        super(frame, true);
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
            optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
          }
        });
        
        optionPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
          public void propertyChange(java.beans.PropertyChangeEvent e) {
            String prop = e.getPropertyName();
            
            if (isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
              String value = optionPane.getValue().toString();
              if (value == JOptionPane.UNINITIALIZED_VALUE) return;
              optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
              
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
    
    private class TableMap {
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
    
    private class FieldMap {
      short sqlType;
      int dbFieldIndex;
      String dbFieldName;
      String valueExpression;
      
      FieldMap(ResultSet rs) throws java.sql.SQLException {
        dbFieldName = rs.getString(4);
        sqlType = rs.getShort(5);
        dbFieldIndex = rs.getInt(17);
        valueExpression = "";
      }
      
      FieldMap(String name, short type, int index, String value) {
        dbFieldName = name;
        sqlType = type;
        dbFieldIndex = index;
        valueExpression = value;
      }
      
      public String toString() {
        return dbFieldName+"("+dbFieldIndex+"): "+valueExpression+" type "+typeString(sqlType);
      }
      
      protected Object getValue(TimeRecord record) throws ClassCastException {
        StringTokenizer toker = new StringTokenizer(valueExpression, "$ ", true);
        Object realValue = null;
        while(toker.hasMoreTokens()) {
          String value = toker.nextToken();
          if(value.equals("$")) {
            value = toker.nextToken();
            if(value.equals("PROJECT")) {
              if(sqlType != java.sql.Types.CHAR) throw new ClassCastException("Must be CHAR SQL type for project name");
              else realValue = record.projectName;
            } else if(value.equals("DECIMALDATE")) {
              if(sqlType != java.sql.Types.DECIMAL) throw new ClassCastException("Must be DECIMAL type for entry date");
              else {
                Calendar today = Calendar.getInstance();
                int year = today.get(Calendar.YEAR);
                int month = today.get(Calendar.MONTH)+1;
                int date = today.get(Calendar.DATE);
                double cymd = (year*10000)+(month*100)+date;
                realValue = new java.math.BigDecimal(cymd);
              }
            } else if(value.equals("SQLDATE")) {
              if(sqlType != java.sql.Types.DATE) throw new ClassCastException("Must be DATE SQL type for entry date");
              else realValue = new java.sql.Date(System.currentTimeMillis());
            } else if(value.equals("TIMESTAMP")) {
              if(sqlType != java.sql.Types.TIMESTAMP) throw new ClassCastException("Must be TIMESTAMP SQL type for entry timestamp");
              else realValue = new java.sql.Timestamp(System.currentTimeMillis());
            } else if(value.equals("HOURS")) {
              if(sqlType != java.sql.Types.DECIMAL) throw new ClassCastException("Must be DECIMAL SQL type for project name");
              else realValue = new java.math.BigDecimal(record.getHours(60*15));
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