package org.okaysoft.core.spring;

import org.hibernate.HibernateException;
import org.hibernate.ejb.event.EJB3FlushEventListener;
import org.hibernate.event.EventSource;
import org.okaysoft.core.log.OkayLogger;

public class PatchedFlushListener extends EJB3FlushEventListener {

	private static final long serialVersionUID = -6913148400994182443L;
	protected static final OkayLogger log = new OkayLogger(PatchedFlushListener.class);
	
	
	@Override
    protected void performExecutions(EventSource session) 
        throws HibernateException {

        session.getPersistenceContext().setFlushing(true);
        try {
            session.getJDBCContext()
                   .getConnectionManager().flushBeginning();
            // we need to lock the collection caches before
            // executing entity inserts/updates in order to
            // account for bidi associations
            session.getActionQueue().prepareActions();
            session.getActionQueue().executeActions();
        }
        catch (HibernateException he) {
            log.info("Could not synchronize database state with session", he);
            throw he;
        }
        finally {
            session.getPersistenceContext().setFlushing(false);
            session.getJDBCContext()
                   .getConnectionManager().flushEnding();
        }
    }
	
}
