package org.guideme.guideme.ui.debug_shell;

import org.eclipse.swt.widgets.Composite;
import org.guideme.guideme.model.Page;

public interface DebugTab {
	public Composite getContent();
	public void onPageChange(Page dispPage, boolean currPage);
}
