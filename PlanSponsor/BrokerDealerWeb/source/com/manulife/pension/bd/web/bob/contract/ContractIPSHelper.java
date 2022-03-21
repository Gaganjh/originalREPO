package com.manulife.pension.bd.web.bob.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.util.Pair;

/**
 * The helper class to retrieve the IPSR information
 * 
 * @author guweigu
 *
 */
public class ContractIPSHelper {

	/**
	 * Retrieve the contract IPS information for the input contractId
	 * 
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public static ContractIPSVO getContractIPSInfo(int contractId)
			throws SystemException {
		ContractServiceDelegate contractService = ContractServiceDelegate
				.getInstance();

		InvestmentPolicyStatementVO baseData = contractService
				.getIpsBaseData(contractId);

		ContractIPSVO contractIPS = new ContractIPSVO();

		// if baseData is not available or ips is off, set the ips off
		if (baseData != null && baseData.isIpsAvailable()) {
			contractIPS.setIpsAvailable(true);
			contractIPS.setAnnualReviewDate(baseData.getAnnualReviewDate());
			InvestmentPolicyStatementVO weightings = contractService
					.getIPSCriteria(contractId);
			// translate the criteria code to description
			Map<String, String> fundMetricsMap = contractService
					.getIpsFundMetrics();

			contractIPS.setIpsWeightingList(getIPSWeightingList(
					weightings.getInvestmentPolicyStatementCriteria(),
					fundMetricsMap));
		} else {
			contractIPS.setIpsAvailable(false);
		}

		return contractIPS;

	}

	/**
	 * Translate the criteria code to description
	 * 
	 * @param investmentPolicyStatementCriteria
	 * @param fundMetricsMap
	 * @return
	 */
	private static List<Pair<String, Integer>> getIPSWeightingList(
			Map<String, Integer> investmentPolicyStatementCriteria,
			Map<String, String> fundMetricsMap) {
		List<Pair<String, Integer>> weightingList = new ArrayList<Pair<String, Integer>>(
				investmentPolicyStatementCriteria.size());

		for (String criteriaCode : investmentPolicyStatementCriteria.keySet()) {
			Pair<String, Integer> weighting = new Pair<String, Integer>(
					fundMetricsMap.get(criteriaCode),
					investmentPolicyStatementCriteria.get(criteriaCode));
			weightingList.add(weighting);
		}
		return weightingList;
	}
	
	/**
	 * return a formated Annual Review Date as MMMM dd format.
	 * Returns blank if the annual date is null
	 * 
	 * @param ips
	 * @return
	 */
	public static String formatAnnualReviewDate(ContractIPSVO ips) {
		DayOfYear date = ips.getAnnualReviewDate();
		if (date == null) {
			return "";
		} else {
			return date.getMonthAsFullText() + " " + date.getDay();
		}
	}
}
