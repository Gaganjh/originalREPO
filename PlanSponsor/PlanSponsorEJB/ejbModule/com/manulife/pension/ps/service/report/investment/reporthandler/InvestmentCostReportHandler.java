package com.manulife.pension.ps.service.report.investment.reporthandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.contract.dao.ContractInformationDAO;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentCostReportData;
import com.manulife.pension.service.contract.valueobject.ContractDetailsOtherVO;
import com.manulife.pension.service.fee.util.Constants.ContractFeeCodes;
import com.manulife.pension.service.fee.util.ContractFeeTransformer;
import com.manulife.pension.service.fee.util.ContractRecordKeepingCharges;
import com.manulife.pension.service.fee.util.ContractRecordKeepingCharges.ContractRecordKeepingChargesUtility;
import com.manulife.pension.service.fee.util.ContractRecordKeepingCharges.EstimatedCostOfRecordKeepingUtility;
import com.manulife.pension.service.fee.util.exception.ContractNotApplicableExeption;
import com.manulife.pension.service.fee.valueobject.ClassType;
import com.manulife.pension.service.fee.valueobject.ContractClass;
import com.manulife.pension.service.fee.valueobject.ContractFee;
import com.manulife.pension.service.fee.valueobject.ContractInvestmentData;
import com.manulife.pension.service.fee.valueobject.EstimatedCostOfRecordKeeping;
import com.manulife.pension.service.fee.valueobject.FeeData;
import com.manulife.pension.service.fee.valueobject.FundFeeVO;
import com.manulife.pension.service.fee.valueobject.InvestmentGroup;
import com.manulife.pension.service.fund.FundServiceHelper;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.FundShortListVO;
import com.manulife.pension.service.fund.valueobject.FundShortListWeightings;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This Report Handler class is used to fetch data 
 * for InvestmentCostReport from the back end layer 
 * and return the data object as 
 * ReportData
 * 
 * @author Murali Chandran
 *
 */
public class InvestmentCostReportHandler implements ReportHandler {

