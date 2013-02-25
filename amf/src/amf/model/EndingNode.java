package amf.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public class EndingNode extends StateNode {
	private static final Image ENDNODE_ICON = createImage("icons/end16.gif");
	@Override
	public String toString() {
		return "Ending Node " + hashCode();
	}
	@Override
	public Image getIcon() {
		return ENDNODE_ICON;
	}
}
