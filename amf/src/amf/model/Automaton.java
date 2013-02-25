package amf.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An automaton. This is the "root" of the model data
 * structure and container of all nodes and transitions.  
 */

public class Automaton extends ModelElement {

	/** Property ID to use when a child is added to this diagram. */
	public static final String CHILD_ADDED_PROP = "Automaton.ChildAdded";
	/** Property ID to use when a child is removed from this diagram. */
	public static final String CHILD_REMOVED_PROP = "Automaton.ChildRemoved";
	private static final long serialVersionUID = 1;
	private List nodes = new ArrayList();

	/**
	 * Add a shape to this diagram. 
	 * @param s  a non-null shape instance
	 * @return true, if the shape was added, false otherwise
	 */
	public boolean addChild(Node s) {
		if (s != null && nodes.add(s)) {
			firePropertyChange(CHILD_ADDED_PROP, null, s);
			return true;
		}
		return false;
	}	
	 // Return a List of Shapes in this diagram. The returned List should not be  modified.	 
	public List getChildren() {
		return nodes;
	}
	 // @return true, if the shape was removed, false otherwise	 
	public boolean removeChild(Node s) {
		if (s != null && nodes.remove(s)) {
			firePropertyChange(CHILD_REMOVED_PROP, null, s);
			return true;
		}
		return false;
	}	
	
	void AssignAttributeStyles()
	{
		//TODO: create new attribute styles for different attributes  if necessary
		//TODO: don't forget to call this method  when necessary
	}
	void Validate()
	{
		//TODO: add validation rules
		//TODO: call this method when necessary		
	}	
}