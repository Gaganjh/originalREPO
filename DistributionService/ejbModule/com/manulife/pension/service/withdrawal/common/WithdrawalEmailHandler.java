package com.manulife.pension.service.withdrawal.common;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.service.party.dao.EmployeeEmailAddressDAO;
import com.manulife.pension.service.party.dao.EmployeeEmailMessageDAO;
import com.manulife.pension.service.party.valueobject.EmployeeEmailAddressValueObject;
import com.manulife.pension.service.party.valueobject.EmployeeEmailMessageValueObject;
import com.manulife.pension.service.withdrawal.exception.WithdrawalEmailException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalEmailVO;

/**
 * WithdrawalEmailHandler creates the email messages to be sent when the state of a request becomes
 * Pending Approval or Pending Review.
 * 
 * @author Mihai Popa
 */
public class WithdrawalEmailHandler {

    private static final Logger logger = Logger.getLogger(WithdrawalEmailHandler.class);

    private static final String CLASS_NAME = WithdrawalEmailHandler.class.getName();

    // 13 for when PPT succesfully submit Withdrawal request
    // 14 for PSW/TPA approved withdrawal request
    // 15 for PSW/TPA denied withdrawal request
    public static final String EVENT_TYPE_ID_SUBMITTED = "13";

    public static final String EVENT_TYPE_ID_APPROVED = "14";

    public static final String EVENT_TYPE_ID_DENIED = "15";

    /**
     * Default Constructor.
     */
    public WithdrawalEmailHandler() {
        if (logger.isDebugEnabled()) {
            logger.debug("enter: WithdrawalEmailHandler()");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit: WithdrawalEmailHandler()");
        }
    }

    /**
     * Sends email to participant for 3 events: 1) submission 2) approval 3) deny
     * 
     * @throws WithdrawalEmailException
     */
    public void sendEmailToParticipant(final WithdrawalEmailVO withdrawalEmailVO,
            final String eventId) throws WithdrawalEmailException {

        EmployeeEmailAddressDAO dao = new EmployeeEmailAddressDAO();
        EmployeeEmailAddressValueObject employeeEmailAddressValueObject = null;
        try {
            employeeEmailAddressValueObject = dao.selectCurrentEmployeeEmailAddress(new BigDecimal(
                    withdrawalEmailVO.getEmployeeProfileId()));
        } catch (DAOException daoException) {
            throw new WithdrawalEmailException(daoException, CLASS_NAME,
                    "sendEmailToParticipant()", "DAO Exception trying to get employee "
                            + "email address for " + "submission id: "
                            + withdrawalEmailVO.getSubmissionId() + " employee profile id: "
                            + withdrawalEmailVO.getEmployeeProfileId());
        }

        // if PPT has email address
        if (employeeEmailAddressValueObject != null) {
            try {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                EmployeeEmailMessageValueObject employeeEmailMessageVo = new EmployeeEmailMessageValueObject();
                employeeEmailMessageVo.setProfileId(new BigDecimal(withdrawalEmailVO
                        .getEmployeeProfileId()));
                employeeEmailMessageVo.setContractNumber(withdrawalEmailVO.getContractId());
                employeeEmailMessageVo.setGeneratedTs(now);
                // 13 for when PPT succesfully submit Withdrawal request
                // 14 for PSW/TPA approved withdrawal request
                // 15 for PSW/TPA denied withdrawal request
                employeeEmailMessageVo.setEventTypeId(eventId);
                employeeEmailMessageVo.setEmailAddressEffectiveTs(employeeEmailAddressValueObject
                        .getEffectiveTs());
                employeeEmailMessageVo.setEmailSubjectText("");
                employeeEmailMessageVo.setEmailReturnAddress("");
                employeeEmailMessageVo.setEmailMessageStatusCode("00");
                employeeEmailMessageVo.setEmailMessageSentTs(null);
                employeeEmailMessageVo.setEmailMessageErrorCount(new Integer(0));
                employeeEmailMessageVo.setEmailMessageLastErrorTs(null);
                employeeEmailMessageVo.setEmailMessageLastErrorCode("");

                EmployeeEmailMessageDAO employeeEmailMessageDAO = new EmployeeEmailMessageDAO();
                employeeEmailMessageDAO.create(employeeEmailMessageVo);

            } catch (DAOException daoException) {
                throw new WithdrawalEmailException(daoException, CLASS_NAME,
                        "sendEmailToParticipant()",
                        "DAO Exception trying to send email to employee " + "submission id: "
                                + withdrawalEmailVO.getSubmissionId() + " employee profile id: "
                                + withdrawalEmailVO.getEmployeeProfileId());
            }
        }
    }
}
