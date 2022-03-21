package com.manulife.pension.ps.web.onlineloans;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.LoanDocumentServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.util.StringUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * Class used for displaying the risk information to UI.
 * 
 * @author gopavas
 *
 */
public class LoanAndWithdrawalDisplayHelper {

	public static final Logger logger = Logger.getLogger(LoanAndWithdrawalDisplayHelper.class);
	
	private static final String MMMM_DD_YYYY = "MMMM dd, yyyy";
	private static final String INTERNAL_USER = "UPI";
	private static final String TPA = "TPA";
	private static final String TPA_USER = "TPA User ";
	private static final String JOHN_HANCOCK_USER = "a John Hancock representative";
	private static final String COMMA = ", ";
	private static final String SPACE = " ";
	private static final String LOAN = "LOAN";
	private static final String WITHDRAWAL = "WITHDRAWAL";

	//CMA Keys 
	public static final int APPROVAL_ONE_CONTENT_KEY = 86849;

	public static final int DETAIL_ONE_A_CONTENT_KEY = 86853;
	public static final int DETAIL_ONE_B_CONTENT_KEY = 87451;
	public static final int DETAIL_TWO_CONTENT_KEY = 86854;
	public static final int DETAIL_THREE_CONTENT_KEY = 86855;
	public static final int DETAIL_FOUR_CONTENT_KEY = 86856;
	public static final int DETAIL_FIVE_CONTENT_KEY = 86857;
	public static final int DETAIL_SIX_CONTENT_KEY = 86858;

	/**
	 * This method used to get the data and prepare atRisk text to UI.
	 *
	 * @param atRiskDetils
	 * @return List
	 * @throws SystemException
	 */
	
