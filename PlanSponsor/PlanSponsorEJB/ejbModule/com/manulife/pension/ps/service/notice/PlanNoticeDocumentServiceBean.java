package com.manulife.pension.ps.service.notice;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.notice.dao.PlanNoticeDocumentChangeDao;
import com.manulife.pension.ps.service.report.notice.dao.PlanNoticeDocumentDAO;
import com.manulife.pension.ps.service.report.notice.valueobject.EmployeeEligibleVO;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.service.report.exception.ReportServiceException;

/**
 * Bean implementation class for Enterprise Bean: PlanNoticeDocumentService
 */
public class PlanNoticeDocumentServiceBean implements SessionBean {
	/**
	 * Default Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(PlanNoticeDocumentServiceBean.class);

	private SessionContext mySessionCtx;

	


	/**
	 * getSessionContext
	 */
	public SessionContext getSessionContext() {
		return mySessionCtx;
	}

	/**
	 * setSessionContext
	 */
	public void setSessionContext(javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
	}

	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}

	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}

	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}

	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}

	/**
	 * This method used to add the custom plan document into DB
	 * @param PlanNoticeDocumentVO
	 * @return
	 */
	public int addCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) {
		if (logger.isDebugEnabled())
			logger.debug("entry -> addCustomPlanNoticeDocument");

		Integer count = 0;

		try {
			count= PlanNoticeDocumentChangeDao.addCustomPlanNoticeDocument(planNoticeDocumentDetail);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- addCustomPlanNoticeDocument");

		return count;
	}


	/**
	 * To Get the max DocumentId
	 * @return
	 * @throws RemoteException
	 */
	public int getMaxDocumentId() throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getMaxDocumentId");

		Integer count = 0;

		try {
			count= PlanNoticeDocumentChangeDao.getNextSequenceId(PlanNoticeDocumentChangeDao.GET_MAX_DOCUMENT_ID);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getMaxDocumentId");

		return count;
	}

	
/**
 * To Get the max Document Order
 * @return
 * @throws RemoteException
 */

	public int getMaxDocumentOrder() throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getMaxDocumentOrder");

		Integer count = 0;

		try {
			count= PlanNoticeDocumentChangeDao.getMaxDocumentOrder();
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getMaxDocumentOrder");

		return count;
	}


	/**
	 *  To Check PlanNotice documentName whether exist or not 
	 * @param String
	 * @return
	 */
	public boolean isNoticeDocumentNameExists(String fileName,Integer ContractId) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> isNoticeExists");

		
		try {
			return PlanNoticeDocumentChangeDao.isNoticeDocumentNameExists(fileName,ContractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- isNoticeExists");
		}
	}


	/**
	 * To Update the Terms and Conditions Acceptance indicator
	 * @param BigDecimal, boolean
	 * @return
	 */
	public boolean updateUsersTermConditionAcceptance(BigDecimal ProfileId, boolean acceptanceInd) throws RemoteException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> updateTermsOfUseAcceptance");

		try {
			return PlanNoticeDocumentChangeDao.updateUsersTermConditionAcceptance(ProfileId, acceptanceInd);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- updateTermsOfUseAcceptance");
		}
		
	}
	
	
	
	/**
	 * To Apply the custom sort Document order
	 * @param planNoticeDocumentDetails
	 * @throws SystemException
	 */
	public  void applyCustomPlanNoticeDocumentSortOrder(List<PlanNoticeDocumentVO> planNoticeDocumentDetails) throws SystemException {

		try {
			PlanNoticeDocumentChangeDao.applyCustomPlanNoticeDocumentSortOrder(planNoticeDocumentDetails);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- applyCustomPlanNoticeDocumentSortOrder");

	}
	

	/**
	 * editCustomPlanNoticeDocument
	 * @param planNoticeDocumentDetail
	 * @return boolean
	 */
	public boolean editCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> addCustomPlanNoticeDocument");
		
		try {
			return PlanNoticeDocumentChangeDao.editCustomPlanNoticeDocument(planNoticeDocumentDetail);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
		if (logger.isDebugEnabled())
			logger.debug("exit <- addCustomPlanNoticeDocument");
		}
	}

	/**
	 * Delete the plan Notice document by updating the soft delete indicator
	 * @param planNoticeDocumentDetail
	 * @return
	 */
	public boolean deleteCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> addCustomPlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.deleteCustomPlanNoticeDocument(planNoticeDocumentDetail);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- addCustomPlanNoticeDocument");
		}	
		
	}

	/**
	 * To check the SoftDeleteIndicator
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SQLException 
	 */
	public PlanNoticeDocumentVO checkPlanNoticeDocumentSoftDeleteIndicator(PlanNoticeDocumentVO planNoticeDocumentVO) throws RemoteException, SQLException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> addCustomPlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.checkPlanNoticeDocumentSoftDeleteIndicator(planNoticeDocumentVO);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- addCustomPlanNoticeDocument");
		}	
		
	}

	/**
	 * To Insert the Notice Document  Logs into DB
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SQLException 
	 */
	public boolean insertCustomPlanNoticeDocumentLogs(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> addCustomPlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.insertCustomPlanNoticeDocumentLogs(planNoticeDocumentChangeHistoryVO);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- addCustomPlanNoticeDocument");
		}	
		
	}

	/**
	 * To get the Notice lock information
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SQLException 
	 */
	public PlanNoticeDocumentVO retreivePlanNoticeLockInfo(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> addCustomPlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.retreivePlanNoticeLockInfo(planNoticeDocumentChangeHistoryVO);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- addCustomPlanNoticeDocument");
		}	
		
	}

	/**
	 * To release the notice lock 
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SQLException 
	 */
	public boolean releaseCustomPlanNoticeDocumentLock(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> addCustomPlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.releaseCustomPlanNoticeDocumentLock(planNoticeDocumentChangeHistoryVO);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- addCustomPlanNoticeDocument");
		}	
		
	}

	/**
	 * To get the CustomPlanNotice DocumentInfo
	 * @param Integer
	 * @return PlanNoticeDocumentVO
	 * @throws SQLException 
	 */
	public PlanNoticeDocumentVO getCustomPlanNoticeDocumentInfo(Integer documentId) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> addCustomPlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.getCustomPlanNoticeDocumentInfo(documentId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- addCustomPlanNoticeDocument");
		}	
		
	}
	
	/**
	 * populates the existing alerts for the particular user
	 * @param profileId
	 * @param contractId
	 * @return List<UserNoticeManagerAlertVO>
	 * @throws SystemException
	 */
	public List<UserNoticeManagerAlertVO> getUserNoticePreferences(BigDecimal profileId, Integer contractId) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getUserNoticePreferences");

		
		try {
			return PlanNoticeDocumentChangeDao.getUserNoticePreferences(profileId,contractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getUserNoticePreferences");
		}	
		
	}
	
	/**
	 * Saves the alert for particular user
	 * @param userNoticePreference
	 * @return
	 * @throws RemoteException
	 */
	public int addUserNoticePreferences(List<UserNoticeManagerAlertVO> userNoticePreference) throws RemoteException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> insertUserNoticePreferences");

		
		try {
			return PlanNoticeDocumentChangeDao.addUserNoticePreferences(userNoticePreference);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getUserNoticePreferences");
		}	
	}
	
	/**
	 *  Populates the Alert frequency dropdown box
	 * @param ContractId
	 * @return
	 * @throws RemoteException
	 */
	public List<LookupDescription> getUserManagerAlertFrequencyCodes(Integer ContractId) throws RemoteException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> insertUserNoticePreferences");

		
		try {
			return PlanNoticeDocumentChangeDao.getUserManagerAlertFrequencyCodes(ContractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getUserNoticePreferences");
		}	
	}
	
	/**
	 * Populates the Alert timing dropdown box
	 * @param contractId
	 * @return
	 * @throws RemoteException
	 */
	public List<LookupDescription> getUserManagerAlertTimingCodes(Integer contractId) throws RemoteException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> insertUserNoticePreferences");

		
		try {
			return PlanNoticeDocumentChangeDao.getUserManagerAlertTimingCodes(contractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getUserNoticePreferences");
		}	
	}
	
	/**
	 * Get the term of use acceptance indicator
	 * @param profileId
	 * @return
	 * @throws RemoteException
	 */
	public String getTermsAndAcceptanceInd(BigDecimal profileId) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> checkUserTermsAndAcceptanceInd");
		String termsAndAcceptanceInd = null;
			try {
		  termsAndAcceptanceInd = PlanNoticeDocumentChangeDao.getTermsAndAcceptanceInd(profileId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- checkUserTermsAndAcceptanceInd");
		}
			return termsAndAcceptanceInd;	
		
	}
	/**
	 * To get the Document Posted UserName
	 * @param contractId
	 * @param documentId
	 * @return
	 */

	public  PlanNoticeDocumentVO getDocumentPostedUsername(int contractId,int documentId){
		if (logger.isDebugEnabled())
			logger.debug("entry -> UploadAndSharePlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.getDocumentPostedUsername(contractId,documentId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- UploadAndSharePlanNoticeDocument");
		}	
		
	}
		
	
	/**
	 * Method to check alert name exist
	 * @param alertName
	 * @return
	 * @throws SystemException
	 */
	public boolean checkAlertNameExists(String alertName,Integer alertId,Integer contractId) throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> checkAlertNameExists");
		boolean alertNameExist = false;
			try {
				alertNameExist = PlanNoticeDocumentChangeDao.checkAlertNameExists(alertName, alertId, contractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- checkAlertNameExists");
		}
			return alertNameExist;	
	}
	
	/**
	 * Delete the Existing alert 
	 * @param alertId
	 * @return
	 * @throws SystemException
	 */
	public boolean deleteAlert(Integer alertId) throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> deleteAlert");
		boolean deletedInd = false;
			try {
				deletedInd = PlanNoticeDocumentChangeDao.deleteAlert(alertId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- deleteAlert");
		}
			return deletedInd;	
	}
	/**
	 *Adds the user action information 
	 * @param alertId
	 * @param WebPageTypeCode
	 * @return
	 * @throws SystemException
	 */
	public boolean userActionLog(Integer contractId,BigDecimal profileId,String userAction) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> addWebPageVisitLog");
		boolean flag = false;
			try {
				flag = PlanNoticeDocumentChangeDao.userActionLog(contractId,profileId,userAction);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- userActionLog");
		}
			return flag;	
		
	}
	
	/**
	 * Get the existing notice manager alert details
	 * @param alertId
	 * @return UserNoticeManagerAlertVO
	 * @throws RemoteException
	 */
	public UserNoticeManagerAlertVO getExistingAlertDetails(int alertId) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getExistingAlertDetails");
		UserNoticeManagerAlertVO existingAlertVO = null;
			try {
				existingAlertVO = PlanNoticeDocumentChangeDao.getExistingAlertDetails(alertId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getExistingAlertDetails");
		}
			return existingAlertVO;	
		
	}
	
	/**
	 * To update the CustomPlanNotice DocumentOrder
	 * @param documentOrderList
	 * @return
	 * @throws RemoteException
	 */
	public boolean updateCustomPlanNoticeDocumentOrder(List<Integer> documentOrderList) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> updateCustomPlanNoticeDocumentOrder");
		boolean flag = false;
			try {
				flag = PlanNoticeDocumentChangeDao.updateCustomPlanNoticeDocumentOrder(documentOrderList);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- updateCustomPlanNoticeDocumentOrder");
		}
			return flag;	
		
	}
	
	/**
	 * Get the count of all users uploaded document
	 * @param contractId
	 * @return
	 * @throws RemoteException
	 */
	public  int getUserUploadedDocumentPostedCount(Integer contractId) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getUserUploadedDocumentPostedCount");
		int uploadedCount  = 0;
			try {
				uploadedCount = PlanNoticeDocumentChangeDao.getUserUploadedDocumentPostedCount(contractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getUserUploadedDocumentPostedCount");
		}
			return uploadedCount;	
		
	}
	
	/**
	 * Get the Notice document change type code 
	 * @return
	 * @throws RemoteException
	 */
	public List<LookupDescription> getContractNoticeDocumentTypeCodes() throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getUserUploadedDocumentPostedCount");
		List<LookupDescription>  contractDocumentChangeTypeCodes  = null;
		try {
			contractDocumentChangeTypeCodes = PlanNoticeDocumentChangeDao.getContractNoticeDocumentTypeCodes();
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getContractNoticeDocumentTypeCodes");
		}
		return contractDocumentChangeTypeCodes;	

	}
	
	/**
	 * To insert the ContractNotice MailingOrder
	 * @param planNoticeMailingOrderVO
	 * @return
	 * @throws RemoteException
	 */
	public boolean insertContractNoticeMailingOrder(PlanNoticeMailingOrderVO planNoticeMailingOrderVO) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> checkUserTermsAndAcceptanceInd");
		boolean flag = false;
			try {
				flag = PlanNoticeDocumentChangeDao.insertContractNoticeMailingOrder(planNoticeMailingOrderVO);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- checkUserTermsAndAcceptanceInd");
		}
			return flag;	
		
	}
