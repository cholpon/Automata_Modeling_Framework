package amf.model;
 
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public class BranchNode extends Node {
	StateType stateType;

	public StateType getStateType() {
		return stateType;
	}

	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}
	
	private static final Image BRANCHNODE_ICON = createImage("icons/branch16.gif");
	private static final long serialVersionUID = 1L;
	
	public String toString() {
		return "Branching Node " + hashCode();
	}

	@Override
	protected void internalLocationChanged(Point oldloc, Point newloc) {
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
	public Image getIcon() {
		return BRANCHNODE_ICON;
	}

}
