/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package amf.gui.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import amf.model.Attribute;
import amf.model.Automaton;
import amf.model.Node;
import amf.model.Transition;


// Factory that maps model elements to edit parts.
public class ShapesEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object modelElement) {
		// get EditPart for model element
		EditPart part = getPartForElement(modelElement);
		// store model element in EditPart
		part.setModel(modelElement);
		return part;
	}	
	private EditPart getPartForElement(Object modelElement) {
		if (modelElement instanceof Automaton) {
			return new DiagramEditPart();
		}
		if (modelElement instanceof Node) {
			return new ShapeEditPart();
		}
		if (modelElement instanceof Transition) {
			return new ConnectionEditPart();
		}
		if (modelElement instanceof Attribute) {
			return new AttributeEditPart();
		}
		throw new RuntimeException("Can't create part for model element: "
				+ ((modelElement != null) ? modelElement.getClass().getName()
						: "null"));
	}

}