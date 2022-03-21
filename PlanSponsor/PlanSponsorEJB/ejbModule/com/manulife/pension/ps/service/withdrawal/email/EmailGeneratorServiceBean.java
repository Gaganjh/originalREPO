package com.manulife.pension.ps.service.withdrawal.email;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.common.Period;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmailProcessingServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.withdrawal.dao.EmailGeneratorDAO;
import com.manulife.pension.ps.service.withdrawal.valueobject.SubmissionEmailVO;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.entity.AvailabilityStatus;
import com.manulife.pension.service.cache.Cache;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.security.SystemUser;
import com.manulife.pension.service.util.AvailabilityUtils;
import com.manulife.pension.service.util.BaseEnterpriseBean;
import com.manulife.pension.service.withdrawal.common.WithdrawalDataManager;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.util.WithdrawalHelper;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.email.BodyPart;
import com.manulife.pension.util.email.EmailMessageException;
import com.manulife.pension.util.email.EmailMessageVO;

/**
 * Used to generate and send email reminders for withdrawal requests. The interval for when to send
 * the emails is configured in the environment.
 * 
 * @see BaseEnvironment
 * @author Mihai Popa
 * @author Paul Glenn
 */
public class EmailGeneratorServiceBean implements SessionBean {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(EmailGeneratorServiceBean.class);

    /**
     * EMAIL_GENERATOR_ENVIRONMENT_KEY.
     */
    private static final String EMAIL_GENERATOR_ENVIRONMENT_KEY = ".emailGenerator.";

    private SessionContext sessionContext;

    private static final String PROPERTY_PS_READY_FOR_ENTRY_NOTES_DB_DSN = "periodicProcess.ReadyForEntryEmailMonitorProcess.notesDbDsn";

    private static final String PROPERTY_PS_READY_FOR_ENTRY_FROM_ADDRESS = "periodicProcess.ReadyForEntryEmailMonitorProcess.fromAddress";

    private static final String PROPERTY_PS_READY_FOR_ENTRY_TO_ADDRESS = "periodicProcess.ReadyForEntryEmailMonitorProcess.toAddress";

    private static final String PROPERTY_PS_READY_FOR_ENTRY_CC_ADDRESS = "periodicProcess.ReadyForEntryEmailMonitorProcess.ccAddress";

    private Hashtable<Integer, Boolean> onlineWithdrawalsHash = new Hashtable<Integer, Boolean>();