	public static List<Object> atRiskDisplayText(AtRiskDetailsInputVO atRiskDetils, AtRiskDetailsVO atRiskDetailsVO) throws SystemException{

		List<Object> atRiskText = new ArrayList<Object>();


		if (atRiskDetils != null && atRiskDetils.isParticipantInitiated()){

			Map<Integer, Integer> content = new LinkedHashMap<Integer, Integer>();

			Map<Integer, String []> detailContent = new LinkedHashMap<Integer, String []>();

			boolean isDetailThreeIncluded = false;

			boolean isEmployeeAddressChanged = false;

			String [] substituteParamDetailOne = new String [4];
			String [] substituteParamdDetailTwo = new String [4];
			String [] substituteParamDetailThree = new String [4];
			String [] substituteParamDetailFour = new String [4];
			String [] substituteParamDetailFive = new String [4];
			String [] substituteParamDetailSix = new String [4];


			String typeCode = ManagedContent.AT_RISK_TEXT;

			try{
				
				AtRiskWebRegistrationVO webRegObj = atRiskDetailsVO.getWebRegistration();
				AtRiskAddressChangeVO riskAddresschangeVO = atRiskDetailsVO.getAddresschange();
				AtRiskForgetUserName atRiskForgetUserName = atRiskDetailsVO.getForgetUserName();
				AtRiskPasswordResetVO atRiskPasswordResetVO = atRiskDetailsVO.getPasswordReset();

				ArrayList<Integer> approvalContentKeys = new ArrayList<Integer>();

				if((webRegObj != null && webRegObj.isWebRegAtRiskPeriod()) || 
						(atRiskForgetUserName != null && atRiskForgetUserName.isAtRiskPeriod()
								&& atRiskPasswordResetVO != null && atRiskPasswordResetVO.isAtRiskPeriod())){ 

					approvalContentKeys.add(APPROVAL_ONE_CONTENT_KEY);

					if(webRegObj != null && webRegObj.getWebRegistrationDate() != null){
						
						Address employeeLatestAddress = null;
						if(riskAddresschangeVO != null)
							employeeLatestAddress = riskAddresschangeVO.getApprovalAddress();
						
						Address webRegistrationEmpAddress = webRegObj.getAddress();

						isEmployeeAddressChanged = isObjectsEquals(webRegistrationEmpAddress, employeeLatestAddress);

						if(!isEmployeeAddressChanged){
							isDetailThreeIncluded = true;
						}

						SimpleDateFormat formatter = new SimpleDateFormat(MMMM_DD_YYYY);
						String participantWebRegisteredDate = formatter.format(webRegObj.getWebRegistrationDate());

						String empRegistrationAddress = getEmpRegistrationAddress(webRegObj, atRiskDetils);
						if(webRegObj.isWebConfirmationLetterAvailable()){
							substituteParamDetailOne [0] = participantWebRegisteredDate;
							substituteParamDetailOne [1]  = empRegistrationAddress;
							detailContent.put(DETAIL_ONE_A_CONTENT_KEY, substituteParamDetailOne);
						}else{
							substituteParamDetailOne [0] = participantWebRegisteredDate;
							detailContent.put(DETAIL_ONE_B_CONTENT_KEY, substituteParamDetailOne);
							
						}

						if(INTERNAL_USER.equals(webRegObj.getConfirmUpdatedUserIdType())
								||TPA.equals(webRegObj.getConfirmUpdatedUserIdType())){

							String regAddressUpdatedBy = registrationAddressUpdatedBy(webRegObj, atRiskDetils);
							substituteParamdDetailTwo[0] = regAddressUpdatedBy;
							detailContent.put(DETAIL_TWO_CONTENT_KEY, substituteParamdDetailTwo);
						}	

						if(isDetailThreeIncluded){

							String empLatestAddress = getEmpLatestAddress(employeeLatestAddress, atRiskDetils);

							substituteParamDetailThree [0] = empLatestAddress;
							detailContent.put(DETAIL_THREE_CONTENT_KEY, substituteParamDetailThree);
						}

						if(isDetailThreeIncluded && 
								(INTERNAL_USER.equals(riskAddresschangeVO.getApprovalUpdatedUserIdType())
										||TPA.equals(riskAddresschangeVO.getApprovalUpdatedUserIdType()))){


							String regAddressUpdatedBy = approvalAddressUpdatedBy(riskAddresschangeVO, atRiskDetils);
							substituteParamDetailFour [0] = regAddressUpdatedBy;

							detailContent.put(DETAIL_FOUR_CONTENT_KEY, substituteParamDetailFour); 

						}
					}
					if(atRiskForgetUserName != null && atRiskForgetUserName.isAtRiskPeriod() && 
							atRiskPasswordResetVO != null && atRiskPasswordResetVO.isAtRiskPeriod()){

						SimpleDateFormat simpleFormat = new SimpleDateFormat(MMMM_DD_YYYY);

						String emailPasswordResetDate = null;
						if(atRiskPasswordResetVO.getEmailPasswordResetDate() != null)
							emailPasswordResetDate = simpleFormat.format(atRiskPasswordResetVO.getEmailPasswordResetDate());

						substituteParamDetailFive [0] = emailPasswordResetDate;

						String mailAddress = null;
						if(atRiskPasswordResetVO.getEmailPasswordResetEmailAddress()!= null)
							mailAddress = atRiskPasswordResetVO.getEmailPasswordResetEmailAddress();

						substituteParamDetailFive [1] = mailAddress;

						String userName = null;
						if(atRiskPasswordResetVO.getEmailPasswordResetInitiatedUserFirstName() != null 
								&& atRiskPasswordResetVO.getEmailPasswordResetInitiatedUserLastName() != null){

							userName = atRiskPasswordResetVO.getEmailPasswordResetInitiatedUserFirstName()+SPACE+
							atRiskPasswordResetVO.getEmailPasswordResetInitiatedUserLastName();
						}

						substituteParamDetailFive [2] = userName;

						String passwordUpdatedBy = passwordResetBy(atRiskPasswordResetVO, atRiskDetils);

						substituteParamDetailFive [3] = passwordUpdatedBy;

						SimpleDateFormat requestDateFormat = new SimpleDateFormat(MMMM_DD_YYYY);


						String requestDate = null;
						if(atRiskForgetUserName.getForgotPasswordRequestedDate() != null)
							requestDate = requestDateFormat.format(atRiskForgetUserName.getForgotPasswordRequestedDate());

						substituteParamDetailSix [0] = requestDate;

						detailContent.put(DETAIL_FIVE_CONTENT_KEY, substituteParamDetailFive); 
						detailContent.put(DETAIL_SIX_CONTENT_KEY, substituteParamDetailSix); 
					}
				} 


				if(LOAN.equals(atRiskDetils.getLoanOrWithdrawalReq().name())){

					LoanDocumentServiceDelegate loanService = LoanDocumentServiceDelegate.getInstance();

					List<ManagedContent> contents = LoanServiceDelegate.getInstance().getManagedContent(atRiskDetils.getSubmissionId(),
							atRiskDetils.getContractId(), atRiskDetils.getProfileId());

					Map<Integer,Integer> managedContent = new HashMap<Integer,Integer>();
					for (ManagedContent atRiskContent : contents) {
						if (typeCode.equals(atRiskContent.getContentTypeCode())) {
							managedContent.put(atRiskContent.getContentKey(),atRiskContent.getContentId());
						}
					}

					ArrayList<String> approvalText = new ArrayList<String>();
					StringBuffer approvalBuff = new StringBuffer();

					for(Integer  contentKey :approvalContentKeys){

						Integer ContentId = getContentIdByKey(contentKey, managedContent,loanService, atRiskDetils);
						approvalBuff.append(getAtRiskText(ContentId, loanService, atRiskDetils));
					}

					approvalText.add(approvalBuff.toString());

					ArrayList<String> detailText = new ArrayList<String>();

					for (Map.Entry<Integer, String []> contentValue : detailContent.entrySet()) {

						Integer ContentId = null;
						ContentId = getContentIdByKey(contentValue.getKey(), managedContent,loanService, atRiskDetils);

						detailText.add(getAtRiskDetailText(getAtRiskText(ContentId, loanService, atRiskDetils), contentValue.getValue()));

						content.put(contentValue.getKey(), ContentId);
					}

					atRiskText.add(approvalText);
					atRiskText.add(detailText);
					atRiskText.add(content);	


				}else if(WITHDRAWAL.equals(atRiskDetils.getLoanOrWithdrawalReq().name())){

					ArrayList<String> approvalText = new ArrayList<String>();
					ArrayList<String> detailText = new ArrayList<String>();
					StringBuffer approvalBuff = new StringBuffer();

					for(Integer  contentKey :approvalContentKeys){
						approvalBuff.append(getContentById(contentKey));
					}

					approvalText.add(approvalBuff.toString());

					for (Map.Entry<Integer, String []> contentValue : detailContent.entrySet()) {

						String detailAtRiskText = null;
						detailAtRiskText = getContentById(contentValue.getKey());

						detailText.add(getAtRiskDetailText(detailAtRiskText,contentValue.getValue()));
					}
					atRiskText.add(approvalText);
					atRiskText.add(detailText);
				}

			}catch(SystemException e){
				throw new SystemException(e,"Unexpected exception in LoanAndWithdrawalDisplayHelper." +
						"atRiskDisplayText() for ::::: "+atRiskDetils.toString() +e.getMessage());
			}
		}
		return atRiskText;

	}	
	/**
	 * This method used to get the content from CMA
	 * 
	 * @param contentId
	 * @return String
	 * @throws SystemException
	 */
	private static String getContentById(Integer contentId) throws SystemException{

		Miscellaneous Message = null;
		try{
			Message = (Miscellaneous)ContentCacheManager.getInstance().getContentById(
					contentId, ContentTypeManager.instance().MISCELLANEOUS);

		} catch (ContentException exception) {
			throw new SystemException(exception.getMessage());
		}

		if (Message == null)
			throw new SystemException("LoanAndWithdrawalDisplayHelper.atRiskDisplayText ::: Content is not found for the CMA key# "+contentId);

		return Message.getText();
	}

