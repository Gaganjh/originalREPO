package com.manulife.pension.bd.web.fundcheck;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.fundcheck.model.FundCheckDocFileResponse;
import com.manulife.pension.fundcheck.model.FundCheckDocumentInfo;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.InvestmentPlatformUpdateVO;
import com.manulife.pension.service.fund.valueobject.InvestmentPlatformUpdateVO.UserIdType;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.util.content.GenericException;

public abstract class BDFundCheckBaseController extends BaseAutoController{
	
	/**
	 * This method is invoked when the user clicks on the fundcheck pdf. The fundcheck pdf is opened in the child window.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doOpenPDF(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		//ActionForward forward = null;
		String forward=null;
		if (logger.isDebugEnabled())
			logger.debug("entry -> doOpenPDF");
		List<GenericException> errors = new ArrayList<GenericException>();
		try {
			FundCheckDocFileResponse fileResponse = getFundCheckPDF(
					actionForm, request);
			
			if(fileResponse.getFileName() != null) {
			BDUserProfile bdUserProfile = BDSessionHelper.getUserProfile(request);
			BDPrincipal mimicUser =   (BDPrincipal) request.getSession().getAttribute(BDConstants.LOGIN_USER_PRINCIPAL);
			
			if (bdUserProfile.isInternalUser()) {
				prepareLogIPUDetailsForInternalUsers(bdUserProfile, fileResponse.getFileName(), actionForm, mimicUser);
			} else if (bdUserProfile.getBDPrincipal().getBDUserRole().getRoleId() == "BRKLV1") {
				prepareLogIPUDetailsForLevel1User(bdUserProfile, fileResponse.getFileName(), actionForm, mimicUser);
			} else {
				prepareLogIPUDetailsForExternalUsers(bdUserProfile, fileResponse.getFileName(), actionForm, mimicUser);
			}
		}
			
			if(fileResponse != null) {
				response.setHeader("Cache-Control", "no-cache, no-store");
		        response.setHeader("Pragma", "no-cache");
				response.setContentType(BDConstants.MIME_TYPE_PDF);
				response.setContentLength(fileResponse.getLength());
				OutputStream out = response.getOutputStream();
				out.write(fileResponse.getReportFragment());
				out.close();
			} else{
				errors.add(new GenericException(BDErrorCodes.REPORT_FILE_NOT_FOUND));
			}
			
		} catch (ServiceUnavailableException sue) {
			logger.error("EReports service not available.", sue);
			errors = new ArrayList<GenericException>();
			errors.add(new GenericException(BDErrorCodes.REPORT_FILE_NOT_FOUND));
			setErrorsInRequest(request, errors);
			request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, true);
			//forward = mapping.findForward(actionForm.getAction());
			forward=actionForm.getAction();
		} catch (Exception e) {
			throw new SystemException(e,"Error in retrieving FundCheck documents info.");
		}
		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			//forward = mapping.findForward(BDConstants.SECONDARY_WINDOW_ERROR_FORWARD);
			forward=BDConstants.SECONDARY_WINDOW_ERROR_FORWARD;
		} 
		if (logger.isDebugEnabled())
			logger.debug("exit -> doOpenPDF");
		return forward;

	}	
	
	/**
	 * Fundcheck info should be shown in the web page as rows of 4 columns. This
	 * method transform the array into a List which contains a List of four
	 * FundCheckDocumentInfo objects.
	 * 
	 * @param fundCheckArray
	 * @return List<List<FundCheckDocumentInfo>>
	 */	
	protected final List<List<FundCheckDocumentInfo>> createRowsOfFundCheckInfo(
			FundCheckDocumentInfo[] fundCheckArray) {
		if (logger.isDebugEnabled()){
			logger.debug("entry -> createRowsOfFundCheckInfo");
		}
		List<List<FundCheckDocumentInfo>> fundCheckInfoRows = new ArrayList<List<FundCheckDocumentInfo>>();
		List<FundCheckDocumentInfo> listRow = new ArrayList<FundCheckDocumentInfo>();
		for (int i = 1; i <= fundCheckArray.length; i++) {
			listRow.add(fundCheckArray[i - 1]);
			if (i % 4 == 0) {
				fundCheckInfoRows.add(listRow);
				listRow = new ArrayList<FundCheckDocumentInfo>();
			}
		}
		if (listRow.size() != 0) {
			fundCheckInfoRows.add(listRow);
		}
		if (logger.isDebugEnabled()){
			logger.debug("exit -> createRowsOfFundCheckInfo");
		}
		return fundCheckInfoRows;
	}
	
