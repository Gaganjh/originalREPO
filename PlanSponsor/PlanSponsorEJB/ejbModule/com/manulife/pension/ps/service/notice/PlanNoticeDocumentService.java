package com.manulife.pension.ps.service.notice;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.EJBObject;

import com.manulife.pension.ps.service.report.notice.valueobject.EmployeeEligibleVO;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;



/**
 * Remote interface for Enterprise Bean: PlanNoticeDocumentService
 */
public interface PlanNoticeDocumentService extends EJBObject {
	
	public int addCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail)  throws RemoteException;
	public int getMaxDocumentId()  throws RemoteException;
	public int getMaxDocumentOrder()  throws RemoteException;
	public boolean isNoticeDocumentNameExists(String fileName,Integer ContractId)  throws RemoteException;
	public boolean editCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail)throws RemoteException;
	public  void applyCustomPlanNoticeDocumentSortOrder(List<PlanNoticeDocumentVO> planNoticeDocumentDetails) throws RemoteException;
	public boolean updateUsersTermConditionAcceptance(BigDecimal ProfileId, boolean acceptanceInd)  throws RemoteException;
	public boolean deleteCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) throws RemoteException;
	public PlanNoticeDocumentVO checkPlanNoticeDocumentSoftDeleteIndicator(PlanNoticeDocumentVO planNoticeDocumentVO) throws RemoteException;
	public boolean insertCustomPlanNoticeDocumentLogs(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws RemoteException;
	public PlanNoticeDocumentVO retreivePlanNoticeLockInfo(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws RemoteException;
	public boolean releaseCustomPlanNoticeDocumentLock(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws RemoteException;
	public PlanNoticeDocumentVO getCustomPlanNoticeDocumentInfo(Integer documentId) throws RemoteException;
	public List<UserNoticeManagerAlertVO> getUserNoticePreferences(BigDecimal profileId, Integer contractId) throws RemoteException;
	public int addUserNoticePreferences(List<UserNoticeManagerAlertVO> userNoticePreference) throws RemoteException;
	public boolean checkAlertNameExists(String alertName, Integer alertId, Integer contractId) throws RemoteException;
	public boolean deleteAlert(Integer alertId) throws RemoteException;
	public boolean userActionLog(Integer contractId,BigDecimal profileId,String userAction) throws RemoteException;
	public UserNoticeManagerAlertVO getExistingAlertDetails(int alertId) throws RemoteException;
	public List<LookupDescription> getUserManagerAlertFrequencyCodes(Integer ContractId) throws RemoteException;
	public List<LookupDescription> getUserManagerAlertTimingCodes(Integer contractId) throws RemoteException;
	public String getTermsAndAcceptanceInd(BigDecimal profileId) throws RemoteException;
	public  PlanNoticeDocumentVO getDocumentPostedUsername(int contractId,int documentId) throws RemoteException;
	public boolean updateCustomPlanNoticeDocumentOrder(List<Integer> documentOrderList) throws RemoteException;
	public int getUserUploadedDocumentPostedCount(Integer contractId) throws RemoteException;
	public List<LookupDescription> getContractNoticeDocumentTypeCodes() throws RemoteException;
	public boolean insertContractNoticeMailingOrder(PlanNoticeMailingOrderVO planNoticeMailingOrderVO) throws RemoteException;
	public boolean getCustomDocumentPresence(Integer documentId) throws RemoteException;
	public boolean updateSoftDelgateIndicator(PlanNoticeDocumentVO planNoticeDocumentDetail)throws RemoteException;
	public boolean updateContractNoticeMailingOrderStatus(PlanNoticeMailingOrderVO planNoticeMailingOrderVO)throws RemoteException;
	public int checkCustomNoticeDocumentCount(Integer contractId) throws RemoteException;
	public boolean checkCustomNoticeDocumentNameChange(Integer documentId, String documentName)throws RemoteException;
	public String checkPlanNoticeSoftDeleteIndicator(int contractId,int documentId,String documentFileName) throws RemoteException;
	public PlanNoticeDocumentVO getPlanNoticeDeletedUsername(int contractId,int documentId) throws RemoteException;
	public LinkedHashMap<BigDecimal,String> getContractNoticeUpdatedUserDetails(Integer contractId, java.sql.Timestamp fromDate, java.sql.Timestamp toDate) throws RemoteException;
	public PlanNoticeDocumentVO getPlanNoticeLockedUserName(String componentKey,long lockUserProfileId) throws RemoteException;
	public boolean updateContractNoticeMailingOrder(PlanNoticeMailingOrderVO contractNoticeMailingOrderVO) throws RemoteException;
	public PlanNoticeMailingOrderVO getContractNoticeMailingOrder(int orderNumber) throws RemoteException;
	public List<EmployeeEligibleVO> getEligibleEmployeeDetails(int contractNumber,List<EmployeeEligibleVO> eligibleEmployeeAddressList) throws RemoteException;
	public PlanNoticeDocumentChangeHistoryVO getCustomPlanNoticeDocumentHistory(Integer contractId, Integer documentId) throws RemoteException;
}
