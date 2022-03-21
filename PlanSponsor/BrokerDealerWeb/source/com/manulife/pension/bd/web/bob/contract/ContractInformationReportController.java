package com.manulife.pension.bd.web.bob.contract;

import static com.manulife.pension.platform.web.CommonConstants.DOUBLE_QUOTES;
import static com.manulife.pension.platform.web.CommonConstants.HYPHON_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.PERCENTAGE_SIGN;
import static com.manulife.pension.platform.web.CommonConstants.SLASH_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.SPACE_SYMBOL;
import static com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData.CSV_REPORT_NAME;
import static com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData.FILTER_AS_OF_DATE;
import static com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData.FILTER_CONTRACT_EFFECTIVE_DATE;
import static com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData.FILTER_CONTRACT_NUMBER;
import static com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData.REPORT_ID;
import static com.manulife.util.render.RenderConstants.MEDIUM_MDY_SLASHED;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.view.MutableContent;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.documents.model.FeeDisclosureDocumentInfo;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO.Address;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO.FeaturesAndServices;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ContractSnapshotVO;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.contract.valueobject.DefaultInvestmentFundVO;
import com.manulife.pension.service.contract.valueobject.FswStatus;
import com.manulife.pension.service.contract.valueobject.MoneySourceVO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.fee.util.Constants.SENDServiceItem;
import com.manulife.pension.service.order.delegate.OrderServiceDelegate;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * ContractInformationReport Action class This class is used to forward the users's request to
 * ContractInformation page
 * 
 * @author Siby Thomas
 */
@Controller
@RequestMapping(value ="/bob/contract")

public class ContractInformationReportController extends BOBReportController {
	@ModelAttribute("contractInformationForm") 
	public ContractInformationReportForm populateForm() 
	{
		return new ContractInformationReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/contractInformation.jsp");
		forwards.put("contractInformation","/contract/contractInformation.jsp");
		forwards.put("default","/contract/contractInformation.jsp");}

    private Category interactionLog = Category.getInstance(ServiceLogRecord.class);

    private ServiceLogRecord logRecord = new ServiceLogRecord("ContractInformationAction");

    private static final String CONTRACT_INFORMATION_PAGE = "contractInformation";

    private static final String XSLT_FILE_KEY_NAME = "ContractInformationReport.XSLFile";

    public static final String DISCONTINUED_STATUS = "DI";

    private static final String PERCENT_FORMAT = "##0.00";

    private static final String DATE_FORMAT = "MMMM dd";

    private static final String DEFAULT_VALUE = "0.000";
    
    private static final String AMT_PATTERN = "##0.000";

    //This variable was updated to suppress the display of Blended Asset Charge in download reports - CL 131387
	private static final Boolean SUPPRESS_BLENDED_ASSET_CHARGE = Boolean.TRUE;
	
	public static final String ACTIVE = "AC";
	public static final String FROZEN = "CF";
	public static final String MTA = "MTA";
	
	public static final String DISTRIBUTION_CHANNEL = "DISTRIBUTION_CHANNEL";
	public static final String GROUP_FIELD_OFFICE_NO = "GROUP_FIELD_OFFICE_NO";
	public static final String GFO_CODE_25270 = "25270";
	public static final String GFO_CODE_25280 = "25280";
	

    public ContractInformationReportController() {
        super(ContractInformationReportController.class);
    }

    /**
     * @see BaseReportController#doCommon()
     */
     
    public String doCommon(BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {

    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCommon");
        }
        ContractInformationReportForm contractinformationForm = (ContractInformationReportForm) reportForm;

        super.doCommon( reportForm,request, response);

       
        BobContext bobContext = getBobContext(request);
        Contract currentContract = bobContext.getCurrentContract();

		ContractInformationReportData reportData = (ContractInformationReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);
		
        if (currentContract.getStatus().equals(DISCONTINUED_STATUS)
                || currentContract.isDefinedBenefitContract()) {
        	contractinformationForm.setShowFiduciaryWarranty(false);
        } else {
        	contractinformationForm.setShowFiduciaryWarranty(true);
        }
        if (currentContract.getHasContractGatewayInd()) {
        	contractinformationForm
					.setGiflFeaturesContentId(ContractInformationReportHelper
							.getGiflFeaturesContentId(bobContext));
		}
        
        currentContract.setHasLifecycle(reportData.getHasLifeCycleFunds());

        String task = getTask(request);
        int contractNumber = currentContract.getContractNumber();
        
        BDUserProfile userProfile = getUserProfile(request);

        if (DEFAULT_TASK.equals(task)) {
            logPageAccess(request, userProfile, contractNumber);
        }

        /*OB3 Track4 Changes: Code to get the Plan Highlights CSF feature*/
        try{
        	ContractServiceFeature summaryPlanHighlightAvailable =
            	ContractServiceDelegate.getInstance().getContractServiceFeature(contractNumber,
            		ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);
            
    		if (summaryPlanHighlightAvailable != null) {
    			 boolean isSummaryPlanHighlightAvailable = ContractServiceFeature.internalToBoolean(
 						summaryPlanHighlightAvailable.getValue()).booleanValue();
 			    boolean isSummaryPlanHighlightReviewed = ContractServiceFeature
 				        .internalToBoolean(summaryPlanHighlightAvailable.getAttributeValue(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED)).booleanValue();
    			boolean value = isSummaryPlanHighlightAvailable && isSummaryPlanHighlightReviewed;
    			contractinformationForm.setPlanHighlightsCSFavailable(value);
    			/*System.out.println("summaryPlanHighlightAvailable ---> "+value);*/
    		}
        }catch(ApplicationException e){
        	logger.error(e.getMessage());
        	throw new SystemException(e.getMessage());
        }
        
 		Set<SENDServiceItem> SENDServiceItems = new HashSet<SENDServiceItem>();
		SENDServiceItems.add(SENDServiceItem.SERVICE_SELECTED);
	/*	TODO: CR32 changes
	 * SENDServiceItems.add(SENDServiceItem.ANNUAL_FEE);
		SENDServiceItems.add(SENDServiceItem.EFFECTIVE_DATE);*/
		Map<SENDServiceItem, Object> values = FeeServiceDelegate.getInstance(
				BDConstants.PS_APPLICATION_ID).getSENDServiceDetails(
				null,
				bobContext.getCurrentContract().getContractNumber(),
				null,
				SENDServiceItems);

		reportData
				.getContractProfileVo()
				.getFeaturesAndServices()
				.getSendService()
				.setEnabled("Y".equals((String) values.get(SENDServiceItem.SERVICE_SELECTED)) ? true : false);
	/*	reportData
				.getContractProfileVo()
				.getFeaturesAndServices()
				.getSendService()
				.setAnnualFee((BigDecimal) values.get(SENDServiceItem.ANNUAL_FEE));
		reportData
				.getContractProfileVo()
				.getFeaturesAndServices()
				.getSendService()
				.setServiceEffectiveDate((Date) values.get(SENDServiceItem.EFFECTIVE_DATE));*/
		if (reportData.getContractProfileVo() != null) {
			FeaturesAndServices featuresAndServices = reportData.getContractProfileVo().getFeaturesAndServices();
			if (featuresAndServices != null) {
				if (featuresAndServices.getContractFeatures() != null) {
					Collection tempCollection;
					tempCollection = featuresAndServices.getContractFeatures();
					if (featuresAndServices.getSendService().isEnabled()) {
						tempCollection.add(BDConstants.SEND_SERVICE_TITLE);

					}
					Collections.sort((List<String>) tempCollection);
				}
			}
		}
		
        if (logger.isDebugEnabled())
            logger.debug(ContractInformationReportForm.class.getName()
                    + ":forwarding to Contract Information Page.");

        return forwards.get(CONTRACT_INFORMATION_PAGE);
    }

    /**
     * @see BaseReportController#getDefaultSort()
     */
    @Override
    protected String getDefaultSort() {
        return null;
    }

    /**
     * @see BaseReportController#getDefaultSortDirection()
     */
    @Override
    protected String getDefaultSortDirection() {
        return null;
    }

