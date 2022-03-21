package com.manulife.pension.ps.web.census.beneficiary.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.CensusConstants;
import com.manulife.pension.ps.web.census.beneficiary.BeneficiaryForm;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignationData;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiarySet;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.ParticipantContractVO;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Util class to convert the employee object to Beneficiary form and set objects.
 * @author Puttaiah Arugunta
 *
 */
public class BeneficiaryUtil {

	private static TreeMap<String,String> relationship = new TreeMap<String,String>();
	private static Map<String, String> beneficiaryType = new HashMap<String, String>();
	
	private static final String CancelledParticipantStatus = "CN";
	/**
	 * Method to convert the employee information into beneficiary related objects.
	 * 
	 * @param beneficiaryForm
	 * @param beneficiarySet
	 * @param employee
	 * @throws SystemException 
	 * @throws NumberFormatException 
	 */
	public static void setEmployeeInformation(UserProfile userProfile,BeneficiaryForm beneficiaryForm, 
			BeneficiarySet beneficiarySet, Employee employee) throws NumberFormatException, SystemException {

		StringBuffer sourceLastUpdated = new StringBuffer();

		if(employee != null){
			beneficiaryForm.setEmployeeFirstName(employee
					.getEmployeeDetailVO().getFirstName());
			beneficiaryForm.setEmployeeLastName(employee
					.getEmployeeDetailVO().getLastName());
			beneficiaryForm.setEmployeeSSN(employee.getEmployeeDetailVO()
					.getSocialSecurityNumber());
		}

		if(beneficiarySet != null){
			if(beneficiarySet.getCreatedTs()!=null){
				beneficiaryForm.setEmployeelastUpdatedDate(beneficiarySet.getCreatedTs());
			}

			String createdUserIdType = beneficiarySet.getCreatedUserIdType();
			
			if (createdUserIdType != null) {
				if (BeneficiaryDesignationData.PW_USER_ID_TYPE.equalsIgnoreCase(createdUserIdType)
						&& BeneficiaryDesignationData.BM_SOURCE_FUNCTION_CODE
												.equalsIgnoreCase(beneficiarySet.getSourceFunctionCode()) ) {
					
					sourceLastUpdated.append(BeneficiaryDesignationData.PW_SOURCE_LAST_UPDATED_BY);
				} else if (userProfile.isInternalUser() && BeneficiaryDesignationData.PWI_USER_ID_TYPE.equals(createdUserIdType)) {
					UserInfo userInfo = SecurityServiceDelegate.getInstance().getDatabaseUserByProfileId(Long.valueOf(beneficiarySet.getCreatedUserId()));
					sourceLastUpdated.append(BeneficiaryDesignationData.PSWE_SOURCE_LAST_UPDATED_BY )
									 .append(Constants.COMMA_SPACE)
									 .append(BeneficiaryDesignationData.PSWI_SOURCE_LAST_UPDATED_BY)
									 .append(Constants.COMMA_SPACE)
									 .append(Constants.OPEN_ANGLE_BRACKET)
									 .append(userInfo.getLastName())
									 .append(Constants.COMMA_SPACE)
									 .append(userInfo.getFirstName())
									 .append(Constants.CLOSED_ANGLE_BRACKET);
				} else if(userProfile.getPrincipal().getRole().isExternalUser() && BeneficiaryDesignationData.PWI_USER_ID_TYPE.equals(createdUserIdType)){
					sourceLastUpdated.append(BeneficiaryDesignationData.PSWE_SOURCE_LAST_UPDATED_BY)
									 .append(Constants.COMMA_SPACE)
									 .append(BeneficiaryDesignationData.PSWI_SOURCE_LAST_UPDATED_BY);
					
				}else if (BeneficiaryDesignationData.PWE_USER_ID_TYPE.equals(createdUserIdType)) {
					sourceLastUpdated.append(BeneficiaryDesignationData.PSWE_SOURCE_LAST_UPDATED_BY);
				} else if (BeneficiaryDesignationData.OEE_SOURCE_FUNCTION_CODE.equals(beneficiarySet
						.getSourceFunctionCode())
						|| BeneficiaryDesignationData.IEC_SOURCE_FUNCTION_CODE.equals(beneficiarySet
								.getSourceFunctionCode())) {
					sourceLastUpdated.append(BeneficiaryDesignationData.OEE_SOURCE_LAST_UPDATED_BY);
				}
			}
			
			beneficiaryForm.setEmployeelastUpdatedBy(sourceLastUpdated.toString());
		}else{
			beneficiaryForm.setEmployeelastUpdatedBy(null);
			beneficiaryForm.setEmployeelastUpdatedDate(null);
		}
	}

