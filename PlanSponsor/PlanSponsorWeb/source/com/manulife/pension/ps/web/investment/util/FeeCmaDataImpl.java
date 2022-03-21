package com.manulife.pension.ps.web.investment.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.investment.util.Constants.CmaKeys;
import com.manulife.pension.ps.web.investment.util.Constants.Location;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.FeeCmaData;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * Stores CMA related values for Fee
 * 
 * @author akarave
 *
 */
public class FeeCmaDataImpl implements FeeCmaData, Serializable {
	
	private static final long serialVersionUID = 1L;
	private Location location;
	

	public FeeCmaDataImpl(Location location) throws SystemException {
		this.location = location;
	}
	
	
	public String getRecordKeepingSummaryIntroduction() throws SystemException {
		return getContent(CmaKeys.RECORD_KEEPING_SUMMARY_INTRODUCTION.getContentId());
	}
	
	public String getRecordkeepingCostsLabel() throws SystemException {
		return getContent(CmaKeys.RECORD_KEEPING_COSTS_LABEL.getContentId());
	}
	
	public String getClassZeroPhaseTwoRecordkeepingCostsLabel() throws SystemException {
		return getContent(CmaKeys.RECORD_KEEPING_COSTS_LABEL_CZERO.getContentId());
	}
	
	public String getSecAIntroduction(boolean isDefinedBenefit) throws SystemException {
		return isDefinedBenefit ? getContent(CmaKeys.SECTION_A_INTRODUCTION_DB.getContentId())
		: getContent(CmaKeys.SECTION_A_INTRODUCTION.getContentId());
	}

	


