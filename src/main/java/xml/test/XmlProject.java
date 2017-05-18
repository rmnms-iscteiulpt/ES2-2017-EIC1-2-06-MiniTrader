package xml.test;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class XmlProject {
   public static void main(String[] args){
      try {	
         File inputFile = new File("MicroTraderPersistence.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();         
         NodeList nList = doc.getElementsByTagName("Order");
         System.out.println("----- Navigate the tree nodes -----");
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.print(nNode.getNodeName() + " ");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               System.out.print("Id:" + eElement.getAttribute("Id"));
               System.out.print(" Type:" + eElement.getAttribute("Type"));
               System.out.print(" Stock:" + eElement.getAttribute("Stock"));
               System.out.print(" Units:" + eElement.getAttribute("Units"));
               System.out.print(" Price:" + eElement.getAttribute("Price"));
               System.out.println();
            }
         }
         System.out.println("----- Search the tree with xpath queries -----");  
         // Query 1 
         XPathFactory xpathFactory = XPathFactory.newInstance();
         XPath xpath = xpathFactory.newXPath();
         XPathExpression expr = xpath.compile("/XML/Order[@Id='2']/@*");
         NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
         System.out.print("Order ");
         for (int i = 0; i < nl.getLength(); i++) {
             System.out.print(nl.item(i).getNodeName()  + ":");
             System.out.print(nl.item(i).getFirstChild().getNodeValue()  + " ");
         }
         // Query 2
         expr = xpath.compile("/XML/Order[@Id='2']/Customer");
         String str = (String) expr.evaluate(doc, XPathConstants.STRING);
         System.out.println();System.out.println("Customer of Order Id=5: " + str);
         
         // Create new element Order with attributes
         Element newElementOrder = doc.createElement("Order");
         newElementOrder.setAttribute("Id", "5");
         newElementOrder.setAttribute("Type", "Buy");
         newElementOrder.setAttribute("Stock", "PT");
         newElementOrder.setAttribute("Units", "15");
         newElementOrder.setAttribute("Price", "20");

         // Create new element Customer
         Element newElementCustomer = doc.createElement("Customer");

         newElementCustomer.setTextContent("Manuel"); 
         newElementOrder.appendChild(newElementCustomer);
         
         // Add new node to XML document root element
         System.out.println("----- Adding new element to root element -----");
         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());         
         System.out.println("Add Order Id='5' Type='Buy' Stock='PT' Units='15' Price='20'");
         Node n = doc.getDocumentElement();
         n.appendChild(newElementOrder);
         // Save XML document
         System.out.println("Save XML document.");
         Transformer transformer = TransformerFactory.newInstance().newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         StreamResult result = new StreamResult(new FileOutputStream("MicroTraderPersistence.xml"));
         DOMSource source = new DOMSource(doc);
         transformer.transform(source, result);
      } catch (Exception e) { e.printStackTrace(); }
   }
}