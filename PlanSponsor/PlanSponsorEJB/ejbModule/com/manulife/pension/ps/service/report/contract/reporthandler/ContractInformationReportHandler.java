package com.manulife.pension.ps.service.report.contract.reporthandler;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.contract.dao.ContractInformationDAO;
import com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData;
import com.manulife.pension.service.contract.dao.CoFiduciaryDAO;
import com.manulife.pension.service.contract.dao.FswDao;
import com.manulife.pension.service.contract.valueobject.CoFiduciaryVO;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.contract.valueobject.FswStatus;
import com.manulife.pension.service.fund.FundServiceHelper;
import com.manulife.pension.service.fund.valueobject.WarrantyValidationVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;


/**
 * This the report handler for Contract Informations report
 * 
 * @author Siby Thomas
 * 
 */
public class ContractInformationReportHandler implements ReportHandler {

    private static final Logger logger = Logger.getLogger(ContractInformationReportHandler.class);
    
    /**
     * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData
     *      (com.manulife.pension.service.report.valueobject.ReportCriteria)
     */
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
        }
        
        Integer contractNumber = (Integer) reportCriteria
                .getFilterValue(ContractInformationReportData.FILTER_CONTRACT_NUMBER);
        
        ContractInformationReportData reportData = new ContractInformationReportData();
        
        ContractInformationDAO.getContractInformationDetails(reportCriteria, reportData);
        ContractInformationDAO.getTpaPrimaryContactDetails(contractNumber,reportData);
        ContractInformationDAO.getPlanSponsorPrimaryContactDetails(contractNumber, reportData);
        ContractInformationDAO.getRMUserInfo(reportCriteria,contractNumber, reportData.getContractSummaryVo());
        
        // sets the has loans asset indicator
        reportData
                .setHasLoanAssets(reportData.getContractSnapshotVo().getPlanAssets()
                .getLoanAssets() != null
                && !new BigDecimal("0.00").equals(reportData.getContractSnapshotVo()
                        .getPlanAssets().getLoanAssets()));
        
        
        //Co-Fid change for suppressing the FSW
        CoFiduciaryVO coFiduciaryVO = CoFiduciaryDAO.getCoFiduciaryData(contractNumber);
        if(coFiduciaryVO.isCoFiduciary()){
        	reportData.getContractSummaryVo().setCoFiduciary(true);
        } else{
        	 // gets the warranty results
            getWarrantyValidationResult(contractNumber, reportData.getContractSummaryVo());
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getReportData");
        }
        
        return reportData;
    }
    
    /**
     * sets the warranty indicator and list of failed asset classes
     * 
     * @param vo ContractSummaryVO
     * @throws SystemException
     */
	@SuppressWarnings("unchecked")
	private static void getWarrantyValidationResult(int contractNumber,
			ContractSummaryVO contractSummaryVO) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getWarrantyValidationResult");
		}

		final FswStatus status = FswStatus.of(FswDao.INSTANCE.getIndicator(contractNumber));
		switch (status) {

		case COFIDUCIARY:
		case NOT_ELIGIBLE:
			contractSummaryVO.setFswStatus(status);
			break;

		default:
			// To get the selected funds
			List<String> fundsToBeValidated = contractSummaryVO.getSelectedFunds();
			StringBuffer fundList = new StringBuffer("");

			// Separate the funds using , operator and append ' for each funds.

			if (null == fundsToBeValidated || fundsToBeValidated.isEmpty()) {
				fundList.append(Constants.SINGLE_QUOTES);
				fundList.append(Constants.SINGLE_QUOTES);
			} else {
				for (String fundId : fundsToBeValidated) {
					if (StringUtils.isNotBlank(fundList.toString())) {
						fundList.append(Constants.COMMA_SYMBOL);
					}
					fundList.append(Constants.SINGLE_QUOTES);
					fundList.append(fundId);
					fundList.append(Constants.SINGLE_QUOTES);
				}
			}
			WarrantyValidationVO warrantyVO = FundServiceHelper.getInstance()
					.validateWarranty(fundList.toString());
			contractSummaryVO.setFswStatus(warrantyVO.isWarrantyMet());
			if (!warrantyVO.isWarrantyMet()) {
				contractSummaryVO.setFailedAssetClasses(warrantyVO
						.getFailedAssetClasses());
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getWarrantyValidationResult");
		}
	}
}