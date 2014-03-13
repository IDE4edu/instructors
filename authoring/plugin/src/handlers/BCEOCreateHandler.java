package handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler2;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.berkeley.eduride.editoroverlay.BoxConstrainedEditorOverlay;

public class BCEOCreateHandler implements IHandler2 {

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
		if (BoxConstrainedEditorOverlay.shouldInstall(editor)) {
			BoxConstrainedEditorOverlay evkl = BoxConstrainedEditorOverlay.ensureInstalled(editor);
			//evkl won't be null because we already check shouldInstall
			
			//TODO: Make sure evkl won't be null on non-ISA files!
			
			if (!evkl.isTurnedOn()) {
				evkl.toggle();
			}
			
			evkl.createMarkers();
			
			if (!evkl.isTurnedOn()) {  //If we have no boxes and toggle, it won't turn on.  This check is for first boxes
				evkl.toggle();
			}
		}
		return null;
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
