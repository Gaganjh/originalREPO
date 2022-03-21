package com.manulife.pension.ps.service.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.util.log.LogUtility;

/**
 * This is an abstract class to deal with typical functions such as
 * looking up the names and getting references to the remote services
 *
 * Specialized classes need to overwrite method to create the specialized EJB Object
 * and do proper casting when making specific method calls to the service
 */
public abstract class AbstractServiceDelegate {

	/** the EJBHome reference */
    protected EJBHome home;
    /** reference to the initial context */
    protected Context context = null;

    protected AbstractServiceDelegate() {
    }

    /**
     * Gets new context
     * @return new Context
     * @throws NamingException
     */
    protected Context getInitialContext() throws NamingException {
        return new InitialContext();
    }

    /**
     * Returns the home class name for this service
     *
     * To be overwritten by specific delegates
     */
    protected abstract String getHomeClassName();

    /**
     * Returns the reference to the remote service
     *
     * To be overwritten by specific delegates
     * a specific delegate typically calls the home.create() to get the remote
	 *
     * @return new EJB Object
     * @throws SystemException
     * @throws RemoteException
     * @throws CreateException
     */
    protected abstract javax.ejb.EJBObject create()
        throws
            SystemException,
            java.rmi.RemoteException,
            javax.ejb.CreateException;

    protected static final int RETRY_COUNT_MAX = 2;

    /**
     * Returns a reference to the remote service
     *
     * This is the main method to be called within each business method
     * each specific delegate can cast the EJBObject to its specialized
     * remote interface
     *
     * @return EJB Object
     * @throws SystemException
     */
    protected javax.ejb.EJBObject getService() throws SystemException {
        javax.ejb.EJBObject remote = null;
        int retryCount = 0;
        Exception lastException = null;

        // try creating the remote
        // if the home reference is stale, refresh it and try again
        try {
            while (retryCount < RETRY_COUNT_MAX) {
                retryCount++;
                try {
                    if (home == null) {
                        refreshHome();
                    }
                    remote = create();
                    break;
                } catch (RemoteException e) {
                    home = null;
                    lastException = e;
                }
            }
            // retried so many times but still failed to create the remote
            if (remote == null) {
                String msg =
                    "After "
                        + RETRY_COUNT_MAX
                        + " many tries still faild to create the remote";
                //if (lastException != null) {
                //    msg += ": " + lastException.getMessage();
                //}
                if ( lastException == null )
	   				lastException = new Exception(msg);

				throw new SystemException(lastException, this.getClass()
						.getName(), "getService", msg);
            }
        } catch (CreateException e) {
			throw new SystemException(e, this.getClass().getName(),
					"getService", "CreateException occurred.");
        }
        return remote;
    }

    /**
     * Looks up the reference to the home class again
     *
     * It is used internally by the getService() method
     *
     * @throws SystemException
     */
    protected void refreshHome() throws SystemException {
        try {
        	if (context == null ) {
	            context = getInitialContext();
        	}

            Object objref = context.lookup(getHomeClassName());
            if (objref != null) {
                home =
                    (EJBHome) PortableRemoteObject.narrow(
                        objref,
                        Class.forName(getHomeClassName()));
            } else {
                throw new NamingException(
                    "Home not found " + getHomeClassName());
            }
		} catch (NamingException e) {
			throw new SystemException(e, this.getClass().getName(),
					"refreshHome", "Lookup failed for " + getHomeClassName()
							+ ".");
		} catch (ClassNotFoundException e) {
			throw new SystemException(e, this.getClass().getName(),
					"refreshHome", "Home class " + getHomeClassName()
							+ " not found.");
		}
    }

    /**
     * Accessor method for the home member. If one does not exist, it looks up one
     *
     * @return new or cached EJBHome
     * @throws SystemException
     */
    protected EJBHome getHome() throws SystemException {
        if (home == null)
            refreshHome();

        return home;
    }

	protected void handleRemoteException(RemoteException re, String methodName) throws SystemException {
		Throwable tr = re.detail;
		SystemException se = null;
		String uniqueId = null;

		while (tr instanceof RemoteException) {
			tr = ((RemoteException)tr).detail;
		}

		if (tr instanceof EJBException) {
			uniqueId = ExceptionHandlerUtility.parseUniqueId(tr.getMessage());
		}

		if (uniqueId == null) {
			se = new SystemException(re.detail, this.getClass().getName(), methodName,
					"This Exception thrown by the container.");
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
		} else {
			se = new SystemException(uniqueId);
			se.setLogged(true);
		}

		throw se;
	}
}
