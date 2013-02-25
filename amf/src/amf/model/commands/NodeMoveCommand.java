package amf.model.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import amf.model.MovableElement;
import amf.model.Node;

/**
 * A command to move a node. The command can be undone or redone.
 */
public class NodeMoveCommand extends Command {	
	//private final Rectangle newBounds;
	private Point oldLocation;
	private final Point newLocation;
	private final ChangeBoundsRequest request;

	private final MovableElement shape;

	public NodeMoveCommand(MovableElement shape, ChangeBoundsRequest req,
			Rectangle newBounds) {
		if (shape == null || req == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.shape = shape;
		this.request = req;
		this.newLocation = newBounds.getLocation();
		//this.newBounds = newBounds.getCopy();
		setLabel("move / resize");
	}
	public boolean canExecute() {
		Object type = request.getType();
		// make sure the Request is of a type we support:
		return (RequestConstants.REQ_MOVE.equals(type)
				|| RequestConstants.REQ_MOVE_CHILDREN.equals(type)
				|| RequestConstants.REQ_RESIZE.equals(type) || RequestConstants.REQ_RESIZE_CHILDREN
				.equals(type));
	}

	public void execute() {
		oldLocation = shape.getLocation();
		redo();
	}
	public void redo() {
		shape.setLocation(newLocation);
	}
	public void undo() {
		//shape.setSize(oldBounds.getSize());
		shape.setLocation(oldLocation);
	}
}
