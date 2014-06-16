package handlers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler2;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.ResourceUtil;

import edu.berkeley.eduride.base_plugin.util.Console;
import edu.berkeley.eduride.editoroverlay.BoxConstrainedEditorOverlay;

public class ExportEduRideXMLHandler implements IHandler2 {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Get the active window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		if (window == null)
			return null;
		// Get the active page
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return null;
		IEditorPart editor = page.getActiveEditor();
		
		IResource res = ResourceUtil.getResource(editor.getEditorInput());
		
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		
		MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
		dialog.setText("Export XML generated");
		dialog.setMessage("NOTE: Please save this file before generating XML code.\n\nCopy the XML code from the console into the ISA file. You must re-export if you edit this file.");
		dialog.open();
		
		
		Console.msg(generateEduRideXML(res));
		
		return null;
	}
	
	
	
	private String generateEduRideXML(IResource res) {
		String xml = "<eduridefile>\n";
		
		//String path = (res.getLocation()).toPortableString();  //too much information
		String path = (res.getFullPath()).toPortableString();
		
		//TODO: might need path to start with /src/ instead of the project name, hacky fix if we need it:
		//if (path.contains("/src/")) { path = path.substring(path.indexOf("/src/"), path.length()); }
		
		xml += "\t<source>" + path + "</source>\n";	
		
		String markerInfo = BoxConstrainedEditorOverlay.exportMarkers(res);
		xml += markerInfo;
		
		String base64 = stringToBase64(getContents((IFile)res));
		xml += "\t<base64>" + base64 + "</base64>\n";
		
		xml += "</eduridefile>";
		return xml;
	}

	/** Get contents of an Eclipse resource file as string. */
	public static String getContents(IFile file) {
		try {
			InputStream input = file.getContents();
			
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append('\n');
			}
			
			String contents = buffer.toString();
			input.close();
			return contents;
		} catch (Exception e) {
			System.out.println("Problem reading file contents.");
			return null;
		}
	}
	
	//input: some string of text
	//output: a string representing the base64 encoding
	public static String stringToBase64(String data) {
		return DatatypeConverter.printBase64Binary(data.getBytes());
	}
	
	
	//input: A string of base64 encoded data.  Do NOT include the <base64> tags!
	//output: A string represented the decoded base64
	//extra whitespace/newlines in the xml should be ignored
	public static String base64ToString(String base64) {
		
		base64 = base64.replaceAll("\\s+","");  //maybe?
		
		byte[] undo = DatatypeConverter.parseBase64Binary(base64);
        try {
            return new String(undo, "UTF-8");
        } catch (Exception e) {
            System.out.println("Error parsing base64");
            return null;
        }
	}
	
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEnabled(Object evaluationContext) {
		// TODO Auto-generated method stub

	}

}