	/**
	 * This method used to compare two objects. 
	 * 
	 * @param empLatestAddress
	 * @param webRegEmpAddress
	 * @return boolean
	 */
	private static boolean isObjectsEquals(Address empLatestAddress, Address webRegistrationEmpAddress){

		boolean objectsEquals = false;
		
		if(empLatestAddress != null && webRegistrationEmpAddress != null){
			if(StringUtils.equalsIgnoreCase(empLatestAddress.getAddressLine1(), webRegistrationEmpAddress.getAddressLine1()))
				if(StringUtils.equalsIgnoreCase(empLatestAddress.getAddressLine2(), webRegistrationEmpAddress.getAddressLine2()))
					if(StringUtils.equalsIgnoreCase(empLatestAddress.getCity(), webRegistrationEmpAddress.getCity()))
						if(StringUtils.equalsIgnoreCase(empLatestAddress.getStateCode(), webRegistrationEmpAddress.getStateCode()))
							if(StringUtils.equalsIgnoreCase(empLatestAddress.getZipCode(), webRegistrationEmpAddress.getZipCode()))
								if(StringUtils.equalsIgnoreCase(empLatestAddress.getCountryCode(), webRegistrationEmpAddress.getCountryCode()))
									objectsEquals = true;
		}

		return objectsEquals;
	}

