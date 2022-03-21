/*
 * Created on Jan 12, 2007
 *
 */
package com.manulife.pension.ps.service.withdrawal.email;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.ejb.EJBObject;

import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;

/**
 * The service is generating notification emails based on the 
 * status and age of an withdrawal request.
 * It relies on the EmailProcessingService for delivery.  
 * 
 * @author Mihai Popa
 * 
 */
public interface EmailGeneratorService extends EJBObject {

	public void generateMessages() 
		throws RemoteException, EmailGeneratorException, SystemException, ApplicationException;
	

    /**
     * Generates an eMail missing notification message for the submission requests
     * approved after a specified date if the Submission ID is not in the list of IDs 
     * passed in as a parameter. 
     *  
     */
    public void sendReadyForEntryNotificationMessage()
        throws RemoteException, EmailGeneratorException;
}