	protected abstract FundCheckDocFileResponse getFundCheckPDF(
			AutoForm form, HttpServletRequest request)
			throws ServiceUnavailableException, SystemException ;

	/**
	 * 
	 * @param bdUserProfile
	 * @param fileName
	 * @param actionForm
	 * @throws SystemException
	 */
	private void prepareLogIPUDetailsForExternalUsers(BDUserProfile bdUserProfile, String fileName, AutoForm actionForm, BDPrincipal mimicUser)
			throws SystemException {
		BDFundCheckLevel2Form form = (BDFundCheckLevel2Form) actionForm;
		String season = StringUtils.trim(form.getSelectedSeason());
		String year = StringUtils.trim(form.getSelectedYear());
		String companyId = StringUtils.trim(form.getSelectedCompanyId());
		String patcipentNoticeInd = form.getParticipantNoticeInd();
		if ("fundCheckInfo".equals(form.getSearchActionType())) {
			for (int i = 0; i < form.getFundCheckInfo().get(0).size(); i++) {
				FundCheckDocumentInfo fundCheckInfo = form.getFundCheckInfo().get(0).get(i);
				if (StringUtils.trim(fundCheckInfo.getCompanyId()).equals(companyId)
						&& StringUtils.trim(fundCheckInfo.getSeason()).equals(season)
						&& StringUtils.trim(fundCheckInfo.getYear()).equals(year))
					logInvestmentPlatformUpdateDetails(bdUserProfile, fileName,
							StringUtils.trim(fundCheckInfo.getContractNumber()),
							StringUtils.trim(fundCheckInfo.getProducerCode()), patcipentNoticeInd,
							StringUtils.trim(fundCheckInfo.getCompanyId()), mimicUser);
			}
		} else if ("fundCheckInfoL2".equals(form.getSearchActionType())) {
			for (int i = 0; i < form.getSearchResultFundCheckInfo().size(); i++) {
				FundCheckDocumentInfo searResultFCDocInfo = form.getSearchResultFundCheckInfo().get(i);
				if (StringUtils.trim(searResultFCDocInfo.getSeason()).equals(season)
						&& StringUtils.trim(form.getSearchResultFundCheckInfo().get(i).getYear()).equals(year)) {
					logInvestmentPlatformUpdateDetails(bdUserProfile, fileName,
							StringUtils.trim(searResultFCDocInfo.getContractNumber()),
							StringUtils.trim(searResultFCDocInfo.getProducerCode()), patcipentNoticeInd,
							StringUtils.trim(searResultFCDocInfo.getCompanyId()), mimicUser);
				}

			}
		}

	}

	/**
	 * 
	 * @param bdUserProfile
	 * @param fileName
	 * @param actionForm
	 * @throws SystemException
	 */
	private void prepareLogIPUDetailsForLevel1User(BDUserProfile bdUserProfile, String fileName, AutoForm actionForm, BDPrincipal mimicUser)
			throws SystemException {
		BDFundCheckLevel1Form form = (BDFundCheckLevel1Form) actionForm;
		String season = StringUtils.trim(form.getSelectedSeason());
		String year = StringUtils.trim(form.getSelectedYear());
		String companyId = StringUtils.trim(form.getSelectedCompanyId());
		String patcipentNoticeInd = form.getParticipantNoticeInd();
		for (int i = 0; i < form.getSearchResultFundCheckInfo().size(); i++) {
			FundCheckDocumentInfo searchFundCheckInfo = form.getSearchResultFundCheckInfo().get(i);
			if (StringUtils.trim(searchFundCheckInfo.getCompanyId()).equals(companyId)
					&& StringUtils.trim(searchFundCheckInfo.getSeason()).equals(season)
					&& StringUtils.trim(searchFundCheckInfo.getYear()).equals(year)) {
				logInvestmentPlatformUpdateDetails(bdUserProfile, fileName,
						StringUtils.trim(searchFundCheckInfo.getContractNumber()),
						StringUtils.trim(searchFundCheckInfo.getProducerCode()), patcipentNoticeInd,
						StringUtils.trim(searchFundCheckInfo.getCompanyId()), mimicUser);
			}
		}
	}

