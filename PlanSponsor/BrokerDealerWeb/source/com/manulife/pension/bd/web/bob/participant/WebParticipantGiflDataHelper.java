package com.manulife.pension.bd.web.bob.participant;

import com.manulife.pension.service.account.valueobject.ParticipantGiflData;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This is a helper class to transform ParticipantGiflData to WebParticipantGiflData.
 * All the basic properties from ParticipantGiflData will be populated in addition to 
 * the properties with NBDW specific formats.
 * 
 * @author Ilamparithi
 *
 */
public class WebParticipantGiflDataHelper {
	
	public static final String DEFAULT_DATE = "9999-12-31";

	public static final String NA = "N/A";

	public static final String AWAITING_DEPOSIT = "Awaiting deposit";
	
	public static WebParticipantGiflData transformToWebFormat(ParticipantGiflData giflData) {
		WebParticipantGiflData webGiflData = new WebParticipantGiflData();
		webGiflData.setContractId(giflData.getContractId());
		webGiflData.setParticipantId(giflData.getParticipantId());
		webGiflData.setGiflSelectionDate(giflData.getGiflSelectionDate());
		webGiflData.setGiflDeselectionDate(giflData.getGiflDeselectionDate());
		webGiflData.setGiflBenefitBaseAmt(giflData.getGiflBenefitBaseAmt());
		webGiflData.setGiflActivationDate(giflData.getGiflActivationDate());
		webGiflData.setGiflNextStepUpDate(giflData.getGiflNextStepUpDate());
		webGiflData.setGiflHoldingPeriodExpDate(giflData.getGiflHoldingPeriodExpDate());
		webGiflData.setGiflLastStepUpDate(giflData.getGiflLastStepUpDate());
		webGiflData.setGiflLastStepUpChangeAmt(giflData.getGiflLastStepUpChangeAmt());
		webGiflData.setWebGiflActivationDate(getWebGiflActivationDate(giflData));
		webGiflData.setWebGiflDeselectionDate(getWebGiflDeselectionDate(giflData));
		webGiflData.setWebGiflHoldingPeriodExpDate(getWebGiflHoldingPeriodExpDate(giflData));
		webGiflData.setWebGiflLastStepUpDate(getWebGiflLastStepUpDate(giflData));
		webGiflData.setWebGiflNextStepUpDate(getWebGiflNextStepUpDate(giflData));
		webGiflData.setWebGiflSelectionDate(getWebGiflSelectionDate(giflData));
		webGiflData.setWebGiflLastStepUpChangeAmt(getWebGiflLastStepUpChangeAmt(giflData));
		webGiflData.setRateForLastIncomeEnhancement(giflData
				.getRateForLastIncomeEnhancement());
		return webGiflData;
	}
	
	/**
	 * Method to convert the giflActivationDate to MM/dd/yyyy format.
	 * 
	 * @param giflData
	 * @return String
	 */
	private static String getWebGiflActivationDate(ParticipantGiflData giflData) {
		String giflActivationDateStr = "";
		String strDeselectionDate = DateRender
				.formatByPattern(giflData.getGiflDeselectionDate(), "",
						RenderConstants.MEDIUM_YMD_DASHED);
		String strActivationDate = DateRender.formatByPattern(
				giflData.getGiflActivationDate(), "", RenderConstants.MEDIUM_YMD_DASHED);
		if (!strDeselectionDate.equals(DEFAULT_DATE)) {
			giflActivationDateStr = NA;
		} else if (strDeselectionDate.equals(DEFAULT_DATE)
				&& strActivationDate.equals(DEFAULT_DATE)) {
			giflActivationDateStr = AWAITING_DEPOSIT;
		} else if (strDeselectionDate.equals(DEFAULT_DATE)
				&& !strActivationDate.equals(DEFAULT_DATE)) {
			giflActivationDateStr = DateRender
					.formatByPattern(giflData.getGiflActivationDate(), "",
							RenderConstants.MEDIUM_MDY_SLASHED);
		}
		return giflActivationDateStr;
	}
	
	/**
	 * Method to convert the giflHoldingPeriodExpDate to MM/dd/yyyy format.
	 * 
	 * @param giflData
	 * @return String
	 */
	private static String getWebGiflHoldingPeriodExpDate(ParticipantGiflData giflData) {
		String giflHoldingPeriodExpDateStr = "";
		String strDeselectionDate = DateRender
				.formatByPattern(giflData.getGiflDeselectionDate(), "",
						RenderConstants.MEDIUM_YMD_DASHED);
		String strActivationDate = DateRender.formatByPattern(
				giflData.getGiflActivationDate(), "", RenderConstants.MEDIUM_YMD_DASHED);
		if (!strDeselectionDate.equals(DEFAULT_DATE)) {
			giflHoldingPeriodExpDateStr = NA;
		} else if (strDeselectionDate.equals(DEFAULT_DATE)
				&& strActivationDate.equals(DEFAULT_DATE)) {
			giflHoldingPeriodExpDateStr = AWAITING_DEPOSIT;
		} else if (strDeselectionDate.equals(DEFAULT_DATE)
				&& !strActivationDate.equals(DEFAULT_DATE)) {
			giflHoldingPeriodExpDateStr = DateRender.formatByPattern(
					giflData.getGiflHoldingPeriodExpDate(), "",
					RenderConstants.MEDIUM_MDY_SLASHED);
		}
		return giflHoldingPeriodExpDateStr;
	}	
	
