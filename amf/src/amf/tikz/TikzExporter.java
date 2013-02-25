package amf.tikz;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import amf.model.*;


public class TikzExporter {
	private final static String header =    "\\documentclass[a4paper,8pt]{article}\n" + 
											"\\usepackage[english]{babel}\n" + 	
											"\\usepackage[T1]{fontenc}\n" +
											"\\usepackage[ansinew]{inputenc}\n" + 
											"\\usepackage{lmodern}	% font definition\n" +
											"\\usepackage{amsmath}	% math fonts\n" +
											"\\usepackage{amsthm}\n" +
											"\\usepackage{amsfonts}\n" +
											"\\usepackage{tikz}\n" +
											"\\newcommand{\\trans}[4]{\\draw[->] (#1, #2) -- (#3, #4)}\n" +
											"\\newcommand{\\transb}[4]{\\draw[-] (#1, #2) -- (#3, #4)}\n" +
											"\\newcommand{\\bnode}[2]{\\fill (#1, #2) circle(0.15); }\n" +
											"\\newcommand{\\state}[2]{\\draw (#1, #2) circle (0.5)}\n" +
											"\\newcommand{\\fstate}[2]{\\draw (#1, #2) circle (0.5); \\draw(#1, #2) circle(0.35)}\n" +
											"\\newcommand{\\sstate}[2]{\\state{#1}{#2}; \\trans{#1-0.5}{#2+0.5}{#1-0.375}{#2+0.375}}\n" +
											"\\newcommand{\\lbl}[3]{\\draw (#1, #2) node{#3}}\n" +
											"\\begin{document}\n" + 
											"\\begin{tikzpicture}[>=stealth]\n";

	private final static String footer =    "\\end{tikzpicture}\n" + 
											"\\end{document}\n";

	private StringBuilder output;
	private Point left;
	private Point right;
	void exportToTikz(String filename, Automaton automaton) throws IOException {
		output = new StringBuilder();
		left = new Point(0, 0);
		right = new Point(0, 0);
		for (Object x : automaton.getChildren()) {
			Node node = (Node) x;
			Point p = node.getLocation();
			Dimension d = node.getSize();
			Point l = p.getTranslated(d.getNegated());
			Point r = p.getTranslated(d);
			left.x = Math.min(left.x, l.x);
			left.y = Math.min(left.y, l.y);
			right.x = Math.max(right.x, r.x);
			right.y = Math.max(right.y, r.y);
		}
				
		for (Object x : automaton.getChildren()) {
			Node node = (Node) x;
			print(node);
			for (Object y : node.getSourceTransitions()) {
				Transition t = (Transition) y;
				print(t);
			}
		}
		FileWriter file = new FileWriter(filename);
		file.write(header);
		file.write(output.toString().replace('_', ' '));
		file.write(footer);
		file.close();
	}

	Point translate(Point p) {
		double x, y;
		x = (p.x - left.x) * 1.0 / (right.x - left.x);
		y = 1.0 - (p.y - left.y) * 1.0 / (right.y - left.y);
		return new Point((int)(x * 1000), (int)(y * 1000));
	}
	
	String coordinateToString(int x) {
		return new Double(x / 100.0).toString();
	}
	

	void print(Node node) {
		if (node instanceof EndingNode) print(node, "fstate");
		else if (node instanceof StartingNode) print(node, "sstate");
		else if (node instanceof StateNode) print(node, "state");
		else if (node instanceof BranchNode) print(node, "bnode");
		else throw new IllegalArgumentException("node type is invalid");
	}

	void printAllAttributes(ModelElement elem)
	{
		if (elem instanceof AttributeProvdier)
		{
			AttributeProvdier ap = (AttributeProvdier) elem;
			for (Attribute a : ap.getAttributes()) {
				print(a);
			}
		}
	}
	void print(Node node, String tikzCmd) {
		Point p = translate(node.getLocation());
		output.append("\\" + tikzCmd);
		output.append("{");
		output.append(coordinateToString(p.x));
		output.append("}");
		output.append("{");
		output.append(coordinateToString(p.y));
		output.append("}");
		output.append(";\n");
		printAllAttributes(node);		
	}
	
	void print(Transition t) {
		Point head = translate(t.getSource().getLocation());
		Point tail = translate(t.getTarget().getLocation());
		// compute direction between node centers
		double ux, uy, len;
		ux = (tail.x - head.x);
		uy = (tail.y - head.y);
	  
	 	
		Dimension delta = new Dimension((int)ux, (int)uy);
		len = Math.sqrt(ux*ux + uy*uy);
		
		// branch nodes are only 30% size of the node (this is a magic number)
		double head_radius_procent =  t.getSource() instanceof BranchNode ? 15 : 50;
		double tail_radius_procent =  t.getTarget() instanceof BranchNode ? 15 : 50;
		
		
		// translate the arrow, so that it is doesn't overlap with nodes
		 
		head = head.getTranslated(delta.getScaled(head_radius_procent / len));
		tail = tail.getTranslated(delta.getScaled(tail_radius_procent / len).getNegated());
		
		// draw no arrow if target is a branching node
		if (t.getTarget() instanceof BranchNode)
			output.append("\\transb");
		else
			output.append("\\trans");
		output.append("{");
		output.append(coordinateToString(head.x));
		output.append("}");
		output.append("{");
		output.append(coordinateToString(head.y));
		output.append("}");
		output.append("{");
		output.append(coordinateToString(tail.x));
		output.append("}");
		output.append("{");
		output.append(coordinateToString(tail.y));
		output.append("}");
		output.append(";\n");
		printAllAttributes(t);		
	}
	
	void print(Attribute a) {
		Point p = translate(a.getLocation());
		String label = a.getLabel();
		output.append("\\lbl");
		output.append("{");
		output.append(coordinateToString(p.x));
		output.append("}");
		output.append("{");
		output.append(coordinateToString(p.y));
		output.append("}");
		output.append("{");
		output.append(label);
		output.append("}");
		output.append(";\n");
	}
	
	public static void main(String[] args) throws IOException {
		// running this program will produce "test.tex" file in 
		// project root folder.
		TikzExporter x = new TikzExporter();
		Automaton automaton = new Automaton();
		StartingNode node1 = new StartingNode();
		node1.setLocation(new Point(100, 100));
		StateNode node2 = new StateNode();
		node2.setLocation(new Point(400, 100));
		EndingNode node3 = new EndingNode();
		node3.setLocation(new Point(200, 400));
		
		Transition t1 = new Transition(node1, node2);
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("x", "y"));
		t1.setAttributes(attributes);
		t1.initialAttributeLayout();
		
		Transition t2 = new Transition(node2, node3);
		
		automaton.addChild(node1);
		automaton.addChild(node2);
		automaton.addChild(node3);
		x.exportToTikz("test.tex", automaton);		
	}
}