    /**
     * Retrieves all the submission requests with a status of pending approval or pending review
     * that are old enough to trigger reminder emails. Based on the number of days since their
     * status change, different types of email messages are sent:
     * <ul>
     * <li> reviewReminder </li>
     * <li> approveReminder </li>
     * <li> carReviewReminder </li>
     * <li> carApproveReminder </li>
     * <li> reviewExpiry </li>
     * <li> approveExpiry </li>
     * <li> createExpiry </li>
     * </ul>
     * 
     * @throws EmailGeneratorException If an exception occurs.
     * @throws SystemException If an exception occurs.
     * @throws ApplicationException If an exception occurs.
     */
    public void generateMessages() throws EmailGeneratorException, SystemException,
            ApplicationException {

        if (logger.isDebugEnabled()) {
            logger.debug("generateMessages: Entry.");
        } // fi

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        final AvailabilityUtils availabilityUtils = AvailabilityUtils.getInstance();
        if (!(availabilityUtils.getIsAvailableLaterToday(today))) {
            // Today is a holiday, so we don't send out emails until it's a biz day again.
            if (logger.isDebugEnabled()) {
                logger.debug("Today is a holiday, so we don't send out "
                        + "emails until it's a business day again.");
            } // fi
            return;
        }

        // reset the contractServiceFeature hashtable
        onlineWithdrawalsHash.clear();

        // Lookup the environment settings for the offset days we use to calculate
        // when to send the emails.
        final BaseEnvironment baseEnvironment = new BaseEnvironment();
        final Map<EmailType, Integer> emailParameters = getValuesFromEnvironment(baseEnvironment);

        // There is an expiry offset, as this reminder is run on the morning of the following
        // day. The offset is configurable from the environment, as the process kicking off the
        // emails might fire after the busniess day, rather than before.
        final int expiryOffset = baseEnvironment.getIntVariable(StringUtils
                .defaultString(baseEnvironment.getApplicationId())
                + EMAIL_GENERATOR_ENVIRONMENT_KEY + "expiryOffset", 0);

        // Now process expiry messages.
        final Integer[] expiryValues = { emailParameters.get(EmailType.APPROVE_EXPIRY_DAYS),
                emailParameters.get(EmailType.REVIEW_EXPIRY_DAYS),
                emailParameters.get(EmailType.CREATOR_EXPIRY_DAYS) };

        final Set<Integer> expiryDays = new HashSet<Integer>();
        // Load the dates, removing duplicates (as it's a 'Set').
        Collections.addAll(expiryDays, expiryValues);

        for (Integer expiryDay : expiryDays) {
            final Period expiryPeriod = availabilityUtils.getNthBusinessPeriod(today, expiryDay
                    + expiryOffset);

            if (logger.isDebugEnabled()) {
                logger.debug("\nFrom TODAY: expiryDay[" + expiryDay + "], expiryPeriod["
                        + expiryPeriod + "]\n");
            } // fi

            // Now send expiry messages.
            final Collection<SubmissionEmailVO> allExpiryPendingRequests;
            try {
                allExpiryPendingRequests = selectAllPendingRequestsForExpiryReminder(expiryPeriod);
            } catch (DAOException daoException) {
                throw new EmailGeneratorException("DAO Exception trying to generate email message",
                        daoException);
            } // end try/catch

            if (logger.isDebugEnabled()) {
                logger.debug("\nAll Pending Expiry Requests for [" + expiryPeriod + "] (Count: ["
                        + allExpiryPendingRequests.size() + "]): " + allExpiryPendingRequests
                        + "\n");
            } // fi

            for (SubmissionEmailVO submissionEmailVO : allExpiryPendingRequests) {

                if (contractStatusIsAcOrCf(submissionEmailVO)
                        && contractHasWithdrawals(submissionEmailVO.getContractId())) {

                    final boolean isReview = submissionEmailVO.getRequestStatus().equalsIgnoreCase(
                            WithdrawalStateEnum.PENDING_REVIEW.getStatusCode());
                    final boolean isApprove = submissionEmailVO.getRequestStatus()
                            .equalsIgnoreCase(WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode());

                    if (expiryDay.equals(emailParameters.get(EmailType.CREATOR_EXPIRY_DAYS))) {
                        // Create event for 'about to expire' withdrawal requests.
                        final WithdrawalRequestMetaData withdrawalRequestMetaData = WithdrawalDataManager
                                .getWithdrawalRequestMetaData(submissionEmailVO.getSubmissionId());

                        WithdrawalHelper
                                .fireWithdrawalAboutToExpireEvent(
                                withdrawalRequestMetaData, SystemUser.SUBMISSION.getProfileId());
                    } // fi

                } // fi
            } // end for
        } // end for

        if (logger.isDebugEnabled()) {
            logger.debug("generateMessages: Exit.");
        } // fi
    }

    /**
     * Gets the configuration values for the number or days offset for each email type.
     * 
     * @param baseEnvironment The {@link BaseEnvironment} object to lookup the settings with.
     * @return Map - The configuration values for each email type.
     */
    private Map<EmailType, Integer> getValuesFromEnvironment(final BaseEnvironment baseEnvironment) {

        final HashMap<EmailType, Integer> map = new HashMap<EmailType, Integer>();

        for (EmailType emailType : EmailType.values()) {

            map.put(emailType, baseEnvironment.getIntVariable(StringUtils
                    .defaultString(baseEnvironment.getApplicationId())
                    + EMAIL_GENERATOR_ENVIRONMENT_KEY + emailType.getCode(), -1));
        } // end for

        return map;
    }

