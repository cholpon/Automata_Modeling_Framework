package amf.tikz;

import java.io.IOException;
import org.eclipse.core.resources.IFile;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.internal.wizards.datatransfer.FileSystemExporter;

import amf.io.XMLreader;
import amf.model.Automaton;


@SuppressWarnings("restriction")
public class TikzFileSystemExporter extends FileSystemExporter {
	@Override
	protected void writeFile(IFile file, IPath destinationPath) throws IOException 
	{		
		XMLreader xr = new XMLreader();
        Automaton diagram = xr.ReadFromXML(file.getLocation().toFile().getAbsolutePath());
        TikzExporter exp = new TikzExporter();
        exp.exportToTikz(destinationPath.toOSString(), diagram);
    }
}
