package com.manulife.pension.bd.web.usermanagement;

import com.manulife.pension.ezk.web.ActionForm;

import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.process.SimpleProcessState;
import com.manulife.pension.service.security.bd.valueobject.FirmRepCreationValueObject;

/**
 * The process context for the FirmRep creation process
 * 
 * @author guweigu
 * 
 */
public class CreateFirmRepProcessContext extends BDWizardProcessContext {
	private static final long serialVersionUID = 1L;

	public static String ACTION_INPUT = "input";

	public final static SimpleProcessState InputState = new SimpleProcessState(
			"input", "/do/createFirmRep/create");

	public final static SimpleProcessState CompleteState = new SimpleProcessState(
			"complete", "/do/createFirmRep/complete");

	public final static SimpleProcessState CancelState = new SimpleProcessState(
			"cancel", URLConstants.BackToUserManagement, true);

	static {
		InputState.addNext(ACTION_CONTINUE, CompleteState);
		InputState.addNext(ACTION_CANCEL, CancelState);
	}

	public static final String ProcessName = "security.createFirmRep";

	private FirmRepCreationValueObject firmRepCreationVO;
	// to reserve the Web Phone number format
	private PhoneNumber phoneNum = new PhoneNumber();
	private boolean completed = false;
	private boolean changed = false;

	/**
	 * Constructor
	 */
	public CreateFirmRepProcessContext() {
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
	public FirmRepCreationValueObject getFirmRepcreationVO() {
		return firmRepCreationVO;
	}

	/**
	 * Update the CreationVO from the submitted form
	 * 
	 * @param form
	 */
	public void updateContext(ActionForm actionForm) throws SystemException {
		if (firmRepCreationVO == null) {
			firmRepCreationVO = new FirmRepCreationValueObject();
		}
		CreateFirmRepForm form = (CreateFirmRepForm) actionForm;
		firmRepCreationVO.setLastName(form.getLastName());
		firmRepCreationVO.setFirstName(form.getFirstName());
		firmRepCreationVO.setEmailAddress(form.getEmailAddress());
		firmRepCreationVO.setPhoneNumber(form.getPhoneNumber().getValue());
		phoneNum.set(form.getPhoneNumber());
		firmRepCreationVO.setPassCode(form.getPassCode());
		firmRepCreationVO.setFirms(BDWebCommonUtils.getFirmList(form
				.getFirmListStr()));
	}

	/**
	 * Populate the Form from the existing firmRepCreationVO
	 * 
	 * @param form
	 */
	public void populateForm(ActionForm actionForm) {
		if (firmRepCreationVO != null) {
			CreateFirmRepForm form = (CreateFirmRepForm) actionForm;
			form.setLastName(firmRepCreationVO.getLastName());
			form.setFirstName(firmRepCreationVO.getFirstName());
			form.setEmailAddress(firmRepCreationVO.getEmailAddress());
			form.getPhoneNumber().set(phoneNum);
			form.setPassCode(firmRepCreationVO.getPassCode());
			form.setFirms(firmRepCreationVO.getFirms());
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
		firmRepCreationVO = null;
	}
}
