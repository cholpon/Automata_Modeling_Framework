package amf.gui.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import amf.model.Automaton;
import amf.model.BranchNode;
import amf.model.EndingNode;
import amf.model.ModelElement;
import amf.model.Node;
import amf.model.StartingNode;
import amf.model.StateNode;
import amf.model.commands.BranchCreateCommand;
import amf.model.commands.NodeCreateCommand;
import amf.model.commands.TranstionCreateCommand;


class ShapeEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {

	class ComplexNodeFigure extends Figure {
		public ComplexNodeFigure() {
			super();
			setLayoutManager(new FreeformLayout());
		}
		protected void paintFigure(Graphics g) {
			Rectangle r = getClientArea();
			int [] p = new int[12];
		    p[0] = r.x; p[1] = r.y;
		    p[2] = r.x + 10; p[3] = r.y + 10;
		    p[4] = r.x + 10; p[5] = r.y + 5;
		    p[6] = r.x + 15; p[7] = r.y + 15;
		    p[8] = r.x + 5; p[9] = r.y + 10;
		    p[10] = r.x + 10; p[11] = r.y + 10;
		    g.drawPolygon(p);
		    Rectangle o = new Rectangle();
		    o.x = r.x + 10; o.y = r.y + 10;
		    o.height = 39; o.width = 39; 
		    g.drawOval(o);
		}
	}
	class BranchNodeFigure extends Ellipse {
		public BranchNodeFigure() {
			super();
		}
	}
	
	private ConnectionAnchor anchor;

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
	// finds a diagram edit part in the object graph 
	DiagramEditPart getDiagram()
	{
		return (DiagramEditPart)getParent();
	}

	protected void createEditPolicies() {
		// allow removal of the associated model element
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ShapeComponentEditPolicy());
		
 
		
		// allow the creation of connections and
		// and the reconnection of connections between Shape instances
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new GraphicalNodeEditPolicy() {
 
					protected Command getConnectionCompleteCommand(
							CreateConnectionRequest request) {
						
						if (request.getStartCommand() instanceof TranstionCreateCommand)
						{
							TranstionCreateCommand cmd = (TranstionCreateCommand) request.getStartCommand();													
							cmd.setTarget((Node) getHost().getModel());					
							return cmd;
						}
						else
						{
							BranchCreateCommand cmd = (BranchCreateCommand) request.getStartCommand();													
							cmd.setTarget((Node) getHost().getModel());					
							return cmd;
						}
					}
 
					protected Command getConnectionCreateCommand(
							CreateConnectionRequest request) {
						Node source = (Node) getHost().getModel();						 
						Boolean branching = request.getNewObjectType() == BranchNode.class;						
						Command cmd = branching ?
							new BranchCreateCommand(source, (Automaton)getHost().getParent().getModel()) :
							new TranstionCreateCommand(source);						 
						request.setStartCommand(cmd);
						return cmd;
					}
					protected Command getReconnectSourceCommand(
							ReconnectRequest request) {
						return null;
						// TODO: enabling reconnection requires correct attribute layout on reconnection
//						Transition conn = (Transition) request
//								.getConnectionEditPart().getModel();
//						Node newSource = (Node) getHost().getModel();
//						TranstionReconnectCommand cmd = new TranstionReconnectCommand(
//								conn);
//						cmd.setNewSource(newSource);
//						return cmd;
					}		 
					protected Command getReconnectTargetCommand(
							ReconnectRequest request) {
						return null;
						// TODO: enabling reconnection requires correct attribute layout on reconnection
//						Transition conn = (Transition) request
//								.getConnectionEditPart().getModel();
//						Node newTarget = (Node) getHost().getModel();
//						TranstionReconnectCommand cmd = new TranstionReconnectCommand(
//								conn);
//						cmd.setNewTarget(newTarget);
//						return cmd;
					}
				});
	}

	protected IFigure createFigure() {
		IFigure f = createFigureForModel();
		//FreeformLayout layout = new FreeformLayout();		
		//f.setLayoutManager(layout);
		f.setOpaque(true); // non-transparent figure
		if (f instanceof BranchNodeFigure) {
			f.setBackgroundColor(ColorConstants.black);
		} else { 
			f.setBackgroundColor(ColorConstants.white);
		}
		return f;
	}

	/**
	 * Return a IFigure depending on the instance of the current model element.
	 * This allows this EditPart to be used for both sublasses of Shape.
	 */
	private IFigure createFigureForModel() {
		Ellipse fig1, fig2;

		if (getModel() instanceof StartingNode) {
			return new ComplexNodeFigure();
		} else if (getModel() instanceof BranchNode) {
			Node n = (Node)getModel();
			n.setSize(new Dimension(20,20));
			return new BranchNodeFigure();
		} else if (getModel() instanceof EndingNode) {
			fig1 = new Ellipse();
			fig2 = new Ellipse();
			fig2.setSize(40, 40);
			fig2.setLocation(new Point(5, 5));
			fig1.add(fig2);
			return fig1;
		} else if (getModel() instanceof StateNode) {
			return new Ellipse();
		} else {
			// if Shapes gets extended the conditions above must be updated
			throw new IllegalArgumentException();
		}
	}

	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}

	private Node getCastedModel() {		
		return (Node) getModel();
	}

	@Override
	protected List getModelChildren() {
		//Node n = getCastedModel();
		return super.getModelChildren();
	}
	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			if ((getModel() instanceof StateNode) || (getModel() instanceof BranchNode))
				anchor = new EllipseAnchor(getFigure());
			else 
			// if Shapes gets extended the conditions above must be updated
				throw new IllegalArgumentException("unexpected model");
		}
		return anchor;
	}

	protected List getModelSourceConnections() {
		return getCastedModel().getSourceTransitions();
	}


	protected List getModelTargetConnections() {
		return getCastedModel().getTargetTransition();
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (Node.SIZE_PROP.equals(prop) || Node.LOCATION_PROP.equals(prop)) {
	 
			refreshVisuals();
			getDiagram().refreshChildren();
		} else if (Node.SOURCE_CONNECTIONS_PROP.equals(prop)) {			 
			refreshSourceConnections();
			getDiagram().refreshChildren();
		} else if (Node.TARGET_CONNECTIONS_PROP.equals(prop)) {		 
			refreshTargetConnections();
			getDiagram().refreshChildren();
		}
	}

	protected void refreshVisuals() {
 		// notify parent container of changed position & location
		// if this line is removed, the XYLayoutManager used by the parent
		// container
		// (the Figure of the ShapesDiagramEditPart), will not know the bounds
		// of this figure
		// and will not draw it correctly.
		Rectangle bounds = new Rectangle(getCastedModel().getLocation(),
				getCastedModel().getSize());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
	}
}
