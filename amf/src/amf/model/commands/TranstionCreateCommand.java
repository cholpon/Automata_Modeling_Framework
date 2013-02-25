 package amf.model.commands;

import java.util.Iterator;

import org.eclipse.gef.commands.Command;

import amf.model.Automaton;
import amf.model.BranchNode;
import amf.model.Node;
import amf.model.Transition;

/**
 * A command to create a connection between two shapes. The command can be
 * undone or redone.
 * <p>
 * This command is designed to be used together with a GraphicalNodeEditPolicy.
 * To use this command properly, following steps are necessary:
 * </p>
 * <ol>
 * <li>Create a subclass of GraphicalNodeEditPolicy.</li>
 * <li>Override the <tt>getConnectionCreateCommand(...)</tt> method, to create a
 * new instance of this class and put it into the CreateConnectionRequest.</li>
 * <li>Override the <tt>getConnectionCompleteCommand(...)</tt> method, to obtain
 * the Command from the ConnectionRequest, call setTarget(...) to set the target
 * endpoint of the connection and return this command instance.</li>
 * </ol>
 * 
 * @see amf.gui.parts.ShapeEditPart#createEditPolicies()
 *      for an example of the above procedure.
 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy
 */
public class TranstionCreateCommand extends Command {
	private Transition transition;
	private final Node source;
	private Node target;	
	public TranstionCreateCommand(Node source) {
		
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel("connection creation");
		this.source = source;
		
	}
	public boolean canExecute() {
		// check for branching node, only one source transition allowed
		if ((target instanceof BranchNode) && (target.getTargetTransition().size() > 0)) {
			return false;
		}
		// connecting 2 branch nodes is not allowed 
		if ((target instanceof BranchNode) && (source instanceof BranchNode)) {
			return false;
		}
		// disallow source -> source connections
		if (source.equals(target)) {
			return false;
		}
		// return false, if the source -> target connection exists already
		for (Iterator iter = source.getSourceTransitions().iterator(); iter
				.hasNext();) {
			Transition conn = (Transition) iter.next();
			if (conn.getTarget().equals(target)) {
				return false;
			}
		}
		return true;
	}
	public void execute() {
		// create a new connection between source and target
			transition = new Transition(source, target);
			// use the supplied line style
			transition.setLineStyle(Transition.SOLID_CONNECTION);
		
		
 
	}
	public void redo() {
		transition.reconnect();
	}
	public void setTarget(Node target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
	}	
	
	public void undo() {
		transition.disconnect();
	}
}
