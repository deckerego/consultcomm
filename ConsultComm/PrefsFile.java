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
      rootNode.setAttribute("version", "2.2");
      doc.appendChild(rootNode);
    }
    rootNode.normalize();
  }
  
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
  
  public String readFirstString(String element, String attribute) {
    String strString = readFirstItem(element, attribute);
    if(strString == null) return "";
    else return strString;
  }
  
  public int readFirstInt(String element, String attribute) {
    String intString = readFirstItem(element, attribute);
    if(intString == null) return 0;
    else return Integer.parseInt(intString);
  }

  public float readFirstFloat(String element, String attribute) {
    String floatString = readFirstItem(element, attribute);
    if(floatString == null) return 0;
    else return Float.parseFloat(floatString);
  }
  
  public long readFirstLong(String element, String attribute) {
    String longString = readFirstItem(element, attribute);
    if(longString == null) return 0;
    else return Long.parseLong(longString);
  }
  
  public double readFirstDouble(String element, String attribute) {
    String doubleString = readFirstItem(element, attribute);
    if(doubleString == null) return 0;
    else return Double.parseDouble(doubleString);
  }
  
  public Boolean readFirstBoolean(String element, String attribute) {
    String booleanString = readFirstItem(element, attribute);
    if(booleanString == null) return null;
    else 
      if(booleanString.equals("true")) return new Boolean(true);
      else return new Boolean(false);
  }
  
  public void saveFirst(String element, String attribute, String value) {
    NodeList elements = doc.getElementsByTagName(element);
    Element newNode = doc.createElement(element);
    newNode.setAttribute(attribute, value);
    Node node = elements.item(0);
    if(node != null) rootNode.replaceChild(newNode, node);
    else rootNode.appendChild(newNode);
  }

  public void saveFirst(String element, String[] attribute, String[] value) {
    NodeList elements = doc.getElementsByTagName(element);
    Element newNode = doc.createElement(element);
    for(int i=0; i < attribute.length; i++)
      newNode.setAttribute(attribute[i], value[i]);
    Node node = elements.item(0);
    if(node != null) rootNode.replaceChild(newNode, node);
    else rootNode.appendChild(newNode);
  }
  
  public void saveFirst(String element, String[] attribute, double[] value) {
    String[] valueList = new String[value.length];
    for(int i=0; i<value.length; i++)
      valueList[i] = Double.toString(value[i]);
    saveFirst(element, attribute, valueList);
  }
  
  public void saveFirst(String element, String attribute, int value) {
    saveFirst(element, attribute, Integer.toString(value));
  }
  
  public void saveFirst(String element, String attribute, long value) {
    saveFirst(element, attribute, Long.toString(value));
  }
  
  public void saveFirst(String element, String attribute, double value) {
    saveFirst(element, attribute, Double.toString(value));
  }
  
  public void saveFirst(String element, String attribute, float value) {
    saveFirst(element, attribute, Float.toString(value));
  }
  
  public void saveFirst(String element, String attribute, boolean value) {
    saveFirst(element, attribute, value ? "true" : "false");
  }
  
  public Element createElement(String element) {
    return doc.createElement(element);
  }
  
  public void appendChild(Element element) {
    rootNode.appendChild(element);
  }
  
  public NodeList getElementsByTagName(String element) {
    return doc.getElementsByTagName(element);
  }
  
  public void removeFirstElement(String element) {
    NodeList elements = doc.getElementsByTagName(element);
    Node node = elements.item(0);
    rootNode.removeChild(node);
  }
  
  public void removeAllChildren(String element) {
    NodeList elements = rootNode.getElementsByTagName(element);
    for(int i=elements.getLength()-1; i>=0; i--)
      rootNode.removeChild(elements.item(i));
  }
  
  public void write() throws TransformerConfigurationException, TransformerException {
    File stylesheet = CsltComm.getFile(this, "stylesheet.xsl");
    doc.getDocumentElement().normalize();
    StreamSource stylesource = new StreamSource(stylesheet);
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(stylesource);
    transformer.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(prefs));
    stylesheet.delete();
  }
}
