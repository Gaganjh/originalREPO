package com.manulife.pension.platform.web.delegate;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.DocumentInfo;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.fundcheck.model.FundCheckDocFileRequest;
import com.manulife.pension.fundcheck.model.FundCheckDocFileResponse;
import com.manulife.pension.fundcheck.model.FundCheckDocListRequest;
import com.manulife.pension.fundcheck.model.FundCheckDocListResponse;
import com.manulife.pension.lp.bos.ereports.ReportService;
import com.manulife.pension.lp.bos.ereports.common.ContractDocumentNotFoundException;
import com.manulife.pension.lp.bos.ereports.common.EReportsApplicationException;
import com.manulife.pension.lp.bos.ereports.common.EReportsSystemException;
import com.manulife.pension.lp.bos.ereports.common.ReportFileNotFoundException;
import com.manulife.pension.lp.bos.ereports.common.RequestValidationException;
import com.manulife.pension.lp.model.ereports.ContractDocFileRequest;
import com.manulife.pension.lp.model.ereports.ContractDocFileResponse;
import com.manulife.pension.lp.model.ereports.ContractDocListRequest;
import com.manulife.pension.lp.model.ereports.ContractDocListResponse;
import com.manulife.pension.lp.model.ereports.ReportFileRequest;
import com.manulife.pension.lp.model.ereports.ReportFileResponse;
import com.manulife.pension.lp.model.ereports.ReportListRequest;
import com.manulife.pension.lp.model.ereports.ReportListResponse;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.log.LogUtility;
/**
 * Service delegate for eReports. Communicates to the Report Server via RMI in
 * order to confirm existence and retrieve the particular report
 *
 * @author drotele LS - 2004-04-06 Exceptions
 */

public class EReportsServiceDelegate extends AbstractRMIServiceDelegate {
    
    private static final Logger logger = Logger.getLogger(EReportsServiceDelegate.class);
	
    private static EReportsServiceDelegate instance = new EReportsServiceDelegate();

    public EReportsServiceDelegate() {
        super();
    }

    /**
     * @return
     */
    public static EReportsServiceDelegate getInstance() {
        return instance;
    }

	private final static String RMI_SERVICE_NAME = "ReportService";

	private static final String SECURED_REPORT_SERVICE_PORT = "SecuredReportServicePort";

	/*
	 * (non-Javadoc)
	 *
	 * @see com.manulife.pension.ps.web.delegate.AbstractRMIServiceDelegate#getReportServiceName()
	 */
	protected String getServiceName() {
		return RMI_SERVICE_NAME;
	}
	/*
	 * This method is used to get the ReportService Port Number 
	 * */
	protected int getPortNumber() {
	    String portNumber=  new BaseEnvironment().getNamingVariable(SECURED_REPORT_SERVICE_PORT, null);
		return  Integer.parseInt(portNumber);
    }
	 
	/**
	 * Retrieves the List of available reports from eReportServer
	 *
	 * @throws SystemException
	 */
	public ReportListResponse getReportList(ReportListRequest listRequest)
			throws SystemException, ServiceUnavailableException {
	if (logger.isDebugEnabled() )
			logger.debug("entry -> getReportList");
		ReportListResponse list = null;
		try {
			ReportService reportService = (ReportService) getRemoteService();
			list = reportService.getReportList(listRequest);

		} catch (EReportsSystemException e) {
			throw new SystemException(e, this.getClass().getName(),
					"getReportList",
					"EReportsSystemException occurred. Inputs ReportFileRequest:"
							+ listRequest);
		} catch (RequestValidationException e) {
			throw new SystemException(e, this.getClass().getName(),
					"getReportList",
					"EReportsSystemException occurred. Inputs ReportFileRequest:"
							+ listRequest);
		} catch (RemoteException e) {
			ServiceUnavailableException rse = new ServiceUnavailableException(
					e, getClass().getName(), "getReportList",
					"Failed to lookup report service [" + getServiceName()
							+ "]");
			LogUtility.logApplicationException(rse);
			throw rse;
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"getReportList",
					"getREportList call failed on ReportService inputs:"
							+ listRequest.toString());
		}

