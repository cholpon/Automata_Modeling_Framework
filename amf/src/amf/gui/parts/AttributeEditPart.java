package amf.gui.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import amf.model.*;

public class AttributeEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {
 
	Label label;

	Attribute getCastedModel() {
		return (Attribute) getModel();
	}

	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			getCastedModel().addPropertyChangeListener(this);
		}
	}
	
	@Override
	protected IFigure createFigure() {

		label = new Label(getCastedModel().getLabel());
		FreeformLayout layout = new FreeformLayout();
		label.setLayoutManager(layout);
		
		Point p = getCastedModel().getLocation();
		//label.setLocation(p);
		label.setBounds(computeTextBounds(p)	);
		label.setOpaque(false); // non-transparent figure
	//	label.setBackgroundColor(ColorConstants.red);
		if (getCastedModel().getName().toString().equals("state_label")) {
			label.setForegroundColor(ColorConstants.red);
		} else {
			label.setForegroundColor(ColorConstants.blue);
		}
		return label;
	}

	private Rectangle computeTextBounds(Point p) {
		return new Rectangle(p.x, p.y, label.getText().length()*6, 15);
	}

	DiagramEditPart getDiagram()
	{
		return (DiagramEditPart)getParent();
	}
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		// TODO: check the bounds that should be updated. add the bounds of this element to the parent
		if (Attribute.LOCATION_PROP.equals(prop) ||
			Attribute.VALUE_PROP.equals(prop)) {
//			if (Attribute.VALUE_PROP.equals(prop))
//			
//				getDiagram().refreshChildren();
			refreshVisuals();
		}
	}

	@Override
	protected void createEditPolicies() 	{ 	}
	@Override
	protected void refreshVisuals() {
		
		label.setText(getCastedModel().getLabel());
		// update location
		Point p = getCastedModel().getLocation();
		Rectangle bounds = computeTextBounds(p);		
		 
		 
		EditPart par = getParent();
			if (par == null)
				return;
		IFigure fig = getFigure();
		AbstractGraphicalEditPart agp =(AbstractGraphicalEditPart) par; 
		agp.setLayoutConstraint(this, fig, bounds);
	}

}
