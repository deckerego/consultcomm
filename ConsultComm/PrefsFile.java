//Standard Components
import java.util.*;
import java.io.*;
//XML Components
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class PrefsFile {
  File prefs;
  Document doc;
  Element rootNode;
  public static File prefsDir = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"CsltComm");
  
  /**
   * Create a new XML preferences file
   * @param file The file to read from
   */
  public PrefsFile(String file) throws ParserConfigurationException, SAXException, IOException {
    prefsDir.mkdir();
    prefs = new File(prefsDir, file);
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    docBuilder = docBuilderFactory.newDocumentBuilder();
    
    if (prefs.exists()) {
      doc = docBuilder.parse(prefs);
      rootNode = doc.getDocumentElement();
    } else {
      doc = docBuilder.newDocument();
      rootNode = doc.createElement("clntcomm");
      rootNode.setAttribute("version", "2.4");
      doc.appendChild(rootNode);
    }
    rootNode.normalize();
    
    //Check to see if we're importing settings from an old version. If so, convert then.
    String version = readFirstItem("clntcomm", "version");
    if(version.equals("2.2")) convertFrom22();
  }
  
  /**
   * Reads the first attribute within the given element. Helpful if you
   * just have a singular value to retreive.
   * @param element The XML element to read from
   * @param attribute The tag attribute that has the necessary string
   * @return The attribute string, <CODE>null</CODE> if nothing is found.
   */
  public String readFirstItem(String element, String attribute) {
    String infoString = null;

    try {
      NodeList infos = doc.getElementsByTagName(element);
      Node info = infos.item(0);
      NamedNodeMap attributes = info.getAttributes();
      infoString = attributes.getNamedItem(attribute).getNodeValue();
    } catch (NullPointerException e) {}    
    return infoString;
  }
  
  /**
   * Reads the first attribute within the given element. Helpful if you
   * just have a singular value to retreive.
   * @param element The XML element to read from
   * @param attribute The tag attribute that has the necessary string
   * @return The attribute string, an empty string if nothing is found.
   * @see #readFirstItem(String, String)
   */
  public String readFirstString(String element, String attribute) {
    String strString = readFirstItem(element, attribute);
    if(strString == null) return "";
    else return strString;
  }
  
  /**
   * Reads the first attribute within the given element and casts it
   * to an integer. Helpful if you just have a singular value to retreive.
   * @param element The XML element to read from
   * @param attribute The tag attribute that has the necessary string
   * @return The attribute integer, 0 if nothing is found.
   * @see #readFirstItem(String, String)
   */
  public int readFirstInt(String element, String attribute) {
    String intString = readFirstItem(element, attribute);
    if(intString == null) return 0;
    else return Integer.parseInt(intString);
  }

  /**
   * Reads the first attribute within the given element and casts it
   * to a float. Helpful if you just have a singular value to retreive.
   * @param element The XML element to read from
   * @param attribute The tag attribute that has the necessary string
   * @return The attribute integer, 0 if nothing is found.
   * @see #readFirstItem(String, String)
   */
  public float readFirstFloat(String element, String attribute) {
    String floatString = readFirstItem(element, attribute);
    if(floatString == null) return 0;
    else return Float.parseFloat(floatString);
  }
  
  /**
   * Reads the first attribute within the given element and casts it
   * to a long. Helpful if you just have a singular value to retreive.
   * @param element The XML element to read from
   * @param attribute The tag attribute that has the necessary string
   * @return The attribute integer, 0 if nothing is found.
   * @see #readFirstItem(String, String)
   */
  public long readFirstLong(String element, String attribute) {
    String longString = readFirstItem(element, attribute);
    if(longString == null) return 0;
    else return Long.parseLong(longString);
  }
  
  /**
   * Reads the first attribute within the given element and casts it
   * to an double. Helpful if you just have a singular value to retreive.
   * @param element The XML element to read from
   * @param attribute The tag attribute that has the necessary string
   * @return The attribute integer, 0 if nothing is found.
   * @see #readFirstItem(String, String)
   */
  public double readFirstDouble(String element, String attribute) {
    String doubleString = readFirstItem(element, attribute);
    if(doubleString == null) return 0;
    else return Double.parseDouble(doubleString);
  }
  
  /**
   * Reads the first attribute within the given element and casts it
   * to a Boolean. Helpful if you just have a singular value to retreive.
   * @param element The XML element to read from
   * @param attribute The tag attribute that has the necessary string
   * @return The attribute integer, <CODE>null</CODE> if nothing is found.
   * @see #readFirstItem(String, String)
   */
  public Boolean readFirstBoolean(String element, String attribute) {
    String booleanString = readFirstItem(element, attribute);
    if(booleanString == null) return null;
    else return new Boolean(booleanString);
  }
  
  /**
   * Save the given attribute as a single element under the root node.
   * @param element The XML element to create/overwrite
   * @param attribute The tag attribute that contains our given value
   * @param value The attribute value to save.
   */
  public void saveFirst(String element, String attribute, String value) {
    NodeList elements = doc.getElementsByTagName(element);
    Element newNode = doc.createElement(element);
    newNode.setAttribute(attribute, value);
    Node node = elements.item(0);
    if(node != null) rootNode.replaceChild(newNode, node);
    else rootNode.appendChild(newNode);
  }

  /**
   * Save the given attribute as a single element under the root node.
   * @param element The XML element to create/overwrite
   * @param attribute The list of tag attributes that contain our given values
   * @param value The list attribute values to save.
   */  
  public void saveFirst(String element, String[] attribute, String[] value) {
    NodeList elements = doc.getElementsByTagName(element);
    Element newNode = doc.createElement(element);
    for(int i=0; i < attribute.length; i++)
      newNode.setAttribute(attribute[i], value[i]);
    Node node = elements.item(0);
    if(node != null) rootNode.replaceChild(newNode, node);
    else rootNode.appendChild(newNode);
  }
  
  /**
   * Save the given attribute as a single element under the root node.
   * @param element The XML element to create/overwrite
   * @param attribute The list of tag attributes that contain our given values
   * @param value The list attribute values to save.
   * @see #saveFirst(String, String[], String[])
   */  
  public void saveFirst(String element, String[] attribute, double[] value) {
    String[] valueList = new String[value.length];
    for(int i=0; i<value.length; i++)
      valueList[i] = Double.toString(value[i]);
    saveFirst(element, attribute, valueList);
  }
  
  /**
   * Save the given attribute as a single element under the root node.
   * @param element The XML element to create/overwrite
   * @param attribute The tag attribute that contains our given value
   * @param value The attribute value to save.
   * @see #saveFirst(String, String, String)
   */
  public void saveFirst(String element, String attribute, int value) {
    saveFirst(element, attribute, Integer.toString(value));
  }
  
  /**
   * Save the given attribute as a single element under the root node.
   * @param element The XML element to create/overwrite
   * @param attribute The tag attribute that contains our given value
   * @param value The attribute value to save.
   * @see #saveFirst(String, String, String)
   */
  public void saveFirst(String element, String attribute, long value) {
    saveFirst(element, attribute, Long.toString(value));
  }
  
  /**
   * Save the given attribute as a single element under the root node.
   * @param element The XML element to create/overwrite
   * @param attribute The tag attribute that contains our given value
   * @param value The attribute value to save.
   * @see #saveFirst(String, String, String)
   */
  public void saveFirst(String element, String attribute, double value) {
    saveFirst(element, attribute, Double.toString(value));
  }
  
  /**
   * Save the given attribute as a single element under the root node.
   * @param element The XML element to create/overwrite
   * @param attribute The tag attribute that contains our given value
   * @param value The attribute value to save.
   * @see #saveFirst(String, String, String)
   */
  public void saveFirst(String element, String attribute, float value) {
    saveFirst(element, attribute, Float.toString(value));
  }
  
  /**
   * Save the given attribute as a single element under the root node.
   * @param element The XML element to create/overwrite
   * @param attribute The tag attribute that contains our given value
   * @param value The attribute value to save.
   * @see #saveFirst(String, String, String)
   */
  public void saveFirst(String element, String attribute, boolean value) {
    saveFirst(element, attribute, value ? "true" : "false");
  }

  /**
   * @param element The XML element to create
   * @see org.w3c.dom.Document#createElement(String)
   */
  public Element createElement(String element) {
    return doc.createElement(element);
  }
  
  /**
   * @param element The XML element to append to the root node
   * @see org.w3c.dom.Node#appendChild(Element)
   */
  public void appendChild(Element element) {
    rootNode.appendChild(element);
  }
  
  /**
   * @param element The XML element name to retreive
   * @return A <CODE>NodeList</CODE> containing all nodes matching the element name
   * @see org.w3c.dom.Document#getElementsByTagName(String)
   */
  public NodeList getElementsByTagName(String element) {
    return doc.getElementsByTagName(element);
  }
  
  /**
   * Removes the first occurence of a given <CODE>Element</CODE>
   * within an XML document
   * @param element The XML element name to remove
   */
  public void removeFirstElement(String element) {
    NodeList elements = doc.getElementsByTagName(element);
    Node node = elements.item(0);
    if(node != null) rootNode.removeChild(node);
  }
  
  /**
   * Removes all occurences of a given <CODE>Element</CODE>
   * within an XML document
   * @param element The XML element name to remove
   */
  public void removeAllChildren(String element) {
    NodeList elements = rootNode.getElementsByTagName(element);
    for(int i=elements.getLength()-1; i>=0; i--)
      rootNode.removeChild(elements.item(i));
  }
  
  /**
   * Commit the given XML representation to a file using the included stylesheet
   * to remove whitespace and preserve indentation
   */
  public void write() throws TransformerConfigurationException, TransformerException {
    File stylesheet = CsltComm.getFile(this, "stylesheet.xsl");
    doc.getDocumentElement().normalize();
    StreamSource stylesource = new StreamSource(stylesheet);
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(stylesource);
    transformer.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(prefs));
    stylesheet.delete();
  }
  
  /**
   * Used to convert ConsultComm 2.2 prefs to the new 2.4 prefs format.
   * All that really happens is removing the project list from
   * ClntComm.def and placing it in projects.xml.
   */
  public void convertFrom22() {
    int selectedIndex = -1;
    
    try {
      //Update root node
      this.rootNode.setAttribute("version", "2.4");
      
      //Get all projects
      NodeList projects = this.getElementsByTagName("project");
      TimeRecordSet times = new TimeRecordSet();
      for(int i=0; i<projects.getLength(); i++){
        Node project = projects.item(i);
        NamedNodeMap attributes = project.getAttributes();
        Node nameNode = attributes.getNamedItem("name");
        String name = nameNode.getNodeValue();
        Node aliasNode = attributes.getNamedItem("alias");
        String alias = null;
        if(aliasNode != null) alias = aliasNode.getNodeValue();
        Node secondsNode = attributes.getNamedItem("seconds");
        long seconds = Long.parseLong(secondsNode.getNodeValue());
        Node billableNode = attributes.getNamedItem("billable");
        boolean billable = ! billableNode.getNodeValue().equals("false");
        Node exportNode = attributes.getNamedItem("export");
        boolean export = exportNode != null ? ! exportNode.getNodeValue().equals("false") : billable;
        
        Node selectedNode = attributes.getNamedItem("selected");
        if(selectedNode != null && selectedNode.getNodeValue().equals("true"))
          selectedIndex = i;
        TimeRecord record = new TimeRecord("TEST", name, seconds, billable);
        times.add(record);
      }

      //Delete projects
      this.removeAllChildren("project");
      
      //Save projects
      PrefsFile projs = new PrefsFile("projects.xml");
      for(int i=0; i<times.size(); i++){
        TimeRecord record = times.elementAt(i);
        Element newNode = projs.createElement("project");
        newNode.setAttribute("name", record.getProjectName());
        newNode.setAttribute("seconds", Long.toString(record.getSeconds()));
        newNode.setAttribute("billable", record.isBillable() ? "true" : "false");
        if(i == selectedIndex) newNode.setAttribute("selected", "true");
        projs.appendChild(newNode);
      }
      
      //Write out new files
      this.write();
      projs.write();
    } catch (Exception e) {
      System.err.println("Cannot read prefs file: "+e);
      e.printStackTrace(System.out);
    }
  }
}
