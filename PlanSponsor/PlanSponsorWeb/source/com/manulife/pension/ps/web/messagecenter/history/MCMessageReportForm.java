package com.manulife.pension.ps.web.messagecenter.history;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.home.SearchContractController;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

public class MCMessageReportForm extends MCMessageHistoryForm {

	@Override
	public void validate(Collection<GenericException> errors) {
		super.validate(errors);
		if (!StringUtils.isEmpty(messageId)) {
			try {
				Integer.parseInt(messageId);
			} catch (NumberFormatException e) {
				errors.add(new ValidationError("messageId", ErrorMessageIdFormat));
			}
		}
		if (StringUtils.isNotEmpty(getContractId())) {
			try {
				InternalUser role = null;
				Contract cd = ContractServiceDelegate.getInstance().getContractDetails(Integer.valueOf(getContractId()),
						EnvironmentServiceDelegate.getInstance().retrieveContractDiDuration((UserRole)role, 0,null));
				if (!StringUtils.equals(cd.getCompanyCode(), StringUtils.equals(Environment.getInstance()
						.getSiteLocation(), "usa") ? GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA
						: GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
					errors.add(new ValidationError("contractId", ErrorCodes.CONTRACT_EXIST_ON_THE_OTHER_SITE,
							new Object[] { Environment.getInstance().getPSOtherSiteMarketingURL(),
									Environment.getInstance().getPSOtherSiteMarketingURL() }));
				}
				
			} catch (ContractNotExistException e) {
				errors.add(new ValidationError("contractId", ErrorCodes.CONTRACT_NUMBER_INVALID));
			} catch (NumberFormatException e) {
				errors.add(new ValidationError("contractId", MCConstants.ErrorContractIdFormat));
			} catch (SystemException e) {
				throw new RuntimeException(e);
			}
		} else {
			setRecipient("");
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -965541584230395282L;

	private String recipient;
	private List<LabelValueBean> recipientList;
	private String messageId;
	private String messageStatus = com.manulife.pension.ps.web.messagecenter.history.MCCarViewUtils.MessageStatus.ALL.getValue();
	private Boolean allMessages;
	private String recipientRole;
	private Boolean viewPersonalization;


	public Boolean getViewPersonalization() {
		return viewPersonalization;
	}

	public void setViewPersonalization(Boolean viewPersonalization) {
		this.viewPersonalization = viewPersonalization;
	}

	public String getRecipientRole() {
		return recipientRole;
	}

	public void setRecipientRole(String recipientRole) {
		this.recipientRole = recipientRole;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = StringUtils.trimToEmpty(messageId);
	}

	public List<LabelValueBean> getRecipientList() {
		return recipientList;
	}

	public void setRecipientList(List<LabelValueBean> recipientList) {
		this.recipientList = recipientList;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getRecipientId() {
		return !StringUtils.isEmpty(recipient)
				&& StringUtils.isNumeric(recipient) ? recipient : "";
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	/**
	 * The order of the field appeared on screen
	 */
	private static int[] errorOrder = new int[] { ErrorContractIdFormat,
			ErrorFromDateInvalidFormat, ErrorFromToDateInvalid,
			ErrorFromDateEmpty, ErrorToDateInvalidFormat, ErrorToDateEmpty,
			ErrorMessageIdFormat, ErrorNameInvalid,
			ErrorCodes.SSN_INVALID, ErrorSubmissionIdFormat };

	protected int[] getErrorOrder() {
		return errorOrder;
	}

	public Boolean getAllMessages() {
		return allMessages;
	}

	public void setAllMessages(Boolean allMessages) {
		this.allMessages = allMessages;
	}
	public String getDefaultFromDate() {
		return "";
	}

	public String getDefaultToDate() {
		return "";
	}	

}
