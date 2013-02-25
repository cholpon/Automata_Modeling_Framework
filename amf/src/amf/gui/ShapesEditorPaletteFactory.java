 
package amf.gui;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;

import amf.model.BranchNode;
import amf.model.EndingNode;
import amf.model.StartingNode;
import amf.model.StateNode;
import amf.model.Transition;
final class ShapesEditorPaletteFactory {

	/** Preference ID used to persist the palette location. */
	private static final String PALETTE_DOCK_LOCATION = "ShapesEditorPaletteFactory.Location";
	/** Preference ID used to persist the palette size. */
	private static final String PALETTE_SIZE = "ShapesEditorPaletteFactory.Size";
	/** Preference ID used to persist the flyout palette's state. */
	private static final String PALETTE_STATE = "ShapesEditorPaletteFactory.State";

	/** Create the "Shapes" drawer. */
	private static PaletteContainer createShapesDrawer() {
		PaletteDrawer componentsDrawer = new PaletteDrawer("Shapes");

		CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
				"Starting Node", "Create a starting node", StartingNode.class,
				new SimpleFactory(StartingNode.class),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
						"icons/start16.gif"), ImageDescriptor.createFromFile(
						ShapesPlugin.class, "icons/start24.gif"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"State Node", "Create a state node", StateNode.class,
				new SimpleFactory(StateNode.class),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
						"icons/normal16.gif"), ImageDescriptor.createFromFile(
						ShapesPlugin.class, "icons/normal24.gif"));
		componentsDrawer.add(component);
		
		
		component = new CombinedTemplateCreationEntry(
				"Ending Node", "Create an ending node", EndingNode.class,
				new SimpleFactory(EndingNode.class),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
						"icons/end16.gif"), ImageDescriptor.createFromFile(
						ShapesPlugin.class, "icons/end24.gif"));
		componentsDrawer.add(component);
//		
//		component = new CombinedTemplateCreationEntry(
//				"Branching Node", "Create a branching node", BranchNode.class,
//				new SimpleFactory(BranchNode.class),
//				ImageDescriptor.createFromFile(ShapesPlugin.class,
//						"icons/branch16.gif"), ImageDescriptor.createFromFile(
//						ShapesPlugin.class, "icons/branch24.gif"));
//		componentsDrawer.add(component);
		
		return componentsDrawer;
	}

	/**
	 * Creates the PaletteRoot and adds all palette elements. Use this factory
	 * method to create a new palette for your graphical editor.
	 * 
	 * @return a new PaletteRoot
	 */
	static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createShapesDrawer());
		return palette;
	}

	/** Create the "Tools" group. */
	private static PaletteContainer createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);

		// Add a marquee tool to the group
		//toolbar.add(new MarqueeToolEntry());

		// Add (solid-line) connection tool
		tool = new ConnectionCreationToolEntry("Transitions",
				"Create a basic transition", new SimpleFactory(Transition.class),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
						"icons/transition16.gif"),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
						"icons/transition24.gif"));
		toolbar.add(tool);
		tool = new ConnectionCreationToolEntry("Branches",
				"Create a branching transition",  new SimpleFactory(BranchNode.class),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
						"icons/branch16.gif"),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
						"icons/branch24.gif"));
		toolbar.add(tool);

 

		return toolbar;
	}

	/** Utility class. */
	private ShapesEditorPaletteFactory() {
		// Utility class
	}

}