/**
 * To get the custom Document Presence
 * @param documentId
 * @return
 * @throws RemoteException
 */
	public boolean getCustomDocumentPresence(Integer documentId) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> checkUserTermsAndAcceptanceInd");
		boolean flag = false;
			try {
				flag = PlanNoticeDocumentChangeDao.getCustomDocumentPresence(documentId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- checkUserTermsAndAcceptanceInd");
		}
			return flag;	
		
	}
	/**
	 * 
	 * To update the SoftDelgate Indicator
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws RemoteException
	 */
	public boolean updateSoftDelgateIndicator(PlanNoticeDocumentVO planNoticeDocumentDetail) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> editCustomPlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.updateSoftDelgateIndicator(planNoticeDocumentDetail);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- editCustomPlanNoticeDocument");
		}	
		
	}
	
	/**
	 * 
	 * To update the ContractNotice Mailing OrderStatus
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws RemoteException
	 */
	public boolean updateContractNoticeMailingOrderStatus(PlanNoticeMailingOrderVO planNoticeMailingOrderVO) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> editCustomPlanNoticeDocument");

		
		try {
			return PlanNoticeDocumentChangeDao.updateContractNoticeMailingOrderStatus(planNoticeMailingOrderVO);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled()){
				logger.debug("exit <- updateContractNoticeMailingOrderStatus");
			}
		}	
		
	}
	
	/**
	 * This method is used to check the  Custom notice document Count value
	 * @param documentId
	 * @return
	 * @throws SystemException
	 * @throws SQLException
	 */
	public  int checkCustomNoticeDocumentCount(Integer contractId) throws RemoteException{
		if (logger.isDebugEnabled()){
			logger.debug("entry -> checkCustomNoticeDocumentCount");
		}

		
		try {
			return PlanNoticeDocumentChangeDao.checkCustomNoticeDocumentCount(contractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled()){
				logger.debug("exit <- checkCustomNoticeDocumentCount");
			}
		}	
		
	}
	

	/**
	 * 
	 * To update the ContractNotice Mailing OrderStatus
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws RemoteException
	 */
	public boolean checkCustomNoticeDocumentNameChange(Integer documentId, String documentName) throws RemoteException{
		if (logger.isDebugEnabled()){
			logger.debug("entry -> editCustomPlanNoticeDocument");
		}

		try {
			return PlanNoticeDocumentChangeDao.checkCustomNoticeDocumentNameChange(documentId, documentName);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled()){
				logger.debug("exit <- updateContractNoticeMailingOrderStatus");
			}
		}	
		
	}
	
	
	
	/**
	 * To check the PlanNotice SoftDelete Indicator
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws RemoteException
	 */
	public  String  checkPlanNoticeSoftDeleteIndicator(int contractId,int documentId,String documentFileName) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> editCustomPlanNoticeDocument");
		String softDeleteIndValue = null;
		try {
			softDeleteIndValue =PlanNoticeDocumentChangeDao.checkPlanNoticeSoftDeleteIndicator(contractId, documentId,documentFileName);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- updateContractNoticeMailingOrderStatus");
		}	
		return softDeleteIndValue;
	}
	
	/**
	 * To get the PlanNotice Deleted Username
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws RemoteException
	 */
	public  PlanNoticeDocumentVO  getPlanNoticeDeletedUsername(int contractId,int documentId) throws RemoteException{
		if (logger.isDebugEnabled()){
			logger.debug("entry -> get the PlanNotice Deleted Username");
		}
		PlanNoticeDocumentVO planNoticeDocumentVO =null;
		try {
			planNoticeDocumentVO =PlanNoticeDocumentChangeDao.getPlanNoticeDeletedUsername(contractId, documentId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled()){
				logger.debug("exit <- get the PlanNotice Deleted Username");
			}
		}	
		return planNoticeDocumentVO;
	}

	/**
	 * To get the ContractNotice Updated User Details
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws RemoteException
	 */
	public  LinkedHashMap<BigDecimal,String> getContractNoticeUpdatedUserDetails(Integer contractId, java.sql.Timestamp fromDate, java.sql.Timestamp toDate) throws RemoteException{
		if (logger.isDebugEnabled()){
			logger.debug("entry -> getContractNoticeUpdatedUserDetails");
		}
		LinkedHashMap<BigDecimal,String> noticeChangedUserDetails = new LinkedHashMap<BigDecimal,String>();
		try {
					noticeChangedUserDetails =PlanNoticeDocumentChangeDao.getContractNoticeUpdatedUserDetails(contractId, fromDate, toDate);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled()){
				logger.debug("exit <- getContractNoticeUpdatedUserDetails");
			}
		}	
		return noticeChangedUserDetails;
	}
	
	/**
	 * 
	 * This method is used to get the  Custom notice document locked UserName
	 * 
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws RemoteException
	 */
	public  PlanNoticeDocumentVO  getPlanNoticeLockedUserName(String componentKey,long lockUserProfileId) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getCustomNoticeDocumentLockedUserName");
		PlanNoticeDocumentVO planNoticeDocumentVO =null;
		try {
				planNoticeDocumentVO =PlanNoticeDocumentChangeDao.getPlanNoticeLockedUserName(componentKey,lockUserProfileId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getCustomNoticeDocumentLockedUserName");
		}	
		return planNoticeDocumentVO;
	}

	/**
	 * Get the contract notice mailing order details for the given orderNumber
	 * @param orderNumber
	 * @return
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public PlanNoticeMailingOrderVO getContractNoticeMailingOrder(int orderNumber)  throws SystemException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getContractNoticeMailingOrder");
		try {
			return PlanNoticeDocumentChangeDao.getContractNoticeMailingOrder(orderNumber);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getContractNoticeMailingOrder");
		}	
		
	}
	
	/**
	 *  This method will update the order status records into Contract Notice Mailing Order 
	 * @param contractNoticeMailingOrderVO
	 * @return
	 * @throws SystemException
	 */
	public  boolean updateContractNoticeMailingOrder(PlanNoticeMailingOrderVO contractNoticeMailingOrderVO) throws SystemException {
		if (logger.isDebugEnabled()){
			logger.debug("entry -> updateContractNoticeMailingOrder");
		}
		try {
			return  PlanNoticeDocumentChangeDao.updateContractNoticeMailingOrder(contractNoticeMailingOrderVO);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled()){
				logger.debug("exit <- updateContractNoticeMailingOrder");
			}
		}	
		
		
	}
	
	/**
	 * this method is used to get the eligible employee list
	 * @param contractNumber
	 * @param eligibleEmployeeAddressList
	 * @return
	 * @throws RemoteException
	 */
	public  List<EmployeeEligibleVO>  getEligibleEmployeeDetails(int contractNumber,List<EmployeeEligibleVO> eligibleEmployeeAddressList ) throws RemoteException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getEligibleEmployeeDetails");
		
		try {
			eligibleEmployeeAddressList = PlanNoticeDocumentChangeDao.getEligibleEmployeeDetails(contractNumber,eligibleEmployeeAddressList);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		}
		finally{
			if (logger.isDebugEnabled())
				logger.debug("exit <- getEligibleEmployeeDetails");
		}	
		return eligibleEmployeeAddressList;
	}
		
	/**
	 * this method is used to get the PlanNoticeDocumentHistory
	 * 
	 * @param contractId
	 * @param documentId
	 * @return
	 * @throws RemoteException
	 */
	public PlanNoticeDocumentChangeHistoryVO getCustomPlanNoticeDocumentHistory(Integer contractId, Integer documentId)
			throws RemoteException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> getCustomPlanNoticeDocumentHistory");
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO;
		try {
			planNoticeDocumentChangeHistoryVO = PlanNoticeDocumentDAO.getCustomPlanNoticeDocumentHistory(contractId,
					documentId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, "");
			throw ExceptionHandlerUtility.wrap(se);
		} catch (ReportServiceException e) {
			SystemException se = new SystemException(e,"");
			throw ExceptionHandlerUtility.wrap(se);
		} finally {
			if (logger.isDebugEnabled())
				logger.debug("exit <- getCustomPlanNoticeDocumentHistory");
		}
		return planNoticeDocumentChangeHistoryVO;
	}
}