    /**
     * Queries the database for all eligible submission requests.
     * 
     * @param reminderPeriod the period the withdrawal was last changed between to select with.
     * @return a list of SubmissionEmailVO objects
     */
    private ArrayList<SubmissionEmailVO> retrieveAllPendingReminderRequests(
            final Period reminderPeriod) throws DAOException {
        return new EmailGeneratorDAO().selectAllPendingRequestsForReminders(reminderPeriod);
    }

    /**
     * Queries the database for all eligible submission requests.
     * 
     * @param expiryPeriod The expiry date to search with.
     * @return ArrayList of {@link SubmissionEmailVO} objects.
     * @throws DAOException If a data exception occurs.
     */
    private ArrayList<SubmissionEmailVO> selectAllPendingRequestsForExpiryReminder(
            final Period expiryPeriod) throws DAOException {
        return new EmailGeneratorDAO().selectAllPendingRequestsForExpiryReminder(expiryPeriod);
    }

    /**
     * Generates an eMail missing notification message for the submission requests approved after a
     * specified date if the Submission ID is not found in the Ready-For-Entry Notes database.
     * 
     * @throws EmailGeneratorException If an exception occurs.
     */
    public void sendReadyForEntryNotificationMessage() throws EmailGeneratorException {

        // try {
        Date previousRunTs;
        try {
            previousRunTs = EmailGeneratorDAO.getReadyForEntryEmailLastReconciledDate();
        } catch (DAOException daoException) {
            logger.error(daoException);
            throw new EmailGeneratorException("Failed to send Ready-For-Entry notification",
                    daoException);
        }

        final BaseEnvironment baseEnvironment = new BaseEnvironment();

        // jdbc:odbc:iWithdrawals
        String psReadyForEntryNotesDbDsn = baseEnvironment.getNamingVariable(
                PROPERTY_PS_READY_FOR_ENTRY_NOTES_DB_DSN, null);

        // ReadyForEntry Reconciliation eMail address
        String psReadyForEntryFromAddress = baseEnvironment.getNamingVariable(
                PROPERTY_PS_READY_FOR_ENTRY_FROM_ADDRESS, null);
        String psReadyForEntryToAddress = baseEnvironment.getNamingVariable(
                PROPERTY_PS_READY_FOR_ENTRY_TO_ADDRESS, null);
        String psReadyForEntryCcAddress = baseEnvironment.getNamingVariable(
                PROPERTY_PS_READY_FOR_ENTRY_CC_ADDRESS, null);

        String notesDbDsn = "jdbc:odbc:" + psReadyForEntryNotesDbDsn;
        String fromAddress = psReadyForEntryFromAddress;
        String[] toAddress = StringUtils.split(psReadyForEntryToAddress, ",");
        String[] ccAddress = StringUtils.split(psReadyForEntryCcAddress, ",");

        logger.debug("Checking for submissions posted after: "
                + SimpleDateFormat.getDateTimeInstance().format(previousRunTs));

        // 1. Retrieve the IDs of all the submissions posted to the Notes Ready-For-Entry Notes
        // database
        // one hour prior to the lastRunDate (WMM-5)
        Calendar lastUpdateCal = Calendar.getInstance();
        lastUpdateCal.setTime(previousRunTs);
        lastUpdateCal.add(Calendar.HOUR, -1);

        Collection<Integer> submissionIds;
        try {
            submissionIds = EmailGeneratorDAO.getPostedSubmissions(notesDbDsn, lastUpdateCal
                    .getTime());
        } catch (DAOException daoException) {
            throw new EmailGeneratorException(
                    "Failed to send Ready-For-Entry notification.  Problem reading posted submissions",
                    daoException);
        }

        // 2. Get the list of Submission requests approved after the lastRunDate
        Collection<SubmissionEmailVO> submissions;
        try {
            submissions = EmailGeneratorDAO.selectReadyForEntryRequests(previousRunTs);
        } catch (DAOException daoException) {
            logger.error(daoException);
            throw new EmailGeneratorException("Failed to send Ready-For-Entry notification",
                    daoException);
        }

        Collection<SubmissionEmailVO> newSubmissions = new ArrayList<SubmissionEmailVO>();

        for (SubmissionEmailVO submission : submissions) {
            if (!submissionIds.contains(submission.getSubmissionId())) {
                newSubmissions.add(submission);
            }
        }

        // 3. If there not all the approved requests have corresponding messages posted in the
        // Notes database
        // Prepare and send the notification message
        if (newSubmissions.size() > 0) {
            EmailProcessingServiceDelegate processingDelegate = EmailProcessingServiceDelegate
                    .getInstance(null);

            Date asOfDate = new Date();
            String subject = "Notes email missing for iwithdrawal";
            String body = generateReadyForEntryReminderMessageBody(newSubmissions, asOfDate);

            ArrayList<BodyPart> bodyParts = new ArrayList<BodyPart>();
            bodyParts.add(new BodyPart("bodyPart", true, BodyPart.TEXT_CONTENT, body, null));

            EmailMessageVO psMessage;
            try {
                psMessage = new EmailMessageVO(null, fromAddress, null, toAddress, ccAddress, null,
                        subject, bodyParts, 0);
            } catch (EmailMessageException emailMessageException) {
                logger.error(emailMessageException);
                throw new EmailGeneratorException("Failed to send Ready-For-Entry notification",
                        emailMessageException);
            }
            try {
                processingDelegate.sendAndReceiveConfirmation(psMessage);
            } catch (SystemException systemException) {
                logger.error(systemException);
                throw new EmailGeneratorException(
                        "System Exception trying to generate Ready-For-Entry notification message",
                        systemException);
            }
        }

        try {
            EmailGeneratorDAO.setReadyForEntryEmailLastReconciledDate(new Date());
        } catch (DAOException daoException) {
            logger.error(daoException);
            throw new EmailGeneratorException(
                    "Failed to update the lastRunDate for Ready-For-Entry notification process",
                    daoException);
        }

    }

