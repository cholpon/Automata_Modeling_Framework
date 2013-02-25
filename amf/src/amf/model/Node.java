package amf.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image; 
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import amf.gui.ShapesPlugin;

 
public abstract class Node  extends  ModelElement implements MovableElement  {

	/**
	 * ID for the Height property value (used for by the corresponding property
	 * descriptor).
	 */
	private static final String HEIGHT_PROP = "Shape.Height";
	/** Property ID to use when the location of this shape is modified. */
	public static final String LOCATION_PROP = "Shape.Location";
	private static final long serialVersionUID = 1;
	/** Property ID to use then the size of this shape is modified. */
	public static final String SIZE_PROP = "Shape.Size";
	/** Property ID to use when the list of outgoing connections is modified. */
	public static final String SOURCE_CONNECTIONS_PROP = "Shape.SourceConn";
	/** Property ID to use when the list of incoming connections is modified. */
	public static final String TARGET_CONNECTIONS_PROP = "Shape.TargetConn";
	protected static Image createImage(String name) {
		InputStream stream = ShapesPlugin.class.getResourceAsStream(name);
		Image image = new Image(null, stream); 
		try {
			stream.close();
		} catch (IOException ioe) {
		}
		return image;
	}

	/** Location of this shape. */
	private Point location = new Point(0, 0);
	/** Size of this shape. */
	private Dimension size = new Dimension(50, 50);
	/** List of outgoing Transitions. */
	private List sourceTransitions = new ArrayList();
	/** List of incoming Transitions. */
	private List targetTransitions = new ArrayList();

	/**
	 * Add an incoming or outgoing connection to this shape.
	 */
	public void addConnection(Transition conn) {
		if (conn == null || conn.getSource() == conn.getTarget()) {
			throw new IllegalArgumentException();
		}
		if (conn.getSource() == this) {
			sourceTransitions.add(conn);
			firePropertyChange(SOURCE_CONNECTIONS_PROP, null, conn);
		} else if (conn.getTarget() == this) {
			targetTransitions.add(conn);
			firePropertyChange(TARGET_CONNECTIONS_PROP, null, conn);
		}
	}

	/**
	 * Return a pictogram (small icon) describing this model element. Children
	 * should override this method and return an appropriate Image.
	 * @return a 16x16 Image or null
	 */
	public abstract Image getIcon();
 
	public Point getLocation() {
		return location.getCopy();
	}

	public Dimension getSize() {
		return size.getCopy();
	}

	/**
	 * Return a List of outgoing Transitions.
	 */
	public List getSourceTransitions() {
		return new ArrayList(sourceTransitions);
	}

	/**
	 * Return a List of incoming Connections.	 */
	public List getTargetTransition() {
		return new ArrayList(targetTransitions);
	}
 
	void removeTransition(Transition conn) {
		if (conn == null) {
			throw new IllegalArgumentException();
		}
		if (conn.getSource() == this) {
			sourceTransitions.remove(conn);
			firePropertyChange(SOURCE_CONNECTIONS_PROP, null, conn);
		} else if (conn.getTarget() == this) {
			targetTransitions.remove(conn);
			firePropertyChange(TARGET_CONNECTIONS_PROP, null, conn);
		}
	}

	public void setLocation(Point newLocation) {
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}
		Point oldLocation = location.getCopy();		 
		internalLocationChanged(oldLocation, newLocation);
		location.setLocation(newLocation);
		firePropertyChange(LOCATION_PROP, null, location);
	}

	protected void internalLocationChanged(Point oldLocation, Point newLocation) { 	}

	public void setSize(Dimension newSize) {
		if (newSize != null) {
			size.setSize(newSize);
			firePropertyChange(SIZE_PROP, null, size);
		}
	}
}