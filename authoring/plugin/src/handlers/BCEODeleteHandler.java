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

public class BCEODeleteHandler implements IHandler2 {

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
			
			if (!evkl.isTurnedOn()) {  //don't delete if we can't see what we're doing
				evkl.toggle();
			} else {
				evkl.deleteMarker();
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
