package amf.io;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.*;

import amf.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import org.jdom.output.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLcreator {

	//No generics
	List nodes;
	Document dom;
	String type;
	
	public XMLcreator(Automaton automaton) {
		//create a list to hold the data
		nodes = automaton.getChildren();
		type = "Markov Chain";
		if (automaton instanceof MarkovChain) //TODO use Automaton type
			{ type  = "Markov Chain"; }
		//Get a DOM object
		createDocument();
	}


	public void WriteToXML(String file){
		System.out.println("Started .. ");
		createDOMTree();
		try {
			printToFile(file);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Generated file successfully.");
	}


	/**
	 * Using JAXP in implementation independent manner create a document object
	 * using which we create a xml tree in memory
	 */
	private void createDocument() {

		//get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
		//get an instance of builder
		DocumentBuilder db = dbf.newDocumentBuilder();

		//create an instance of DOM
		dom = db.newDocument();

		}catch(ParserConfigurationException pce) {
			//dump it
			System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			System.exit(1);
		}

	}

	/**
	 * The real workhorse which creates the XML structure
	 */
	private void createDOMTree(){

		//create the root element <Automaton>
		Element rootEle = dom.createElement("Automaton");
		rootEle.setAttribute("Type",type);
		dom.appendChild(rootEle);

		Iterator it  = nodes.iterator();
		while(it.hasNext()) {
			Node n = (Node)it.next();
			//For each Node object  create <Node> element and attach it to root
			Element nodeEle = createNodeElement(n);
			rootEle.appendChild(nodeEle);
		}

	}

	/**
	 * Helper method which creates a XML element <Node>
	 * @param n The Node for which we need to create an xml representation
	 * @return XML element snippet representing a Node
	 */
	private Element createNodeElement(Node n){
		
		Element NodeEle = dom.createElement("Node");
		String NodeType="";
		if (n instanceof StartingNode) {
			NodeType = "start state";
		} else if (n instanceof EndingNode) {
			NodeType = "end state";
		} else if (n instanceof BranchNode) {
			NodeType = "branch node";
		} else if (n instanceof StateNode) {
			NodeType = "basic state";
		} 
		//Get the location of the node
		Element LocEle = createLocationElement(n.getLocation());
		NodeEle.appendChild(LocEle);
		//Get the attributes
		Element AttributesEle = dom.createElement("NodeAttributes");
		if(n instanceof StateNode){
			for(Object a: ((StateNode) n).getAttributes())
			{
				Attribute attr = ((Attribute) a);
				Element AttrEle = dom.createElement("Attribute");
				AttrEle.setAttribute("Name",attr.getName());
				AttrEle.setAttribute("Value",attr.getValue());
				//store the attributes' location relative to the attributes parent node
				Point attrLocation = attr.getLocation().translate(-n.getLocation().x, -n.getLocation().y);
				Element LocAttrEle = createLocationElement(attrLocation);
				AttrEle.appendChild(LocAttrEle);
				AttributesEle.appendChild(AttrEle);
			}
		}
		NodeEle.appendChild(AttributesEle);
		NodeEle.setAttribute("Type", NodeType);
		NodeEle.setAttribute("ID", Integer.toString(n.hashCode()));
		//TODO append ID to the object NodeEle.setAttribute("ID", id);
		
		Element SourceTrEle = dom.createElement("SourceTr");
		for(Object t: n.getSourceTransitions())
			{
				Element TrEle = createTransitionElement((Transition) t);
				SourceTrEle.appendChild(TrEle);
			}
		NodeEle.appendChild(SourceTrEle);
		
		Element TargetTrEle = dom.createElement("TargetTr");
		for(Object t: n.getTargetTransition())
			{
				Element TrEle = createTransitionElement((Transition) t);
				TargetTrEle.appendChild(TrEle);
			}
		NodeEle.appendChild(TargetTrEle);
		
		return NodeEle;

	}
	
	private Element createLocationElement(Point location){
		Element LocEle = dom.createElement("Location");
		Element xEle = dom.createElement("x");
		Text x = dom.createTextNode(Integer.toString(location.x));
		xEle.appendChild(x);
		Element yEle = dom.createElement("y");
		Text y = dom.createTextNode(Integer.toString(location.y));
		yEle.appendChild(y);
		LocEle.appendChild(xEle);
		LocEle.appendChild(yEle);		
		return LocEle;
	}
	
	private Element createTransitionElement(Transition t){
		Element TrEle = dom.createElement("Transition");
		//Getting the Attributes of the Transition
		for(Object a: ((Transition) t).getAttributes())
		{
			Attribute attr = ((Attribute) a);
			Element AttrEle = dom.createElement("Attribute");
			AttrEle.setAttribute("Name",attr.getName());
			AttrEle.setAttribute("Value",attr.getValue());
			Element LocAttrEle = createLocationElement(attr.getLocation());
			AttrEle.appendChild(LocAttrEle);
			TrEle.appendChild(AttrEle);
		}
		Element SourceEle = dom.createElement("Source");
		Text Sid = dom.createTextNode(Integer.toString(t.getSource().hashCode()));
		SourceEle.appendChild(Sid);
		Element TargetEle = dom.createElement("Target");
		Text Tid = dom.createTextNode(Integer.toString(t.getTarget().hashCode()));
		TargetEle.appendChild(Tid);
		TrEle.appendChild(SourceEle);
		TrEle.appendChild(TargetEle);
	return TrEle;
	}
	/**
	 * This method prints the XML document to file.
	 * @param filename
	 * @throws TransformerException 
     */
	private void printToFile(String file) throws TransformerException{
		
		System.out.println("Write to: "+file);
		  
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
			    
		StreamResult streamResult = new StreamResult(file);
		DOMSource domSource = new DOMSource(dom);
		transformer.transform(domSource, streamResult);
	
	}
	/*
	public static void main(String args[]) {

		//create an instance
		XMLcreator xce = new XMLcreator(new Automaton());

		//run the example
		xce.WriteToXML("AMF_test2.xml");
	}*/
}


