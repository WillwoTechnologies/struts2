package org.apache.struts2.portlet.dispatcher;

import org.apache.struts2.xwork2.ActionContext;
import org.apache.struts2.xwork2.util.logging.Logger;
import org.apache.struts2.xwork2.util.logging.LoggerFactory;
import org.apache.struts2.portlet.servlet.PortletServletResponse;
import org.apache.struts2.portlet.servlet.PortletServletResponseJSR286;

import javax.portlet.*;
import java.io.IOException;

import static org.apache.struts2.portlet.PortletConstants.EVENT_PHASE;
import static org.apache.struts2.portlet.PortletConstants.SERVE_RESOURCE_PHASE;

public class Jsr286Dispatcher extends Jsr168Dispatcher {

	private final static Logger LOG = LoggerFactory.getLogger(Jsr286Dispatcher.class);


	@Override
	public void processEvent( EventRequest request, EventResponse response)
			throws PortletException, IOException {
		if (LOG.isDebugEnabled()) LOG.debug("Entering processEvent");
		resetActionContext();
		try {
			// We'll use the event name as the "action"
			serviceAction(request, response,
					getRequestMap(request), getParameterMap(request),
					getSessionMap(request), getApplicationMap(),
					portletNamespace, EVENT_PHASE);
			if (LOG.isDebugEnabled()) LOG.debug("Leaving processEvent");
		} finally {
			ActionContext.setContext(null);
		}
	}

	@Override
	public void serveResource( ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		if (LOG.isDebugEnabled()) LOG.debug("Entering serveResource");
		resetActionContext();
		try {
			serviceAction(request, response,
					getRequestMap(request), getParameterMap(request),
					getSessionMap(request), getApplicationMap(),
					portletNamespace, SERVE_RESOURCE_PHASE);
		}
		finally {
			ActionContext.setContext(null);
		}
	}

    @Override
    protected String getDefaultActionPath( PortletRequest portletRequest ) {
        if (portletRequest instanceof EventRequest) {
            return ((EventRequest) portletRequest).getEvent().getName();
        }
        return super.getDefaultActionPath(portletRequest);
    }

    @Override
    protected PortletServletResponse createPortletServletResponse( PortletResponse response ) {
        return new PortletServletResponseJSR286(response);
    }
}
