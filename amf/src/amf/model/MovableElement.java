package amf.model;


import org.eclipse.draw2d.geometry.Point;

public interface MovableElement {
	void setLocation(Point newLocation);
	Point getLocation();
}