	/**
	 * Method to convert the giflLastStepUpDate to MM/dd/yyyy format.
	 * 
	 * @param giflData
	 * @return String
	 */
	private static String getWebGiflLastStepUpDate(ParticipantGiflData giflData) {
		String giflLastStepUpDateStr = "";
		String strLastStepUpDate = DateRender.formatByPattern(
				giflData.getGiflLastStepUpDate(), "", RenderConstants.MEDIUM_YMD_DASHED);
		String strDeselectionDate = DateRender
				.formatByPattern(giflData.getGiflDeselectionDate(), "",
						RenderConstants.MEDIUM_YMD_DASHED);
		if (strLastStepUpDate.equals(DEFAULT_DATE)) {
			giflLastStepUpDateStr = NA;
		} else if (strDeselectionDate.equals(DEFAULT_DATE)
				&& !strLastStepUpDate.equals(DEFAULT_DATE)) {
			giflLastStepUpDateStr = DateRender
					.formatByPattern(giflData.getGiflLastStepUpDate(), "",
							RenderConstants.MEDIUM_MDY_SLASHED);
		} else {
			giflLastStepUpDateStr = DateRender
					.formatByPattern(giflData.getGiflLastStepUpDate(), "",
							RenderConstants.MEDIUM_MDY_SLASHED);
		}
		return giflLastStepUpDateStr;
	}	

	/**
	 * Method to convert the giflNextStepUpDate to MM/dd/yyyy format.
	 * 
	 * @param giflData
	 * @return String
	 */
	private static String getWebGiflNextStepUpDate(ParticipantGiflData giflData) {
		String giflNextStepUpDateStr = "";
		String strDeselectionDate = DateRender
				.formatByPattern(giflData.getGiflDeselectionDate(), "",
						RenderConstants.MEDIUM_YMD_DASHED);
		String strActivationDate = DateRender.formatByPattern(
				giflData.getGiflActivationDate(), "", RenderConstants.MEDIUM_YMD_DASHED);
		if (!strDeselectionDate.equals(DEFAULT_DATE)) {
			giflNextStepUpDateStr = NA;
		} else if (strDeselectionDate.equals(DEFAULT_DATE)
				&& strActivationDate.equals(DEFAULT_DATE)) {
			giflNextStepUpDateStr = AWAITING_DEPOSIT;
		} else if (strDeselectionDate.equals(DEFAULT_DATE)
				&& !strActivationDate.equals(DEFAULT_DATE)) {
			giflNextStepUpDateStr = DateRender
					.formatByPattern(giflData.getGiflNextStepUpDate(), "",
							RenderConstants.MEDIUM_MDY_SLASHED);
		}
		return giflNextStepUpDateStr;
	}
	
	/**
	 * Method to convert the giflSelectionDate to MM/dd/yyyy format.
	 * 
	 * @param giflData
	 * @return String
	 */
	private static String getWebGiflSelectionDate(ParticipantGiflData giflData) {
		return DateRender.formatByPattern(
				giflData.getGiflSelectionDate(), "", RenderConstants.MEDIUM_MDY_SLASHED);
	}
	
	/**
	 * Method to convert the giflDeselectionDate to yyyy-MM-dd format.
	 * 
	 * @param giflData
	 * @return String
	 */
	private static String getWebGiflDeselectionDate(ParticipantGiflData giflData) {
		return DateRender
				.formatByPattern(giflData.getGiflDeselectionDate(), "",
						RenderConstants.MEDIUM_YMD_DASHED);
	}
	
	/**
	 * Method to convert the giflLastStepUpChangeAmt to a String
	 * 
	 * @param giflData
	 * @return String
	 */
	private static String getWebGiflLastStepUpChangeAmt(ParticipantGiflData giflData) {
		String giflLastStepUpChangeAmtStr = ""; 
		String strLastStepUpDate = DateRender.formatByPattern(
				giflData.getGiflLastStepUpDate(), "", RenderConstants.MEDIUM_YMD_DASHED);
		String strDeselectionDate = DateRender
				.formatByPattern(giflData.getGiflDeselectionDate(), "",
						RenderConstants.MEDIUM_YMD_DASHED);
		if (strLastStepUpDate.equals(DEFAULT_DATE)) {
			giflLastStepUpChangeAmtStr = NA;
		} else if (strDeselectionDate.equals(DEFAULT_DATE)
				&& !strLastStepUpDate.equals(DEFAULT_DATE)) {
			giflLastStepUpChangeAmtStr = giflData.getGiflLastStepUpChangeAmt()
					.toString();
		} else {
			giflLastStepUpChangeAmtStr = giflData.getGiflLastStepUpChangeAmt()
					.toString();
		}
		return giflLastStepUpChangeAmtStr;
	}	
	
}
