package com.manulife.pension.platform.web.contract;


import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.documents.model.CoFid321DocumentInfo;
import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.DocumentInfo;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.ereports.ContractDocListRequest;
import com.manulife.pension.lp.model.ereports.ContractDocListResponse;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.util.DataUtility;
import com.manulife.pension.ps.cache.CofidProperties;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.contract.valueobject.Contract;

/**
 * Action helper class for CoFid Contract Documents details.
 * 
 * @author Sreenivasa Koppula
 *
 */

public class ContractDocumentsHelper {

	 public static final Logger logger = Logger.getLogger(ContractDocumentsHelper.class);
	 
	 private static final String DOCT_TYPE = ".pdf";
	 private static final String CONTRACT_PDF_FILENAME_SEPARATOR = "_";
	 public static final String COFID_CONTRACT_DOCUMENT = "coFidContractDoc";
	 public static final String MIME_TYPE_PDF = "application/pdf";
	 public static final String OPT_OUT_IND_YES= "Y";
	 public static final String OPT_OUT_IND_NO= "N";

	 private ContractDocumentsHelper() {
		 
	 }
	 
	 public static ContractDocumentsHelper getInstance() {
		 return new ContractDocumentsHelper();
	 }
	 
	/**
	 * Returns all the ContractDocuments for this contract.
	 * @param contract
	 * @return ContractDocumentInfo
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 */
	public static ContractDocumentInfo[] getContractDocuments(Contract contract)
			throws SystemException, ServiceUnavailableException {

		EReportsServiceDelegate delegate = EReportsServiceDelegate
				.getInstance();
		ContractDocListResponse contractDocListResponse;
		
		// prepares request parameters
		ContractDocListRequest contractDocListRequest = new ContractDocListRequest();
		contractDocListRequest.setContractNumber((new Integer(contract
				.getContractNumber())).toString());

		// logg the call to eReports service
		if (logger.isDebugEnabled())
			logger.debug("calling eReports service with the following param: "
								+ ";" + contractDocListRequest.getContractNumber());

		contractDocListResponse = delegate
				.getContractDocList(contractDocListRequest);
		
		return contractDocListResponse.getContractDocList();
	}
	
	/**
	 * Method to populate the quarterly PDF document for selected contract.
	 * 
	 * @param contract
	 */
	public static void getPdfContractDocument(HttpServletRequest request, HttpServletResponse response,
			Contract currentContract) throws SystemException, ServiceUnavailableException{
		String fileName = null;
		
		if(null!=request.getParameter(COFID_CONTRACT_DOCUMENT))
		{
			fileName= request.getParameter(COFID_CONTRACT_DOCUMENT);
		} 
		
		
		if (logger.isDebugEnabled()) {
			logger.debug(fileName);
		}
		
		DocumentInfo documentInfo = new CoFid321DocumentInfo(String
				.valueOf(currentContract.getContractNumber()),
				fileName, StringUtils.EMPTY);
		
		DocumentFileOutput documentFileOutput = EReportsServiceDelegate
		.getInstance().getDocument(documentInfo);
		
		setFileToResponse(request, response, fileName, documentFileOutput
				.getReportFragment(), MIME_TYPE_PDF);	
	}
	
	public ContractDocumentInfo getNoticePdfContractDocumentInfo(Contract contract,
			Date cofidMontEndDate) throws SystemException, ServiceUnavailableException{
		
		ContractDocumentInfo[] contractDocuments  = getContractDocuments(contract);	
		ContractDocumentInfo participantNoticeDocumentInfo = null;
		
		for(ContractDocumentInfo contractDocument : contractDocuments) {
			if(StringUtils.equals(contractDocument.getDocumentType(), RequestConstants.DOCUMENT_TYPE_COFID)
					&& StringUtils.equals(StringUtils.trimToEmpty(contractDocument.getDocumentSubType()), "REC")
					&& cofidMontEndDate.compareTo(contractDocument.getDocumentCreatedDate()) == 0) {
			
				participantNoticeDocumentInfo = contractDocument;
				break;
			}
		}
		
		
		return participantNoticeDocumentInfo;
		
	}
	
	public ContractServiceDelegate getContractServiceDelegate(){
		return ContractServiceDelegate.getInstance();
	}
	
	public void getNoticePdfContractDocument(HttpServletRequest request, HttpServletResponse response,
			Contract currentContract) throws SystemException, ServiceUnavailableException{
		Date cofidMontEndDate = getContractServiceDelegate()
					.getCoFidLastQuarterEndDate();

		ContractDocumentInfo participantNoticeDocumentInfo = getNoticePdfContractDocumentInfo(
				currentContract, cofidMontEndDate);
		
		//get the name from property file by using participantNoticeDocumentInfo.getPartyIdentifierCode().
		String fileName = CofidProperties
				.getInstance()
				.getProperty(
						MessageFormat
								.format(CofidProperties.PARTICIPANT_NOTICE_FILE_NAME_PREFIX,
										participantNoticeDocumentInfo
												.getPartyIdentifierCode()));

		StringBuffer eReportsFileName  = new StringBuffer();
		eReportsFileName.append(fileName);
		eReportsFileName.append(DataUtility.yearOfCurrentQuarter(cofidMontEndDate));
		eReportsFileName.append(CONTRACT_PDF_FILENAME_SEPARATOR);
		eReportsFileName.append(currentContract.getContractNumber());
		eReportsFileName.append(DOCT_TYPE);
		
		DocumentInfo documentInfo = new CoFid321DocumentInfo(String
				.valueOf(currentContract.getContractNumber()),
				eReportsFileName.toString(), StringUtils.EMPTY);
		
		DocumentFileOutput documentFileOutput = EReportsServiceDelegate
		.getInstance().getDocument(documentInfo);
		
		setFileToResponse(request, response, eReportsFileName.toString(), documentFileOutput
				.getReportFragment(), MIME_TYPE_PDF);
			
	}
	
	/**
	 * Writes the byte[] to the response stream
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @param downloadData
	 * @param contentType
	 * 
	 * @throws SystemException
	 */
	public static void setFileToResponse(HttpServletRequest request,
			HttpServletResponse response, String fileName, byte[] downloadData,
			String contentType) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> setFileToResponse");
		}
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setContentType(contentType);

		String userAgent = request.getHeader("User-Agent");

		if (userAgent == null || userAgent.indexOf("MSIE 5") < 0) {
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
		}

		response.setContentLength(downloadData.length);
		try {
			response.getOutputStream().write(downloadData);
		} catch (IOException ioException) {
			throw new SystemException(ioException,
					"Exception writing downloadData.");
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException ioException) {
				throw new SystemException(ioException,
						"Exception closing output stream.");
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- setFileToResponse");
		}
	}
	
	/**
	 * Method to check Participant Notice Document is Available.
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @param downloadData
	 * @param contentType
	 * 
	 * @throws SystemException
	 */
	public boolean isParticipantNoticeDocumentAvailable(int contractId,
			Date cofidMontEndDate, HttpServletRequest request) throws SystemException {

		return Boolean
				.valueOf(ContractServiceDelegate.getInstance()
						.isContractDocAvailable(String.valueOf(contractId),
								cofidMontEndDate,
								RequestConstants.DOCUMENT_TYPE_COFID, "REC"));

	}
}