    /**
     * Retrieves the last <code>maximumOverdueDays<code> business days 

     * @param startCal the day from which we should go back in history
     * @param maximumOverdueDays the number of business days we should go back
     * @return a list of dates garanteed to be business days
     * @throws Exception 
     * @throws AccountException
     */
    private Date[] buildBusinessDayHistory(final Calendar startCal, final int maximumOverdueDays)
            throws AccountException, Exception {

        List table = new ArrayList();
        int daysFromCurrentDate = 0;
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        while (daysFromCurrentDate < maximumOverdueDays) {
            Date dateToCheck = startCal.getTime();
            AvailabilityStatus status = AccountServiceDelegate.getInstance()
                    .getNYSEAvailabilityStatusAsOf(dateToCheck, true);
            if (status.isAvailableLaterToday()) {
                table.add(dateToCheck);
                daysFromCurrentDate++;
            }
            startCal.add(Calendar.DATE, -1);
        }
        return (Date[]) table.toArray(new Date[0]);
    }

    /**
     * Generates the message body for the Ready-For-Entry monitor notification
     * 
     * @param submissionList List of SubmissionEmailVOs to be included in the eMail body
     * @param date when the mail message is generated
     * @return the message body
     * @throws EmailGeneratorException
     */
    private String generateReadyForEntryReminderMessageBody(
            final Collection<SubmissionEmailVO> submissionList, final Date asOfDate)
            throws EmailGeneratorException {

        // NOTE: It's a business requirement to format date as ET (not using z for timezone)
        // thus pinning it to the Eastern Time (EST/EDT)
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ssa");

        StringBuffer sBody = new StringBuffer("As of ");
        sBody.append(df.format(asOfDate));
        sBody
                .append(", one or more i:withdrawal requests have been identified with no email record "
                        + "in the i:withdrawals mail-in database. The missing email(s) are listed below. Systems "
                        + "staff have also been copied on this email so they can investigate the cause of the problem.");
        sBody.append("\n\n");
        sBody
                .append("To ensure timely processing of the request, access the Plan Sponsor Website "
                        + "and print a copy of the request. To access i:withdrawals, after logging on to the "
                        + "web site select the contract of the missing email, navigate to the Tools page and "
                        + "select i:distributions. On the request list page, select the i:withdrawal request "
                        + "with the matching submission number (you can also search by the submission number "
                        + "to find it in the list). When the request is displayed, click the print friendly "
                        + "link in the upper right corner of the page.");
        sBody.append("\n\n");

        /*
         * Table header (5 columns all left-aligned): - Contract # [14] - Submission # [16] -
         * Participant Name [46] - Approved Date/Time [30]
         */
        sBody.append(StringUtils.rightPad("Contract", 9)).append(
                StringUtils.rightPad("Submission", 11)).append(
                StringUtils.rightPad("Approved Date/Time", 22)).append("Participant Name");
        sBody.append("\n");
        sBody.append(StringUtils.rightPad("-------- ", 9)).append(
                StringUtils.rightPad("---------- ", 11)).append(
                StringUtils.rightPad("---------------------", 22)).append("----------------");
        sBody.append("\n");

        for (SubmissionEmailVO vo : submissionList) {
            String fullName = vo.getFirstName() + " ";
            if (vo.getMiddleInitial() != null && vo.getMiddleInitial().length() > 0) {
                fullName += vo.getMiddleInitial() + " ";
            }
            fullName += vo.getLastName();

            sBody.append(StringUtils.rightPad(vo.getContractId().toString(), 9)).append(
                    StringUtils.rightPad(vo.getSubmissionId().toString(), 11)).append(
                    StringUtils.rightPad(df.format(vo.getLastStatusUpdateTS()), 22)).append(
                    fullName);
            sBody.append("\n");
        }
        sBody.append("\n");
        // sBody.append("\nSystems staff have also been copied on this email so they can investigate
        // the cause of the problem.");
        return sBody.toString();
    }

