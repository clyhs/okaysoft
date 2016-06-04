package org.okaysoft.core.spring;

import java.io.File;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.context.ContextLoader;



public class OkayContextLoaderListener extends ContextLoader implements
		ServletContextListener {
	
	private ContextLoader contextLoader;

	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		SystemListener.contextInitialized(sce);
		this.contextLoader = createContextLoader();
		if (this.contextLoader == null) {
			this.contextLoader =this;
		}
		this.contextLoader.initWebApplicationContext(sce.getServletContext());

	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		SystemListener.contextDestroyed(sce);
		if (this.contextLoader != null) {
			this.contextLoader.closeWebApplicationContext(sce.getServletContext());
		}
		ContextCleanupListener.cleanupAttributes(sce.getServletContext());

	}

	@Deprecated
	protected ContextLoader createContextLoader() {
		return null;
	}

	/**
	 * Return the ContextLoader used by this listener.
	 * @return the current ContextLoader
	 * @deprecated in favor of simply subclassing APDPlatContextLoaderListener itself
	 * (which extends ContextLoader, as of Spring 3.0)
	 */
	@Deprecated
	public ContextLoader getContextLoader() {
		return this.contextLoader;
	}

	
	
   
}

class ContextCleanupListener implements ServletContextListener {

	private static final Log logger = LogFactory.getLog(ContextCleanupListener.class);


	public void contextInitialized(ServletContextEvent event) {
	}

	public void contextDestroyed(ServletContextEvent event) {
		cleanupAttributes(event.getServletContext());
	}


	
	static void cleanupAttributes(ServletContext sc) {
		Enumeration attrNames = sc.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			String attrName = (String) attrNames.nextElement();
			if (attrName.startsWith("org.springframework.")) {
				Object attrValue = sc.getAttribute(attrName);
				if (attrValue instanceof DisposableBean) {
					try {
						((DisposableBean) attrValue).destroy();
					}
					catch (Throwable ex) {
						logger.error("Couldn't invoke destroy method of attribute with name '" + attrName + "'", ex);
					}
				}
			}
		}
	}

}
