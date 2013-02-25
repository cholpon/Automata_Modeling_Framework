 
package amf.gui.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.CoordinateListener;
import org.eclipse.draw2d.EventDispatcher;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;

import amf.model.BranchNode;
import amf.model.ModelElement;
import amf.model.Transition;
import amf.model.commands.TransitionDeleteCommand;

/**
 * Edit part for Connection model elements.
 * <p>
 * This edit part must implement the PropertyChangeListener interface, so it can
 * be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
class ConnectionEditPart extends AbstractConnectionEditPart implements
		PropertyChangeListener {

	
	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate(); 
			((ModelElement) getModel()).addPropertyChangeListener(this);
		}
	}
 
	protected void createEditPolicies() {
		// Selection handle edit policy.
		// Makes the connection show a feedback, when selected by the user.
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
		// Allows the removal of the connection model element
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new ConnectionEditPolicy() {
					protected Command getDeleteCommand(GroupRequest request) {
						return new TransitionDeleteCommand(getCastedModel());
					}
				});
	}

	protected IFigure createFigure() {
		PolylineConnection connection = (PolylineConnection) super
				.createFigure();
		// if arrow flag is set then enable arrow decoration
		if (getCastedModel().getTarget() instanceof BranchNode) {
			connection.setTargetDecoration(null);
		} else {
			connection.setTargetDecoration(new PolygonDecoration());	
		}
		connection.setLineStyle(getCastedModel().getLineStyle()); // line
																	// drawing
																	// style
		return connection;
	}

	/**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate(); 
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}

	private Transition getCastedModel() {
		return (Transition) getModel();
	}

	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();
		if (Transition.LINESTYLE_PROP.equals(property)) {
			((PolylineConnection) getFigure()).setLineStyle(getCastedModel()
					.getLineStyle());
		}
	 
	}

}