	public String getSecADeductedRecordKeepingChargesPaymentMethod(boolean isDefinedBenefit) throws SystemException {
		return isDefinedBenefit ? getContent(CmaKeys.SECTION_A_DD_RK_CHARGES_PAYMENT_METHOD_DB.getContentId())
		: getContent(CmaKeys.SECTION_A_DD_RK_CHARGES_PAYMENT_METHOD.getContentId());
	}

	
	public String getSecAAdjustmentRecordKeepingChagresLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_A_ADJUSTMENT_RK_CHAGRES_LABEL.getContentId());
	}

	
	public String getSecABilledRecordKeepingChargesPaymentMethod() throws SystemException {
		return getContent(CmaKeys.SECTION_A_BL_RK_CHARGES_PAYMENT_METHOD.getContentId());
	}

	
	public String getSecAParticipantStatementFeeLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_A_PARTICIPANT_STATEMENT_FEE_LABEL.getContentId());
	}

	
	public String getSecADeductedParticipantStmtFeePaymentMethod() throws SystemException {
		return getContent(CmaKeys.SECTION_A_DD_PARTICIPANT_STATEMENT_FEE_PAYMENT_METHOD.getContentId());
	}


	public String getSecABilledParticipantStmtFeePymentMethod() throws SystemException {
		return getContent(CmaKeys.SECTION_A_BL_PARTICIPANT_STATEMENT_FEE_PAYMENT_METHOD.getContentId());
	}

	
	public String getSecARecordKeepingFeeLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_A_RK_FEE_LABEL.getContentId());
	}

	
	public String getSecARecordKeepingFeePaymentMethod() throws SystemException {
		return getContent(CmaKeys.SECTION_A_RK_FEE_PAYMENT_METHOD.getContentId());
	}
	
	public String getSecATotalLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_A_TOTAL_LABEL.getContentId());
	}

	public String getSecBIntroduction() throws SystemException {
		return getContent(CmaKeys.SECTION_B_INTRODUCTION.getContentId());
	}

	
	public String getSecBRevenueUnderlyingFundLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_B_REVENUE_UNDERLYING_FUND_LABEL.getContentId());
	}


	public String getSecBRevenueUnderlyingFundPaymentMethod() throws SystemException {
		return getContent(CmaKeys.SECTION_B_REVENUE_UNDERLYING_FUND_PAYMENT_METHOD.getContentId());
	}


	public String getSecBRevenueJhChargesLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_B_REVENUE_JH_CHARGES_LABEL.getContentId());
	}


	public String getSecBRevenueJhChargesPaymentMethod() throws SystemException {
		return getContent(CmaKeys.SECTION_B_REVENUE_JH_CHARGES_PAYMENT_METHOD.getContentId());
	}


	public String getSecBAdjustmentRevenueJhChagresLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_B_ADJUSTMENT_REVENUE_JH_CHAGRES_LABEL.getContentId());
	}


	public String getSecBPriceCreditsLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_B_PRICE_CREDITS_LABEL.getContentId());
	}

	
	public String getSecBTotalLabel() throws SystemException {
		return getContent(CmaKeys.SECTION_B_TOTAL_LABEL.getContentId());
	}

	
	public String getRevenueNonJhChargesLabel() throws SystemException {
		return getContent(CmaKeys.REVENUE_NON_JH_CHARGES_LABEL.getContentId());
	}
	
	
	public String getTotalRevenueUsedLabel() throws SystemException {
		return getContent(CmaKeys.TOTAL_REVENUE_USED_LABEL.getContentId());
	}
	
	//Class Zero Phase 2 Changes
	public String getDollarBasedIntroLabel() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_INTRO_LABEL.getContentId());
	}
	
	public String getDollarBasedSubheadingLabel() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_DESC_LABEL.getContentId());
	}
	
	public String getDollarBasedDDPFLabel() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_DDPF_LABEL.getContentId());
	}
	
	public String getDollarBasedDDPFPaymentMethod() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_DDPF_PAYMENT_METHOD.getContentId());
	}
	
	public String getDollarBasedBLPFLabel() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_BLPF_LABEL.getContentId());
	}
	
	public String getDollarBasedBLPFPaymentMethod() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_BLPF_PAYMENT_METHOD.getContentId());
	}
	
	public String getDollarBasedDetailedStmtFeeLabel() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_DETLD_STMT_FEE_LABEL.getContentId());
	}
	
	public String getDollarBasedDetailedStmtFeePaymentMethod() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_DETLD_STMT_FEE_PAYMENT_METHOD.getContentId());
	}
	
	public String getDollarBasedRecordKeepingChargesLabel() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_RECORD_KEEPING_CHARGES_LABEL.getContentId());
	}
	
	public String getDollarBasedRecordKeepingChargesPaymentMethod() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_RECORD_KEEPING_CHARGES_PAYMENT_METHOD.getContentId());
	}
	
	public String getDollarBasedSmallPlanPptFeeLabel() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_SMALL_PLAN_PPT_FEE_LABEL.getContentId());
	}
	
	public String getDollarBasedAdminFeeLabel() throws SystemException {
		return getContent(CmaKeys.DOLLAR_BASED_ADMIN_FEE_LABEL.getContentId());
	}
	
	// report foot notes.
	
	// footnotes N6
	public String getImpSecN6Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N6_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N1
	public String getImpSecN1Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N1_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N2
	public String getImpSecN2Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N2_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N7
	public String getImpSecN7Footnote(boolean isDefinedBenefit) throws SystemException {
		return isDefinedBenefit ? getContent(CmaKeys.REPORT_IMP_INFO_N7_FOOTNOTE_DB_KEY.getContentId()) :
		getContent(CmaKeys.REPORT_IMP_INFO_N7_FOOTNOTE_KEY.getContentId());
	}
	
	// base asset charge text
	public String getBaseAssetChargeText() throws SystemException {
		return getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_BASE_ASSET_CHARGE_TEXT.getContentId());
	}
	
	// footnotes annualized ppt fee text
	public String getAnnualizedPptFeeText(boolean withComma) throws SystemException {
		return withComma ? getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_ANNUALIZED_PPT_FEE_TEXT_WITH_COMMA.getContentId()) :
			getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_ANNUALIZED_PPT_FEE_TEXT.getContentId());
	}
	
	// footnotes pinpoint Credits fee text
	public String getpinpointCreditsFeeText(boolean withComma) throws SystemException {
		return withComma ? getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_PINPOINT_CREDITS_FEE_TEXT_WITH_COMMA.getContentId()) :
			getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_PINPOINT_CREDITS_FEE_TEXT.getContentId());
	}
	
	// footnotes N10
	public String getImpSecN10Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N10_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N11
	public String getImpSecN11Footnote(boolean isDefinedBenefit) throws SystemException {
		return isDefinedBenefit ? getContent(CmaKeys.REPORT_IMP_INFO_N11_FOOTNOTE_DB_KEY.getContentId()) :
		getContent(CmaKeys.REPORT_IMP_INFO_N11_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N12
	public String getImpSecN12Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N12_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N13
	public String getImpSecN13Footnote(boolean isDefinedBenefit) throws SystemException {
		return isDefinedBenefit ? getContent(CmaKeys.REPORT_IMP_INFO_N13_FOOTNOTE_DB_KEY.getContentId()) :
			getContent(CmaKeys.REPORT_IMP_INFO_N13_FOOTNOTE_KEY.getContentId());
	}

	// footnotes N14
	public String getImpSecN14Footnote(boolean isDefinedBenefit) throws SystemException {
		return isDefinedBenefit ? getContent(CmaKeys.REPORT_IMP_INFO_N14_FOOTNOTE_DB_KEY.getContentId())
				: getContent(CmaKeys.REPORT_IMP_INFO_N14_FOOTNOTE_KEY.getContentId());
	}

	// footnotes N15	
	public String getImpSecN15Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N15_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N19 msv
	public String getImpSecN19MSVFootnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N19_FOOTNOTE_MSV_KEY.getContentId());
	}
	
	// footnotes N19 nmy
	public String getImpSecN19NMYFootnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N19_FOOTNOTE_NMY_KEY.getContentId());
	}
	
	// footnotes N20
	public String getImpSecN20Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N20_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N21
	public String getImpSecN21Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N21_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N23
	public String getImpSecN23Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N23_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N24
	public String getImpSecN24Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N24_FOOTNOTE_KEY.getContentId());
	}
	
	// footnotes N25
	public String getImpSecN25Footnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N25_FOOTNOTE_KEY.getContentId());
	}
	
	public String getReportSec2N7RecordKeepingFeeLabel(boolean appendComma) throws SystemException { 
		return appendComma ? getContent(CmaKeys.REPORT_RECORD_KEEPING_FEE_N7_KEY_WITH_COMMA.getContentId()) 
				: getContent(CmaKeys.REPORT_RECORD_KEEPING_FEE_N7_KEY.getContentId());
	}
	
	//Class Zero Phase Two Footnotes
	// footnotes N7
	public String getImpSecN7FootnoteClassZeroPhaseTwo() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N7_FOOTNOTE_KEY_CLASSZ_P2.getContentId());
	}
	
	// base asset charge text
	public String getBaseAssetChargeTextClassZero() throws SystemException {
		return getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_BASE_ASSET_CHARGE_TEXT_CLASSZ_P2.getContentId());
	}
	
	// footnotes annualized ppt fee text
	public String getAnnualizedPptFeeTextClassZeroPhaseTwo(boolean withComma) throws SystemException {
		return withComma ? getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_ANNUALIZED_PPT_FEE_TEXT_WITH_COMMA_CLASSZ_P2.getContentId()) :
			getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_ANNUALIZED_PPT_FEE_TEXT_CLASSZ_P2.getContentId());
	}
	
	// footnotes pinpoint Credits fee text
	public String getpinpointCreditsFeeTextClassZeroPhaseTwo(boolean withComma) throws SystemException {
		return withComma ? getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_PINPOINT_CREDITS_FEE_TEXT_WITH_COMMA_CLASSZ_P2.getContentId()) :
			getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_PINPOINT_CREDITS_FEE_TEXT_CLASSZ_P2.getContentId());
	}
	
	// footnotes N11
	public String getImpSecN11FootnoteClassZeroPhaseTwoN11() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N11_FOOTNOTE_KEY_CLASSZ_P2.getContentId());
	}
	
	// base asset charge text
	public String getBaseAssetChargeTextClassZeroPhaseTwoN11() throws SystemException {
		return getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_BASE_ASSET_CHARGE_TEXT_CLASSZ_P2_N11.getContentId());
	}
	
	// footnotes annualized ppt fee text
	public String getAnnualizedPptFeeTextClassZeroPhaseTwoN11(boolean withComma) throws SystemException {
		return withComma ? getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_ANNUALIZED_PPT_FEE_TEXT_WITH_COMMA_CLASSZ_P2_N11.getContentId()) :
			getContent(CmaKeys.REPORT_RECORD_KEEPING_CHARGES_ANNUALIZED_PPT_FEE_TEXT_CLASSZ_P2_N11.getContentId());
	}
	
	/**
	 * Returns cma content for given id
	 * @param contentId
	 * @return
	 */
	private String getContent(Integer contentId) throws SystemException{
		String text = StringUtils.EMPTY;
		try {
			Miscellaneous content = (Miscellaneous) ContentCacheManager
					.getInstance().getContentById(contentId,
							ContentTypeManager.instance().MISCELLANEOUS);

			switch (location) {
			case NY:
				if (content != null) {
					text = content.getNyText();
				}
				break;
			default:
				if (content != null) {
					text = content.getText();
				}
				break;
			}
		} catch (ContentException exception){
			throw new SystemException("Exception Occured while retreiving Content for content Id: "+ contentId);
		}
		
		return text;
	}


	@Override
	public String getSecADeductedRecordKeepingChagresLabel()
			throws SystemException {
		return getContent(CmaKeys.SECTION_A_RK_CHAGRES_LABEL.getContentId());
	}


	@Override
	public String getSecABilledRecordKeepingChagresLabel()
			throws SystemException {
		return getContent(CmaKeys.SECTION_A_RK_CHAGRES_LABEL.getContentId());
	}
	
	@Override
	public String getImpSecN1FootnoteMarker() throws SystemException {
		return Constants.FOOTNOTE_N1_TEXT;
	}


	@Override
	public String getImpSecN2FootnoteMarker() throws SystemException {
		return Constants.FOOTNOTE_N2_TEXT;
	}


	@Override
	public String getImpSecN7FootnoteMarker() throws SystemException {
		return Constants.FOOTNOTE_N7_TEXT;
	}


	@Override
	public String getImpSecN10FootnoteMarker() throws SystemException {
		return Constants.FOOTNOTE_N10_TEXT;
	}


	@Override
	public String getImpSecN11FootnoteMarker() throws SystemException {
		return Constants.FOOTNOTE_N11_TEXT;
	}


	@Override
	public String getImpSecN25FootnoteMarker() throws SystemException {
		return Constants.FOOTNOTE_N25_TEXT;
	}
	
	@Override
	public String getImpSecN10DBFootnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N10_FOOTNOTE_DB_KEY.getContentId());
	}

	@Override
	public String getImpSecN12DBFootnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N12_FOOTNOTE_DB_KEY.getContentId());
	}


	@Override
	public String getImpSecN24DBFootnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N24_FOOTNOTE_DB_KEY.getContentId());
	}


	@Override
	public String getImpSecN10NCZFootnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N10_FOOTNOTE_NCZ_KEY.getContentId());
	}


	@Override
	public String getImpSecN12NCZFootnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N12_FOOTNOTE_NCZ_KEY.getContentId());
	}


	@Override
	public String getImpSecN24NCZFootnote() throws SystemException {
		return getContent(CmaKeys.REPORT_IMP_INFO_N24_FOOTNOTE_NCZ_KEY.getContentId());
	}
	
}