    /**
     * Test if the contract has withdrawals (maybe it has been turned off since creation of
     * request).
     */
    private boolean contractHasWithdrawals(final Integer contractId) throws SystemException,
            ApplicationException {

        if (onlineWithdrawalsHash.containsKey(contractId)) {
            return onlineWithdrawalsHash.get(contractId).booleanValue();
        } else {
            final ContractServiceDelegate csd = ContractServiceDelegate.getInstance();
            final ContractServiceFeature csf = csd.getContractServiceFeature(contractId,
                    ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
            final Boolean hasWithdrawals = BooleanUtils.isTrue(ContractServiceFeature
                    .internalToBoolean(csf.getValue()));
            onlineWithdrawalsHash.put(contractId, hasWithdrawals);
            return hasWithdrawals;
        }
    }

    private boolean contractStatusIsAcOrCf(final SubmissionEmailVO submissionEmailVO) {

        return ((StringUtils.equals(submissionEmailVO.getContractStatus(),
                ContractInfo.CONTRACT_STATUS_ACTIVE)) || (StringUtils.equals(submissionEmailVO
                .getContractStatus(), ContractInfo.CONTRACT_STATUS_FROZEN)));

    }

    /**
     * TODO getHome Description.
     * 
     * @param homeClass
     * @param name
     * @return
     */
    protected EJBHome getHome(final Class homeClass, final String name) {
        EJBHome home = (EJBHome) Cache.getFromCache(EJBHome.class.getName(), name);

        if (home == null) {
            try {
                Context context = new InitialContext();
                home = (EJBHome) PortableRemoteObject.narrow(context.lookup(name), homeClass);
                Cache.cacheObject(EJBHome.class.getName(), name, home, Cache.EXPIRES_NEVER);
            } catch (ClassCastException e) {
            	logger.error(e);
                throw new EJBException(e.toString());
            } catch (NamingException e) {
            	logger.error(e);
                throw new EJBException(e.toString());
            }

        }
        return home;
    }

    /**
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(final SessionContext sessionContext) throws EJBException,
            RemoteException {
        this.sessionContext = sessionContext;
    }
    /**
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }
    /**
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.SessionBean#ejbCreate()
     */
    public void ejbCreate() throws CreateException {
    }

}
