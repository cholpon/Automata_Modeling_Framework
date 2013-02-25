package amf.model.commands;

import java.io.Console;
import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.console.DebugGEF;
import org.eclipse.osgi.framework.internal.core.ConsoleMsg;

import amf.model.Attribute;
import amf.model.AttributeProvdier;
import amf.model.Automaton;
import amf.model.Node;

/**
 * A command to add a Node to a Diagram. The command can be undone or
 * redone.
 */
public class NodeCreateCommand extends Command {

	private Node newShape;
	private final Automaton parent;
	private Rectangle bounds;

	/**
	 * Create a command that will add a new Shape to a ShapesDiagram.
	 * 
	 * @param newShape
	 *            the new Shape that is to be added
	 * @param parent
	 *            the ShapesDiagram that will hold the new element
	 * @param bounds
	 *            the bounds of the new shape; the size can be (-1, -1) if not
	 *            known
	 * @throws IllegalArgumentException
	 *             if any parameter is null, or the request does not provide a
	 *             new Shape instance
	 */
	public NodeCreateCommand(Node newShape, Automaton parent,
			Rectangle bounds) {
		this.newShape = newShape;
		this.parent = parent;
		this.bounds = bounds;
		setLabel("shape creation");
	}

	/**
	 * Can execute if all the necessary information has been provided.
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return newShape != null && parent != null && bounds != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		
		newShape.setLocation(bounds.getLocation());
		if (newShape instanceof AttributeProvdier)
		{
			AttributeProvdier p = (AttributeProvdier) newShape;
			p.CreateAttributes();			
		}
		Dimension size = bounds.getSize();
		if (size.width > 0 && size.height > 0)
			newShape.setSize(size);
		redo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		parent.addChild(newShape);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		parent.removeChild(newShape);
	}

}