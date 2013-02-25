
package amf.model.commands;

import org.eclipse.gef.commands.Command;

import amf.model.Transition;

/**
 * A command to disconnect (remove) a connection from its endpoints. The command
 * can be undone or redone.
 */
public class TransitionDeleteCommand extends Command {

	private final Transition connection;

	public TransitionDeleteCommand(Transition conn) {
		if (conn == null) {
			throw new IllegalArgumentException();
		}
		setLabel("connection deletion");
		this.connection = conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		connection.disconnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		connection.reconnect();
	}
}