    /**
     * @see BaseReportController#getDownloadData()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws SystemException {

        ContractInformationReportData reportData = (ContractInformationReportData) report;
        ContractInformationReportForm contractinformationForm = (ContractInformationReportForm) reportForm;
        StringBuffer buffer = new StringBuffer();
        BobContext bobContext = getBobContext(request);
        Contract currentContract = bobContext.getCurrentContract();
        ContractProfileVO profileVo = reportData.getContractProfileVo();
        ContractSnapshotVO snapShotVo = reportData.getContractSnapshotVo();
        ContractSummaryVO summaryVO = reportData.getContractSummaryVo();
        String asOfDate = String
                .valueOf(currentContract.getContractDates().getAsOfDate().getTime());

        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.CONTRACT_INFORMATION_PATH, request);
        String intro1Text = ContentUtility.getContentAttributeText(layoutPageBean,
                CommonContentConstants.INTRO1_TEXT, null);
        
        String intro2Text = ContentUtility.getContentAttributeText(layoutPageBean,
                CommonContentConstants.INTRO2_TEXT, null);
       
        String pageName = ContentUtility.getContentAttributeText(layoutPageBean, CommonContentConstants.PAGE_NAME, null);
        buffer.append(ContentUtility.filterCMAContentForCSV(pageName)).append(LINE_BREAK);
        buffer.append(currentContract.getCompanyName()).append(COMMA).append(
                currentContract.getContractNumber()).append(LINE_BREAK);
        buffer.append(LINE_BREAK);

        if (!StringUtils.isEmpty(intro1Text)) {
            intro1Text = ContentUtility.filterCMAContentForCSV(intro1Text);
            buffer.append(intro1Text).append(LINE_BREAK);
        }

        if (!StringUtils.isEmpty(intro2Text)) {
            intro2Text = ContentUtility.filterCMAContentForCSV(intro2Text);
            buffer.append(intro2Text).append(LINE_BREAK);
        }

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        buffer.append(ContentUtility.filterCMAContentForCSV(bodyHeader1)).append(LINE_BREAK);

        buffer.append(LINE_BREAK);

        buffer.append("Contact Information,").append(LINE_BREAK);
        buffer.append("Mailing Address,");

        if (StringUtils.isNotEmpty(profileVo.getAddress().getLine1())) {
            buffer.append(profileVo.getAddress().getLine1()).append(LINE_BREAK).append(COMMA);
        }
        if (StringUtils.isNotEmpty(profileVo.getAddress().getline2())) {
            buffer.append(profileVo.getAddress().getline2()).append(LINE_BREAK).append(COMMA);
        }
        StringBuffer addressLine3 = new StringBuffer(StringUtils.trimToEmpty(profileVo.getAddress()
                .getCity()));
        if (profileVo.getAddress().isCompleteStateCode()
                && !StringUtils.isEmpty(addressLine3.toString())) {
            addressLine3.append(COMMA);
        }
        if (StringUtils.isNotEmpty(profileVo.getAddress().getStateCode())) {
            addressLine3.append(profileVo.getAddress().getStateCode());
            if (StringUtils.isNotEmpty(profileVo.getAddress().getZipCode())) {
                addressLine3.append(COMMA);
            }
        }
        if (StringUtils.isNotEmpty(profileVo.getAddress().getZipCode())) {
            addressLine3.append(profileVo.getAddress().getZipCode());
        }
        buffer.append(escapeField(addressLine3.toString())).append(LINE_BREAK);

        if (DISCONTINUED_STATUS.equals(currentContract.getStatus())) {
            buffer.append("Discontinued Contract, For Assistance").append(LINE_BREAK);
            buffer.append(COMMA).append(
                    ContentHelper.getContentText(
                            BDContentConstants.MISCELLANEOUS_GENERAL_PHONE_NUMBER,
                            ContentTypeManager.instance().MESSAGE, null)).append(LINE_BREAK);
        } else {
            buffer.append("Client Account Rep.,");
            if (StringUtils.isNotEmpty(summaryVO.getCarContact().getName())) {
            	buffer.append(summaryVO.getCarContact().getName()).append(LINE_BREAK);
            	buffer.append(COMMA);
            }
            
            if (currentContract.isDefinedBenefitContract()) {

                buffer.append(
                        ContentUtility.jsEsc(ContentHelper.getContentText(
                                BDContentConstants.MISCELLANEOUS_DB_CONTRACT_PHONE_NUMBER,
                                ContentTypeManager.instance().MESSAGE, null)));
            } else {
                buffer.append(
                        ContentUtility.jsEsc(ContentHelper.getContentText(
                                BDContentConstants.PS_CONTACTS_ACCOUNT_REPRESENTATIVE_PHONE,
                                ContentTypeManager.instance().MESSAGE, null)));

            }
            if (StringUtils.isNotEmpty(summaryVO.getCarContact().getExtension())) {
	            buffer.append(COMMA).append("ext.").append(COMMA).append(
	                    summaryVO.getCarContact().getExtension());
            }
            buffer.append(LINE_BREAK);
            if (StringUtils.isNotEmpty(summaryVO.getCarContact().getEmail())) {
	            buffer.append(COMMA).append(summaryVO.getCarContact().getEmail()); 
             }
            buffer.append(LINE_BREAK);
        }
        
        buffer.append("Relationship Manager Contact,");
		if (summaryVO.isRmContractExist()) {
			buffer.append(summaryVO.getRmUserName())
					.append(LINE_BREAK);
			if (StringUtils.isNotEmpty(summaryVO.getRmUserPhoneNumber())) {
				buffer.append(COMMA).append(summaryVO.getRmUserPhoneNumber());
				if (StringUtils.isNotEmpty(summaryVO.getRmUserPhoneExtension())) {
					buffer.append(COMMA).append("ext.").append(COMMA).append(summaryVO.getRmUserPhoneExtension());
				}
				buffer.append(LINE_BREAK);
			}
			if (StringUtils.isNotEmpty(summaryVO.getRmUserEmail())) {
				buffer.append(COMMA).append(summaryVO.getRmUserEmail());
				buffer.append(LINE_BREAK);
			}

		} else {
			getRMStaticData(summaryVO);
			buffer.append(summaryVO.getRmUserName()).append(LINE_BREAK).append(COMMA)
					.append(summaryVO.getRmUserPhoneNumber()).append(LINE_BREAK).append(COMMA)
					.append(summaryVO.getRmUserEmail()).append(LINE_BREAK);
		}
		
        buffer.append("TPA Contact,");
        int tpaCount =0;
        String tpaName = "";
        String tpaPhone = "";
        String tpaEmail = "";
        boolean tpaPhoneAvailable = false;
        boolean tpaEmailAvailable = false;
        
		if (reportData.getTpaPrimaryContactDetails().size() > 0) {
			for (ContactVO contact : reportData.getTpaPrimaryContactDetails()) {
				if(StringUtils.isNotBlank(contact.getPhone())){
					tpaPhoneAvailable = true;
				}
				if (StringUtils.isNotBlank(contact.getEmail())) {
					tpaEmailAvailable = true;
				}
			}
			for (ContactVO contact : reportData.getTpaPrimaryContactDetails()) {
				if (tpaCount > 0) {
					tpaName += COMMA;
					tpaName += WHITE_SPACE_CHAR;
				}
				tpaName += contact.getName();
				if(tpaPhoneAvailable){
					if(tpaCount > 0){
						tpaPhone += COMMA;
						tpaPhone += WHITE_SPACE_CHAR;
					}
					if(StringUtils.isBlank(contact.getPhone())){
						tpaPhone +="Not available";
					}else{
						tpaPhone += contact.getPhone();
					}
					
				}
				if(tpaEmailAvailable){
					if (tpaCount > 0) {
						tpaEmail += COMMA;
						tpaEmail += WHITE_SPACE_CHAR;
					}
					if(StringUtils.isBlank(contact.getEmail())){
						tpaEmail +="Not available";
					}else{
						tpaEmail += contact.getEmail();
					}
				}
				tpaCount++;
			}
			buffer.append(QUOTE);
			buffer.append(tpaName);
			buffer.append(QUOTE).append(LINE_BREAK).append(COMMA);
		} else if (reportData.getTpaContactName() != null
				&& StringUtils.isNotBlank(reportData.getTpaContactName())) {
			buffer.append(reportData.getTpaContactName()).append(LINE_BREAK)
					.append(COMMA);
		} else {
			buffer.append("Not available").append(LINE_BREAK).append(COMMA);
		}
		 buffer.append(
                reportData.getTpaFirmName() == null ? SPACE_SYMBOL : reportData.getTpaFirmName());

		if (reportData.getTpaPrimaryContactDetails().size() > 0) {
			if(StringUtils.isNotBlank(tpaPhone)){
				buffer.append(LINE_BREAK).append(COMMA);
				buffer.append(QUOTE);
				buffer.append(tpaPhone);
				buffer.append(QUOTE);
			}
			if(StringUtils.isNotBlank(tpaEmail)){
				buffer.append(LINE_BREAK).append(COMMA);
				buffer.append(QUOTE);
				buffer.append(tpaEmail);
				buffer.append(QUOTE);
			}
		}
		buffer.append(LINE_BREAK);
		
        buffer.append("Plan Sponsor Contact,");
        
        if(reportData.getPlanSponsorPrimaryContactDetails().size() >0 ){
        	int psCount =0;
            String psName = "";
            String psPhone = "";
            String psEmail = "";
            boolean psPhoneAvailable = false;
            boolean psEmailAvailable = false;
            for (ContactVO contact : reportData.getPlanSponsorPrimaryContactDetails()) {
				if(StringUtils.isNotBlank(contact.getPhone())){
					psPhoneAvailable = true;
				}
				if (StringUtils.isNotBlank(contact.getEmail())) {
					psEmailAvailable = true;
				}
			}
        	for (ContactVO contact : reportData.getPlanSponsorPrimaryContactDetails()) {
        		if(psCount > 0){
					psName+=COMMA;
					psName+=WHITE_SPACE_CHAR;
				}
        		psName+=contact.getName();
        		if(psPhoneAvailable){
        			if(psCount > 0){
        				psPhone+=COMMA;
    					psPhone+=WHITE_SPACE_CHAR;
        			}
					if(StringUtils.isBlank(contact.getPhone())){
						psPhone +="Not available";
					}else{
						psPhone += contact.getPhone();
					}
        		}
        		if(psEmailAvailable){
        			if(psCount > 0){
    					psEmail+=COMMA;
    					psEmail+=WHITE_SPACE_CHAR;
        			}
        			if(StringUtils.isBlank(contact.getEmail())){
        				psEmail+="Not available";
        			}else{
        				psEmail+=contact.getEmail();
					}
				}
				
				psCount++;
			}
        	
        	buffer.append(QUOTE);
        	buffer.append(psName);
        	buffer.append(QUOTE);
			if(StringUtils.isNotBlank(psPhone)){
				buffer.append(LINE_BREAK).append(COMMA);
				buffer.append(QUOTE);
				buffer.append(psPhone);
				buffer.append(QUOTE);
			}
			if(StringUtils.isNotBlank(psEmail)){
				buffer.append(LINE_BREAK).append(COMMA);
				buffer.append(QUOTE);
				buffer.append(psEmail);
				buffer.append(QUOTE);
			}
        }
        buffer.append(LINE_BREAK);
		
        if (!currentContract.isDefinedBenefitContract()) {
            buffer.append("Participant Toll Free Services,").append(LINE_BREAK);
            buffer.append(ContentHelper.getContentText(
                    BDContentConstants.CONTACT_SERVICE_REP_LABEL,
                    ContentTypeManager.instance().MESSAGE, null)+",").append(ContentHelper.getContentText(
                            BDContentConstants.MISCELLANEOUS_PARTICIPANT_TOLL_FREE_NUMBER,
                            ContentTypeManager.instance().MESSAGE, null)).append(LINE_BREAK);
            buffer.append(" ,").append(ContentHelper.getContentText(
            		BDContentConstants.CONTACT_SERVICE_REP_NUMBER_SPANISH,
                    ContentTypeManager.instance().MESSAGE, null)).append(LINE_BREAK);
            
            buffer.append(ContentHelper.getContentText(
                    BDContentConstants.CONTACT_ROLLOVER_EDUCATION_SPECIALIST_LABEL,
                    ContentTypeManager.instance().MESSAGE, null)+",").append(ContentHelper.getContentText(
            		BDContentConstants.CONTACT_ROLLOVER_EDUCATION_SPECIALIST_PHONE,
                    ContentTypeManager.instance().MESSAGE, null)).append(LINE_BREAK);
            
            buffer.append(ContentHelper.getContentText(
                    BDContentConstants.CONTACT_CONSOLIDATION_SPECIALIST_LABEL,
                    ContentTypeManager.instance().MESSAGE, null)+",").append(ContentHelper.getContentText(
            		BDContentConstants.CONTACT_CONSOLIDATION_SPECIALIST_PHONE,
                    ContentTypeManager.instance().MESSAGE, null)).append(LINE_BREAK+LINE_BREAK); 
            
            buffer.append("Enrollment Form Fax,").append(
                    ContentHelper.getContentText(
                            BDContentConstants.MISCELLANEOUS_ENROLLMENT_FORM_FAX_NUMBER,
                            ContentTypeManager.instance().MESSAGE, null)).append(LINE_BREAK);
        }
        if (currentContract.isDefinedBenefitContract()) {
            buffer.append("Other Form Fax,").append(
                    ContentHelper.getContentText(
                            BDContentConstants.DB_FAX_NUMBER, ContentTypeManager
                                    .instance().MESSAGE, null)).append(LINE_BREAK);
        } else {
            buffer.append("Other Form Fax,").append(
                    ContentHelper.getContentText(
                            BDContentConstants.MISCELLANEOUS_OTHER_FORM_FAX_NUMBER, ContentTypeManager
                                    .instance().MESSAGE, null)).append(LINE_BREAK);
        }
       
        buffer.append(LINE_BREAK);

        buffer.append("Contract Options,").append(LINE_BREAK);
        buffer.append("Contract Features");
        Collection features = profileVo.getFeaturesAndServices().getContractFeatures();
        for (Object featue : features) {
			if (StringUtils.isNotBlank(String.valueOf(featue))) {
				buffer.append(COMMA).append(featue).append(LINE_BREAK);
			}
		}
        
        buffer.append(LINE_BREAK);
        
        String giflFeaturesSectionTitle = "";
		if (bobContext.getContractProfile().getContract()
				.getHasContractGatewayInd()) {
			if (BDConstants.GIFL_VERSION_03.equals(bobContext
					.getContractProfile().getContract().getGiflVersion())) {
				giflFeaturesSectionTitle = BDConstants.GIFL_SELECT_TITLE;
			} else {
				giflFeaturesSectionTitle = BDConstants.GIFL_V1_V2_TITLE;
			}
			buffer.append(giflFeaturesSectionTitle);
			MutableContent content = null;
			try {
				content = (MutableContent) BrowseServiceDelegate.getInstance()
						.findContent(
								ContractInformationReportHelper
										.getGiflFeaturesContentId(bobContext),
								ContentTypeManager.instance().MISCELLANEOUS);
			} catch (ContentException ce) {
				logger.error("Exception while retrieving GIFL features from CMA ", ce);
				throw new SystemException("Exception while retrieving GIFL features from CMA "
	                    + ce.toString());
			}
			String giflFeaturesCMAText = ContentUtility
					.getContentAttribute(content,
							CommonContentConstants.TEXT, null,
							new String[] { ContractInformationReportHelper
									.getGIFLFeePercentageDisplay(currentContract
											.getContractNumber()) });
			List<String> giflFeatures = ContractInformationReportHelper
					.parseGiflFeaturesFromCMAText(giflFeaturesCMAText);
			for (String giflFeature : giflFeatures) {
				buffer.append(COMMA).append(giflFeature).append(LINE_BREAK);
			}
			buffer.append(LINE_BREAK);
		}
		
		if(profileVo.getFeaturesAndServices().getSendService().isEnabled()) {
			buffer.append("SEND Service").append(LINE_BREAK);
			buffer.append("\"Newly Eligible, Annual\"").append(LINE_BREAK);
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMinimumFractionDigits(2);
			buffer.append("Annual Fee: ")
					.append(numberFormat.format(
							profileVo.getFeaturesAndServices().getSendService().getAnnualFee() == null ? 0.00 : 
	                        profileVo.getFeaturesAndServices().getSendService().getAnnualFee())).append(PERCENTAGE_SIGN)
					.append(" as of ")
					.append(DateRender.formatByPattern(profileVo.getFeaturesAndServices().getSendService().getServiceEffectiveDate(),
							SPACE_SYMBOL, MEDIUM_MDY_SLASHED))
					.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
		}
		
		if (reportData.getManagedAccountServiceFeature() != null) {
			buffer.append("Managed Accounts");
			buffer.append(COMMA).append(reportData.getManagedAccountServiceFeature()).append(LINE_BREAK);
			buffer.append(LINE_BREAK);
		}
        
        buffer.append("Investments,").append("Number of Funds Available,").append(
                profileVo.getFeaturesAndServices().getAvailableFundsNumber()).append(LINE_BREAK);
        buffer.append(COMMA).append("Number of Funds Selected,").append(
                profileVo.getFeaturesAndServices().getSelectedFundsNumber()).append(LINE_BREAK);

        buffer.append(COMMA).append("Default Investment Option(s)");
        if (profileVo.getDefaultInvestments() == null
                || profileVo.getDefaultInvestments().isEmpty()) {
            buffer.append(COMMA).append("No default investment option currently selected.").append(
                    "Please contact your Client Account Representative ").append(
                    "to change the default investment option for your contract.")
                    .append(LINE_BREAK);
        } else {
            for (DefaultInvestmentFundVO vo : (Collection<DefaultInvestmentFundVO>) profileVo
                    .getDefaultInvestments()) {
                buffer.append(COMMA);
                if (vo.isLifeCycleFund()) {
                	buffer.append(vo.getFundFamilyDisplayName()).append(COMMA);
                } else {
                    buffer.append(vo.getFundName()).append(COMMA);
                }
                buffer.append(vo.getPercentage()).append(PERCENTAGE_SIGN).append(LINE_BREAK)
                        .append(COMMA);
            }
        }
        buffer.append(LINE_BREAK);
        
        if (contractinformationForm.getShowFiduciaryWarranty()) {
            buffer.append("Fiduciary Standards Warranty (FSW)").append(COMMA);
            if (reportData.getContractSummaryVo().getFswStatus() == FswStatus.QUALIFIED) {
                buffer.append("Yes");
			} else if (reportData.getContractSummaryVo().getFailedAssetClasses() != null
					&& !reportData.getContractSummaryVo().getFailedAssetClasses().isEmpty()) {
                buffer.append("No");
            } else {
            	buffer.append("Not eligible");
            }
            buffer.append(LINE_BREAK).append(LINE_BREAK);
        }

        Collection channels = profileVo.getFeaturesAndServices().getAccessChannels();
        if (!currentContract.isDefinedBenefitContract() && channels != null && !channels.isEmpty()) {
            buffer.append("Access Channels");
            for (Object channel : channels) {
                buffer.append(COMMA).append(channel).append(LINE_BREAK);
            }
        }
        buffer.append("Direct Debit Selected,");
        if (profileVo.getFeaturesAndServices().getIsDirectDebitSelected()) {
            buffer.append("Yes").append(LINE_BREAK);
        } else {
            buffer.append("No").append(LINE_BREAK);
        }

        buffer.append(LINE_BREAK);

        buffer.append("Contract Assets,").append(LINE_BREAK);
        buffer.append("Total Contract Assets,").append(
                escapeField(NumberFormat.getCurrencyInstance().format(
                        snapShotVo.getPlanAssets().getTotalPlanAssetsAmount() == null ? 0.0
                                : snapShotVo.getPlanAssets().getTotalPlanAssetsAmount()))).append(
                LINE_BREAK);
        buffer.append("Assets in Cash Account,").append(
                escapeField(NumberFormat.getCurrencyInstance().format(
                        snapShotVo.getPlanAssets().getCashAccountAmount()))).append(LINE_BREAK);
        if (new BigDecimal("0.0").equals(snapShotVo.getPlanAssets().getUninvestedAssetsAmount())) {
            buffer.append("Pending Transaction,").append(
                    escapeField(NumberFormat.getCurrencyInstance().format(
                            snapShotVo.getPlanAssets().getUninvestedAssetsAmount()))).append(
                    LINE_BREAK);
        }

        buffer.append("Assets Allocated,").append(
                escapeField(NumberFormat.getCurrencyInstance().format(
                        snapShotVo.getPlanAssets().getAllocatedAssetsAmount()))).append(LINE_BREAK);

        if (currentContract.isLoanFeature() && reportData.getHasLoanAssets()) {
            buffer.append("Loan Assets,").append(
                    escapeField(NumberFormat.getCurrencyInstance().format(
                            snapShotVo.getPlanAssets().getLoanAssets()))).append(LINE_BREAK);
        }

        if (currentContract.isPBA()) {
            buffer.append("PBA Assets,").append(
                    escapeField(NumberFormat.getCurrencyInstance().format(
                            snapShotVo.getPlanAssets().getPersonalBrokerageAccountAmount())))
                    .append(LINE_BREAK);
        }

        if (!currentContract.isDefinedBenefitContract()) {
            buffer.append("Participants,").append(reportData.getParticipantCount()).append(
                    LINE_BREAK);
        }
        buffer.append("As of,").append(DateRender.format(new Date(Long.valueOf(asOfDate)), MEDIUM_MDY_SLASHED)).append(LINE_BREAK);

		if (!SUPPRESS_BLENDED_ASSET_CHARGE) {
			if (!isInvalidDate(reportData.getAssetChargeAsOfDate())) {
				buffer.append("Blended Asset Charge (%),").append(
						NumberRender.formatByPattern(reportData
								.getBlendedAssetCharge(), DEFAULT_VALUE,
								AMT_PATTERN, 3, BigDecimal.ROUND_HALF_DOWN))
						.append(COMMA).append("as of,").append(
								DateRender.formatByPattern(reportData
										.getAssetChargeAsOfDate(),
										SPACE_SYMBOL, SPACE_SYMBOL,
										"MM/dd/yyyy")).append(LINE_BREAK);
			}
		}

        buffer.append(LINE_BREAK);

        if (currentContract.isDefinedBenefitContract()) {
            buffer.append("Allocations Details,").append(LINE_BREAK);
            buffer.append("Last Allocation,");
        } else {
            buffer.append("Payroll Allocations Details,").append(LINE_BREAK);
            buffer.append("Last Payroll Allocation,");
        }
        buffer.append(
                escapeField(NumberFormat.getCurrencyInstance().format(
                        summaryVO.getLastAllocationAmount()))).append(LINE_BREAK);
        buffer.append("Invested Date,").append(
                summaryVO.getLastSubmissionDate() == null ? SPACE_SYMBOL : summaryVO
                        .getLastSubmissionDate()).append(LINE_BREAK);
        buffer.append("Applicable as of,").append(
                summaryVO.getLastPayrollDate() == null ? SPACE_SYMBOL : summaryVO
                        .getLastPayrollDate()).append(LINE_BREAK);

        buffer.append(LINE_BREAK);

        buffer.append("Money Types & Sources,").append(LINE_BREAK);
        buffer.append(LINE_BREAK);
        buffer.append("Code,").append("Money Type(s)").append(LINE_BREAK);
        for (MoneyTypeVO vo : reportData.getMoneyTypes()) {
            buffer.append(vo.getContractShortName()).append(COMMA).append(vo.getContractLongName())
                    .append(LINE_BREAK);
        }
        buffer.append(LINE_BREAK);
        buffer.append("Code,").append("Money Source(s)").append(LINE_BREAK);
        for (MoneySourceVO vo : reportData.getMoneySources()) {
            buffer.append(vo.getContractShortName()).append(COMMA).append(vo.getContractLongName())
                    .append(LINE_BREAK);
        }

        buffer.append(LINE_BREAK);

        buffer.append("Key Dates,").append(LINE_BREAK);
        buffer.append("Contract Effective,").append(
                profileVo.getContractEffectiveDate() == null ? SPACE_SYMBOL : profileVo
                        .getContractEffectiveDate()).append(LINE_BREAK);
        buffer.append("Plan Year End,").append(
                DateRender.formatByPattern(profileVo.getContractYearEndDate(), SPACE_SYMBOL,
                        SPACE_SYMBOL, "MMMM dd")).append(LINE_BREAK);
        if (summaryVO.getIsContractHasGAFunds()  && profileVo.getGuaranteedAccountTransferDates() != null
                && !profileVo.getGuaranteedAccountTransferDates().isEmpty()) {
            buffer.append("Guaranteed Acct. Transfer**");
            for (Object date : profileVo.getGuaranteedAccountTransferDates()) {
                buffer.append(COMMA).append(
                        DateRender.formatByPattern(date, SPACE_SYMBOL, SPACE_SYMBOL, "MMMM dd"))
                        .append(LINE_BREAK);
            }
        }

        buffer.append(LINE_BREAK);

        buffer.append(
                ContentHelper.getContentText(
                        BDContentConstants.CONTRACT_INFO_PAGE_CONTRACT_ACCESS_CODE_SECTION_TITLE,
                        ContentTypeManager.instance().MESSAGE, null)).append(LINE_BREAK);
        buffer.append(ContentHelper.getContentText(BDContentConstants.MESSAGE_CONTRACT_ACCESS_CODE,
                ContentTypeManager.instance().MESSAGE, null));

        buffer.append("Enrollment access number,").append(
                profileVo.getContractAccessCode() == null ? SPACE_SYMBOL : profileVo
                        .getContractAccessCode()).append(LINE_BREAK);

        buffer.append(LINE_BREAK);

        buffer.append("Statement Details,").append(LINE_BREAK);
        buffer.append("Basis,").append(profileVo.getStatementInfo().getBasis()).append(LINE_BREAK);
        buffer.append("Last Printed,").append(
                profileVo.getStatementInfo().getLastPrintDate() == null ? SPACE_SYMBOL : profileVo
                        .getStatementInfo().getLastPrintDate()).append(LINE_BREAK);
        if (!currentContract.isDefinedBenefitContract()) {
            buffer.append("Delivery Method,").append(
                    profileVo.getStatementInfo().getDeliveryMethod()).append(LINE_BREAK);
            buffer.append("Statement Type,")
                    .append(profileVo.getStatementInfo().getStatementType()).append(LINE_BREAK);
            buffer.append("Permitted Disparity,").append(
                    reportData.getIsPermittedDisparity() ? "Yes" : "No").append(LINE_BREAK);
            buffer.append("Vesting Shows on Statements,").append(
                    reportData.getIsVestingShownOnStatements() ? "Yes" : "No").append(LINE_BREAK);
        }
        return buffer.toString().getBytes();
    }

	/**
	 * This method returns a boolean value indicating if the date passed in as a
	 * parameter is a valid date or not.
	 * 
	 * @param date
	 * @return - a boolean value indicating if the date passed in as a parameter
	 *         is a valid date or not
	 */
	private boolean isInvalidDate(Date date) {
		boolean isInvalidDate = false;
		Date dateWithYear9999 = new GregorianCalendar(9999, Calendar.DECEMBER,
				31).getTime();
		Date dateWithYear0001 = new GregorianCalendar(1, Calendar.JANUARY, 1)
				.getTime();

		if (date != null) {
			if (dateWithYear9999.equals(date) || dateWithYear0001.equals(date)) {
				isInvalidDate = true;
			}
		}
		return isInvalidDate;
    }
    /**
     * @See BDReportAction#prepareXMLFromReport()
     */
    @Override
    public Document prepareXMLFromReport(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws SystemException, ParserConfigurationException {

        ContractInformationReportForm form = (ContractInformationReportForm) reportForm;
        ContractInformationReportData data = (ContractInformationReportData) report;
        ContractProfileVO profileVo = data.getContractProfileVo();
        ContractSnapshotVO snapShotVo = data.getContractSnapshotVo();
        ContractSummaryVO summaryVO = data.getContractSummaryVo();

        PDFDocument doc = new PDFDocument();
        String contentText;

        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.CONTRACT_INFORMATION_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.CONTRACT_INFORMATION);

        setIntroXMLElements(layoutPageBean, doc, rootElement, request);

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);

