package com.manulife.pension.bd.web.bob.participant;

import com.manulife.pension.service.account.valueobject.ParticipantGiflData;

/**
 * This is a web version of the ParticipantGiflData bean. This extends all the
 * properties from the ParicipantGiflData and also introduces its own properties
 * to show the data in NBDW specific formats. Both PSW and EZK share the same
 * formats. That logic is already present in the ParticipantGiflData. Since we
 * need different formats for NBDW we are introducing new properties to hold the
 * new formats. While all the basic properties from the ParticipantGiflData will
 * be populated and can be used in NBDW, the display properties specific to PSW
 * and EZK (property names starting with "display") will not be populated for
 * NBDW and should not be used anywhere in NBDW.
 * 
 * @author Ilamparithi
 * 
 */

public class WebParticipantGiflData extends ParticipantGiflData {
	
	private static final long serialVersionUID = 1L;

	private String webGiflSelectionDate = null;

	private String webGiflDeselectionDate = null;

	private String webGiflActivationDate = null;

	private String webGiflNextStepUpDate = null;

	private String webGiflHoldingPeriodExpDate = null;

	private String webGiflLastStepUpDate = null;

	private String webGiflLastStepUpChangeAmt = null;

	/**
	 * Returns the webGiflSelectionDate
	 * 
	 * @return String
	 */
	public String getWebGiflSelectionDate() {
		return webGiflSelectionDate;
	}

	/**
	 * Sets webGiflSelectionDate
	 * 
	 * @param webGiflSelectionDate
	 */
	public void setWebGiflSelectionDate(String webGiflSelectionDate) {
		this.webGiflSelectionDate = webGiflSelectionDate;
	}

	/**
	 * Returns webGiflDeselectionDate
	 * 
	 * @return String
	 */
	public String getWebGiflDeselectionDate() {
		return webGiflDeselectionDate;
	}

	/**
	 * Sets webGiflDeselectionDate
	 * 
	 * @param webGiflDeselectionDate
	 */
	public void setWebGiflDeselectionDate(String webGiflDeselectionDate) {
		this.webGiflDeselectionDate = webGiflDeselectionDate;
	}

	/**
	 * Returns webGiflActivationDate
	 * 
	 * @return String
	 */
	public String getWebGiflActivationDate() {
		return webGiflActivationDate;
	}

	/**
	 * Sets webGiflActivationDate
	 * 
	 * @param webGiflActivationDate
	 */
	public void setWebGiflActivationDate(String webGiflActivationDate) {
		this.webGiflActivationDate = webGiflActivationDate;
	}

	/**
	 * Returns webGiflNextStepUpDate
	 * 
	 * @return String
	 */
	public String getWebGiflNextStepUpDate() {
		return webGiflNextStepUpDate;
	}

	/**
	 * Sets webGiflNextStepUpDate
	 * 
	 * @param webGiflNextStepUpDate
	 */
	public void setWebGiflNextStepUpDate(String webGiflNextStepUpDate) {
		this.webGiflNextStepUpDate = webGiflNextStepUpDate;
	}

	/**
	 * Returns webGiflHoldingPeriodExpDate
	 * 
	 * @return String
	 */
	public String getWebGiflHoldingPeriodExpDate() {
		return webGiflHoldingPeriodExpDate;
	}

	/**
	 * Sets webGiflHoldingPeriodExpDate
	 * 
	 * @param webGiflHoldingPeriodExpDate
	 */
	public void setWebGiflHoldingPeriodExpDate(String webGiflHoldingPeriodExpDate) {
		this.webGiflHoldingPeriodExpDate = webGiflHoldingPeriodExpDate;
	}

	/**
	 * Returns webGiflLastStepUpDate
	 * 
	 * @return String
	 */
	public String getWebGiflLastStepUpDate() {
		return webGiflLastStepUpDate;
	}

	/**
	 * Sets webGiflLastStepUpDate
	 * 
	 * @param webGiflLastStepUpDate
	 */
	public void setWebGiflLastStepUpDate(String webGiflLastStepUpDate) {
		this.webGiflLastStepUpDate = webGiflLastStepUpDate;
	}

	/**
	 * Returns webGiflLastStepUpChangeAmt
	 * 
	 * @return String
	 */
	public String getWebGiflLastStepUpChangeAmt() {
		return webGiflLastStepUpChangeAmt;
	}

	/**
	 * Sets webGiflLastStepUpChangeAmt
	 * 
	 * @param webGiflLastStepUpChangeAmt
	 */
	public void setWebGiflLastStepUpChangeAmt(String webGiflLastStepUpChangeAmt) {
		this.webGiflLastStepUpChangeAmt = webGiflLastStepUpChangeAmt;
	}

	

	
}
