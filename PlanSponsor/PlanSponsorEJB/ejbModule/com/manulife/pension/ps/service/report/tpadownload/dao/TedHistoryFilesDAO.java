package com.manulife.pension.ps.service.report.tpadownload.dao;

import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesItem;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesReportData;
import com.manulife.pension.service.contract.report.dao.SelectContractDAO;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.utility.ConversionUtility;

public class TedHistoryFilesDAO extends BaseDatabaseDAO {

	private static final String className = TedHistoryFilesDAO.class.getName();
    private static final Logger logger = Logger.getLogger(TedHistoryFilesDAO.class);

	//Make sure nobody instantiates this class
	private TedHistoryFilesDAO()
	{
	}

   	public static ReportData getReportData(ReportCriteria criteria) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("enter -> getReportData");
		}

		TedHistoryFilesReportData data = new TedHistoryFilesReportData(criteria, 0);
    	ArrayList files = new ArrayList();  // initialize so no nulls
		data.setDetails(files);  // initialize so no nulls
		int numSuccessful = 0;
    	TedFTPHelper ftpService=null;
    	try {
        	// Check that the FTP server is available first;
        	try {
        		ftpService = new TedFTPHelper(Boolean.parseBoolean(TedFTPServer.getTedFtpInd()));
        		ftpService.connect();
        	} catch (Exception e) {
				data.setReturnCode(TedHistoryFilesReportData.STATUS_FTP_SERVER_DOWN);
				logger.error("Cannot establish connection to the TED FTP Server",e);
				return data;
        	}

    		String contractNumber = (String)criteria.getFilterValue(TedHistoryFilesReportData.FILTER_CONTRACT_NUMBER);
    		if (contractNumber != null && !contractNumber.trim().equals("")) {
    			contractNumber = contractNumber.trim();
    			data.setContractNumber(contractNumber);
        		// get a list of contracts available to TPA
    			SelectContract[] contracts = getContracts((String)criteria.getFilterValue(TedHistoryFilesReportData.FILTER_PROFILE_ID), (String)criteria.getFilterValue(TedHistoryFilesReportData.FILTER_SITE_LOCATION));
        		if (contracts != null && contracts.length > 0) {
        			boolean contained = false;

        			for (int i=0; i < contracts.length; i++) {
        				if (String.valueOf(contracts[i].getContractNumber()).equals(contractNumber)) {
        					contained=true;
        					data.setContractName(contracts[i].getContractName());
        					break;
        				}

        			}

        			if (contained) {
        				String[] listing=null;
        				try {
			        		ftpService.connect();
			        		listing = ftpService.getDirectoryListing(TedFTPServer.getTpaEDownloadFilesDir() + "\\" + contractNumber);
			        		ftpService.disconnect();
			        		if (listing!=null) {
			        			for(int i = 0; i < listing.length; i++) {
			        				TedHistoryFilesItem historyFile = ftpService.parseOutFileDetails(listing[i]);
			        				if (historyFile != null) {
			        					files.add(historyFile);
			        					numSuccessful ++;
			        				}
			        			}
			        			if (files.size() == 0) {
			        				data.setReturnCode(TedHistoryFilesReportData.STATUS_NO_HISTORY_FILES_AVAILABLE);
			        			}
			        		} else {
			        			data.setReturnCode(TedHistoryFilesReportData.STATUS_NO_HISTORY_FILES_AVAILABLE);
			        		}
        				} catch (Throwable t) {
        					if (listing != null) {
        						throw new SystemException(t, className, "getReportData", "Unable to obtain or the directory listing from the TED FTP Server.");
        					} else {
        						throw new SystemException(t, className, "getReportData", "Unable to parse the directory listing retrieved from the TED FTP Server.");
        					}
        				}
        			} else {
        				data.setReturnCode(TedHistoryFilesReportData.STATUS_NO_ACCESS_TO_CONTRACT);
        			}
        		} else {
        			data.setReturnCode(TedHistoryFilesReportData.STATUS_NO_CONTRACT_NUMBER_PROVIDED);
        		}
    		}
    	} catch (Throwable e) {
    		throw new SystemException(e, className, "getReportData", "And unexpected error occurred.");
    	} finally {
    		try {
    			ftpService.disconnect();
    		} catch (Exception z) {
    			// eat the exception
    		}
    	}
    	ArrayList sortedFiles = sortData(files, criteria);
		data.setDetails(sortedFiles);
		data.setTotalCount(sortedFiles.size());

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> getReportData");
		}

		return data;
	}

	public static SelectContract[] getContracts(String profileId, String siteLocation) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getContracts");
		}

		SelectContract[] contractDetails = new SelectContract[0];
		ReportCriteria criteria = new ReportCriteria(SelectContractReportData.REPORT_ID);
		//set report criteria
		criteria.insertSort("contractName", ReportSort.ASC_DIRECTION);
		criteria.setPageSize(9999);
		criteria.addFilter(SelectContractReportData.FILTER_CLIENT_ID, profileId);
		criteria.addFilter(SelectContractReportData.FILTER_SITE_LOCATION, siteLocation);
		criteria.addFilter(SelectContractReportData.FILTER_USER_ROLE, ConversionUtility.convertToStoredProcRole(new ThirdPartyAdministrator()));
		criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, null);

		try {
			ReportData vo = SelectContractDAO.getReportData(criteria);
			if (vo != null && vo.getDetails() != null) {
				contractDetails = (SelectContract[])vo.getDetails().toArray(new SelectContract[0]);
			}
		} catch (Exception e) {
			throw new SystemException(e, className, "getContracts", "Problem occurred during SelectContractDAO.getReportData() call.");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getContracts");
		}
		return contractDetails;
	}

	public static ArrayList sortData(ArrayList list, ReportCriteria criteria) throws SystemException {
		ArrayList results;
		if (logger.isDebugEnabled()) {
			logger.debug("enter -> sortData");
		}
		try {
			String sortField = criteria.getSorts().get(0).getSortField();
	    	String sortDirection = criteria.getSorts().get(0).getSortDirection();
	    	TedHistoryFileComparator comparator = new TedHistoryFileComparator(sortField, sortDirection);
	    	TreeSet treeSet = new TreeSet(comparator);
	    	treeSet.addAll(list);
	    	while (treeSet.size() > 9) {
	    		Object item = treeSet.last();
	    		treeSet.remove(item);
	    	}
			results = new ArrayList(treeSet.size());
			results.addAll(treeSet);

		} catch (Exception e) {
			throw new SystemException(e, className, "sortData", "Problem occurred during the programatic sort of the history files.");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- sortData");
		}
		return results;
	}



}

