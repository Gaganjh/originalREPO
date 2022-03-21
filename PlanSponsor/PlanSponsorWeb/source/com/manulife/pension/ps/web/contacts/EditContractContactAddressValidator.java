package com.manulife.pension.ps.web.contacts;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.Constants.FirstClientContactFeatureValue;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.contacts.util.AddressVO;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.valueobject.Address;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class EditContractContactAddressValidator extends ValidatorUtil implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return EditContractContactAddressForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors error) {
		BindingResult bindingResult = (BindingResult) error;
		if(!bindingResult.hasErrors()){
		Collection errors = new ArrayList();
		EditContractContactAddressForm editAddressForm = (EditContractContactAddressForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		String[] errorCodes = new String[100];
		UserProfile loggedUserProfile = getUserProfile(request);
		Contract contract = loggedUserProfile.getCurrentContract();
		
		if (editAddressForm.isSaveAction()) {

			if (FirstClientContactFeatureValue.CLIENT_AND_OTHER.getValue()
					.equals(
							editAddressForm.getFirstPointOfContact()
									.getFirstClientContact())) {
				if (StringUtils.isBlank(editAddressForm
						.getFirstPointOfContact().getFirstClientContactOther())
						|| StringUtils.isBlank(editAddressForm
								.getFirstPointOfContact()
								.getFirstClientContactOtherType())) {
					ValidationError errorNew = new ValidationError(
							"",
							ErrorCodes.OTHER_THIRD_PARTY_AND_OR_TYPE_NOT_SELECTED);
					errors.add(errorNew);
				}
			}
			//ME - CL -122023 - Allow CAR user can edit the address records
			boolean nonIS_InternalUser = getUserProfile(request).isInternalUser() && !getUserProfile(request).isInternalServicesCAR();
			if (!nonIS_InternalUser) {
				for (AddressVO addressVO : editAddressForm.getContactAddresses()) {
	
					if (Address.Type.LEGAL.getCode().equals(
							addressVO.getAddressTypeCode())) {
						// CPS.150
						if (StringUtils.isNotBlank(editAddressForm
								.getLegalAddressLine1())
								&& StringUtils.isBlank(addressVO.getAddressLine1())) {
							ValidationError errorNew = new ValidationError("",
									ErrorCodes.LEGAL_ADDRESS_REMOVED);
							errors.add(errorNew);
						}
	
						if (editAddressForm.isApplyToAllAddresses()
								&& addressVO.isBlank()) {
							// CPS 134
							// Apply to all addresses check box checked & legal
							// address is blank all parts.
							ValidationError errorNew = new ValidationError(
									"",
									ErrorCodes.LEGAL_ADDRESS_MUST_BE_COMPLETED_TO_USE_COPY_ADDRESSES);
							errors.add(errorNew);
						} else {
							// CPS.135
							if (editAddressForm.isApplyToAllAddresses()) {
								// Copy legal address to all other addresses
								EditContractContactAddressActionHelper
										.applyLegalAddressToAll(addressVO,
												editAddressForm);
							}
						}
	
						// CPS.132 - Legal address State is “NY” and the Company ID
						// is 019 (USA)
						if (StringUtils.isNotBlank(addressVO.getStateCode())) {
							if ("NY".equalsIgnoreCase(addressVO.getStateCode())
									&& contract.getCompanyCode().equalsIgnoreCase(
											Constants.COMPANY_ID_US)) {
								Object[] params = { Address
										.getAddressTypeForCode(addressVO
												.getAddressTypeCode()) };
								ValidationError errorNew = new ValidationError(
										"",
										ErrorCodes.LEGAL_STATE_NY_CANNOT_BE_FOR_USA,
										params);
								errors.add(errorNew);
							}
						}
						// CPS.133 - TODO - revisit
						if (StringUtils.isNotBlank(addressVO.getStateCode())
								&& !"NY".equalsIgnoreCase(addressVO.getStateCode())
								&& contract.getCompanyCode().equalsIgnoreCase(
										Constants.COMPANY_ID_NY)) {
	
							ValidationError errorNew = new ValidationError("",
									ErrorCodes.LEGAL_STATE_MUST_BE_NY_FOR_NY);
							errors.add(errorNew);
						}
						// PoBox not allowed in Legal address line1 and line2
						EditContractContactAddressActionHelper
								.validateAddressForPostBox(addressVO, errors); // CL
						// 111755
					} // end if - Legal address validation
	
					// CPS136 - TODO - Need to revisit for type code
					if (!StringUtils.isNumeric(addressVO.getZipCode())) {
						Object[] params = { Address.getAddressTypeForCode(addressVO
								.getAddressTypeCode()) };
						ValidationError errorNew = new ValidationError("",
								ErrorCodes.ZIP_CODE_NOT_NUMERIC, params);
						errors.add(errorNew);
					} else { // CPS138 -
						if (StringUtils.isNotBlank(addressVO.getZipCode())
								&& StringUtils.isNotBlank(addressVO.getStateCode())) {
							EditContractContactAddressActionHelper
									.validateUsZipCode(errors, addressVO, false,
											false);
						}
					}
	
					// CPS137 -
					if (!EditContractContactAddressActionHelper
							.isValidAddress(addressVO)) {
						// error message
						Object[] params = { Address.getAddressTypeForCode(addressVO
								.getAddressTypeCode()) };
						ValidationError errorNew = new ValidationError("",
								ErrorCodes.ADDRESS_INCOMPLETE, params);
						errors.add(errorNew);
					}
	
					if (!EditContractContactAddressActionHelper
							.isAsciiPrintable(addressVO)) {
						// error message
						Object[] params = { Address.getAddressTypeForCode(addressVO
								.getAddressTypeCode()) };
						ValidationError errorNew = new ValidationError("",
								ErrorCodes.InvalidAsciiCharacter, params);
						errors.add(errorNew);
					}
	
					// CPS 139 - PoBox not allowed in Courier address line1 and
					// line2
					if (addressVO.getAddressTypeCode().equalsIgnoreCase(
							Address.COURIER_CODE)) {
						EditContractContactAddressActionHelper
								.validateAddressForPostBox(addressVO, errors);
					}
				}
			}
		}

		
		if(!errors.isEmpty()){
		    for (Object e : errors) {
				if (e instanceof GenericException) {
					GenericException errorEx=(GenericException) e;
					errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
					bindingResult.addError(new ObjectError(error
							                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
				}
			}
		    if(request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null){
		    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
		    }
		    request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
			request.setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
	        }
		}
	}
	
}

	