        Contract currentContract = getBobContext(request).getCurrentContract();
        boolean isDBContract = currentContract.isDefinedBenefitContract();
        Date asOfDate = currentContract.getContractDates().getAsOfDate();
        //doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, DateRender.formatByPattern(
        //        asOfDate, null, RenderConstants.MEDIUM_MDY_SLASHED));

        // Contract Info - start
        Element reportDetail = doc.createElement(BDPdfConstants.REPORT_DETAIL);
        
        if (summaryVO.isRmContractExist()) {
        	
			if (StringUtils.isNotBlank(summaryVO.getRmUserPhoneNumber())
					&& StringUtils.isNotBlank(summaryVO.getRmUserPhoneExtension())) {
				summaryVO.setRmUserPhoneNumber(summaryVO.getRmUserPhoneNumber()+" ext. "+summaryVO.getRmUserPhoneExtension());
			}
        }
        else {
			getRMStaticData(summaryVO);
		} 

        setContactInfoXMLElements(doc, reportDetail, profileVo, summaryVO, data, currentContract);
        setAssetsInfoXMLElements(doc, reportDetail, snapShotVo, data, currentContract);
        setFeaturesInfoXMLElements(doc, reportDetail, profileVo, summaryVO, data, form,
                isDBContract, request);
        setKeyDatesInfoXMLElements(doc, reportDetail, profileVo, summaryVO);
        setMoneyTypesInfoXMLElements(doc, reportDetail, data);
        setPayrollInfoXMLElements(doc, reportDetail, summaryVO, isDBContract);
        setStatementDetailsXMLElements(doc, reportDetail, profileVo, data, isDBContract);