	/**
	 * This method used to get the registration Address of employee.
	 * @param webRegObj
	 * @return String
	 * @throws SystemException 
	 */
	private static String getEmpRegistrationAddress(AtRiskWebRegistrationVO webRegObj, AtRiskDetailsInputVO atRiskDetils) throws SystemException{

		if(webRegObj.getAddress() == null) return null;
		
		StringBuffer address = new StringBuffer();
		if(webRegObj.getAddress() != null){

			if(webRegObj.getAddress().getAddressLine1() != null) 
				address.append(webRegObj.getAddress().getAddressLine1().trim());

			if(webRegObj.getAddress().getAddressLine2() != null) 
				address.append(SPACE+webRegObj.getAddress().getAddressLine2().trim());

			if(webRegObj.getAddress().getCity() != null) 
				address.append(COMMA+webRegObj.getAddress().getCity().trim());

			if(webRegObj.getAddress().getStateCode() != null) 
				address.append(COMMA+webRegObj.getAddress().getStateCode().trim());

			if(webRegObj.getAddress().getZipCode() != null) 
				address.append(SPACE+webRegObj.getAddress().getZipCode().trim());

			if (webRegObj.getAddress().getCountryCode() != null) 
				address.append(SPACE+webRegObj.getAddress().getCountryCode().trim());

		}

		
		if (StringUtils.isEmpty(address.toString()))
			throw new SystemException("employee registration address is not fount for : "+atRiskDetils.toString());
		 
		return address.toString();
	}
	
	/**
	 * This method used to get the latest address of employee.
	 * 
	 * @param webRegObj
	 * @return String
	 * @throws SystemException 
	 */
	private static String getEmpLatestAddress(Address latestAddress, AtRiskDetailsInputVO atRiskDetils) throws SystemException{

		StringBuffer address = new StringBuffer();
		if(latestAddress != null){

			if(latestAddress.getAddressLine1() != null) 
				address.append(latestAddress.getAddressLine1().trim());

			if(latestAddress.getAddressLine2() != null) 
				address.append(SPACE+latestAddress.getAddressLine2().trim());

			if(latestAddress.getCity() != null) 
				address.append(COMMA+latestAddress.getCity().trim());

			if(latestAddress.getStateCode() != null) 
				address.append(COMMA+latestAddress.getStateCode().trim());

			if(latestAddress.getZipCode() != null) 
				address.append(SPACE+latestAddress.getZipCode().trim());

			if (latestAddress.getCountryCode() != null) 
				address.append(SPACE+latestAddress.getCountryCode().trim());

		}

		if (StringUtils.isEmpty(address.toString()))
			throw new SystemException("employee latest address is not fount for : "+atRiskDetils.toString());

		return address.toString();
	}

