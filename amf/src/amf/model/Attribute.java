package amf.model;

import org.eclipse.draw2d.geometry.Point;

public class Attribute extends ModelElement implements MovableElement {
	private static final long serialVersionUID = 1L;
	public static final String LOCATION_PROP = "Attribute.Location";
	public static final String VALUE_PROP = "Attribute.Value";
	
	String value;
	String name;
	Point location;
	
	AttributeStyle style = new AttributeStyle();
	public Attribute(String name, String value)	
	{
		this.name = name;
		this.value = value;	
		this.location = new Point(10, 10);
	}	
	public String getValue() {
		if (value == null)
			value = "";
		return value;
	}
	public void setValue(String value) {
		String old = this.value;
		this.value = value;
		firePropertyChange(VALUE_PROP, old, value);
	}
	public String getName() {
		return name;
	}
	public AttributeStyle getStyle() {
		if (style == null)
			style = new AttributeStyle();
		return style;
	}
	public void setStyle(AttributeStyle style) {
		this.style = style;
	}
	public Point getLocation() {
		if (location == null)
		{
			location = new Point(100, 100);
		}
		return location.getCopy();
	}
	public void setLocation(Point position) {
		this.location = position;
		firePropertyChange(LOCATION_PROP, null, position);
	}
	public String getLabel() {
		return getStyle().getCaption(this);	
	}
	

}
