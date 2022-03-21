package com.manulife.pension.platform.web.delegate;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.ps.service.notice.PlanNoticeDocumentService;
import com.manulife.pension.ps.service.notice.PlanNoticeDocumentServiceHome;
import com.manulife.pension.ps.service.report.notice.valueobject.EmployeeEligibleVO;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;

/**
 * @author krishta
 *This method used to invoke the serviceBean call 
 */
public class PlanNoticeDocumentServiceDelegate extends
		PsAbstractServiceDelegate {

	private static PlanNoticeDocumentServiceDelegate instance = new PlanNoticeDocumentServiceDelegate();
	/**
	 * 
	 */
	public PlanNoticeDocumentServiceDelegate() {
		// TODO Auto-generated constructor stub
	}

	public static PlanNoticeDocumentServiceDelegate getInstance()
    {
    	return instance;
    }
	/**
	 * @see AbstractServiceDelegate#getHomeClassName()
	 */
	protected String getHomeClassName() {
		return PlanNoticeDocumentServiceHome.class.getName();
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.service.delegate.AbstractServiceDelegate#create()
	 */
	@Override
	protected EJBObject create() throws SystemException, RemoteException,
			CreateException {
		return ((PlanNoticeDocumentServiceHome)getHome()).create();
	}


	/**
	 * This method used to add the custom plan document into DB
	 * @param planNoticeDocumentDetail
	 * @return int
	 * @throws SystemException 
	 */
	public int addCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) throws SystemException {

		Integer count = 0;
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			count = service.addCustomPlanNoticeDocument(planNoticeDocumentDetail);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return count;
	}

	/**
	 * To Get the max DocumentId
	 * @param planNoticeDocumentDetail
	 * @return int
	 * @throws SystemException 
	 */
	public int getMaxDocumentId() throws SystemException {

		Integer count = 0;
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			count = service.getMaxDocumentId();
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return count;
	}

	/**
	 * To Get the max Document Order  
	 * @param planNoticeDocumentDetail
	 * @return int
	 * @throws SystemException 
	 */
	public int getMaxDocumentOrder() throws SystemException {

		Integer count = 0;
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			count = service.getMaxDocumentOrder();
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return count;
	}
	
	/**
	 *  To Check the PlanNotice documentName whether exist or not
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SystemException 
	 */
	public boolean isNoticeDocumentNameExists(String fileName,Integer ContractId) throws SystemException {
		
		Boolean check = false;
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			 check = service.isNoticeDocumentNameExists(fileName,ContractId);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return check;
	}
	
	/**
	 * Update the Terms and Conditions Acceptance indicator
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SystemException 
	 */
	public boolean updateUsersTermConditionAcceptance(BigDecimal ProfileId, boolean acceptanceInd) throws SystemException {
		
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
		boolean status = false;
		try
		{
			status =  service.updateUsersTermConditionAcceptance(ProfileId,acceptanceInd);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}
		
		return status;
	}
	/** 
	 * To Apply the custom sort Document order
	 * @param List<PlanNoticeDocumentVO>
	 * @return void
	 * @throws SystemException 
	 */
	public void applyCustomPlanNoticeDocumentSortOrder(List<PlanNoticeDocumentVO> planNoticeDocumentDetails) throws SystemException {

		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			service.applyCustomPlanNoticeDocumentSortOrder(planNoticeDocumentDetails);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

	}
	
	/**
	 * To get the edit custom plan notice details
	 * @param PlanNoticeDocumentVO
	 * @return boolean
	 * @throws SystemException 
	 * @throws SystemException 
	 */
	public boolean editCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail)throws RemoteException, SystemException{

	PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
		boolean status = false;
		try
		{
			status =  service.editCustomPlanNoticeDocument(planNoticeDocumentDetail);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}
		return status;
	}
	/**
	 * Delete the plan Notice document by updating the soft delete indicator
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SystemException 
	 */
	public boolean deleteCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) throws SystemException {

		boolean flag = false;
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			flag = service.deleteCustomPlanNoticeDocument(planNoticeDocumentDetail);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return flag;
	}
	
	/**
	 * This method is used to check the  PlanNoticeDocumentSoftDeleteIndicator value
	 * @param planNoticeDocumentVO
	 * @return PlanNoticeDocumentVO
	 * @throws SystemException 
	 */
	public PlanNoticeDocumentVO checkPlanNoticeDocumentSoftDeleteIndicator(PlanNoticeDocumentVO planNoticeDocumentVO) throws SystemException {

		PlanNoticeDocumentVO planNoticeDocument = new PlanNoticeDocumentVO();
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			planNoticeDocument = service.checkPlanNoticeDocumentSoftDeleteIndicator(planNoticeDocumentVO);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return planNoticeDocument;
	}
	
	/**
	 * This method will insert record into DB
	 * @param PlanNoticeDocumentChangeHistoryVO
	 * @return boolean
	 * @throws SystemException 
	 */
	public boolean insertCustomPlanNoticeDocumentLogs(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws SystemException {

		boolean flag = false;
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			flag = service.insertCustomPlanNoticeDocumentLogs(planNoticeDocumentChangeHistoryVO);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return flag;
	}

	/**
	 * Retrieve the Plan Notice document information
	 * @param PlanNoticeDocumentChangeHistoryVO
	 * @return PlanNoticeDocumentVO
	 * @throws SystemException 
	 */
	public PlanNoticeDocumentVO retreivePlanNoticeLockInfo(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws SystemException {

		PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			planNoticeDocumentVO = service.retreivePlanNoticeLockInfo(planNoticeDocumentChangeHistoryVO);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return planNoticeDocumentVO;
	}

	/**
	 * To release the lock
	 * @param PlanNoticeDocumentChangeHistoryVO
	 * @return boolean
	 * @throws SystemException 
	 */
	public boolean releaseCustomPlanNoticeDocumentLock(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws SystemException {

		boolean flag = false;
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			flag = service.releaseCustomPlanNoticeDocumentLock(planNoticeDocumentChangeHistoryVO);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return flag;
	}

	/**
	 * Get custom plan notice information
	 * @param PlanNoticeDocumentChangeHistoryVO
	 * @return boolean
	 * @throws SystemException 
	 */
	public PlanNoticeDocumentVO getCustomPlanNoticeDocumentInfo(Integer documentId) throws SystemException {

		PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			planNoticeDocumentVO = service.getCustomPlanNoticeDocumentInfo(documentId);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return planNoticeDocumentVO;
	}
	
	/**
	 * To get the userNotice preferences
	 * @param profileId
	 * @param contractId
	 * @return List<UserNoticeManagerAlertVO>
	 * @throws SystemException
	 */
	public List<UserNoticeManagerAlertVO> getUserNoticePreferences(BigDecimal profileId, Integer contractId) throws SystemException {

		List<UserNoticeManagerAlertVO> userNoticePreference = null;
		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

		try
		{
			userNoticePreference = service.getUserNoticePreferences(profileId,contractId);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return userNoticePreference;
	}
	
	/**
	 * To get the plan noticePreferences count
	 * @param userNoticePreference
	 * @return
	 * @throws SystemException
	 */
	public int addUserNoticePreferences(List<UserNoticeManagerAlertVO> userNoticePreference) throws SystemException {

		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
		Integer userNoticeInsertCount = 0;
		try
		{
			userNoticeInsertCount = service.addUserNoticePreferences(userNoticePreference);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return userNoticeInsertCount;
	}
	
	/**
	 * To get the alert Frequency
	 * @param ContractId
	 * @return
	 * @throws SystemException
	 */
	public List<LookupDescription> getUserManagerAlertFrequencyCodes(Integer ContractId) throws SystemException {

		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
		List<LookupDescription> alertFrequencyCodes = null;
		try
		{
			alertFrequencyCodes = service.getUserManagerAlertFrequencyCodes(ContractId);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return alertFrequencyCodes;
	}
	
	/**
	 * To get the UserManager alertTiming
	 * @param ContractId
	 * @return
	 * @throws SystemException
	 */
	public List<LookupDescription> getUserManagerAlertTimingCodes(Integer ContractId) throws SystemException {

		PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
		List<LookupDescription> alertTimingCodes = null;
		try
		{
			alertTimingCodes = service.getUserManagerAlertTimingCodes(ContractId);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "addCustomPlanNoticeDocument");
		}

		return alertTimingCodes;
	}
	
	/**
	 * To get the TermsAndAcceptanceInd 
	 * @param profileId
	 * @return
	 * @throws SystemException
	 */
	 
	 public String getTermsAndAcceptanceInd(BigDecimal profileId) throws SystemException{
		 String termsAndAcceptance = null;
		 PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
		 try
			{
			termsAndAcceptance = service.getTermsAndAcceptanceInd(profileId);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "CheckUserTermsAndAcceptance");
			}
		return termsAndAcceptance;
	    }

	 /**
	  * To get the DocumentPostedUserName
	  * @param contractId
	  * @param documentId
	  * @return
	  * @throws SystemException
	  */
	 public  PlanNoticeDocumentVO getDocumentPostedUsername(int contractId,int documentId) throws SystemException{
		 PlanNoticeDocumentVO planNoticeDocumentVO = null;
		 PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
				planNoticeDocumentVO = service.getDocumentPostedUsername(contractId,documentId);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "uploadAndSharePlanNoticeDocument");
			}

			return planNoticeDocumentVO;
		}
		 

	 /**
		 * check if AlertName Exists
		 * @param alertName
		 * @return
		 * @throws SystemException
		 */
	 @SuppressWarnings("deprecation")
		public boolean checkAlertNameExists(String alertName, Integer alertId, Integer contractId) throws SystemException {

			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			boolean aleartNameExists = false;
			try
			{
				aleartNameExists = service.checkAlertNameExists(alertName, alertId, contractId);
				}
				catch(RemoteException e)
				{
					handleRemoteException(e, "checkAlertNameExists");
				}

			return aleartNameExists;
		}
	 
	 /**
		 * delete the particular alert
		 * @param alertId
		 * @return
		 * @throws SystemException
		 */
		@SuppressWarnings("deprecation")
		public boolean deleteAlert(Integer alertId) throws SystemException {

			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			boolean deleted = false;
			try
			{
				deleted = service.deleteAlert(alertId);
				}
				catch(RemoteException e)
				{
					handleRemoteException(e, "deleteAlert");
				}

			return deleted;
		}
		/**
		 * log the user action information
		 * @param profileId
		 * @param WebPageTypeCode
		 * @return
		 * @throws SystemException
		 */
		@SuppressWarnings("deprecation")
		public boolean userActionLog(Integer contractId,BigDecimal profileId,String userAction) throws SystemException {

			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			boolean logged = false;
			try
			{
				logged = service.userActionLog(contractId,profileId,userAction);
				}
				catch(RemoteException e)
				{
					handleRemoteException(e, "userActionLog");
				}

			return logged;
		}
		
		 /**
		 * Get the existing notice manager alert details
		 * @param alertId
		 * @return UserNoticeManagerAlertVO
		 * @throws SystemException
		 */
		@SuppressWarnings("deprecation")
		public UserNoticeManagerAlertVO getExistingAlertDetails(int alertId) throws SystemException {

			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			UserNoticeManagerAlertVO existingAlertVO  = null;
			try
			{
				existingAlertVO = service.getExistingAlertDetails(alertId);
				}
				catch(RemoteException e)
				{
					handleRemoteException(e, "getExistingAlertDetails");
				}

			return existingAlertVO;
		}
		
		/**
		 * To Update the CustomPlanNoticeDocumentOrder
		 * @param doumentID
		 * @return boolean
		 * @throws SystemException 
		 */
		public boolean updateCustomPlanNoticeDocumentOrder(List<Integer> documentOrderList) throws SystemException {

			boolean flag = false;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
				flag = service.updateCustomPlanNoticeDocumentOrder(documentOrderList);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "insertContractNoticeMailingOrder");
			}

			return flag;
		}
		
		/**
		 * Get the count of all users uploaded document
		 * @param contractId
		 * @return
		 * @throws RemoteException
		 */
		public  int getUserUploadedDocumentPostedCount(Integer contractId) throws SystemException{

			int uploadedCount = 0;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
				uploadedCount = service.getUserUploadedDocumentPostedCount(contractId);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "getUserUploadedDocumentPostedCount");
			}

			return uploadedCount;
		}

		/**
		 * Get the contract Document change type codes
		 * @return
		 * @throws SystemException
		 */
		public List<LookupDescription> getContractNoticeDocumentTypeCodes() throws SystemException{
			
			List<LookupDescription>  contractDocumentChangeTypeCodes  = null;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
				contractDocumentChangeTypeCodes = service.getContractNoticeDocumentTypeCodes();
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "getUserUploadedDocumentPostedCount");
			}

			return contractDocumentChangeTypeCodes;
		}
		

		/**
		 * To insert the ContractNoticeMailingOrder
		 * @param planNoticeMailingOrderVO
		 * @return boolean
		 * @throws SystemException 
		 */
		public boolean insertContractNoticeMailingOrder(PlanNoticeMailingOrderVO planNoticeMailingOrderVO) throws SystemException {

			boolean flag = false;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
				flag = service.insertContractNoticeMailingOrder(planNoticeMailingOrderVO);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "insertContractNoticeMailingOrder");
			}

			return flag;
		}

		/**
		 * To get the CustomDocumentPresence
		 * @param planNoticeMailingOrderVO
		 * @return boolean
		 * @throws SystemException 
		 */
		public boolean getCustomDocumentPresence(Integer documentId) throws SystemException {

			boolean flag = false;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
				flag = service.getCustomDocumentPresence(documentId);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "insertContractNoticeMailingOrder");
			}

			return flag;
		}
		/**
		 * update the SoftDelgateIndicator
		 * 
		 * @param planNoticeDocumentDetail
		 * @return
		 * @throws SystemException
		 */
		
		public boolean updateSoftDelgateIndicator(PlanNoticeDocumentVO planNoticeDocumentDetail) throws SystemException {

			boolean flag = false;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
			     flag = service.updateSoftDelgateIndicator(planNoticeDocumentDetail);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "editCustomPlanNoticeDocument");
			}

			return flag;
		}
		

		/**
		 * To update the ContractNoticeMailingOrderStatus
		 * @param planNoticeMailingOrderVO
		 * @return boolean
		 * @throws SystemException 
		 */
		public boolean updateContractNoticeMailingOrderStatus(PlanNoticeMailingOrderVO planNoticeMailingOrderVO) throws SystemException {

			boolean flag = false;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
				flag = service.updateContractNoticeMailingOrderStatus(planNoticeMailingOrderVO);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "insertContractNoticeMailingOrder");
			}

			return flag;
		}

		
		/**
		 * This method is used to check the  Custom notice document Count value
		 * @param contractId
		 * @return
		 * @throws SystemException
		 */
		public  int checkCustomNoticeDocumentCount(Integer contractId) throws SystemException{
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			int customNoticeCount = 0;
			try
			{
				customNoticeCount = service.checkCustomNoticeDocumentCount(contractId);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "checkCustomNoticeDocumentCount");
			}
			return customNoticeCount;
		}

		/**
		 * This method is used to check the  Custom notice document name change
		 * @param documentId
		 * @param documentName
		 * @return
		 * @throws SystemException
		 */
		public  boolean checkCustomNoticeDocumentNameChange(Integer documentId, String documentName) throws SystemException{
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			boolean customNoticeChanged = false;
			try
			{
				customNoticeChanged = service.checkCustomNoticeDocumentNameChange(documentId, documentName);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "checkCustomNoticeDocumentCount");
			}
			return customNoticeChanged;

			
		}
		
		/**
		 * This method is used to check the  Custom notice document name change
		 * @param documentId
		 * @param documentName
		 * @return
		 * @throws SystemException
		 */
		public  String  checkPlanNoticeSoftDeleteIndicator(int contractId,int documentId,String documentFileName) throws SystemException{
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			String  SofeDeleteIndValue = null;
			try
			{
				SofeDeleteIndValue = service.checkPlanNoticeSoftDeleteIndicator(contractId,documentId,documentFileName);
			}catch(RemoteException e)
			{
				handleRemoteException(e, "during retrive the  plan notice SofeDeleteIndValue");
			}
		    return SofeDeleteIndValue;
		}
		
		/**
		 * This method is used to check the  Custom notice document name change
		 * @param documentId
		 * @param documentName
		 * @return
		 * @throws SystemException
		 */
		public  PlanNoticeDocumentVO  getPlanNoticeDeletedUsername(int contractId,int documentId) throws SystemException{
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			PlanNoticeDocumentVO  planNoticeDocumentVO = null;
			try
			{
				planNoticeDocumentVO = service.getPlanNoticeDeletedUsername(contractId, documentId);
			}catch(RemoteException e)
			{
				handleRemoteException(e, "during retrive the  plan notice deleted Username");
			}
		    return planNoticeDocumentVO;
		}

		/**
		 * To get the ContractNotice Updated UserDetails
		 * @param documentId
		 * @param documentName
		 * @return
		 * @throws SystemException
		 */
		public  LinkedHashMap<BigDecimal,String> getContractNoticeUpdatedUserDetails(Integer contractId, java.sql.Timestamp fromDate, java.sql.Timestamp toDate) throws SystemException{
			LinkedHashMap<BigDecimal,String> noticeChangedUserDetails = new LinkedHashMap<BigDecimal,String>();
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			try {
					noticeChangedUserDetails = service.getContractNoticeUpdatedUserDetails(contractId, fromDate, toDate);
			}catch(RemoteException e)
			{
				handleRemoteException(e, "during retrive the  plan notice deleted Username");
			}
			return noticeChangedUserDetails;
		}
		
		/**
		 *  This method will update the order status records into Contract Notice Mailing Order 
		 * @param contractNoticeMailingOrderVO
		 * @return
		 * @throws SystemException
		 */
		public  boolean updateContractNoticeMailingOrder(PlanNoticeMailingOrderVO contractNoticeMailingOrderVO) throws Exception {
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			try {
				return service.updateContractNoticeMailingOrder(contractNoticeMailingOrderVO);
			}catch(RemoteException e)
			{
				handleRemoteException(e, "during update Contract Notice Mailing Order");
			}
			return false;
		}

		/**
		 * Get the contract notice mailing order details for the given orderNumber
		 * @param orderNumber
		 * @return
		 * @throws SystemException
		 * @throws ReportServiceException
		 */
		public PlanNoticeMailingOrderVO getContractNoticeMailingOrder(int orderNumber)  throws Exception{
			PlanNoticeMailingOrderVO planNoticeMailingOrderVO = null;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			try
			{
				planNoticeMailingOrderVO = service.getContractNoticeMailingOrder( orderNumber);
			}catch(RemoteException e)
			{
				handleRemoteException(e, "during retrive Contract Notice Mailing Order");
			}
			return planNoticeMailingOrderVO;
		}
		
		/**
		 * This method is used to get the  Custom notice document locked UserName 
		 * @param documentId
		 * @param documentName
		 * @return
		 * @throws SystemException
		 */
		public  PlanNoticeDocumentVO  getPlanNoticeLockedUserName(String componentKey,long lockUserProfileId) throws SystemException{
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();
			PlanNoticeDocumentVO  planNoticeDocumentVO = null;
			try
			{
				planNoticeDocumentVO = service.getPlanNoticeLockedUserName(componentKey,lockUserProfileId);
			}catch(RemoteException e)
			{
				handleRemoteException(e, "during retrive the  plan notice locked Username");
			}
		    return planNoticeDocumentVO;
		}
		
		/**
		 * this method is used to get the eligible employee list
		 * @param contractNumber
		 * @return
		 * @throws SystemException
		 */
		public List<EmployeeEligibleVO> getEligibleEmployeeDetails(int contractNumber) throws SystemException {

			List<EmployeeEligibleVO> eligibleEmployeeAddressList = null;
			PlanNoticeDocumentService service = (PlanNoticeDocumentService)getService();

			try
			{
				eligibleEmployeeAddressList = service.getEligibleEmployeeDetails(contractNumber,eligibleEmployeeAddressList);
			}
			catch(RemoteException e)
			{
				handleRemoteException(e, "getEligibleEmployeeDetails");
			}

			return eligibleEmployeeAddressList;
		}
		
		/**
		 * This method is used to get the plan notice document change history
		 * 
		 * @param contractId
		 * @param documentId
		 * @return
		 * @throws SystemException
		 */
		public PlanNoticeDocumentChangeHistoryVO getCustomPlanNoticeDocumentHistory(Integer contractId,
				Integer documentId) throws SystemException {
			PlanNoticeDocumentService service = (PlanNoticeDocumentService) getService();
			PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO = null;
			try {
				planNoticeDocumentChangeHistoryVO = service.getCustomPlanNoticeDocumentHistory(contractId, documentId);
			} catch (RemoteException e) {
				handleRemoteException(e, "during retrive the  plan notice document change history");
			}
			return planNoticeDocumentChangeHistoryVO;
		}
		
	 }


	 