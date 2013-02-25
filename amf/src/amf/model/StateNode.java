package amf.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public class StateNode extends Node implements AttributeProvdier {
	StateType stateType;

	public StateType getStateType() {
		return stateType;
	}

	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}

	List<Attribute> Attributes;

	public List<Attribute> getAttributes() {
		if (Attributes == null)
			Attributes = new ArrayList<Attribute>();
		return Attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		Attributes = attributes;
	}

	/** A 16x16 pictogram of an elliptical shape. */
	private static final Image STATENODE_ICON = createImage("icons/normal16.gif");

	// this thing is used for serialization only
	private static final long serialVersionUID = 1;

	public Image getIcon() {
		return STATENODE_ICON;
	}

	public String toString() {
		return "State Node " + hashCode();
	}

	
	@Override
	protected void internalLocationChanged(Point oldloc, Point newloc) {
		List<Attribute> atr = getAttributes();
		layoutAtrs(oldloc, newloc, atr, 1.0);
		List srcTrans =  getSourceTransitions();
		List targTrans =  getTargetTransition();
		for (int i = 0; i<srcTrans.size(); i++)
		{
			Transition tr = (Transition) srcTrans.get(i);
			layoutAtrs(oldloc, newloc, tr.getAttributes(), 0.5); 			
			 
		}
 		for (int i = 0; i<targTrans.size(); i++)
		{
			Transition tr = (Transition) targTrans.get(i);
			layoutAtrs(oldloc, newloc, tr.getAttributes(), 0.5); 
		}
		
	}

	private void layoutAtrs(Point oldloc, Point newloc, List<Attribute> atr, double s) {
		for (int i = 0; i < atr.size(); i++) {
			Attribute a = atr.get(i);
			Point p = a.getLocation().translate(newloc.getDifference(oldloc).scale(s));
			a.setLocation(p);
		}
	}

	@Override
	public void CreateAttributes() {
		getAttributes().add(new Attribute("state_label", "text"));
		initialAttributeLayout();
	}

	private void initialAttributeLayout() {
		for (int i = 0; i < Attributes.size(); i++) {
			Attribute a = Attributes.get(i);
			Point globalLoc = a.getLocation();
			Point p = globalLoc.translate(getLocation().getDifference(
					new Point(10, i * 20)));
			a.setLocation(p);
		}

	}
}
