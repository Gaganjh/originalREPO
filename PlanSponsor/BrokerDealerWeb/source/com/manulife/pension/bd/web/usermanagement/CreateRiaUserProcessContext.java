package com.manulife.pension.bd.web.usermanagement;

import com.manulife.pension.ezk.web.ActionForm;

import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.process.SimpleProcessState;
import com.manulife.pension.service.security.bd.valueobject.RiaUserCreationValueObject;

/**
 * The process context for the FirmRep creation process
 * 
 * @author guweigu
 * 
 */
public class CreateRiaUserProcessContext extends BDWizardProcessContext {
	private static final long serialVersionUID = 1L;

	public static String ACTION_INPUT = "input";

	public final static SimpleProcessState InputState = new SimpleProcessState(
			"input", "/do/createRiaUser/create");

	public final static SimpleProcessState CompleteState = new SimpleProcessState(
			"complete", "/do/createRiaUser/complete");

	public final static SimpleProcessState CancelState = new SimpleProcessState(
			"cancel", URLConstants.BackToUserManagement, true);

	static {
		InputState.addNext(ACTION_CONTINUE, CompleteState);
		InputState.addNext(ACTION_CANCEL, CancelState);
	}

	public static final String ProcessName = "security.createRiaUser";

	private RiaUserCreationValueObject riaUserCreationVO;
	// to reserve the Web Phone number format
	private PhoneNumber phoneNum = new PhoneNumber();
	private boolean completed = false;
	private boolean changed = false;

	/**
	 * Constructor
	 */
	public CreateRiaUserProcessContext() {
	}

	/**
	 * Returns the start state
	 */
	@Override
	public ProcessState getStartState() {
		return InputState;
	}

	/**
	 * Returns the FirmRepCreationValueObject
	 * 
	 * @return FirmRepCreationValueObject
	 */
	public RiaUserCreationValueObject getRiaUsercreationVO() {
		return riaUserCreationVO;
	}

	/**
	 * Update the CreationVO from the submitted form
	 * 
	 * @param form
	 */
	public void updateContext(ActionForm actionForm) throws SystemException {
		if (riaUserCreationVO == null) {
			riaUserCreationVO = new RiaUserCreationValueObject();
		}
		CreateRiaUserForm form = (CreateRiaUserForm) actionForm;
		riaUserCreationVO.setLastName(form.getLastName());
		riaUserCreationVO.setFirstName(form.getFirstName());
		riaUserCreationVO.setEmailAddress(form.getEmailAddress());
		riaUserCreationVO.setPhoneNumber(form.getPhoneNumber().getValue());
		phoneNum.set(form.getPhoneNumber());
		riaUserCreationVO.setPassCode(form.getPassCode());
		riaUserCreationVO.setFirms(BDWebCommonUtils.getRIAFirmList(form
				.getFirmListStr()));
		riaUserCreationVO.setNoRolePartyId(form.getNoRolePartyId());
	}

	/**
	 * Populate the Form from the existing firmRepCreationVO
	 * 
	 * @param form
	 */
	public void populateForm(ActionForm actionForm) {
		if (riaUserCreationVO != null) {
			CreateRiaUserForm form = (CreateRiaUserForm) actionForm;
			form.setLastName(riaUserCreationVO.getLastName());
			form.setFirstName(riaUserCreationVO.getFirstName());
			form.setEmailAddress(riaUserCreationVO.getEmailAddress());
			form.getPhoneNumber().set(phoneNum);
			form.setPassCode(riaUserCreationVO.getPassCode());
			form.setFirms(riaUserCreationVO.getFirms());
			form.setNoRolePartyId(riaUserCreationVO.getNoRolePartyId());
			form.setCompleted(isCompleted());
			form.setChanged(changed);
		}
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * reset the internal state
	 */
	public void reset() {
		completed = false;
		changed = false;
		phoneNum.clear();
		riaUserCreationVO = null;
	}
}
