 
package amf.gui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
 
public class ShapesPlugin extends AbstractUIPlugin {

	/**
	 * Single plugin instance.
	 * 
	 * @uml.property name="singleton"
	 * @uml.associationEnd
	 */
	private static ShapesPlugin singleton;

	/**
	 * Returns the shared plugin instance.
	 */
	public static ShapesPlugin getDefault() {
		return singleton;
	}

	/**
	 * The constructor.
	 */
	public ShapesPlugin() {
		if (singleton == null) {
			singleton = this;
		}
	}

}