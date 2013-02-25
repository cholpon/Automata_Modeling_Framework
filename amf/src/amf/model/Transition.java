 
package amf.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;

/**
 * A connection between two nodes.
 */
public class Transition extends ModelElement implements AttributeProvdier {

	public static final Integer SOLID_CONNECTION = new Integer(Graphics.LINE_SOLID);
	
	//public static final Integer DASHED_CONNECTION = new Integer(Graphics.LINE_DASH);
	/** Property ID to use when the line style of this connection is modified. */
	public static final String LINESTYLE_PROP = "LineStyle";
	//public static final String ATTRIB_CREATED ="Attributes";
	private static final long serialVersionUID = 1;

	/** True, if the connection is attached to its endpoints. */
	private boolean isConnected;
	/** Line drawing style for this connection. */
	private int lineStyle = Graphics.LINE_SOLID;

	private Node source;
	private Node target;

	public Transition(Node source, Node target) { 
		CreateAttributes();
		reconnect(source, target);
		initialAttributeLayout();
	}

	/**
	 * Disconnect this connection from the shapes it is attached to.
	 */
	public void disconnect() {
		if (isConnected) {
			source.removeTransition(this);
			target.removeTransition(this);
			isConnected = false;
		}
	}

	public int getLineStyle() { return lineStyle; 	}
	public Node getSource() { return source; }	
	public Node getTarget() { return target; }
	/**
	 * Reconnect this connection. The connection will reconnect with the shapes
	 * it was previously attached to.
	 */
	public void reconnect() {
		if (!isConnected) {
			source.addConnection(this);
			target.addConnection(this);
			isConnected = true;
			 
			
		}
	}
	public void reconnect(Node newSource, Node newTarget) {
		if (newSource == null || newTarget == null || newSource == newTarget) {
			throw new IllegalArgumentException();
		}
		disconnect();
		this.source = newSource;
		this.target = newTarget;
		reconnect();
	}
	public void setLineStyle(int lineStyle) {
		if (lineStyle != Graphics.LINE_DASH && lineStyle != Graphics.LINE_SOLID) {
			throw new IllegalArgumentException();
		}
		this.lineStyle = lineStyle;
		firePropertyChange(LINESTYLE_PROP, null, new Integer(this.lineStyle));
	}

	List<Attribute> Attributes;
	public void setAttributes(List<Attribute> attributes) {
		Attributes = attributes;
	}
	
	@Override
	public List<Attribute> getAttributes() {
		if (Attributes == null)
			Attributes = new ArrayList<Attribute>();
		return Attributes;
	}

	public Point getMidLocation()
	{
		Point s = source.getLocation();
		Point t = target.getLocation();
		return new Point((s.x + t.x) / 2.0, (s.y + t.y) / 2.0);
	}
	public void initialAttributeLayout() {
		for (int i = 0; i < getAttributes().size(); i++) {
			Attribute a = getAttributes().get(i);
			Point oldLloc = a.getLocation();
			
			Point p = oldLloc.translate(getMidLocation().getDifference(
					new Point(10, i * 20)));
			a.setLocation(p);
		}

	}
	
	public void initialAttributeLayout(Point Location) {
		for (int i = 0; i < getAttributes().size(); i++) {
			Attribute a = getAttributes().get(i);
					
			Point p = Location.translate(getMidLocation().getDifference(
					new Point(10, i * 20)));
			a.setLocation(p);
		}

	}
	
	@Override
	public void CreateAttributes() {
		getAttributes().add(new Attribute("trans_label", "some text"));
		//firePropertyChange(ATTRIB_CREATED, null, getAttributes());
		
	}
}