	/**
	 * 
	 * @param bdUserProfile
	 * @param fileName
	 * @param actionForm
	 * @throws SystemException
	 */
	private void prepareLogIPUDetailsForInternalUsers(BDUserProfile bdUserProfile, String fileName, AutoForm actionForm, BDPrincipal mimicUser)
			throws SystemException {
		BDFundCheckInternalForm form = (BDFundCheckInternalForm) actionForm;
		String patcipentNoticeInd = form.getParticipantNoticeInd();
		String season = StringUtils.trim(form.getSelectedSeason());
		String year = StringUtils.trim(form.getSelectedYear());
		String companyId = StringUtils.trim(form.getSelectedCompanyId());

		if ("fundCheckInfo".equals(form.getSearchActionType())) {
			for (int i = 0; i < form.getSearchResultFundCheckInfo().get(0).size(); i++) {
				FundCheckDocumentInfo fundCheckInfo = form.getSearchResultFundCheckInfo().get(0).get(i);
				// if suppose to execute when company id is null
				if (StringUtils.trim(fundCheckInfo.getSeason()).equals(season) 
						&& StringUtils.trim(fundCheckInfo.getYear()).equals(year)
						&& null == fundCheckInfo.getCompanyId()) {
					logInvestmentPlatformUpdateDetails(bdUserProfile, fileName,
							StringUtils.trim(fundCheckInfo.getContractNumber()),
							StringUtils.trim(fundCheckInfo.getProducerCode()), patcipentNoticeInd,
							StringUtils.trim(fundCheckInfo.getCompanyId()), mimicUser);
				} else if (StringUtils.trim(fundCheckInfo.getSeason()).equals(season) // Producer code check
						&& StringUtils.trim(fundCheckInfo.getYear()).equals(year)
						&& StringUtils.trim(fundCheckInfo.getCompanyId()).equals(companyId)) {
					logInvestmentPlatformUpdateDetails(bdUserProfile, fileName,
							StringUtils.trim(fundCheckInfo.getContractNumber()),
							StringUtils.trim(fundCheckInfo.getProducerCode()), patcipentNoticeInd,
							StringUtils.trim(fundCheckInfo.getCompanyId()), mimicUser);
				}
			}
		} else if ("fundCheckInfoL2".equals(form.getSearchActionType())) {
			for (int i = 0; i < form.getSearchResultFundCheckInfoL2().size(); i++) {
				FundCheckDocumentInfo fundCheckInfoL2 = form.getSearchResultFundCheckInfoL2().get(i);
				if (StringUtils.trim(fundCheckInfoL2.getSeason()).equals(season)
						&& StringUtils.trim(fundCheckInfoL2.getYear()).equals(year)) {
					logInvestmentPlatformUpdateDetails(bdUserProfile, fileName, fundCheckInfoL2.getContractNumber(),
							fundCheckInfoL2.getProducerCode(), patcipentNoticeInd, fundCheckInfoL2.getCompanyId(), mimicUser);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param userProfile
	 * @param fileName
	 * @param contractNumber
	 * @param producerCode
	 * @throws SystemException
	 */
	public void logInvestmentPlatformUpdateDetails(BDUserProfile bdUserProfile, String fileName, String contractNumber,
			String producerCode, String patcipentNoticeInd, String companyCode, BDPrincipal mimicUser)
			throws SystemException {

		InvestmentPlatformUpdateVO ipuDetails = new InvestmentPlatformUpdateVO();
		if (null == contractNumber) {
			ipuDetails.setContractId(0);
		} else {
			ipuDetails.setContractId(Integer.parseInt(contractNumber));
		}
		ipuDetails.setApplicationId(BDConstants.APPLICATION_ID);

		if (null != patcipentNoticeInd && !patcipentNoticeInd.isEmpty()) {
			if (patcipentNoticeInd.equals(BDConstants.TRUE)) {
				if (fileName.endsWith("SP.pdf")) {
					ipuDetails.setFunctionCode(BDConstants.PATCIPENT_NOTICE_SPANISH);
				} else if (fileName.endsWith("EN.pdf")) {
					ipuDetails.setFunctionCode(BDConstants.PATCIPENT_NOTICE_ENGLISH);
				}
			}
		} else if (null != companyCode && !companyCode.isEmpty() && null != producerCode && !producerCode.isEmpty()) {
			if (companyCode.equals(BDConstants.COUNTRY_USA)) {
				ipuDetails.setFunctionCode(BDConstants.PRODUCER_NOTICE_USA);
			} else {
				ipuDetails.setFunctionCode(BDConstants.PRODUCER_NOTICE_NY);
			}
		} else {
			ipuDetails.setFunctionCode(BDConstants.PS_NOTICE);
		}
		ipuDetails.setProposalNo(0);
		ipuDetails.setReferenceText(fileName);
		if (mimicUser != null) {
			if (mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("FIELD")
					|| mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("CAR")
					|| mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("BDWCAR")
					|| mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("BDWSCR")
					|| mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("NACCT")
					|| mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("RUM")
					|| mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("RVP")
					|| mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("FLDADM")
					|| mimicUser.getBDUserRole().getRoleType().getUserRoleCode().equals("CMAMGR")) {
			ipuDetails.setUserIdType(UserIdType.getIdType(UserIdType.INTERNALUSER));
			}else {
				ipuDetails.setUserIdType(UserIdType.getIdType(UserIdType.EXTERNALUSER));
			}
			ipuDetails.setUserFirstName(mimicUser.getFirstName());
			ipuDetails.setUserLastName(mimicUser.getLastName());
			ipuDetails.setUserId(mimicUser.getUserName());
			ipuDetails.setUserRoleCode(mimicUser.getBDUserRole().getRoleType().getUserRoleCode());
			ipuDetails.setEmail(mimicUser.getEmailAddress());
		} else {
			if (bdUserProfile.isInternalUser()) {
				ipuDetails.setUserIdType(UserIdType.getIdType(UserIdType.INTERNALUSER));
				ipuDetails.setUserFirstName(bdUserProfile.getBDPrincipal().getFirstName());
				ipuDetails.setUserLastName(bdUserProfile.getBDPrincipal().getLastName());
				ipuDetails.setUserId(bdUserProfile.getBDPrincipal().getUserName());
			} else {
				ipuDetails.setUserIdType(UserIdType.getIdType(UserIdType.EXTERNALUSER));
				ipuDetails.setUserFirstName(bdUserProfile.getBDPrincipal().getFirstName());
				ipuDetails.setUserLastName(bdUserProfile.getBDPrincipal().getLastName());
				ipuDetails.setUserId(String.valueOf(bdUserProfile.getBDPrincipal().getProfileId()));
			}
			ipuDetails.setUserRoleCode(bdUserProfile.getBDPrincipal().getBDUserRole().getRoleType().getUserRoleCode());
			ipuDetails.setEmail(bdUserProfile.getBDPrincipal().getEmailAddress());
		}
		ipuDetails.setRequestedTime(new Date().getTime());
		ipuDetails.setOosOrderNumber(0);
		if (producerCode == null) {
			ipuDetails.setProducerCode(0);
		} else {
			ipuDetails.setProducerCode(Integer.parseInt(producerCode));
		}

		if (ipuDetails.getProducerCode() == 0) {
			ipuDetails.setLogIdType("CN");
		} else {
			ipuDetails.setLogIdType("PC");
		}
		FundServiceDelegate.getInstance().logFunctionRequestInfo(ipuDetails);
	}

}
