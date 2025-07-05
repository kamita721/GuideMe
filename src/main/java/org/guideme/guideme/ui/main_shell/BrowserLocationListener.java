package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

public class BrowserLocationListener implements LocationListener{
	private static final Logger LOGGER = LogManager.getLogger();

    /*
     * Prevent a page from hijacking the browser frame.
     * 
     * One place this comes up is with naivly written <form> elements. By default, pressing enter on them causes the browser to "submit"
     * the form, by navigating to the relevent URL, which is almost certainly not was intended.
     * 
     * A more neferious guide writer could probably find some way to navigate the browswer someplace to do something nefaroius.
     */
    @Override
    public void changing(LocationEvent event) {
        switch (event.location){
            case "about:blank":
            break;
            default:
                event.doit = false;
            break;
        }
        LOGGER.debug("changing: " + event);
    }

    @Override
    public void changed(LocationEvent event) {
        LOGGER.debug("changed: " + event);
    }
    
}
