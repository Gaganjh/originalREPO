package com.manulife.pension.ps.service.delegate;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.withdrawal.email.EmailGeneratorException;
import com.manulife.pension.ps.service.withdrawal.email.EmailGeneratorService;
import com.manulife.pension.ps.service.withdrawal.email.EmailGeneratorServiceHome;
import com.manulife.pension.service.delegate.AbstractServiceDelegate;
import com.manulife.pension.util.BaseEnvironment;

/**
 * This class will be used to allow access for clients to EmailGeneratorService
 * 
 * @author Mihai Popa
 * 
 */
public class EmailGeneratorServiceDelegate extends AbstractServiceDelegate {

    private static EmailGeneratorServiceDelegate instance;

    private static final Logger logger = Logger.getLogger(EmailGeneratorServiceDelegate.class);

    private static final String className = EmailGeneratorServiceDelegate.class.getName();

    /**
     * @return
     */
    public static EmailGeneratorServiceDelegate getInstance() {
        if (instance == null) {
            instance = new EmailGeneratorServiceDelegate();
        }

        return instance;
    }

    /**
     * @see AbstractServiceDelegate#getHomeClassName()
     */
    protected String getHomeClassName() {
        return EmailGeneratorServiceHome.class.getName();
    }

    /**
     * @see AbstractServiceDelegate#getRemote(EJBHome)
     */
    protected EJBObject create() throws SystemException, RemoteException, CreateException {
        return ((EmailGeneratorServiceHome) getHome()).create();
    }

    /**
     * @see AbstractServiceDelegate#getApplicationId()
     */
    protected String getApplicationId() {
        return new BaseEnvironment().getApplicationId();
    }

    /**
     * @see com.manulife.pension.ps.service.withdrawal.email.EmailGeneratorServiceBean.generateMessages()
     */
    public void generateMessages() throws SystemException {

        EmailGeneratorService service = (EmailGeneratorService) getService();
        try {
            service.generateMessages();
        } catch (EmailGeneratorException e) {
            throw new SystemException(e, "EmailGeneratorServiceDelegate EmailGeneratorException.");
        } catch (RemoteException e) {
            throw new SystemException(e, "EmailGeneratorServiceDelegate RemoteException.");
        } catch (ApplicationException e) {
            throw new SystemException(e, "EmailGeneratorServiceDelegate ApplicationException.");
        }
    }

    /**
     * Generates an eMail missing notification message for the submission requests approved after a
     * specified date if the Submission ID is not in the list of IDs passed in as a parameter.
     */
    public void sendReadyForEntryNotificationMessage() throws SystemException {
        EmailGeneratorService service = (EmailGeneratorService) getService();
        try {
            service.sendReadyForEntryNotificationMessage();
        } catch (EmailGeneratorException e) {
            throw new SystemException(e, "EmailGeneratorServiceDelegate EmailGeneratorException: " + e);
        } catch (RemoteException e) {
            throw new SystemException(e, "EmailGeneratorServiceDelegate RemoteException:" + e);
        }
    }
}