	/**
	 * This method used to get address Changed information.
	 * 
	 * @param riskAddressChange
	 * @param atRiskDetils
	 * @return String
	 * @throws SystemException
	 */
	private static String approvalAddressUpdatedBy(AtRiskAddressChangeVO riskAddressChange, AtRiskDetailsInputVO atRiskDetils) throws SystemException{

		String addressChangedBy = null;
		if (riskAddressChange != null && INTERNAL_USER.equals(riskAddressChange.getApprovalUpdatedUserIdType())) {
			addressChangedBy = JOHN_HANCOCK_USER;
		} else if(riskAddressChange != null && TPA.equals(riskAddressChange.getApprovalUpdatedUserIdType())){
			if(riskAddressChange.getCreatedUserFistName() != null && riskAddressChange.getCreatedUserLastName() != null)
				addressChangedBy = TPA_USER+riskAddressChange.getCreatedUserFistName().trim()+SPACE+riskAddressChange.getCreatedUserLastName().trim();
		}

		if (addressChangedBy == null)
			throw new SystemException("Approval address updated by content is not found for : "+atRiskDetils.toString());

		return addressChangedBy;
	}
	
	/**
	 * This method used to get registration address updated information.
	 * @param webRegVo
	 * @param atRiskDetils
	 * @return
	 * @throws SystemException
	 */
	private static String registrationAddressUpdatedBy(AtRiskWebRegistrationVO webRegVo, AtRiskDetailsInputVO atRiskDetils) throws SystemException{

		String addressUpdatedBy = null;

		if(INTERNAL_USER.equals(webRegVo.getConfirmUpdatedUserIdType())){
			addressUpdatedBy = JOHN_HANCOCK_USER;
		}else if(TPA.equals(webRegVo.getConfirmUpdatedUserIdType())){

			if(webRegVo.getConfirmUpdatedUserFirstName() != null 
					&& webRegVo.getConfirmUpdatedUserLastName() != null)
				addressUpdatedBy = TPA_USER+webRegVo.getConfirmUpdatedUserFirstName()+SPACE+webRegVo.getConfirmUpdatedUserLastName().trim(); 
		}

		if (addressUpdatedBy == null)
			throw new SystemException("Confirmation mail updated  content is not found for : "+atRiskDetils.toString());

		return addressUpdatedBy;
	}
	
	/**
	 * This method used to get the information of password reset
	 * 
	 * @param atRiskPasswordReset
	 * @param userName
	 * @param atRiskDetils
	 * @return String
	 * @throws SystemException
	 */
	private static String passwordResetBy(AtRiskPasswordResetVO atRiskPasswordReset, AtRiskDetailsInputVO atRiskDetils) throws SystemException{

		String passwordResetBy = null;
		if (INTERNAL_USER.equals(atRiskPasswordReset.getEmailAddressLastUpdatedUserIdType())) {
			passwordResetBy = JOHN_HANCOCK_USER;
		} else if(atRiskPasswordReset.getEmailAddressLastUpdatedUserFirstName() != null 
					&& atRiskPasswordReset.getEmailAddressLastUpdatedUserLastName() != null ){

			passwordResetBy = atRiskPasswordReset.getEmailAddressLastUpdatedUserFirstName().trim()+SPACE+
			atRiskPasswordReset.getEmailAddressLastUpdatedUserLastName().trim();
		}
		if (passwordResetBy == null) {
			EmployeeDetailVO employeeDetail;
			EmployeeServiceDelegate employeeService = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
			try {
				employeeDetail = employeeService.getEmployeeLastUpdatedInfo(atRiskDetils.getProfileId().longValue(), atRiskDetils.getContractId());
				employeeDetail.getLastUpdatedUserId();
				employeeDetail.getLastUpdatedUserIdType();
				passwordResetBy = getSpecifiedLastUpdatedName(employeeDetail.getLastUpdatedUserId(), employeeDetail.getLastUpdatedUserIdType());
			} catch (RemoteException e) {
				throw new SystemException("Remote Exception found for : "+atRiskDetils.toString());
			}
		}
		//if (passwordResetBy == null)
		//	throw new SystemException("Password reset content is not found for : "+atRiskDetils.toString());

		return passwordResetBy;
	}
	
