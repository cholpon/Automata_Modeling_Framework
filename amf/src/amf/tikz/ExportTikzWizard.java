package amf.tikz;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.internal.adaptor.EclipseAdaptorHook;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceExportPage1;

@SuppressWarnings("restriction")
public class ExportTikzWizard extends Wizard implements IExportWizard {

	class ExportTikzWizardPage extends WizardFileSystemResourceExportPage1 
	{
			private static final String DEFAULT_EXTENSION = ".amf";
		  protected ExportTikzWizardPage(IStructuredSelection selection) {
		        super("export to tikz", null);		        
		    }
		  @Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			super.createControl(parent);			
			
		}

		  @Override
		protected Button createButton(Composite parent, int id, String label,
				boolean defaultButton) {
			Button b =  super.createButton(parent, id, label, defaultButton);
			if (id == IDialogConstants.SELECT_TYPES_ID)
				b.setVisible(false);
			return b;
		}
 
		 //static string DEFAULT_EXTENSION = ".amf";
		  @Override
		protected boolean validateSourceGroup() {
		// TODO Auto-generated method stub
			  List resourcesToExport = getWhiteCheckedResources();
			  for (Object o: resourcesToExport)
			  {
				  if (o instanceof org.eclipse.core.internal.resources.File)
				  {
					  String path = ((org.eclipse.core.internal.resources.File) o).getName();
					  int i = path.lastIndexOf('.');
					  if (!path.substring(i).equalsIgnoreCase(DEFAULT_EXTENSION))
						  return false;					 
				  }
				  else 
					  return false;
			  }
			  return super.validateSourceGroup();		
		}
 	    public boolean finish() {
	        java.util.List resourcesToExport = getWhiteCheckedResources();
	        if (!ensureTargetIsValid(new File(getDestinationValue()))) {
				return false;
			}


	        //Save dirty editors if possible but do not stop if not all are saved
	        saveDirtyEditors();
	        // about to invoke the operation so save our state
	        saveWidgetValues();

	        return executeExportOperation(new TikzExportOperation(null,
	                resourcesToExport, getDestinationValue(), this));
	    }
		
	}
		  
	 
	
	public ExportTikzWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		setWindowTitle("Export to TIKZ Wizard"); //NON-NL
		page = new ExportTikzWizardPage(selection);
	}
	ExportTikzWizardPage page;
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
 		addPage(page);
		super.addPages();
	}
	@Override
	public boolean performFinish() {
	//	page.
		// TODO Auto-generated method stub
		//return page.finish();
		return page.finish();
		
	}

}