        contentText = ContentHelper.getContentText(
                BDContentConstants.CONTRACT_INFO_PAGE_CONTRACT_ACCESS_CODE_SECTION_TITLE,
                ContentTypeManager.instance().MESSAGE, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.ACCESS_CODE_SECTION_TITLE, reportDetail, doc,
                contentText);

        contentText = ContentHelper.getContentText(BDContentConstants.MESSAGE_CONTRACT_ACCESS_CODE,
                ContentTypeManager.instance().MESSAGE, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.ACCESS_CODE_MSG, reportDetail, doc, contentText);

        doc.appendTextNode(reportDetail, BDPdfConstants.ACCESS_CODE, profileVo
                .getContractAccessCode());
        // Contract Info - end

        doc.appendElement(rootElement, reportDetail);
        
        // Sets footer, footnotes and disclaimer
        setFooterXMLElements(layoutPageBean, doc, rootElement, request);
        
        // Sets footnote text for GIFL.
        if (currentContract.getHasContractGatewayInd()) {
        	contentText = ContentHelper.getContentText(BDContentConstants.GUARANTEED_INCOME_FEATURE_FOOTNOTE,
        			ContentTypeManager.instance().valueOf(BDContentConstants.TYPE_PAGEFOOTNOTE), null);
            PdfHelper.convertIntoDOM(BDPdfConstants.GIFL_FOOTNOTE, rootElement, doc, contentText);
        }
        