			if (logger.isDebugEnabled() )
			logger.debug("exit <- getReportList");
		return list;
	}

	/**
	 * Retrieves a specific report from eReportServer
	 *
	 * This method returns the complete report as opposed to a fraction of it
	 * which the original service method did
	 *
	 */
	public ReportFileResponse getReportFile(ReportFileRequest request)
			throws SystemException, ServiceUnavailableException, ReportFileNotFoundException {

		byte[] buf = null;
		//int buffLenght = 128 * 1024;
		ReportFileResponse response = null;
		request.setReadLength(128 * 1024);
		request.setReadStart(0); //always start from the beginning

	if (logger.isDebugEnabled() )
			logger.debug("entry -> getReportFile");

		try {
			ReportService reportService = (ReportService) getRemoteService();
			response = reportService.getReportFile(request);

			// create a byte output stream based on the file lenght
			ByteArrayOutputStream bo = new ByteArrayOutputStream(response
					.getLength());
			if (response != null && response.getFileName() != null) {

				// read data from the remote file
				while (response.getLength() != -1) {
					// write bytes to the buffer
					bo.write(response.getReportFragment());
					request.setReadStart(request.getReadStart()
							+ response.getLength());
					// read next chunk
					response = reportService.getReportFile(request);
				}
				// get the ByteArray from the straem
				buf = bo.toByteArray();
				bo.close();
			}
			// update the response properties to reflect that the whole file is
			// in response now
			response.setLength(buf.length);
			response.setReportFragment(buf);
		} catch (RemoteException e) {
			ServiceUnavailableException rse = new ServiceUnavailableException(
					e, getClass().getName(), "getReportList",
					"Failed to lookup report service [" + getServiceName()
							+ "]");
			LogUtility.logApplicationException(rse);
			throw rse;
		} catch (java.io.IOException e) {
			throw new SystemException(e, this.getClass().getName(),
					"getReportFile",
					"IOException occurred. Inputs ReportFileRequest:" + request);
		} catch(ReportFileNotFoundException e) {
			throw e;
		} catch (EReportsApplicationException e) {
			throw new SystemException(e, this.getClass().getName(),
					"getReportFile",
					"EReportsApplicationException occurred. Inputs ReportFileRequest:"
							+ request);
		} catch (EReportsSystemException e) {
			throw new SystemException(e, this.getClass().getName(),
					"getReportFile",
					"EReportsSystemException occurred. Inputs ReportFileRequest:"
							+ request);
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"getReportFile",
					"getREportList call failed on ReportService inputs:"
							+ request);
		}
			if (logger.isDebugEnabled() )
			logger.debug("exit <- getReportFile");
		return response;
	}
	
	/**
	 * This method calls the e-report service and fetches the list of Contract and Amendment docs 
	 * available for a particular ContractNumber.
	 * 
	 * @param contractDocListRequest
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 * @return ContractDocListResponse
	 */
	public ContractDocListResponse getContractDocList(ContractDocListRequest contractDocListRequest) 
	                   throws SystemException, ServiceUnavailableException {
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> getContractDocList");
		
		ContractDocListResponse contractDocListResponse = null;
		
		try {
			ReportService reportService = (ReportService) getRemoteService();
			contractDocListResponse = reportService.getContractDocList(contractDocListRequest);

		} catch (EReportsSystemException e) {
			throw new SystemException(e, this.getClass().getName(),
					"getContractDocList",
					"EReportsSystemException occurred. Inputs ContractDocListRequest:"
							+ contractDocListRequest);
		} catch (RequestValidationException e) {
			throw new SystemException(e, this.getClass().getName(),
					"getContractDocList",
					"EReportsSystemException occurred. Inputs ContractDocListRequest:"
							+ contractDocListRequest);
		} catch (RemoteException e) {
			ServiceUnavailableException serviceUnavailableException = new ServiceUnavailableException(
					e, getClass().getName(), "getContractDocList",
					"Failed to lookup report service [" + getServiceName()
							+ "]");
			LogUtility.logApplicationException(serviceUnavailableException);
			throw serviceUnavailableException;
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"getContractDocList",
					"getContractDocList call failed on ReportService inputs:"
							+ contractDocListRequest.toString());
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getContractDocList");

		return contractDocListResponse;
	}
	
	/**
	 * This method is used to read the Contract/Amendment Pdf document from the ereport folder.
	 * 
	 * @param contractDcoFileRequest
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 * @throws ContractDocumentNotFoundException
	 * @return ContractDocFileResponse
	 */
	public ContractDocFileResponse getContractDocFile(ContractDocFileRequest contractDcoFileRequest)
	                   throws SystemException, ServiceUnavailableException, ContractDocumentNotFoundException {

		byte[] buffer = null;
		ContractDocFileResponse contractDocFileResponse = null;
		//contractDcoFileRequest.setReadLength(128 * 1024);
		contractDcoFileRequest.setReadLength(2048 * 1024);//CL # 93712. Increasing fragment size to 2 MB for improving performance. Avg Contract Pdf size could be 1.3 Mb. 
		contractDcoFileRequest.setReadStart(0);

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getContractDocFile");
		}

		try {
			ReportService reportService = (ReportService) getRemoteService();
			contractDocFileResponse = reportService.getContractDocFile(contractDcoFileRequest);

			// create a byte output stream based on the file length
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(contractDocFileResponse
					.getLength());
			if (contractDocFileResponse != null && contractDocFileResponse.getFileName() != null) {

				// read data from the remote file
				while (contractDocFileResponse.getLength() != -1) {
					// write bytes to the buffer
					byteArrayOutputStream.write(contractDocFileResponse.getReportFragment());
					contractDcoFileRequest.setReadStart(contractDcoFileRequest
							.getReadStart()
							+ contractDocFileResponse.getLength());
					// read next chunk
					contractDocFileResponse = reportService
							.getContractDocFile(contractDcoFileRequest);
				}
				// get the ByteArray from the stream
				buffer = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.close();
			}
			// update the response properties to reflect that the whole file is
			// in response now
			contractDocFileResponse.setLength(buffer.length);
			contractDocFileResponse.setReportFragment(buffer);
		} catch (RemoteException remoteException) {
			ServiceUnavailableException serviceUnavailableException = new ServiceUnavailableException(
					remoteException, getClass().getName(), "getContractDocFile",
					"Failed to lookup report service [" + getServiceName()
							+ "]");
			LogUtility.logApplicationException(serviceUnavailableException);
			throw serviceUnavailableException;
		} catch (java.io.IOException ioException) {
			throw new SystemException(ioException, this.getClass().getName(),
					"getContractDocFile",
					"IOException occurred. Inputs ContractDocFileRequest:"
							+ contractDcoFileRequest);
		} catch (ContractDocumentNotFoundException contractDocumentNotFoundException) {
			throw contractDocumentNotFoundException;
		} catch (EReportsApplicationException eReportsApplicationException) {
			throw new SystemException(eReportsApplicationException, this.getClass().getName(),
					"getContractDocFile",
					"EReportsApplicationException occurred. Inputs ContractDocFileRequest:"
							+ contractDcoFileRequest);
		} catch (EReportsSystemException eReportsSystemException) {
			throw new SystemException(eReportsSystemException, this.getClass().getName(),
					"getContractDocFile",
					"EReportsSystemException occurred. Inputs ContractDocFileRequest:"
							+ contractDcoFileRequest);
		} catch (Exception exception) {
			throw new SystemException(exception, this.getClass().getName(),
					"getContractDocFile",
					"getContractDocFile call failed on ReportService inputs:"
							+ contractDcoFileRequest);
		}
		
		if (logger.isDebugEnabled())
			logger.debug("exit <- getContractDocFile");

			return contractDocFileResponse;
	}
	
	/**
	 * This method returns all the pdf related info for the given contract. If
	 * both current and previous pdfs available both info will be returned or
	 * only the info related to the available pdf will be returned.
	 * 
	 * @param fundCheckDocListRequest
	 * @return FundCheckDocListResponse
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 */
	public FundCheckDocListResponse getContractFundCheckDocList(
			FundCheckDocListRequest fundCheckDocListRequest)
			throws SystemException, ServiceUnavailableException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> getFundCheckDocList");

		FundCheckDocListResponse fundCheckDocListResponse = null;

		try {
			ReportService reportService = (ReportService) getRemoteService();
			fundCheckDocListResponse = reportService
					.getContractFundCheckDocList(fundCheckDocListRequest);
		} catch (EReportsSystemException e) {
			logger.info("Exception in method getContractFundCheckDocList"+e.getMessage() );
			throw new SystemException(
					e,
					"EReportsSystemException occurred in getContractFundCheckDocList(). Inputs ContractFundCheckDocList: "
							+ fundCheckDocListRequest);
		} catch (RemoteException e) {
			logger.info("Exception in method getContractFundCheckDocList"+e.getMessage() );
			ServiceUnavailableException serviceUnavailableException = new ServiceUnavailableException(
					e, getClass().getName(), "getContractFundCheckDocList",
					"Failed to lookup report service [" + getServiceName()
							+ "]");
			LogUtility.logApplicationException(serviceUnavailableException);
			throw serviceUnavailableException;
		}

		catch (Exception e) {
			logger.info("Exception in method getContractFundCheckDocList"+e.getMessage() );
			throw new SystemException(e,
					"getFundCheckDocList call failed on ReportService inputs:"
							+ fundCheckDocListRequest.toString());
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getContractFundCheckDocList");

		return fundCheckDocListResponse;

	}
	
	/**
	 * This method fetches and returns the fundcheck pdf from eReports
	 * for the given contract.
	 * 
	 * @param fundCheckDcoFileRequest
	 * @return FundCheckDocFileResponse
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 * @throws FundCheckDocumentNotFoundException
	 */
	//TODO: also test for some PDFs more than 2MB size
	public FundCheckDocFileResponse getContractFundCheckDocFile(
			FundCheckDocFileRequest fundCheckDcoFileRequest)
			throws SystemException, ServiceUnavailableException
			 {

		byte[] buffer = null;
		FundCheckDocFileResponse fundCheckDocFileResponse = null;
		fundCheckDcoFileRequest.setReadLength(2048 * 1024);// CL # 93712.
		// Increasing fragment size to 2 MB for improving
		// performance. Avg Contract Pdf size could be 1.3 Mb.
		fundCheckDcoFileRequest.setReadStart(0);

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getContractFundCheckDocFile");
		}

		try {
			ReportService reportService = (ReportService) getRemoteService();
			fundCheckDocFileResponse = reportService
					.getContractFundCheckDocFile(fundCheckDcoFileRequest);

			// create a byte output stream based on the file length
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					fundCheckDocFileResponse.getLength());
			if (fundCheckDocFileResponse != null
					&& fundCheckDocFileResponse.getFileName() != null) {

				// read data from the remote file
				while (fundCheckDocFileResponse.getLength() != -1) {
					// write bytes to the buffer
					byteArrayOutputStream.write(fundCheckDocFileResponse
							.getReportFragment());
					fundCheckDcoFileRequest
							.setReadStart(fundCheckDcoFileRequest
									.getReadStart()
									+ fundCheckDocFileResponse.getLength());
					// read next chunk
					fundCheckDocFileResponse = reportService
							.getContractFundCheckDocFile(fundCheckDcoFileRequest);
				}
				// get the ByteArray from the stream
				buffer = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.close();
			}
			// update the response properties to reflect that the whole file is
			// in response now
			if(buffer == null){
				buffer = new byte[0];
			}
			fundCheckDocFileResponse.setLength(buffer.length);
			fundCheckDocFileResponse.setReportFragment(buffer);
		} catch (RemoteException remoteException) {
			logger.info("Exception in method getContractFundCheckDocFile"+remoteException.getMessage() );
			ServiceUnavailableException serviceUnavailableException = new ServiceUnavailableException(
					remoteException, getClass().getName(),
					"getContractFundCheckDocFile", "Failed to lookup report service ["
							+ getServiceName() + "]");
			LogUtility.logApplicationException(serviceUnavailableException);
			throw serviceUnavailableException;
		}  catch (EReportsApplicationException eReportsApplicationException) {
			logger.info("Exception in method getContractFundCheckDocFile"+eReportsApplicationException.getMessage() );
			throw new SystemException(
					eReportsApplicationException,
					"EReportsApplicationException occurred in getContractFundCheckDocFile(). Inputs FundCheckDcoFileRequest:"
							+ fundCheckDcoFileRequest);
		} catch (EReportsSystemException eReportsSystemException) {
			logger.info("Exception in method getContractFundCheckDocFile"+eReportsSystemException.getMessage() );
			throw new SystemException(
					eReportsSystemException,
					"EReportsSystemException occurred in getContractFundCheckDocFile(). Inputs FundCheckDcoFileRequest:"
							+ fundCheckDcoFileRequest);
		} catch (Exception exception) {
			logger.info("Exception in method getContractFundCheckDocFile"+exception.getMessage() );
			throw new SystemException(exception,
					"getContractFundCheckDocFile call failed on ReportService inputs:"
							+ fundCheckDcoFileRequest);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getContractFundCheckDocFile");

		return fundCheckDocFileResponse;
	}

	/**
	 * This method returns all the pdf related info for the given producer code. If
	 * both current and previous pdfs available both info will be returned or
	 * only the info related to the available pdf will be returned.
	 * 
	 * @param fundCheckDocListRequest
	 * @return FundCheckDocListResponse
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 */
	
	public FundCheckDocListResponse getProducerFundCheckDocList(
			FundCheckDocListRequest fundCheckDocListRequest)
			throws SystemException, ServiceUnavailableException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> getFundCheckDocList");

		FundCheckDocListResponse fundCheckDocListResponse = null;

		try {
			ReportService reportService = (ReportService) getRemoteService();
			fundCheckDocListResponse = reportService
					.getProducerFundCheckDocList(fundCheckDocListRequest);
		} catch (EReportsSystemException e) {
			logger.info("Exception in method getProducerFundCheckDocList"+e.getMessage() );
			throw new SystemException(
					e,
					"EReportsSystemException occurred in getProducerFundCheckDocList(). Inputs fundCheckDocListRequest: "
							+ fundCheckDocListRequest);
		}catch (RemoteException e) {
			logger.info("Exception in method getProducerFundCheckDocList"+e.getMessage() );
			ServiceUnavailableException serviceUnavailableException = new ServiceUnavailableException(
					e, getClass().getName(), "getProducerFundCheckDocList",
					"Failed to lookup report service [" + getServiceName()
							+ "]");
			LogUtility.logApplicationException(serviceUnavailableException);
			throw serviceUnavailableException;
		}

		catch (Exception e) {
			logger.info("Exception in method getProducerFundCheckDocList"+e.getMessage() );
			throw new SystemException(e,
					"getProducerFundCheckDocList call failed on ReportService inputs:"
							+ fundCheckDocListRequest.toString());
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getProducerFundCheckDocList");

		return fundCheckDocListResponse;

	}
	
	/**
	 * This method fetches and returns the fundcheck pdf from eReports for
	 * the given producer code.
	 * 
	 * @param fundCheckDcoFileRequest
	 * @return FundCheckDocFileResponse
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 * @throws ContractDocumentNotFoundException
	 */
	
	public FundCheckDocFileResponse getProducerFundCheckDocFile(
			FundCheckDocFileRequest fundCheckDcoFileRequest)
			throws SystemException, ServiceUnavailableException
			 {

		byte[] buffer = null;
		FundCheckDocFileResponse fundCheckDocFileResponse = null;
		fundCheckDcoFileRequest.setReadLength(2048 * 1024);// CL # 93712.
		// Increasing fragment size to 2 MB for improving
		// performance. Avg Contract Pdf size could be 1.3 Mb.
		fundCheckDcoFileRequest.setReadStart(0);

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getProducerFundCheckDocFile");
		}

		try {
			ReportService reportService = (ReportService) getRemoteService();
			fundCheckDocFileResponse = reportService
					.getProducerFundCheckDocFile(fundCheckDcoFileRequest);

			// create a byte output stream based on the file length
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					fundCheckDocFileResponse.getLength());
			if (fundCheckDocFileResponse != null
					&& fundCheckDocFileResponse.getFileName() != null) {
//TODO SSA to handle the case when response is null (PDF not found) Although not handled in methods above, if fundCheckDocFileResponse or fundCheckDocFileResponse.getFileName() are null, wouldn't that warrant an exception? Callers to this should always pass a valid contractNumber or producerCode otherwise the method should throw an IllegalArgumentException. This is because this method is called after the list of available PDFs has been retrieved and displayed. Another case is if the user alters the request so it tries to open a file which does not belong to the current contract or producer code. Alternately, the javadoc should state that if no PDF is found, null is returned, which should be handled in the action class.
				
				// read data from the remote file
				while (fundCheckDocFileResponse.getLength() != -1) {
					// write bytes to the buffer
// TODO SSA to handle IOException like in similar methods above
					byteArrayOutputStream.write(fundCheckDocFileResponse
							.getReportFragment());
					fundCheckDcoFileRequest
							.setReadStart(fundCheckDcoFileRequest
									.getReadStart()
									+ fundCheckDocFileResponse.getLength());
					// read next chunk
					fundCheckDocFileResponse = reportService
							.getProducerFundCheckDocFile(fundCheckDcoFileRequest);
				}
//TODO SSA to handle case when buffer is null, like in similar methods above
				// get the ByteArray from the stream
				buffer = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.close();
			}
			// update the response properties to reflect that the whole file is
			// in response now
			fundCheckDocFileResponse.setLength(buffer.length);
			fundCheckDocFileResponse.setReportFragment(buffer);
		} catch (RemoteException remoteException) {
			logger.info("Exception in method getProducerFundCheckDocFile"+remoteException.getMessage() );
			ServiceUnavailableException serviceUnavailableException = new ServiceUnavailableException(
					remoteException, getClass().getName(),
					"getProducerFundCheckDocFile", "Failed to lookup report service ["
							+ getServiceName() + "]");
			LogUtility.logApplicationException(serviceUnavailableException);
			throw serviceUnavailableException;
		} catch (EReportsApplicationException eReportsApplicationException) {
			logger.info("Exception in method getProducerFundCheckDocFile"+eReportsApplicationException.getMessage() );
			throw new SystemException(
					eReportsApplicationException,
					"EReportsApplicationException occurred in getProducerFundCheckDocFile(). Inputs FundCheckDcoFileRequest:"
							+ fundCheckDcoFileRequest);
		} catch (EReportsSystemException eReportsSystemException) {
			logger.info("Exception in method getProducerFundCheckDocFile"+eReportsSystemException.getMessage() );
			throw new SystemException(
					eReportsSystemException,
					"EReportsSystemException occurred in getProducerFundCheckDocFile(). Inputs FundCheckDcoFileRequest:"
							+ fundCheckDcoFileRequest);
		} catch (Exception exception) {
			logger.info("Exception in method getProducerFundCheckDocFile"+exception.getMessage() );
			throw new SystemException(exception,
					"getProducerFundCheckDocFile call failed on ReportService inputs:"
							+ fundCheckDcoFileRequest);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getProducerFundCheckDocFile");

		return fundCheckDocFileResponse;
	}
	
	/**
	 * This method the the document PDF from report server
	 * 
	 * @param DocumentInfo
	 * @return DocumentFileOutput
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 */
	public DocumentFileOutput getDocument(DocumentInfo request)
			throws SystemException, ServiceUnavailableException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> getDocument");
		
		DocumentFileOutput output = null;
		try {
			ReportService reportService = (ReportService) getRemoteService();
			output = reportService.getDocument(request);
		} catch (EReportsSystemException e) {
			logger.info("Exception in method getFeeDisclousrePdf"+e.getMessage() );
			throw new SystemException(e, "EReportsSystemException occurred in getDocument(). Inputs getDocument: "
							+ request);
		} catch (RemoteException e) {
			logger.info("Exception in method getDocument" + e.getMessage() );
			ServiceUnavailableException serviceUnavailableException = new ServiceUnavailableException(
					e, getClass().getName(), "getDocument",
					"Failed to lookup report service [" + getServiceName()
							+ "]");
			LogUtility.logApplicationException(serviceUnavailableException);
			throw serviceUnavailableException;
		}catch (Exception e) {
			logger.info("Exception in method getDocument"+e.getMessage() );
			throw new SystemException(e,
					"getDocument call failed on ReportService inputs:"
							+ output.toString());
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getDocument");

		return output;

	}
	
	/**
	 * This method to store the document through report server
	 * 
	 * @param DocumentFileOutput documentFileOutput
	 * @throws SystemException
	 */
	public void saveDocument(DocumentFileOutput documentFileOutput)
			throws SystemException, ServiceUnavailableException {
		logger.debug("entry -> saveDocument");
		try {
			ReportService reportService = (ReportService) getRemoteService();
			reportService.saveDocument(documentFileOutput);
		} catch (EReportsSystemException e) {
			logger.info("Exception in method save document"+e.getMessage() );
			throw new SystemException(e, "EReportsSystemException occurred in saveDocument() ");
		} catch (RemoteException e) {
			logger.info("Exception in method save" + e.getMessage() );
			ServiceUnavailableException serviceUnavailableException = new ServiceUnavailableException(
					e, getClass().getName(), "saveDocument",
					"Failed to lookup report service [" + getServiceName()
							+ "]");
			LogUtility.logApplicationException(serviceUnavailableException);
			throw serviceUnavailableException;
		} catch (Exception e) {
			logger.info("Exception in method saveDocument"+e.getMessage() );
			throw new SystemException(e,
					"saveDocument call failed on ReportService ");
		}
		logger.debug("exit -> saveDocument");
	}
}