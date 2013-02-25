package amf.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import amf.model.*;

public class XMLreader {

	
	Document dom;


	public XMLreader(){
		
	}

	public Automaton ReadFromXML(String file) {
		//parse the xml file and get the dom object
		parseXmlFile(file);
		Automaton diagram = parseDocument();
		return diagram;
	}

	//Get the DOM model
	private void parseXmlFile(String file){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(file);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	//Get the diagram
	private Automaton parseDocument(){
		Automaton diagram = new Automaton();
		try{
		//get the root element and initialize Automaton depending on the Type attr of the root element
		Element docEle = dom.getDocumentElement();
		String AutomatonType = docEle.getAttribute("Type");
		if(AutomatonType.equals("Markov Chain")){
			diagram = new MarkovChain();
		}//TODO implement other Automaton types else if(AutomatonType.equals("sometype")){}
		
		//get a nodelist of <Node> elements
		NodeList nl = docEle.getElementsByTagName("Node");
		if(nl != null && nl.getLength() > 0) {
			//Creating the nodes
			Point DefaultLocation = new Point(50,150); // here we use some default location for cases when
			List LostLocationNodes = new ArrayList();
			for(int i = 0 ; i < nl.getLength();i++) {
				
				//get the Node element
				Element el = (Element)nl.item(i);
				
				//get the Node object
				Node n = getNode(el);
				Point location = getLocation(el);
				DefaultLocation = DefaultLocation.getTranslated(200, 0);
				if((location.x == 0 && location.y == 0)|| (location.x <= 0 || location.y <= 0))
					{
						n.setLocation(DefaultLocation);
						LostLocationNodes.add(n);
					}
				else
					{
						n.setLocation(location);
					}
							
				//add the Node to the automaton
				diagram.addChild(n);
			}
			// Creating the transitions
			for(int i = 0 ; i < nl.getLength();i++) {
				
				//get the Node element
				Element el = (Element)nl.item(i);
				//get the list of the source transitions
				NodeList sTr = ((Element) el.getElementsByTagName("SourceTr").item(0)).getElementsByTagName("Transition");
				if(sTr != null && sTr.getLength() > 0) {
				for(int j = 0 ; j < sTr.getLength(); j++){
					Element trEle = (Element) sTr.item(j);
					try{
					String sourceID = ((Element) trEle.getElementsByTagName("Source").item(0)).getTextContent();
					String targetID = ((Element) trEle.getElementsByTagName("Target").item(0)).getTextContent();
					Node source = null;
					Node target = null;
					//Get the source and target node objects
					for(Object n: diagram.getChildren()){
						if(Long.toString(((Node) n).getID()).equals(sourceID)){
							source = (Node) n;
						}
						if(Long.toString(((Node) n).getID()).equals(targetID)){
							target = (Node) n;
						}
					}
					//creating a transition
					Transition tr = new Transition(source, target);
					List trAttributes = getAttributes(trEle);			
					tr.setAttributes(trAttributes);
					if( LostLocationNodes.contains(source) || LostLocationNodes.contains(target))
						{
							tr.initialAttributeLayout(new Point());
						}
					}
					catch(NullPointerException e){};
				}
				}
			}
		}
	}
		catch(NullPointerException e){}
		return diagram;
	}


	/**
	 * take a node element and read the values in, create
	 * a Node object and return it
	 * @param n
	 * @return
	 */
	private Node getNode(Element n) {
		Node node = null;
		String Type = n.getAttribute("Type");
		String ID = n.getAttribute("ID");

		//initialize the Node depending on the type
		if (Type.equals("start state")){
			node = new StartingNode();
		}
		else if(Type.equals("end state")){
			node = new EndingNode();
		}
		else if(Type.equals("branch node")){
			node = new BranchNode();
		}
		else if(Type.equals("basic state")){
			node = new StateNode();
		}
		//Set the internal ID
		if(ID!=null && ID.length()!=0){
			node.setID(Long.parseLong(ID));
		}
		//Get the properties of the Node
		if(node instanceof StateNode){
			try{
				List attributes = getAttributes((Element)n.getElementsByTagName("NodeAttributes").item(0));			
				((StateNode) node).setAttributes(attributes);
			}
			catch(NullPointerException e){}
		}
		
		return node;
	}

	//Get the location of the element
	private Point getLocation(Element ele){
		Point location = new Point();
		try{
			int x = Integer.parseInt(((Element)ele.getElementsByTagName("x").item(0)).getTextContent());
			int y = Integer.parseInt(((Element)ele.getElementsByTagName("y").item(0)).getTextContent());	
			location = new Point(x,y);
		}
		catch(NullPointerException e){}
		catch(NumberFormatException e){}
		return location;
	}
	
	//Get the list of attributes from the Attributes element
	private List getAttributes(Element attributesEle){
		List attributes = new ArrayList();
		try{
			NodeList AttrList = attributesEle.getElementsByTagName("Attribute");
			if(AttrList != null && AttrList.getLength() > 0) {
				for(int i = 0 ; i < AttrList.getLength();i++) {
					Element attrEle = (Element)AttrList.item(i); 
					String name = attrEle.getAttribute("Name");
					String value = attrEle.getAttribute("Value"); 
					Attribute a = new Attribute(name, value);
					//Get the location of the attribute
					int x = Integer.parseInt(((Element) attrEle.getElementsByTagName("x").item(0)).getTextContent());
					int y = Integer.parseInt(((Element) attrEle.getElementsByTagName("y").item(0)).getTextContent());
					Point location = new Point(x,y);
					a.setLocation(location);
					attributes.add(a);
					}
			}
		}
		catch(NullPointerException e){};
		return attributes;
	}

}
