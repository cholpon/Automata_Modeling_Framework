package amf.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public class StartingNode extends StateNode {
	private static final Image STARTNODE_ICON = createImage("icons/start16.gif");
	@Override
	public String toString() {
		return "Starting Node " + hashCode();
	}
	@Override
	public Image getIcon() {
		return STARTNODE_ICON;
	}
}
