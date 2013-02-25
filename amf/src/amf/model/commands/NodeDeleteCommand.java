
package amf.model.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import amf.model.Automaton;
import amf.model.Node;
import amf.model.Transition;

/**
 * A command to remove a shape from its parent. The command can be undone or
 * redone.

 */
public class NodeDeleteCommand extends Command {
	/**
	 * Shape to remove.
	 * 
	 * @uml.property name="child"
	 * @uml.associationEnd
	 */
	private final Node child;

	/**
	 * ShapeDiagram to remove from.
	 * 
	 * @uml.property name="parent"
	 * @uml.associationEnd
	 */
	private final Automaton parent;
	/** Holds a copy of the outgoing connections of child. */
	private List sourceConnections;
	/** Holds a copy of the incoming connections of child. */
	private List targetConnections;
	/** True, if child was removed from its parent. */
	private boolean wasRemoved;

	/**
	 * Create a command that will remove the shape from its parent.
	 * 
	 * @param parent
	 *            the ShapesDiagram containing the child
	 * @param child
	 *            the Shape to remove
	 * @throws IllegalArgumentException
	 *             if any parameter is null
	 */
	public NodeDeleteCommand(Automaton parent, Node child) {
		if (parent == null || child == null) {
			throw new IllegalArgumentException();
		}
		setLabel("shape deletion");
		this.parent = parent;
		this.child = child;
	}

	/**
	 * Reconnects a List of Connections with their previous endpoints.
	 * 
	 * @param connections
	 *            a non-null List of connections
	 */
	private void addConnections(List connections) {
		for (Iterator iter = connections.iterator(); iter.hasNext();) {
			Transition conn = (Transition) iter.next();
			conn.reconnect();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return wasRemoved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		// store a copy of incoming & outgoing connections before proceeding
		sourceConnections = child.getSourceTransitions();
		targetConnections = child.getTargetTransition();
		redo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		// remove the child and disconnect its connections
		wasRemoved = parent.removeChild(child);
		if (wasRemoved) {
			removeConnections(sourceConnections);
			removeConnections(targetConnections);
		}
	}

	/**
	 * Disconnects a List of Connections from their endpoints.
	 * 
	 * @param connections
	 *            a non-null List of connections
	 */
	private void removeConnections(List connections) {
		for (Iterator iter = connections.iterator(); iter.hasNext();) {
			Transition conn = (Transition) iter.next();
			conn.disconnect();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		// add the child and reconnect its connections
		if (parent.addChild(child)) {
			addConnections(sourceConnections);
			addConnections(targetConnections);
		}
	}
}