	/**
	 * Get the Beneficiary relationship and Beneficiary Type
	 * @param beneficiaryForm
	 * @throws SystemException
	 */
	public static void getBeneficiaryRelationshipAndType(
			BeneficiaryForm beneficiaryForm) throws SystemException {
		//Invoke environment Service either relationship or beneficiaryType is empty
		EnvironmentServiceDelegate environmentService = EnvironmentServiceDelegate.getInstance();
		if( relationship.isEmpty()){
			
			relationship = (TreeMap<String, String>) environmentService.getRelationshipToParticipant();
			
			if(relationship != null && !relationship.isEmpty()){
				beneficiaryForm.setRelationship(relationship);	
			}
		}
		if (beneficiaryType.isEmpty()) {

			beneficiaryType = environmentService.getBeneficiaryType();
			if (beneficiaryType != null && !beneficiaryType.isEmpty()) {
				beneficiaryForm.setBeneficiaryTypeMap(beneficiaryType);
			}
		}
	}
	
	 /**
	    * Method will return true if OBD CSF is Y otherwise false
	    * 
	    * @param contractNumber
	    * @return boolean
	    * @throws ServletException
	    */ 
	    public static boolean isOBDSCSFAvailable(String contractNumber)
						throws ServletException {
	    	
	    	try {
	    		ContractServiceFeature obdCsf = ContractServiceDelegate.getInstance()
	    			.getContractServiceFeature(Integer.parseInt(contractNumber),
	    					ServiceFeatureConstants.ONLINE_BENEFICIARY_DESIGNATION);

	    		if (obdCsf != null) {
	    			if(ServiceFeatureConstants.YES.equals(obdCsf.getValue())){
	    				return true;
	    			}
	    		}
	    	} catch (SystemException e) {
	    		throw new ServletException(
	    				"Error processing in isOBDSCSFAvailable method for Contract Id = " 
	    				+ contractNumber + ". Exception::"	+ e.toString());
	    	} catch (NumberFormatException e) {
	    		throw new NumberFormatException(
	    				"Error processing in isOBDSCSFAvailable method for Contract Id = " 
	    				+ contractNumber + ". Exception::"	+ e.toString());
	    	} catch (ApplicationException e) {
	    		throw new ServletException(
	    				"Error processing in isOBDSCSFAvailable method for Contract Id = " 
	    				+ contractNumber +". Exception::"	+ e.toString());
	    	}
	    	return false;
	    }
	    
	    /**
		 * Do not allow external user to view/edit for employee has C employment
		 * status
		 * 
		 * @param employee
		 * @param user
		 * @return
		 */
		public static boolean isAccessibleForEmployee(Employee employee,
				UserProfile user) {
			ParticipantContractVO participant = employee.getParticipantContract();
			if (participant != null
					&& CancelledParticipantStatus.equals(participant
							.getParticipantStatusCode())) {
				return false;
			}
			if (employee.getEmployeeDetailVO()!= null && CensusConstants.EMPLOYMENT_STATUS_CANCEL.equals(employee.getEmployeeDetailVO()
					.getEmploymentStatusCode())
					&& !user.isInternalUser()) {
				return false;
			} else {
				return true;
			}
		}
}