        return doc.getDocument();

    }

    /**
     * This method sets summary information XML elements
     * 
     * @param doc
     * @param rootElement
     * @param data
     * @param layoutPageBean
     * @param request
     */
    private void setContactInfoXMLElements(PDFDocument doc, Element reportDetail,
            ContractProfileVO profileVo, ContractSummaryVO summaryVO,
            ContractInformationReportData data, Contract currentContract) {

        String contentText;
        contentText = ContentHelper.getContentText(
                BDContentConstants.CONTRACT_INFO_PAGE_CONTACT_INFO_SECTION_TITLE,
                ContentTypeManager.instance().MESSAGE, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.CONTACT_SECTION_TITLE, reportDetail, doc,
                contentText);

        Address address = profileVo.getAddress();
        doc.appendTextNode(reportDetail, BDPdfConstants.ADDRESS_LINE1, address.getLine1());
        doc.appendTextNode(reportDetail, BDPdfConstants.ADDRESS_LINE2, address.getline2());
        if (StringUtils.isNotEmpty(address.getCity())) {
            doc.appendTextNode(reportDetail, BDPdfConstants.CITY, address.getCity());
        }
        if (StringUtils.isNotEmpty(address.getStateCode())) {
            doc.appendTextNode(reportDetail, BDPdfConstants.STATE_CODE, address.getStateCode());
        }
        if (StringUtils.isNotEmpty(address.getZipCode())) {
            doc.appendTextNode(reportDetail, BDPdfConstants.ZIP_CODE, address.getZipCode());
        }
        if (address.isCompleteStateCode()) {
            doc.appendTextNode(reportDetail, BDPdfConstants.COMPLETE_STATE_CODE, null);
        }

        if (!currentContract.isDefinedBenefitContract()) {
        	
        	//For a service representative : (English) Label.
        	contentText = ContentHelper.getContentText(
                    BDContentConstants.CONTACT_SERVICE_REP_LABEL,
                    ContentTypeManager.instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PPT_TOLL_FREE_SERVICE_NO_LABEL_IN_ENGLISH, reportDetail, doc,
                    contentText);
            
        	//For a service representative : (English) get data from CMA Database.
        	contentText = ContentHelper.getContentText(
                    BDContentConstants.MISCELLANEOUS_PARTICIPANT_TOLL_FREE_NUMBER,
                    ContentTypeManager.instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PPT_TOLL_FREE_SERVICE_NO, reportDetail, doc,
                    contentText);
                      
            //For a service representative : (Spanish)
            contentText = ContentHelper.getContentText(
                    BDContentConstants.CONTACT_SERVICE_REP_NUMBER_SPANISH,
                    ContentTypeManager.instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PPT_TOLL_FREE_SERVICE_NO_IN_SPANISH, reportDetail, doc,
                    contentText);
            
          //For a rollover education specialist Label
            contentText = ContentHelper.getContentText(
                    BDContentConstants.CONTACT_ROLLOVER_EDUCATION_SPECIALIST_LABEL,
                    ContentTypeManager.instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PPT_ROLLOVER_EDUCATION_SPECIALIST_LABEL, reportDetail, doc,
                    contentText);
            
            //For a rollover education specialist Number
            contentText = ContentHelper.getContentText(
                    BDContentConstants.CONTACT_ROLLOVER_EDUCATION_SPECIALIST_PHONE,
                    ContentTypeManager.instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PPT_ROLLOVER_EDUCATION_SPECIALIST_NO, reportDetail, doc,
                    contentText);
            
          //For a Consolidation specialist Label
            contentText = ContentHelper.getContentText(
                    BDContentConstants.CONTACT_CONSOLIDATION_SPECIALIST_LABEL,
                    ContentTypeManager.instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PPT_CONSOLIDATION_SPECIALIST_PHONE_LABEL, reportDetail, doc,
                    contentText);
            
            //For a Consolidation specialist No
            contentText = ContentHelper.getContentText(
                    BDContentConstants.CONTACT_CONSOLIDATION_SPECIALIST_PHONE,
                    ContentTypeManager.instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PPT_CONSOLIDATION_SPECIALIST_PHONE_NO, reportDetail, doc,
                    contentText);
            
            contentText = ContentHelper.getContentText(
                    BDContentConstants.MISCELLANEOUS_ENROLLMENT_FORM_FAX_NUMBER, ContentTypeManager
                            .instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.ENROLL_FORM_FAX_NO, reportDetail, doc,
                    contentText);
        }
        if (DISCONTINUED_STATUS.equals(currentContract.getStatus())) {
            contentText = ContentHelper.getContentText(
                    BDContentConstants.MISCELLANEOUS_GENERAL_PHONE_NUMBER, ContentTypeManager
                            .instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.GENERAL_PHONE_NO, reportDetail, doc,
                    contentText);
        } else {
            doc.appendTextNode(reportDetail, BDPdfConstants.CAR_NAME, summaryVO.getCarContact()
                    .getName());
            if (currentContract.isDefinedBenefitContract()) {
                contentText = ContentHelper.getContentText(
                        BDContentConstants.MISCELLANEOUS_DB_CONTRACT_PHONE_NUMBER,
                        ContentTypeManager.instance().MISCELLANEOUS, null);
                contentText = contentText + " ext. " + summaryVO.getCarContact().getExtension();
                PdfHelper.convertIntoDOM(BDPdfConstants.PPT_TOLL_FREE_NO, reportDetail, doc,
                        contentText);
            } else {
                contentText = ContentHelper.getContentText(
                        BDContentConstants.PS_CONTACTS_ACCOUNT_REPRESENTATIVE_PHONE,
                        ContentTypeManager.instance().MISCELLANEOUS, null);
                contentText = contentText + " ext. " + summaryVO.getCarContact().getExtension();
                PdfHelper.convertIntoDOM(BDPdfConstants.CAR_PHONE_NO, reportDetail, doc,
                        contentText);
            }
        }
        
		doc.appendTextNode(reportDetail, BDPdfConstants.CAR_EMAIL, summaryVO.getCarContact().getEmail());
		doc.appendTextNode(reportDetail, "rmName",
				convertNullToEmpty(summaryVO.getRmUserName()));
		doc.appendTextNode(reportDetail, "rmPhone", convertNullToEmpty(summaryVO.getRmUserPhoneNumber()));
		doc.appendTextNode(reportDetail, "rmEmail", convertNullToEmpty(summaryVO.getRmUserEmail()));

	       int tpaCount =0;
	        String tpaName = "";
	        String tpaPhone = "";
	        String tpaEmail = ""; 
	        boolean tpaPhoneAvailable = false;
	        boolean tpaEmailAvailable = false;
        if(data.getTpaPrimaryContactDetails().size() >0 ){
        	
        	for (ContactVO contact : data.getTpaPrimaryContactDetails()) {
				if(StringUtils.isNotBlank(contact.getPhone())){
					tpaPhoneAvailable = true;
				}
				if (StringUtils.isNotBlank(contact.getEmail())) {
					tpaEmailAvailable = true;
				}
			}
        	
        	for (ContactVO contact : data.getTpaPrimaryContactDetails()) {
        		if(tpaCount > 0){
					tpaName+=COMMA;
					tpaName+=WHITE_SPACE_CHAR;
				}
        		tpaName+=contact.getName();
        		if(tpaPhoneAvailable){
        			if(tpaCount > 0){
        				tpaPhone+=COMMA;
    					tpaPhone+=WHITE_SPACE_CHAR;
        			}
        			if(StringUtils.isBlank(contact.getPhone())){
						tpaPhone +="Not available";
					}else{
						tpaPhone += contact.getPhone();
					}
        		}
        		if(tpaEmailAvailable){
        			if(tpaCount > 0){
        				tpaEmail+=COMMA;
    					tpaEmail+=WHITE_SPACE_CHAR;
        			}
					if(StringUtils.isBlank(contact.getEmail())){
						tpaEmail +="Not available";
					}else{
						tpaEmail += contact.getEmail();
					}
				}
				
				tpaCount++;
			}
			} else if (data.getTpaContactName() != null
				&& StringUtils.isNotBlank(data.getTpaContactName())) {
			tpaName += data.getTpaContactName();
		} else {
			tpaName += "Not available";
		}
        	doc.appendTextNode(reportDetail, BDPdfConstants.TPA_FIRM_NAME, data.getTpaFirmName());//TPA_EMAIL
	        doc.appendTextNode(reportDetail, BDPdfConstants.TPA_PHONE, tpaPhone);
	        doc.appendTextNode(reportDetail, BDPdfConstants.TPA_EMAIL, tpaEmail);
	        doc.appendTextNode(reportDetail, BDPdfConstants.TPA_CONTACT_NAME, tpaName);
        	
		
	        
		
		if(data.getPlanSponsorPrimaryContactDetails().size() >0 ){
        	int psCount =0;
	        String psName = "";
	        String psPhone = "";
	        String psEmail = "";
	        boolean psPhoneAvailable = false;
            boolean psEmailAvailable = false;
            for (ContactVO contact : data.getPlanSponsorPrimaryContactDetails()) {
				if(StringUtils.isNotBlank(contact.getPhone())){
					psPhoneAvailable = true;
				}
				if (StringUtils.isNotBlank(contact.getEmail())) {
					psEmailAvailable = true;
				}
			}
        	for (ContactVO contact : data.getPlanSponsorPrimaryContactDetails()) {
        		if(psCount > 0){
					psName+=COMMA;
					psName+=WHITE_SPACE_CHAR;
					
				}
        		psName+=contact.getName();
        		if(psPhoneAvailable){
        			if(psCount > 0){
        				psPhone+=COMMA;
    					psPhone+=WHITE_SPACE_CHAR;
        			}
					if(StringUtils.isBlank(contact.getPhone())){
						psPhone +="Not available";
					}else{
						psPhone += contact.getPhone();
					}
        		}
        		if(psEmailAvailable){
        			if(psCount > 0){
        				psEmail+=COMMA;
    					psEmail+=WHITE_SPACE_CHAR;
        			}
        			if(StringUtils.isBlank(contact.getEmail())){
        				psEmail+="Not available";
        			}else{
        				psEmail+=contact.getEmail();
					}
				}
				
				psCount++;
			}
        	doc.appendTextNode(reportDetail, BDPdfConstants.PS_CONTACT_NAME, psName);
	        doc.appendTextNode(reportDetail, BDPdfConstants.PS_PHONE, psPhone);
	        doc.appendTextNode(reportDetail, BDPdfConstants.PS_EMAIL, psEmail);
        	
		} 
        if (currentContract.isDefinedBenefitContract()) {
            contentText = ContentHelper.getContentText(
                    BDContentConstants.DB_FAX_NUMBER, ContentTypeManager
                            .instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.OTHER_FORM_FAX_NO, reportDetail, doc,
                    contentText);
        } else {
            contentText = ContentHelper.getContentText(
                    BDContentConstants.MISCELLANEOUS_OTHER_FORM_FAX_NUMBER, ContentTypeManager
                            .instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.OTHER_FORM_FAX_NO, reportDetail, doc,
                    contentText);
        }

    }

    /**
     * @See BDReportAction#getXSLTFileName()
     */
    @Override
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }

    /**
     * @see BaseReportController#getReportId()
     */
    @Override
    protected String getReportId() {
        return REPORT_ID;
    }

    /**
     * @see BaseReportController#getReportName()
     */
    @Override
    protected String getReportName() {
        return CSV_REPORT_NAME;
    }

    /**
     * @see BaseReportController#populateReportCriteria()
     */
    @Override
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
            HttpServletRequest request) throws SystemException {
        Contract contract = getBobContext(request).getCurrentContract();
        criteria.addFilter(FILTER_CONTRACT_NUMBER, new Integer(contract.getContractNumber()));
        criteria.addFilter(FILTER_AS_OF_DATE, contract.getContractDates().getAsOfDate());
        criteria.addFilter(FILTER_CONTRACT_EFFECTIVE_DATE, contract.getEffectiveDate());
    }

    /**
     * @see BaseReportController#getFileName()
     */
    @Override
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {

        Contract currentContract = getBobContext(request).getCurrentContract();
        String asOfDate = String
                .valueOf(currentContract.getContractDates().getAsOfDate().getTime());
        String csvFileName = getReportName()
                + HYPHON_SYMBOL
                + currentContract.getContractNumber()
                + HYPHON_SYMBOL
                + DateRender.format(new Date(Long.valueOf(asOfDate)), MEDIUM_MDY_SLASHED).replace(
                        SLASH_SYMBOL, SPACE_SYMBOL) + CSV_EXTENSION;
        return csvFileName;
    }

    /**
     * don't want excel to think the , is the next field
     * 
     * @param field
     * @return String
     */
    private String escapeField(String field) {
        if (field.indexOf(COMMA) != -1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append(DOUBLE_QUOTES).append(field).append(DOUBLE_QUOTES);
            return newField.toString();
        } else {
            return field;
        }
    }

    /**
     * This method sets assets information XML elements
     * 
     * @param doc
     * @param reportDetail
     * @param snapShotVo
     * @param data
     * @param currentContract
     */
    private void setAssetsInfoXMLElements(PDFDocument doc, Element reportDetail,
            ContractSnapshotVO snapShotVo, ContractInformationReportData data,
            Contract currentContract) {
        String contentText = ContentHelper.getContentText(
                BDContentConstants.CONTRACT_INFO_PAGE_CONTRACT_ASSETS_SECTION_TITLE,
                ContentTypeManager.instance().MESSAGE, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.ASSETS_TITLE, reportDetail, doc, contentText);

        doc.appendTextNode(reportDetail, BDPdfConstants.TOTAL_ASSETS, NumberRender.formatByType(
                snapShotVo.getPlanAssets().getTotalPlanAssetsAmount(), null,
                RenderConstants.CURRENCY_TYPE));
        doc.appendTextNode(reportDetail, BDPdfConstants.CASH_ACCOUNT_AMT, NumberRender
                .formatByType(snapShotVo.getPlanAssets().getCashAccountAmount(), null,
                        RenderConstants.CURRENCY_TYPE));
        BigDecimal uninvestedAssetsAmount = snapShotVo.getPlanAssets().getUninvestedAssetsAmount();
        if (uninvestedAssetsAmount.doubleValue() != (double) 0.0) {
            doc.appendTextNode(reportDetail, BDPdfConstants.UNINVESTED_AMT, NumberRender
                    .formatByType(uninvestedAssetsAmount, null, RenderConstants.CURRENCY_TYPE));
        }
        doc.appendTextNode(reportDetail, BDPdfConstants.ALLOCATED_AMT, NumberRender.formatByType(
                snapShotVo.getPlanAssets().getAllocatedAssetsAmount(), null,
                RenderConstants.CURRENCY_TYPE));

        if (currentContract.isLoanFeature() && data.getHasLoanAssets()) {
            doc.appendTextNode(reportDetail, BDPdfConstants.LOAN_ASSETS, NumberRender
                    .formatByType(snapShotVo.getPlanAssets().getLoanAssets(), null,
                            RenderConstants.CURRENCY_TYPE));
        }

        if (currentContract.isPBA()) {
            doc.appendTextNode(reportDetail, BDPdfConstants.PBA_ACCOUNT_AMT, NumberRender
                    .formatByType(snapShotVo.getPlanAssets().getPersonalBrokerageAccountAmount(),
                            null, RenderConstants.CURRENCY_TYPE));
        }

        if (!currentContract.isDefinedBenefitContract()) {
            doc.appendTextNode(reportDetail, BDPdfConstants.NUM_OF_PPT, String.valueOf(data
                    .getParticipantCount()));
        }

        Date asOfDate = currentContract.getContractDates().getAsOfDate();
        doc.appendTextNode(reportDetail, BDPdfConstants.ASOF_DATE, DateRender.formatByPattern(
        		asOfDate, null, RenderConstants.MEDIUM_MDY_SLASHED));


		if (!SUPPRESS_BLENDED_ASSET_CHARGE) {
			if (!isInvalidDate(data.getAssetChargeAsOfDate())) {
				doc.appendTextNode(reportDetail,
						BDPdfConstants.BLENDED_ASSET_CHARGE, NumberRender
								.formatByPattern(data.getBlendedAssetCharge(),
										DEFAULT_VALUE, AMT_PATTERN, 3,
										BigDecimal.ROUND_HALF_DOWN));
				doc.appendTextNode(reportDetail,
						BDPdfConstants.ASSET_CHARGE_AS_OF_DATE, DateRender
								.format(data.getAssetChargeAsOfDate(), null));
			}
		}
    }

    /**
     * This method sets payroll information XML elements
     * 
     * @param doc
     * @param reportDetail
     * @param isDBContract
     */
    private void setPayrollInfoXMLElements(PDFDocument doc, Element reportDetail,
            ContractSummaryVO summaryVO, boolean isDBContract) {
        String contentText = null;
        if (isDBContract) {
            contentText = ContentHelper.getContentText(
                    BDContentConstants.CONTRACT_INFO_PAGE_ALLOCATION_DETAILS_SECTION_TITLE,
                    ContentTypeManager.instance().MESSAGE, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PAYROLL_ALLOCATION_TITLE, reportDetail, doc,
                    contentText);
        } else {
            contentText = ContentHelper.getContentText(
                    BDContentConstants.CONTRACT_INFO_PAGE_PAYROLL_ALLOCATION_DETAILS_SECTION_TITLE,
                    ContentTypeManager.instance().MESSAGE, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.PAYROLL_ALLOCATION_TITLE, reportDetail, doc,
                    contentText);
        }

        doc.appendTextNode(reportDetail, BDPdfConstants.LAST_ALLOCATION_AMT, NumberRender
                .formatByType(summaryVO.getLastAllocationAmount(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE));
        doc.appendTextNode(reportDetail, BDPdfConstants.LAST_CONTRIB_DATE, DateRender
                .formatByPattern(summaryVO.getLastSubmissionDate(), null,
                        RenderConstants.MEDIUM_MDY_SLASHED));
        doc.appendTextNode(reportDetail, BDPdfConstants.LAST_PAYROLL_DATE, DateRender
                .formatByPattern(summaryVO.getLastPayrollDate(), null,
                        RenderConstants.MEDIUM_MDY_SLASHED));
    }

    /**
     * This method sets key dates information XML elements
     * 
     * @param doc
     * @param reportDetail
     * @param profileVo
     */
    private void setKeyDatesInfoXMLElements(PDFDocument doc, Element reportDetail,
            ContractProfileVO profileVo, ContractSummaryVO summaryVO) {
        String contentText = ContentHelper.getContentText(
                BDContentConstants.CONTRACT_INFO_PAGE_KEY_DATES_SECTION_TITLE, ContentTypeManager
                        .instance().MESSAGE, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.KEY_DATES_SECTION_TITLE, reportDetail, doc,
                contentText);

        doc.appendTextNode(reportDetail, BDPdfConstants.CONTRACT_EFFECTIVE_DATE, DateRender.format(
                profileVo.getContractEffectiveDate(), null));
        doc.appendTextNode(reportDetail, BDPdfConstants.CONTRACT_YEAR_END_DATE, DateRender
                .formatByPattern(profileVo.getContractYearEndDate(), null, DATE_FORMAT));
        if (summaryVO.getIsContractHasGAFunds() && profileVo.getGuaranteedAccountTransferDates().size() > 0) {
            for (Object date : profileVo.getGuaranteedAccountTransferDates()) {
                doc.appendTextNode(reportDetail, BDPdfConstants.TRANSFER_DATE, DateRender
                        .formatByPattern(date, null, DATE_FORMAT));
            }
        }
    }

    /**
     * This method sets features information XML elements
     * 
     * @param doc
     * @param reportDetail
     * @param profileVo
     * @param summaryVO
     * @param data
     * @param form
     */
    @SuppressWarnings("unchecked")
    private void setFeaturesInfoXMLElements(PDFDocument doc, Element reportDetail,
            ContractProfileVO profileVo, ContractSummaryVO summaryVO,
            ContractInformationReportData data, ContractInformationReportForm form,
            boolean isDBContract, HttpServletRequest request) throws SystemException {
    	
    	BobContext bobContext = getBobContext(request);
        String contentText = ContentHelper.getContentText(
                BDContentConstants.CONTRACT_INFO__PAGE_CONTRACT_OPTIONS_SECTION_TITLE,
                ContentTypeManager.instance().MESSAGE, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.CONTRACT_OPTIONS_SECTION_TITLE, reportDetail, doc,
                contentText);

        Collection features = profileVo.getFeaturesAndServices().getContractFeatures();
        for (Object feature : features) {
			if (StringUtils.isNotBlank(feature.toString())) {
				doc.appendTextNode(reportDetail, BDPdfConstants.FEATURE,
						feature.toString());
			}
		}
        
        String giflFeaturesSectionTitle = "";
        if(bobContext.getContractProfile().getContract().getHasContractGatewayInd()) {
	        if(BDConstants.GIFL_VERSION_03.equals(bobContext.getContractProfile().getContract().getGiflVersion())) {
	        	giflFeaturesSectionTitle = BDConstants.GIFL_SELECT_TITLE;
	        }
	        else {
	        	giflFeaturesSectionTitle = BDConstants.GIFL_V1_V2_TITLE;
	        }
	        doc.appendTextNode(reportDetail, BDPdfConstants.GIFL_FEATURES_SECTION_TITLE,
	        		giflFeaturesSectionTitle);
			MutableContent content = null;
			try {
				content = (MutableContent) BrowseServiceDelegate.getInstance()
						.findContent(
								ContractInformationReportHelper
										.getGiflFeaturesContentId(bobContext),
								ContentTypeManager.instance().MISCELLANEOUS);
			} catch (ContentException ce) {
				logger.error("Exception while retrieving GIFL features from CMA ", ce);
				throw new SystemException("Exception while retrieving GIFL features from CMA "
	                    + ce.toString());
			}
			String giflFeaturesCMAText = ContentUtility
			.getContentAttribute(content,
					CommonContentConstants.TEXT, null,
					new String[] { ContractInformationReportHelper
							.getGIFLFeePercentageDisplay(bobContext.getCurrentContract()
									.getContractNumber()) });
			List<String> giflFeatures = ContractInformationReportHelper
					.parseGiflFeaturesFromCMAText(giflFeaturesCMAText);
			for (String giflFeature : giflFeatures) {
				doc.appendTextNode(reportDetail, BDPdfConstants.GIFL_FEATURE,
						giflFeature.toString());
			}
        }
        
        
		if (profileVo.getFeaturesAndServices().getSendService().isEnabled()) {
			doc.appendTextNode(reportDetail, BDPdfConstants.SEND_SERVICE_IND,
					Boolean.TRUE.toString());
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMinimumFractionDigits(2);
			doc.appendTextNode(reportDetail, BDPdfConstants.SEND_SERVICE_FEE,
					        numberFormat.format(
							profileVo.getFeaturesAndServices().getSendService().getAnnualFee() == null ? 0.00 : 
	                        profileVo.getFeaturesAndServices().getSendService().getAnnualFee()) + "%");
			doc.appendTextNode(reportDetail,
					BDPdfConstants.SEND_SERVICE_EFFECTIVE_DATE, DateRender.formatByPattern(
									profileVo.getFeaturesAndServices().getSendService().getServiceEffectiveDate(),
									SPACE_SYMBOL, MEDIUM_MDY_SLASHED));
		}
		
		if (data.getManagedAccountServiceFeature() != null) {
			doc.appendTextNode(reportDetail, BDPdfConstants.MANAGED_ACCOUNT, data.getManagedAccountServiceFeature());
		}
		
        doc.appendTextNode(reportDetail, BDPdfConstants.SELECTED_FUNDS, NumberRender.formatByType(
                profileVo.getFeaturesAndServices().getSelectedFundsNumber(),
                BDConstants.ZERO_STRING, RenderConstants.INTEGER_TYPE));
        doc.appendTextNode(reportDetail, BDPdfConstants.AVAILABLE_FUNDS, NumberRender.formatByType(
                profileVo.getFeaturesAndServices().getAvailableFundsNumber(),
                BDConstants.ZERO_STRING, RenderConstants.INTEGER_TYPE));

        if (profileVo.getDefaultInvestments().size() > 0) {
            Element defaultInvestmentElement;
            Iterator defaultInvestments = profileVo.getDefaultInvestments().iterator();
            while (defaultInvestments.hasNext()) {
                defaultInvestmentElement = doc.createElement(BDPdfConstants.DEFAULT_INVESTMENT);
                DefaultInvestmentFundVO defaultInvestment = (DefaultInvestmentFundVO) defaultInvestments
                        .next();
                if (!defaultInvestment.isLifeCycleFund()) {
                    doc.appendTextNode(defaultInvestmentElement, BDPdfConstants.FUND_NAME,
                            defaultInvestment.getFundName());
                } else {
                	// For fund family names under Lifecycle Category
                	doc.appendTextNode(defaultInvestmentElement, BDPdfConstants.FUND_NAME,
                            defaultInvestment.getFundFamilyDisplayName());
                }
                doc.appendTextNode(defaultInvestmentElement, BDPdfConstants.FUND_PERCENTAGE,
                        NumberRender.formatByPattern(defaultInvestment.getPercentage(), null,
                                PERCENT_FORMAT));
                doc.appendElement(reportDetail, defaultInvestmentElement);
            }

        }
        
        if (form.getShowFiduciaryWarranty()) {
            doc.appendTextNode(reportDetail, BDPdfConstants.SHOW_FIDUCIARY_WARRANTY, null);
            if (summaryVO.getFswStatus() == FswStatus.QUALIFIED) {
                doc.appendTextNode(reportDetail, BDPdfConstants.WARRANTY_MET, "Yes");
            }  else if (summaryVO.getFailedAssetClasses() != null
					&& !summaryVO.getFailedAssetClasses().isEmpty()) {
            	 doc.appendTextNode(reportDetail, BDPdfConstants.WARRANTY_MET, "No"); 
            } else {
            	 doc.appendTextNode(reportDetail, BDPdfConstants.WARRANTY_MET, "Not eligible");
            }
        }
        if (profileVo.getFeaturesAndServices().getIsDirectDebitSelected()) {
            doc.appendTextNode(reportDetail, BDPdfConstants.DIRECT_DEBIT_SELECTED, null);
        }
        if (!isDBContract) {
            Collection accessChannels = profileVo.getFeaturesAndServices().getAccessChannels();
            if (accessChannels != null) {
                for (Object accessChannel : accessChannels) {
                    doc.appendTextNode(reportDetail, BDPdfConstants.ACCESS_CHANNEL, accessChannel
                            .toString());
                }
            }
        }
    }

    /**
     * This method sets features information XML elements
     * 
     * @param doc
     * @param reportDetail
     * @param data
     */
    private void setMoneyTypesInfoXMLElements(PDFDocument doc, Element reportDetail,
            ContractInformationReportData data) {
        String contentText = ContentHelper.getContentText(
                BDContentConstants.CONTRACT_INFO_PAGE_MONEY_TYPES_AND_SOURCES_SECTION_TITLE,
                ContentTypeManager.instance().MESSAGE, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.MONEY_TYPES_AND_SOURCES_SECTION_TITLE,
                reportDetail, doc, contentText);

        for (MoneyTypeVO vo : data.getMoneyTypes()) {
            Element moneyType = doc.createElement(BDPdfConstants.MONEY_TYPE);
            doc.appendTextNode(moneyType, BDPdfConstants.SHORT_NAME, vo.getContractShortName());
            doc.appendTextNode(moneyType, BDPdfConstants.LONG_NAME, vo.getContractLongName());
            doc.appendElement(reportDetail, moneyType);
        }
        for (MoneySourceVO vo : data.getMoneySources()) {
            Element moneySource = doc.createElement(BDPdfConstants.MONEY_SOURCE);
            doc.appendTextNode(moneySource, BDPdfConstants.SHORT_NAME, vo.getContractShortName());
            doc.appendTextNode(moneySource, BDPdfConstants.LONG_NAME, vo.getContractLongName());
            doc.appendElement(reportDetail, moneySource);
        }
    }

    /**
     * This method sets statement details XML elements
     * 
     * @param doc
     * @param reportDetail
     * @param profileVo
     */
    private void setStatementDetailsXMLElements(PDFDocument doc, Element reportDetail,
            ContractProfileVO profileVo, ContractInformationReportData data, boolean isDBContract) {
        String contentText = ContentHelper.getContentText(
                BDContentConstants.CONTRACT_INFO_PAGE_STATEMENT_DETAILS_SECTION_TITLE,
                ContentTypeManager.instance().MESSAGE, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.STMT_DETAILS_SECTION_TITLE, reportDetail, doc,
                contentText);

        doc.appendTextNode(reportDetail, BDPdfConstants.BASIS, profileVo.getStatementInfo()
                .getBasis());
        doc.appendTextNode(reportDetail, BDPdfConstants.LAST_PRINT_DATE, DateRender.format(
                profileVo.getStatementInfo().getLastPrintDate(), null));
        if (!isDBContract) {
        	String feeDisclosure = StringUtils.trimToEmpty(profileVo.getStatementInfo().getFeeDisclosure());
            if ( !"No".equals(feeDisclosure) && feeDisclosure.length()>0)  {
                doc.appendTextNode(reportDetail, BDPdfConstants.FEE_DISCLOSURE, profileVo
                        .getStatementInfo().getFeeDisclosure());
            }
            if (data.getIsPermittedDisparity()) {
                doc.appendTextNode(reportDetail, BDPdfConstants.PERMIT_DISPARITY,
                        BDConstants.YES_VALUE);
            } else {
                doc.appendTextNode(reportDetail, BDPdfConstants.PERMIT_DISPARITY,
                        BDConstants.NO_VALUE);
            }
            if (data.getIsVestingShownOnStatements()) {
                doc.appendTextNode(reportDetail, BDPdfConstants.VESTING_SHOWN_ON_STMT, profileVo
                        .getStatementInfo().getBasis());
            }
            doc.appendTextNode(reportDetail, BDPdfConstants.DELIVERY_METHOD, profileVo
                    .getStatementInfo().getDeliveryMethod());
            if (StringUtils.isNotEmpty(profileVo.getStatementInfo().getStatementType())) {
                doc.appendTextNode(reportDetail, BDPdfConstants.STMT_TYPE, profileVo
                        .getStatementInfo().getStatementType());
            }
        }
    }

    /**
     * This method is used to log the Page Access into MRL.
     * 
     * @param request - HttpServletRequest
     * @param userProfile - BDUserProfile object.
     * @param contractNumber the Contract Number
     */
    private void logPageAccess(HttpServletRequest request, BDUserProfile userProfile,
            Integer contractNumber) {

        StringBuffer logData = new StringBuffer();
        Long profileID = null;
        if (userProfile.isInMimic()) {
            BDUserProfile mimickingUserProfile = BlockOfBusinessUtility
                    .getMimckingUserProfile(request);
            profileID = mimickingUserProfile.getBDPrincipal().getProfileId();
        } else {
            profileID = userProfile.getBDPrincipal().getProfileId();
        }
        logData.append(BDConstants.BRL_LOG_USER_PROFILE_ID).append(profileID).append(
                BDConstants.SEMICOLON_SYMBOL);

        logData.append(BDConstants.BRL_LOG_PAGE_ACCESSED).append("Contract Information Overview").append(
                BDConstants.SEMICOLON_SYMBOL);

        logData.append(BDConstants.BRL_LOG_MIMIC_MODE).append(
                userProfile.isInMimic() ? BDConstants.YES_VALUE : BDConstants.NO_VALUE).append(
                BDConstants.SEMICOLON_SYMBOL);

        if (userProfile.isInMimic()) {
            logData.append(BDConstants.BRL_LOG_MIMICKED_USER_PROFILE_ID).append(
                    userProfile.getBDPrincipal().getProfileId()).append(
                    BDConstants.SEMICOLON_SYMBOL);
        }

        logData.append(BDConstants.BRL_LOG_DATE_OF_ACTION).append(new Date()).append(
                BDConstants.SEMICOLON_SYMBOL);

        logData.append(BDConstants.BRL_LOG_ACTION_TAKEN).append(
                "BD Contract Information Overview page access").append(BDConstants.SEMICOLON_SYMBOL);

        logData.append("Contract=").append(contractNumber).append(";");

        logWebActivity("BD Contract Information Overview page access", logData.toString(), userProfile,
                logger, interactionLog, logRecord);
    }

    /**
     * Logs the web activities
     * 
     * @param action
     * @param profile
     * @param currentPage
     * @param nextPage
     * @param form
     */
    private static void logWebActivity(String action, String logData, BDUserProfile profile,
            Logger logger, Category interactionLog, ServiceLogRecord logRecord) {
        try {
            ServiceLogRecord record = (ServiceLogRecord) logRecord.clone();
            record.setServiceName("ContractInformationAction");
            record.setMethodName(action);
            record.setData(logData);
            record.setDate(new Date());
            record.setPrincipalName(profile.getBDPrincipal().getUserName());
            record.setUserIdentity(profile.getBDPrincipal().getProfileId()
                    + profile.getBDPrincipal().getUserName());

            interactionLog.error(record);
        } catch (CloneNotSupportedException e) {
            // log the error, but don't interrupt regular processing
            logger.error("error trying to log a button press for profile id ["
                    + "] contract number [" + "]: " + e);
        }
    }
    
    /*
	 * @param regulatoryDisclosureForm
	 * @param contractNumber
	 * @throws SystemException
	 */
	private void checkIfInforceFeeDisclosurePdfAvailable( ContractInformationReportForm contractinformationForm ,
			int contractNumber) throws SystemException {
		FeeDisclosureDocumentInfo documentInfo = OrderServiceDelegate
				.getInstance().getInforceFeeDisclosurePdfDetails(contractNumber, 0);
		if (documentInfo != null) {
			contractinformationForm.setHasInforceFeeDisclosurePdf(true);
		} else {
			contractinformationForm.setHasInforceFeeDisclosurePdf(false);
		}
	}
	@RequestMapping(value ="/contractInformation/", method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("contractInformationForm") ContractInformationReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			try {
				doCommon( (ContractInformationReportForm) form, request,response);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
		
			
			forward=super.doDefault( form, request, response);
			convertAndFectchRMInfo(request);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
	@RequestMapping(value ="/contractInformation/" ,params={"task=filter"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("contractInformationForm") ContractInformationReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
	   if(bindingResult.hasErrors()){
		   try {
				doCommon( (ContractInformationReportForm) form, request, response);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
			forward=super.doFilter( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
    @RequestMapping(value ="/contractInformation/" ,params={"task=printPDF"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doPrintPDF (@Valid @ModelAttribute("contractInformationForm") ContractInformationReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
	   if(bindingResult.hasErrors()){
		   try {
				doCommon( (ContractInformationReportForm) form, request, response);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
			forward=super.doPrintPDF( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
   
   @RequestMapping(value ="/contractInformation/" ,params={"task=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
   public String doSort (@Valid @ModelAttribute("contractInformationForm") ContractInformationReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
	   String forward=preExecute(form, request, response);
       if ( StringUtils.isNotBlank(forward)) {
    	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
       }
	   if(bindingResult.hasErrors()){
		   try {
				doCommon( (ContractInformationReportForm) form, request, response);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
			forward=super.doSort( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
   @RequestMapping(value ="/contractInformation/", params={"task=download"},method =  {RequestMethod.GET}) 
   public String doDownload (@Valid @ModelAttribute("contractInformationForm") ContractInformationReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
	   String forward=preExecute(form, request, response);
       if ( StringUtils.isNotBlank(forward)) {
    	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
       }
	   if(bindingResult.hasErrors()){
		   try {
				doCommon( (ContractInformationReportForm) form, request, response);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
			forward=super.doDownload( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
   
   @RequestMapping(value ="/contractInformation/", params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
   public String doDownloadAll (@Valid @ModelAttribute("contractInformationForm") ContractInformationReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
	   String forward=preExecute(form, request, response);
       if ( StringUtils.isNotBlank(forward)) {
    	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
       }
	   if(bindingResult.hasErrors()){
		   try {
				doCommon( (ContractInformationReportForm) form, request, response);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
			forward=super.doDownloadAll( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }  
	
	private void convertAndFectchRMInfo(HttpServletRequest request) throws SystemException {

		ContractInformationReportData reportData = (ContractInformationReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);
		ContractSummaryVO contractSummaryVO = reportData.getContractSummaryVo();

		if (!contractSummaryVO.isRmContractExist()) {
			getRMStaticData(contractSummaryVO);
		}

	}

	String convertNullToEmpty(String rmname) {
		if (rmname == null) {
			rmname = "";
		}

		return rmname;

	}

	public void getRMStaticData(ContractSummaryVO contractSummaryVO) throws SystemException {
		String rmName = "";
		String rmPhone = "";
		String rmEmail = "";
		MutableContent rmContent = null;
		try {
			rmContent = (MutableContent) BrowseServiceDelegate.getInstance().findContent(
					BDContentConstants.RELATIONSHIP_MANAGER_STATIC_INFO_TEXT,
					ContentTypeManager.instance().MISCELLANEOUS);
		} catch (Exception ce) {
			logger.error("Exception while retrieving Ium contract static data from CMA ", ce);
			throw new SystemException("Exception while retrieving Ium contract static data fromCMA " + ce.toString());
		}

		String rmCMAText = "";
		if (rmContent != null) {
			rmCMAText = ContentUtility.getContentAttribute(rmContent, "text", null, null);
		}

		String[] rmiDetailsArray = rmCMAText.split(",");
		if (rmiDetailsArray.length > 1) {
			rmName = rmiDetailsArray[0];
			rmPhone = rmiDetailsArray[1];
			rmEmail = rmiDetailsArray[2];
		}
		contractSummaryVO.setRmUserName(rmName);
		contractSummaryVO.setRmUserPhoneNumber(rmPhone);
		contractSummaryVO.setRmUserEmail(rmEmail);

	}
	
	/**This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 * 
	 */
   @Autowired
   private BDValidatorFWInput  bdValidatorFWInput;
@InitBinder
  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
    binder.bind( request);
    binder.addValidators(bdValidatorFWInput);
}
	
	
}
