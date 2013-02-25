
package amf.gui.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import amf.model.Automaton;
import amf.model.Node;


public class ShapesTreeEditPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Node) {
			return new ShapeTreeEditPart((Node) model);
		}
		if (model instanceof Automaton) {
			return new DiagramTreeEditPart((Automaton) model);
		}
		return null; // will not show an entry for the corresponding model
						// instance
	}

}
