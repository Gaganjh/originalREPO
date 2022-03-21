/**
 * Created on August 23, 2006
 */
package com.manulife.pension.ps.service.withdrawal.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.withdrawal.dao.LoanAndWithdrawalDAO;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalReportData;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.SecurityHelper;

/**
 * The Loan & Withdrawal Report Handler.
 * 
 * @author Mihai Popa
 */
public final class LoanAndWithdrawalReportHandler implements ReportHandler {  
	
	/**
	 * Gets the report data.
	 * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {

		LoanAndWithdrawalReportData reportData = null;
        Integer contractId = (Integer) criteria
				.getFilterValue(LoanAndWithdrawalReportData.FILTER_CONTRACT_NUMBER);
        boolean returnEmptyList = false;        
        LoanSettings loanSettings = null;
        
        if (contractId != null) {
        	
			loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(
					contractId);			
			if (loanSettings == null) {
				returnEmptyList = true;
			} else {

				String roleCode = (String) criteria.getFilterValue(LoanAndWithdrawalReportData.FILTER_ROLE_CODE);
				if (StringUtils.equals(roleCode, "TPA")) {
					
					Principal principal = (Principal) criteria
							.getFilterValue(LoanAndWithdrawalReportData.FILTER_PRINCIPAL);
					UserRole roleForContract = SecurityHelper
							.getUserRoleForContract(principal, principal
									.getRole(), contractId);
					
					// if there are no roles for this contract or no permission for
					// view withdrawal or view loans
					if (roleForContract == null
							|| (!roleForContract
									.hasPermission(PermissionType.VIEW_ALL_WITHDRAWALS) && !roleForContract
									.hasPermission(PermissionType.VIEW_ALL_LOANS))) {

						returnEmptyList = true;
					} else {
						// populate the request type again based on the loan and withdrawal csf information

						String requestType = (String) criteria
								.getFilterValue(LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE);
						ContractServiceFeature withdrawalsCSF = null;

						try {
							withdrawalsCSF = ContractServiceDelegate
									.getInstance()
									.getContractServiceFeature(
											contractId,
											ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
						} catch (ApplicationException e) {
							 ExceptionHandlerUtility.handleApplicationException(e);
						}
						
						

						String OnlineLoanCsfValue = "N";
						String onlineWithdrawalCsfValue = "N";
						
						if (withdrawalsCSF != null) {
							onlineWithdrawalCsfValue = withdrawalsCSF.getValue();
						}
						
						if (loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()) {
							OnlineLoanCsfValue = "Y";
						}

						if ("L".equals(requestType)) {
							
							if ("N".equals(OnlineLoanCsfValue)) {
								returnEmptyList = true;
							}
						} else if ("W".equals(requestType)) {

							if ("N".equals(onlineWithdrawalCsfValue)) {
								returnEmptyList = true;
							}
						} else if ("WL".equals(requestType)) {
							
							if (("N".equals(onlineWithdrawalCsfValue) && "N"
									.equals(OnlineLoanCsfValue))) {
								
								returnEmptyList = true;
							} else if ("Y".equals(OnlineLoanCsfValue)
									&& "N".equals(onlineWithdrawalCsfValue)) {
								
								criteria.addFilter(	LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE, "L");
							} else if ("N".equals(OnlineLoanCsfValue)
									&& "Y".equals(onlineWithdrawalCsfValue)) {
								
								criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE,	"W");
							}
						}
					}
				}
			}
		}
		
        if (returnEmptyList) {
            reportData = new LoanAndWithdrawalReportData(criteria, 0);
            reportData.setDetails(new ArrayList<LoanAndWithdrawalItem>());
        } else {
            reportData = LoanAndWithdrawalDAO.getReportData(criteria);
            List details = (List) reportData.getDetails();
            filter(details, criteria);
            // sort(details, criteria.getSorts());
            // reportData.setDetails(page(details, criteria));
            reportData.setDetails(details);
        }
        
		return reportData;	
	}

	
	
	/**
	 * Return only the items for the current page and page size.
	 * 
	 * @param items the list to paginate
	 * @param criteria to get the paging info from
	 * 
	 * @return a list of page details
	 */
	private static List page(List items, ReportCriteria criteria) {
		List pageDetails = new ArrayList();
		if (items != null) {
		    if (criteria.getPageSize() == ReportCriteria.NOLIMIT_PAGE_SIZE) {
		        pageDetails.addAll(items);
		    } else {
		    
		        for (int i = criteria.getStartIndex() - 1; 
		        i < criteria.getPageNumber() * criteria.getPageSize() && i < items.size(); i++) {
		            pageDetails.add(items.get(i));
		        }
			}
		}
		return pageDetails;
	}

	/**
	 * Sort the list according to the required sort order.
	 * 
	 * @param items the list of items to sort
	 * @param sorts the sorting fields list
	 */
	private static void sort(List items, ReportSortList sorts) {
		
		if (items != null && sorts != null && sorts.size() != 0) {
			ReportSort firstSort = (ReportSort) sorts.get(0);
			Collections.sort(items, 
					LoanAndWithdrawalItem.getComparator(firstSort.getSortField(), firstSort.getSortDirection()));
		}
	}

	/**
	 * Apply non-DB level filters.
	 * 
	 * @param items the list of items to filter
	 * @param criteria the fiter criteria
	 * @return the filtered list.
	 */
	private static void filter(List items, ReportCriteria criteria) {

		//FIXME -- implement
	}

}