	private static String getSpecifiedLastUpdatedName(String lastUpdatedUserId,
			String lastUpdatedUserIdType) {
		if (lastUpdatedUserIdType != null && lastUpdatedUserIdType.equalsIgnoreCase("SYS")) {
			return "John Hancock System";
		}
		if (lastUpdatedUserIdType != null && (lastUpdatedUserIdType.equalsIgnoreCase("CAR") ||
				lastUpdatedUserIdType.equalsIgnoreCase("PAR") ||
				lastUpdatedUserIdType.equalsIgnoreCase("UPI"))) {
			return "John Hancock Representative";
		}
		if (lastUpdatedUserIdType != null && lastUpdatedUserIdType.equalsIgnoreCase("PAY")) {
			return "Payroll Company";
		}
		logger.error("getSpecifiedLastUpdatedName failed. lastUpdatedUserId = " + lastUpdatedUserId + ", lastUpdatedUserIdType = " + lastUpdatedUserIdType);
		return "Null";
	}
	private static Integer getContentIdByKey(Integer contentKey, Map<Integer, Integer> managedContent, 
			LoanDocumentServiceDelegate loanService, AtRiskDetailsInputVO atRiskDetils ) throws SystemException{

		Integer contentId= null;

		try {
			
			if(managedContent.size() > 0){
				if(managedContent.get(contentKey) != null){
					contentId = managedContent.get(contentKey);
				}

			}

			if(contentId == null)
				contentId = getContentId(contentKey,loanService);


		} catch (SystemException e) {
			throw new SystemException(e," Unexpected exception in LoanAndWithdrawalDisplayHelper." +
					"atRiskDisplayText() for "+atRiskDetils.toString());
		}

		return contentId;
	}
	
	
	private static Integer getContentId(Integer contentKey, LoanDocumentServiceDelegate loanDocumentServiceDelegate)
			throws SystemException {
		
		Integer contentId = null;
		if (contentKey != null) {
			ContentText contentHtml = loanDocumentServiceDelegate
					.getContentTextByKey(contentKey);
            if (contentHtml != null) {
            	contentId = contentHtml.getId();
            } else {
                throw new SystemException("Unexpected null returned for "
                        + "contentHtml for contentKey = "
                        + contentKey);
            }
            
            if(contentId == null)
            	throw new SystemException("Unexpected null returned for "
                        + "contentId for contentKey = "
                        + contentKey);
		}else{
			 throw new SystemException("Unexpected null returned for "
                     + "contentKey = ");
		}
		
		return contentId;
	}
	
	
	private static String getAtRiskText(Integer contentId, 
			LoanDocumentServiceDelegate loanService, AtRiskDetailsInputVO atRiskDetils) throws SystemException{
		
		String atRiskMessage = null;
		try{
		
			ContentText contentText = loanService.getContentTextById(contentId);

			if (contentText != null) {
				atRiskMessage = contentText.getText();
			} else {
				throw new SystemException("Unexpected null returned for "
						+ "contentText for contentId = "
						+ contentId);
			}

		}catch (SystemException e) {
			throw new SystemException(e," Unexpected exception in LoanAndWithdrawalDisplayHelper." +
					"atRiskDisplayText() for "+atRiskDetils.toString());
		}
		
		return atRiskMessage;
	}
	
	
	private static String getAtRiskDetailText(String cmaContent, 
			String [] substituteParams) throws SystemException{
		
		return StringUtility.substituteParams(cmaContent, substituteParams [0], substituteParams [1], substituteParams [2], substituteParams [3]);
		
	}
	
}