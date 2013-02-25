package amf.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public abstract class ModelElement implements Serializable, IPropertySource  {
	/** An empty property descriptor. */
	private static final IPropertyDescriptor[] EMPTY_ARRAY = new IPropertyDescriptor[0];
 
	/** Delegate used to implemenent property-change-support. */
	private transient PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);
	
	private static final long serialVersionUID = 1;
	private PropertyChangeSupport	getpcsDelegate()
	{
		if (pcsDelegate == null)
			pcsDelegate = new PropertyChangeSupport(this);
		return pcsDelegate;
	}
 
	public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException();
		}
	 
		getpcsDelegate().addPropertyChangeListener(l);
	}
	protected void firePropertyChange(String property, Object oldValue, Object newValue) {
		if (getpcsDelegate().hasListeners(property)) {
			getpcsDelegate().firePropertyChange(property, oldValue, newValue);
		}
	}
	public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
 		if (l != null) {
			getpcsDelegate().removePropertyChangeListener(l);
		}
	}
 
	public Object getEditableValue() {
		return this;
	}

 
	private transient IPropertyDescriptor[] descriptors;
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (this instanceof AttributeProvdier)
		{
			if (descriptors == null) {
				AttributeProvdier ap = (AttributeProvdier) this;
				ArrayList<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>(ap.getAttributes().size());
				for (int i=0; i<ap.getAttributes().size(); i++)
				{
					list.add(new TextPropertyDescriptor(
							ap.getAttributes().get(i), 
							ap.getAttributes().get(i).getName()));	
				}			 	
				descriptors =  list.toArray(new IPropertyDescriptor[]{}); 
			}
			 
			return descriptors;
		}
		else
		return EMPTY_ARRAY;
	}

	/**
	 * Children should override this. The default implementation returns null.
	 */
	public Object getPropertyValue(Object id) {
		if (this instanceof AttributeProvdier)
		{
			AttributeProvdier ap = (AttributeProvdier) this;
			int i = ap.getAttributes().indexOf(id);
			return ap.getAttributes().get(i).getValue();
		}
		else
		return null;
	}

	/**
	 * Children should override this. The default implementation returns false.
	 */
	public boolean isPropertySet(Object id) {
		return true;
	}
 
	/** AMF. storage for the internal ID of the element, empty until setID method is called. Used for the XML serialization */
	long ID;
	
	public long getID(){
		return this.ID;
	}
  
	public void setID(long id){
		this.ID = id;
	}

	/** 
	 * Deserialization constructor. Initializes transient fields.
	 * @see java.io.Serializable
	 */
	private void readObject(ObjectInputStream in) 
	throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		pcsDelegate = new PropertyChangeSupport(this);
	}

 

	/**
	 * Children should override this. The default implementation does nothing.
	 */
	public void resetPropertyValue(Object id) {
		// do nothing
	}

	/**
	 * Children should override this. The default implementation does nothing.
	 */
	public void setPropertyValue(Object id, Object value) {
		if (this instanceof AttributeProvdier)
		{
			AttributeProvdier ap = (AttributeProvdier) this;
			int i = ap.getAttributes().indexOf(id);
			ap.getAttributes().get(i).setValue((String)value);
		}		 
	}
}
