package com.manulife.pension.ps.web.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.bos.common.gft.GFTSystemException;
import com.manulife.pension.lp.bos.common.gft.RequestValidationException;
import com.manulife.pension.lp.bos.gft.uploadservice.UploadService;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.lp.model.gft.UploadFileRequest;
import com.manulife.pension.lp.model.gft.UploadFileResponse;
import com.manulife.pension.lp.model.gft.UploadInfoRequest;
import com.manulife.pension.lp.model.gft.UploadInfoResponse;
import com.manulife.pension.platform.web.delegate.AbstractRMIServiceDelegate;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.delegate.exception.UploadFileCannotFoundOrEmptyException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.log.LogUtility;

/**
 * @author drotele
 * This class uses RMI connection to perform i:file operations
 * File upload etc.
 *
 */
public class FileUploadServiceDelegate extends AbstractRMIServiceDelegate {

	private final static String RMI_SERVICE_NAME = "GFTUploadService";

	private static final String SECURED_GFT_SERVICE_PORT = "SecuredGFTServicePort";

	private static FileUploadServiceDelegate instance =
		new FileUploadServiceDelegate();

	/**
	 * constructor
	 */
	public FileUploadServiceDelegate() {
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.delegate.AbstractRMIServiceDelegate#getReportServiceName()
	 */
	protected String getServiceName() {
		return RMI_SERVICE_NAME;
	}
	/*
	 * This method is used to get the GFT Service  Port Number 
	 */
	protected int  getPortNumber() {
		String portNumber= new  BaseEnvironment().getNamingVariable(SECURED_GFT_SERVICE_PORT, null);
		return Integer.parseInt(portNumber); 
	}
		  

	/**
	 * overrige the RMI server name that could be different for the
	 * i:file server
	 * @return
	 */
	protected String getRmiServer() {
		if (this.rmiServer == null) {
			this.rmiServer = Environment.getInstance().getRMIiFileServerName();
		}

		return rmiServer;
	}

	/**
	 * @return
	 */
	public static FileUploadServiceDelegate getInstance() {
		return instance;
	}

    /**
     * @return failover server name
     */
    protected String getRmiFailoverServer() {
        // get RMI Server Name. Issue Environment lookup if it is not specified
        if (this.rmiFailoverServer == null) {
            this.rmiFailoverServer = Environment.getInstance().getRMIiFileServerNameFailover();
        }
        return rmiFailoverServer;
    }
    
	/**
	 * Service Delegate method to upload the i:file
	 *
	 * @param gftsubmissionsummaryrequest
	 * @return GFTSubmissionSummaryResponse
	 * @throws SystemException
	 */
	public UploadFileResponse uploadFile(
		InputStream inputStream,
		GFTUploadDetail gftUploadDetail)
		throws SystemException, UploadFileCannotFoundOrEmptyException {

		UploadFileResponse rsp = null;
		try {
            UploadService uploadService = null;
            try {
                uploadService = (UploadService) getRemoteService();
            } catch (RemoteException e) {
                // log exception, but don't throw it
                SystemException se = new SystemException(
                        e,
                        getClass().getName(),
                        "uploadFile",
                        "RemoteException trying to getRemoteService for uploadFile. "
                            + "Will try failover server now. Exception: " + e.getMessage());
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);

                // if UploadService can't be accessed, try again from the failover server
                uploadService = (UploadService) getRemoteServiceFailover();
            }

            rsp = send(gftUploadDetail, inputStream, uploadService);
		} catch (UploadFileCannotFoundOrEmptyException e) {
			//this will be caught in action and mapped to web error code and
			//presented to user
			throw e;
		} catch (Exception e) {
			SystemException se =
				new SystemException(
					e,
					this.getClass().getName(),
					"uploadFile",
					e.getMessage());
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw se;
		}

		return rsp;
	}

	/**
	 * Service Delegate method to upload the i:file
	 *
	 * @return UploadInfoResponse
	 * @throws SystemException
	 */
	public UploadInfoResponse storeUploadInformation(UploadInfoRequest uploadInfoRequest)
		throws SystemException, UploadFileCannotFoundOrEmptyException {

		UploadInfoResponse rsp = null;
		try {
            UploadService uploadService = null;
            try {
    			uploadService = (UploadService) getRemoteService();
            } catch (RemoteException e) {
                // log exception, but don't throw it
                SystemException se = new SystemException(
                        e,
                        getClass().getName(),
                        "storeUploadInformation",
                        "RemoteException trying to getRemoteService for storeUploadInformation. "
                                + "Will try failover server now. Exception: " + e.getMessage());
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);

                // if UploadService can't be accessed, try again from the failover server
                uploadService = (UploadService) getRemoteServiceFailover();
            }
            rsp = uploadService.storeUploadInformation(uploadInfoRequest);

        } catch (Exception e) {
			SystemException se =
				new SystemException(
					e,
					FileUploadServiceDelegate.class.getName(),
					"storeUploadInformation",
					null);
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw se;
		}

		return rsp;
	}

	public String replaceFileName(String filename) {

		String delim = "\\";
		StringTokenizer token = new StringTokenizer(filename, delim);
		StringBuffer buffer = new StringBuffer();

		while (token.hasMoreTokens()) {
			buffer.append(token.nextToken());
			if (token.hasMoreTokens()) {
				buffer.append(delim + delim);
			}
		}

		return buffer.toString();
	}

	private byte[] getReadInData(byte[] buffer, int read) {
		if (read == buffer.length) {
			return buffer;
		} else {
			byte[] data = new byte[read];
			System.arraycopy(buffer, 0, data, 0, read);
			return data;
		}
	}

	/**
	 *
	 */
	private UploadFileResponse send(
		GFTUploadDetail gftUploadDetail,
		InputStream inputStream,
		UploadService service)
		throws
			IOException,
			RemoteException,
			GFTSystemException,
			RequestValidationException,
			UploadFileCannotFoundOrEmptyException {

		UploadFileRequest request = new UploadFileRequest();
		request.setContractNumber(gftUploadDetail.getContractNumber());
		request.setUserSSN(gftUploadDetail.getUserSSN());
		UploadFileResponse response = new UploadFileResponse();

		byte[] buffer = new byte[1024 * 16]; //16K
		int total = 0;
		int read = inputStream.read(buffer);
		if (read == -1) {
			String newFileName = replaceFileName(gftUploadDetail.getFileName());
			throw new UploadFileCannotFoundOrEmptyException(
				FileUploadServiceDelegate.class.getName(),
				"send",
				"The file specified '"
					+ newFileName
					+ "' does not exist or is empty.");
		}
		total += read;
		request.setFileFragment(getReadInData(buffer, read));
		response = service.uploadFile(request);
		request.setTempFileName(response.getTempFileName());
		request.setSubmissionId(response.getSubmissionId());

		while ((read = inputStream.read(buffer)) != -1) {
			total += read;
			request.setFileFragment(getReadInData(buffer, read));
			response = service.uploadFile(request);
		}

		gftUploadDetail.setFileSizeInBytes(total);
		return response;
	}

}
