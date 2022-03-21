package com.manulife.pension.platform.web.delegate;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.ais.RMISSLClientSocketFactory;

/**
 * This is an abstract class to deal with typical functions used by eReports
 * such as looking up the names and getting references to the remote objects
 * 
 * @author drotele
 *  
 */

public abstract class AbstractRMIServiceDelegate {

	/** the ReportService reference */
	private static final Logger logger = Logger.getLogger(AbstractRMIServiceDelegate.class);
	protected Remote remoteService;
	protected String rmiServer = null;
	protected String rmiFailoverServer = null;

	protected AbstractRMIServiceDelegate() {
	}

	protected static final int RETRY_COUNT_MAX = 2;
	private static final String RMI_TYPE = "RMIType";
	private static final String SSL_TRUST_STORE = "SSLTrustStore";
	private static final String SSL_TRUST_STORE_PWD = "SSLTrustStorePassword";

	
	protected abstract String getServiceName();
	protected abstract int getPortNumber();

	/**
	 * Returns a reference to the remote report service
	 * 
	 * @return ReportService
	 * @throws SystemException
	 */
	protected Remote getRMIService() throws SystemException, RemoteException {
		return getRMIService(true);
	}

	protected Remote getRMIService(boolean usePrimaryServer) throws SystemException, RemoteException {
	
		int retryCount = 0;
		Throwable lastException = null;
		Remote reportService = null;
		String  RMIType =getRMIType();
		int  securedPort =getPortNumber();
		// try creating the remote
		while (retryCount < RETRY_COUNT_MAX) {
			retryCount++;
			try {
				String connectionString;
				String hostName;
				String sslTrustStore;
				String sslTrustStorePwd;
				if (usePrimaryServer) {
					connectionString = "rmi://" + this.getRmiServer() + "/"
						+ getServiceName();
					hostName = this.getRmiServer();
				} else {
					connectionString = "rmi://" + this.getRmiFailoverServer() + "/"
					+ getServiceName();
					hostName = this.getRmiFailoverServer();
				}
				if(Objects.nonNull(RMIType) && "secured".equalsIgnoreCase(RMIType))  {
					
					sslTrustStore = getSSLTrustStore();
					sslTrustStorePwd = getSSLTrustStorePassword();
					
					if(Objects.nonNull(sslTrustStore) && Objects.nonNull(sslTrustStorePwd))  {
						System.setProperty("MF.ssltrustStore", sslTrustStore);
						System.setProperty("MF.ssltrustStorePassword", sslTrustStorePwd);
					}
					Registry registry = null;
					if(StringUtils.equals("ReportService", getServiceName())){
						registry = LocateRegistry.getRegistry(
								hostName, securedPort,
								new RMISSLClientSocketFactory());
					}
					if(StringUtils.equals("GFTUploadService", getServiceName())){
						registry = LocateRegistry.getRegistry(
								hostName, securedPort,
								new RMISSLClientSocketFactory());
					}
					if(StringUtils.equals("STPDataCheckerService", getServiceName())) {
						registry = LocateRegistry.getRegistry(hostName,securedPort, 
								new RMISSLClientSocketFactory());
					}
					if(StringUtils.equals("SubmissionJournalService", getServiceName())) {
						registry = LocateRegistry.getRegistry(hostName, securedPort, 
								new RMISSLClientSocketFactory());
					}
					if(Objects.nonNull(registry))
						reportService = registry.lookup(getServiceName());
					if (logger.isDebugEnabled()) {
		                logger.debug("look up [" + getServiceName() + "] via secured connection successfully");
		            }
				}else {
					reportService = Naming.lookup(connectionString);
					
				}
				break;
			} catch (Throwable t) {
				reportService = null;
				lastException = t;
			}
		}
		// retried so many times but still failed to create the remote object
		if (reportService == null) {

			/*
			 * If the last exception we have is a remote exception, throw the
			 * remote exception to outside.
			 */
			if (lastException instanceof RemoteException) {
				throw (RemoteException) lastException;
			}
			if (lastException instanceof NotBoundException) {
				throw new RemoteException("Service not bound to RMIRegistry", lastException);
			}

			String msg = "After " + RETRY_COUNT_MAX
					+ " tries, it still fails to create the remote service";
			if (lastException != null) {
				msg += ": " + lastException.getMessage();
			} else
				lastException = new Exception(msg);

			throw new SystemException(lastException, this.getClass().getName(),
					"getRMIService", msg);
		}

		return reportService;
	}

	/**
	 * Accessor method for the Report Service. If it does not exist, it creates
	 * it
	 * 
	 * @return new or cached ReportService
	 * @throws ServiceDelegateException
	 */
	public Remote getRemoteService() throws SystemException, RemoteException {
		/* caching feature was disabled so that 
		 * failed RMI handles would not linger in the memory
		 * also it would make writing remote client methods easier (you wouldn't need try-catch-retry)
		 */
		// if (this.remoteService == null) {
			// this.remoteService = getRMIService();
		// }
		// return this.remoteService;
		return getRMIService();
	}
	/**
	 * Accessor method for the Report Service using failover server. If it does not exist, 
	 * it creates it
	 * 
	 * @return new or cached ReportService
	 * @throws ServiceDelegateException
	 */
	public Remote getRemoteServiceFailover() throws SystemException, RemoteException {
		return getRMIService(false);
	}

	/**
	 * @return server name
	 */
	protected  String getRmiServer(){
	    if (rmiServer == null) {
            rmiServer = CommonEnvironment.getInstance().getRMIServerName();
        }
        return rmiServer;
	}

	/**
	 * This default implementation returns the same server name as the getRmiServer method.
	 * Override this method in your implementation class to return a separate failover name. 
	 * @return failover server name
	 */
	protected  String getRmiFailoverServer(){
	    // get RMI Server Name. Issue Environment lookup if it is not specified
        if (this.rmiFailoverServer == null) {
            this.rmiFailoverServer = CommonEnvironment.getInstance().getRMIServerName();
        }
        return rmiFailoverServer;
	}

	/**
	 * @param string
     */ 
	public void setRmiServer(String string) {
		rmiServer = string;
	}	
	/**
	 * This method is used to get the RMI type (i.e secured or unsecured)
	 */
	private String getRMIType() {
        return new BaseEnvironment().getNamingVariable(RMI_TYPE, null);
    }

	private String getSSLTrustStore() {
		return new BaseEnvironment().getNamingVariable(SSL_TRUST_STORE, null);
	}
	
	private String getSSLTrustStorePassword() {
		return new BaseEnvironment().getNamingVariable(SSL_TRUST_STORE_PWD, null);
	}
}