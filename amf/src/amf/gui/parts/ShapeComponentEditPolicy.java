
package amf.gui.parts;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import amf.model.Automaton;
import amf.model.Node;
import amf.model.commands.NodeDeleteCommand;

class ShapeComponentEditPolicy extends ComponentEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(
	 * org.eclipse.gef.requests.GroupRequest)
	 */
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Object parent = getHost().getParent().getModel();
		Object child = getHost().getModel();
		if (parent instanceof Automaton && child instanceof Node) {
			return new NodeDeleteCommand((Automaton) parent, (Node) child);
		}
		return super.createDeleteCommand(deleteRequest);
	}
}