    private static final int REPORT_PRECISION = 2;
    /* *(non-Javadoc)
	 * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {

		int contractNumber = 0;
		Date asOfDate = null;
		
		InvestmentCostReportData reportData;
		FeeData feeData = null;

		contractNumber = ((Integer) reportCriteria.getFilterValue(InvestmentCostReportData.FILTER_CONTRACT_NO));

		//Date
    	String s_asOfDate = (String)reportCriteria.getFilterValue(InvestmentCostReportData.FILTER_AS_OF_DATE);
    	boolean selectedView = (Boolean)reportCriteria.getFilterValue(InvestmentCostReportData.SELECTED_VIEW);
    	if (s_asOfDate != null) {
    		asOfDate = new Date(Long.valueOf(s_asOfDate).longValue());
    	}
    	
    	//Date
    	boolean checkForFundFeeChanges = (Boolean)reportCriteria.getFilterValue(InvestmentCostReportData.FILTER_CHECK_FOR_HISTORY);
    	
    	boolean includeOnlyMerrillCovered = (Boolean)reportCriteria.getFilterValue(InvestmentCostReportData.FILTER_MERRILL_COVERED_FUNDS);
		if (asOfDate != null) {
			FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance("PS");

			ContractInvestmentData contractInvestmentData = feeServiceDelegate.getContractInvestmentData(
							contractNumber, asOfDate, checkForFundFeeChanges, selectedView, false);
			
			Map<InvestmentGroup, List<FundFeeVO>> funds = contractInvestmentData.getFundFeeDetails();
			Map<InvestmentGroup, List<FundFeeVO>> merrillFunds = new TreeMap<InvestmentGroup, List<FundFeeVO>>();
			
			reportData = new InvestmentCostReportData(reportCriteria, contractInvestmentData.getContractDetails()
							.getNumberOfFundsSelected());
			
			reportData.setIsDefinedBenefitContract(contractInvestmentData.getContractDetails()
					.isDefinedBenefitContract());
			// fund data
			reportData.setFeeEffectiveDate(contractInvestmentData.getAsOfDate());
			if (!selectedView && includeOnlyMerrillCovered) {
				Set<String> merrillFundIds = FundServiceDelegate.getInstance().getMerrillCoveredFundInvestmentIds();
				List<String> mlRestrictedFunds = FundServiceDelegate.getInstance().getMLRestrictedFunds();
				
				for (Map.Entry<InvestmentGroup, List<FundFeeVO>> entry : funds.entrySet()) {
					List<FundFeeVO> vo = new ArrayList<FundFeeVO>();
					for (FundFeeVO fundFeeVo : entry.getValue()) {
						if ((merrillFundIds.contains(fundFeeVo.getFundId()) || fundFeeVo.isSelectedFund()) && !mlRestrictedFunds.contains(fundFeeVo.getFundId())) {
							vo.add(fundFeeVo);
						}
					}
					if (vo.size() > 0) {
						merrillFunds.put(entry.getKey(), vo);
					}
				}
				reportData.setInvestmentData(merrillFunds);
			}else{
			reportData.setInvestmentData(funds);
			}
			reportData.setNumberOfFundsSelected(contractInvestmentData.getContractDetails().getNumberOfFundsSelected());

			// contract class
			if (contractInvestmentData.getContractDetails().getContractClass() == null) {
				reportData.setNonClass1SignatureContract(false);
				reportData.setContractClassName(StringUtils.EMPTY);
			} else if(contractInvestmentData.getContractDetails().getContractClass().isClass1()) {
				reportData.setNonClass1SignatureContract(false);
				reportData.setContractClassName(contractInvestmentData.getContractDetails().getContractClass()
						.getClassLongName());				
			} else {
				reportData.setNonClass1SignatureContract(true);
				reportData.setContractClassName(contractInvestmentData.getContractDetails().getContractClass()
						.getClassLongName());
			}

			// set SVF indicator
			reportData.setHasSvfFund(StringUtils.equals(contractInvestmentData.getContractDetails().getSvfIndicator(), "Y") ? true
					: false);
			reportData.setHasSvpFund(contractInvestmentData.getContractDetails().isSvpIndicator());
			
			
			
			try {
				feeData = feeServiceDelegate.getPlanCharacteristicsDetails( contractNumber, asOfDate);
			} catch (ContractNotApplicableExeption e) {
				throw new SystemException("exception occured. details are " + e);
			}
			reportData.setContractDetails(feeData.getContractDetails());
			
            final ContractFee fees = feeData.getContractFee();
            
			// ML Weightings Start

			ContractFee contractFee;
			ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
			// #239 - Merrill Lynch Contract
			boolean isMerrillContract = false;
			if (contractNumber != 0) {
				ContractDetailsOtherVO contractDetailsOtherVO = contractServiceDelegate
						.getContractDetailsOther(contractNumber);
				if (contractDetailsOtherVO != null && contractDetailsOtherVO.isMerrillLynch()) {
					isMerrillContract = true;
				}
			}

			boolean isFSLmatched = false;
			String location = contractInvestmentData.getContractDetails().getCompanyCode();
			HashSet<String> selectedFunds = new HashSet<String>();

			Map<String, String> selectedFundsMap = ContractInformationDAO.getFundsSelected(contractNumber);
			Iterator<Entry<String, String>> its = selectedFundsMap.entrySet().iterator();
			while (its.hasNext()) {
				Map.Entry<String, String> it = its.next();
				String fundSelectedFlag = (String) it.getValue();
				if (fundSelectedFlag.equals("Y")) {
					selectedFunds.add(it.getKey());
				}
			}

			FundShortListVO fundShortListVO = null;
			String fundShortListCode = FundServiceHelper.getInstance().getContractFundShortList(contractNumber, asOfDate);

			fundShortListVO = FundServiceHelper.getInstance().getFundShortListWeightings(location, selectedFunds,
					asOfDate, fundShortListCode);

			HashMap<String, FundShortListWeightings> fslFundShortListWeightings = new HashMap<String, FundShortListWeightings>();
			// #240 - Use Fund Short List Weighting Flag
			isFSLmatched = fundShortListVO.isFSLmatchedFlag();
			// #242 - Fund Short List Weighting
			fslFundShortListWeightings = fundShortListVO.getFslFundShortListWeightings();

			BigDecimal averageUnderlyingFundNetCost;
			BigDecimal averageRevenueFromSubAccount;
			BigDecimal averageTotalRevenue;
			BigDecimal averageRevenueFromUnderlyingFund;

			if (isMerrillContract && isFSLmatched) {
				contractFee = feeServiceDelegate.calculateFSLWeightedAverages(contractNumber, asOfDate,
						feeData.getContractDetails().getContractClass(), feeData.getContractDetails(), true,
						fslFundShortListWeightings);
				
				// #224
				averageUnderlyingFundNetCost = contractFee.getFslWeightedAverageUnderlyingFundNetCost();
				
				// averageRevenueFromUnderlyingFund
				averageRevenueFromUnderlyingFund = contractFee
						.getFslWeightedAverageRevenueFromUnderlyingFund();


				// #225
				averageRevenueFromSubAccount = contractFee.getFslWeightedAverageRevenueFromSubaccount();

				// #226
				averageTotalRevenue = contractFee.getFslWeightedAverageTotalRevenue();

			} else {

				// load Averages
				averageUnderlyingFundNetCost = ((BigDecimal) ObjectUtils
						.defaultIfNull(fees.getUnroundedAverageFerRate(), BigDecimal.ZERO))
								.subtract((BigDecimal) ObjectUtils.defaultIfNull(fees.getUnroundedAverageJhRevenue(),
										BigDecimal.ZERO));
				{
					averageRevenueFromSubAccount = ((BigDecimal) ObjectUtils.defaultIfNull(fees.getUnroundedAverageAmcRate(),
									BigDecimal.ZERO))
											.add((BigDecimal) ObjectUtils
													.defaultIfNull(fees.getUnroundedAverageSsfRate(), BigDecimal.ZERO))
											.add((BigDecimal) ObjectUtils
													.defaultIfNull(fees.getUnroundedAverageUruRate(), BigDecimal.ZERO));
					averageTotalRevenue = ((BigDecimal) ObjectUtils.defaultIfNull(fees.getUnroundedAverageJhRevenue(),
									BigDecimal.ZERO))
											.add((BigDecimal) ObjectUtils
													.defaultIfNull(fees.getUnroundedAverageAmcRate(), BigDecimal.ZERO))
											.add((BigDecimal) ObjectUtils
													.defaultIfNull(fees.getUnroundedAverageSsfRate(), BigDecimal.ZERO))
											.add((BigDecimal) ObjectUtils
													.defaultIfNull(fees.getUnroundedAverageUruRate(), BigDecimal.ZERO));
				}
				averageRevenueFromUnderlyingFund = ((BigDecimal) ObjectUtils.defaultIfNull(fees.getAverageJHRevenue(),
						BigDecimal.ZERO));
			}
            reportData.setAverageUnderlyingFundNetCost(averageUnderlyingFundNetCost.setScale(REPORT_PRECISION, RoundingMode.HALF_UP));
            reportData.setAverageRevenueFromSubAccount(averageRevenueFromSubAccount.setScale(REPORT_PRECISION, RoundingMode.HALF_UP));
            reportData.setAverageTotalRevenueUsedTowardsPlanCosts(averageTotalRevenue.setScale(REPORT_PRECISION, RoundingMode.HALF_UP));
            reportData.setAverageRevenueFromUnderlyingFund(averageRevenueFromUnderlyingFund.setScale(REPORT_PRECISION, RoundingMode.HALF_UP));
            
			// load Estimated Cost Of Record Keeping Charges
			if(feeData.getContractFee() != null) {
				
				final int contractId = feeData.getContractId();
				final Date runDate = feeData.getAsOfDate();
				
				ContractFeeTransformer fee = new ContractFeeTransformer(feeData.getContractFee().getContractFeeDetails());
				final EstimatedCostOfRecordKeeping ecr =
				        ContractRecordKeepingCharges.getPersistedContractEstimatedCostOfRecordKeeping(
				                feeData.getContractId(),
				                feeData.getAsOfDate(),
				                fee,
				                feeData.getContractDetails(),
				                feeData.getContractFee().getUnroundedAverageAmcRate(),
				                feeData.getContractFee().getAverageJHRevenue(),
                                averageRevenueFromSubAccount,
				                feeData
								.getFundDataAsOfDate(),
				                new EstimatedCostOfRecordKeepingUtility() {
                                    
                                    public EstimatedCostOfRecordKeeping retrieveEstimatedCostOfRecordKeeping(final int contractId, final Date asOfDate) throws SystemException {
                                        return FeeServiceDelegate.getInstance("PS").retrieveEstimatedCostOfRecordKeeping(contractId, asOfDate);
                                    }
                                    
                                    public void persistEstimatedCostOfRecordKeeping(final EstimatedCostOfRecordKeeping ecr, boolean isPreAlignmentDate)
                                    throws SystemException {
                                        FeeServiceDelegate.getInstance("PS").persistEstimatedCostOfRecordKeeping(ecr, isPreAlignmentDate);
                                    }
                                    
                                    public List<ClassType> getClassTypes(Date asOfDate) throws SystemException{
                                    	return FeeServiceDelegate.getInstance("PS").getClassTypes(asOfDate);
                                    }
                                    
                                },
                                
                                new ContractRecordKeepingChargesUtility() {
                                	
                                	Map<ContractFeeCodes, Object> feeMap = null;
    		                    	
    		                    	@Override
    		    					public Map<ContractFeeCodes, Object> getOtherContractFeeDetails()
    		    							throws SystemException {
    		    						if (feeMap == null) {
    		    								feeMap = FeeServiceDelegate.getInstance(
    	        										"PS")
    		    										.getOtherContractFeeDetails(
    		    												contractId, runDate);
    		    						}
    		
    		    						return feeMap;
    		    					}
                                	
        							@Override
        							public String getBusinessParam(String paramName)
        									throws SystemException {
        								return EnvironmentServiceDelegate.getInstance()
        										.getBusinessParam(paramName);
        							}
        							
        						});
				
				reportData.setRecordedEstimatedCostOfRKCharges(ecr.retrieveRecordedEstimatedCostOfRKChargesList());
				// get fee waiver funds
				reportData.setFeeWaiverFunds(FundServiceDelegate.getInstance().getFundFeeWaiverIndicator());
				reportData.setRestrictedFunds(FundServiceDelegate.getInstance().getRestrictedFunds());
			}

		} else {
			reportData = new InvestmentCostReportData(reportCriteria, 0);
		}
		
		return reportData; 
	}
}
