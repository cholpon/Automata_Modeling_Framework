package amf.model;

import java.io.Serializable;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
 

// Determines how attribute is drawn. Inherit other styles from this one for different styles.

public class AttributeStyle implements Serializable {
	Boolean visible;
	String caption;
	Color color;	
	public Boolean getVisible(Attribute a) {
		return true;
	}
	public String getCaption(Attribute a) {
		return a.getName()+ ":" + a.getValue();
	}
	public Color getColor(Attribute a) {
		return ColorConstants.green;
	}
}
